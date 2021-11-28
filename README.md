# 스프링배치완벽가이드 시작
- JobRepository : 실행중인 잡의 상태를 기록하는 데 사용됨
- JobLauncher : 잡을 구동하는 데 사용됨
- JobExplorer : JobRepository 를 사용해 읽기 전용 작업을 수행하는 데 사용됨
- JobRegistry : 특정한 런처 구현체를 사용할 때 잡을 찾는 용도로 사용됨
- PlatformTransactionManager : 잡 진행 과정에서 트랜젝션을 다루는 데 사용됨
- JobBuilderFactory : 잡을 생성하는 빌더
- StepBuilderFactory : 스텝을 생성하는 빌더

잡의 실행은 기본적으로 스프링 부트는 구성된 ApplicationContext 내에서 찾은 모든 잡을 구동 시에 실행한다.
메인 메서드에서 스프링 부트를 부트스트랩 할 때 ApplicationContext 가 생성되고 , JobLauncherCommandLineRunner 가 실행됐으며,
잡이 수행됐다. 잡은 첫 번째 스탭을 실행한다. 이때 트랜젝션이 시작됐으며, Tasklet 이 실행됐고, 결과가 JobRepository 에 갱신됐다.

# 01-잡구성하기
JobInstance 가 잡 이름 및 잡에 전달된 식별 파라미터로 식별된다.
이때 동일한 파라미터를 사용해 동일한 잡을 두번 이상 사용할 수 었다.

- 잡 파라미터를 CommandLineJobRunner 를 통해 전달 할 수 있다.
```
ex : java -jar demo.jar name=Michael
```
이떄 --접두사를 사용해 잡 파라미터를 전달하면 안 된다. -D아규먼트를 사용해 패치 애플리케이션에 전달해서도 안 된다.

- 스프링 배치는 파라미터의 타입을 변환하는 기능을 제공한다.
```
java -jar demo.jar executionDate(date)=2020/12/27
```

- 특정 잡 파라미터가 식별에 사용되지 않도록 지정하기 (접두사사용)
```
java -jar demo.jar executionDate(date)=2020/12/27 -name=Michael
```

## 01-잡구성하기 01) 잡 파라미터에 접근하기
- ChunkContext : step1 의 tasklet 을 보면 execute 메서드가 두개의 파라미터를 전달 방고있다.
stepContribution 은 아직 커밋되지 않은 현재 트랜젝션에 대한 정보(쓰기수, 읽기수 등)를 가지고 있다.
chunkContext 는 ChunkContext 의 인스턴스로 실행 시점의 잡 상태를 제공한다. 또한 tasklet 내에서는 처리 중인 청크와 관련된 정보도 가지고 있다.
해당 청크 정보는 스텝 및 잡과 관련된 정보도 가지고 있다. ChunkContext 는 JobParameters 가 포함된 StepContext 의 참조가 있다.

- 늦은 바인딩 : 특정 부분에 파라미터를 전달하는 가장 쉬운 방법은 스프링 구성을 사용해 주입하는 것이다.
JobParameters 는 변경할 수 없으므로 부트스트랩 시에 바인딩하는 것이 좋다.

*잡에게 파라미터를 전달하고 이를 사용할 수 있도록 제공하는 기능 외에,
스프링 배치 프레임워크에 내장된 두가지 특화기능이 있다.
첫번째는 파라미터 유효성 검증이며
두번쨰는 파라미터를 실행시마다 증가시키는 기능이다.*

## 01-잡구성하기 01) 잡 파라미터 유효성 검증하기
- ParameterValidatorV1.jav 처럼 직접 구현할 수도 있지만,
- 스프링 배치는 모든 필수 파라미터가 누락없이 전달됐는지 확인하는 검증기인 DefaultJobParametersValidator 를 기본적으로 제공한다.
DefaultJobParametersValidator 는 requiredKeys 와 optionalKeys 라는 두가지 선택적인 의존성이 있다.
각각 필수 파라미터 목록과 필수가 아닌 파라미터 목록을 의미한다.

- 기본적으로 하나의 JobBuilder 에 하나의 JobParameterValidator 만 등록할 수 있다.
스프링 배치는 이러한 문제점을 해결하기위해 CompositeJobParametersValidator 를 제공한다.

## 01-잡구성하기 01) 잡 파라미터 증가시키기
- JobParametersIncrementer 는 잡에서 사용할 파라미터를 고유하게 생성할 수 있도록 스프링 배치가 제공하는 인터페이스이다.
기본적으로 파라미터 이름이 run.id 인 long 타입 파라미터의 값을 증가시킨다.
- RunIdIncrementer 를 적용하려면 잡 구성 작업 외에 추가적으로 JobParametersValidator 에 추가해 줘야한다.

- 잡 실행시 마다 타임스탬프를 파라미터로 사용할 수도 있다.(`DailyJobTimestamper.java`) 이 방식을 사용하면 하루에 한번 실행되는 잡에 사용하기 유용할 것이다.

# 02-잡리스너
스프링 배치는 모든 측면에서 생명주기가 잘 정의돼 있다.
잡 실행과 관련이 있다면 JobExecutionListener 를 사용할 수 있다. 이 인터페이스는 beforeJob 과 afterJob 의 두 메서드를 제공한다.
beforeJob 과 afterJob 는 잡 생명 주기에서 가장 먼저 실행되거나 가장 나중에 실행된다. 다음과 같은 사례에 이러한 콜백을 적용할 수 있다.
- 알림 : 스프링 클라우드 테스크 (spring cloud task) 는 잡의 시작이나 종료를 다른 시스템에 알리는 메시지 큐메시지를 생성하는 JobExecutionListener 를 제공한다.
- 초기화 : 잡 실행 전에 준비해둬야 할 뭔가가 있다면 beforeJob 메서드가 해당 로직을 실행하기에 좋은 곳이다.
- 정리 : 실행 이후에 정리작업을 수행한다(파일을 삭제하거나, 보관하는 작업 등). 정리 작업은 잡의 성공 / 실패에 영향을 미치지 않지만 실행돼야 한다.
afterJob 은 이러한 일을 처리하기에 완벽한 곳이다.

잡 리스너를 작성하는 두가지 방법이 있다.
첫번째는 JobExecutionListener 인터페이스를 구현하는 방법이다.
JobExecutionListener 는 beforeJob 과 afterJob 이라는 두 메서드를 가지고 있다.
각각 잡이 실행되기 전과 후에 실행된다. afterJob 은 잡의 완료상태에 관계없이 호출된다.
그러므로 잡의 종료 상태에 따라 어떤 일을 수행할지 결정할 수도 있다.
`JobExecutionListener.java` -> 또한 JobExecutionListener 구현없이 `@BeforeJob` , `@AfterJob` 애너테이션을 사용할 수도 있다. `JobExecutionListener.java`
애너테이션을 사용하려면 구성 방법이 약간 달라진다. 스프링 배치에서 이 리스너를 잡에 주입하려면 래핑을 해야한다.
```
    @Bean
    public Job job() {
        //
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
                .incrementer(new DailyJobTimestamper()) // DailyJobTimestamper 사용시
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }
```
*실제로 사용해보니 래핑을 하지않아도 돌아가긴했다.*
```
    @Bean
    public Job job() {
        //
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
                .incrementer(new DailyJobTimestamper()) // DailyJobTimestamper 사용시
                .listener(new JobLoggerListener())
                .build();
    }
```
애너테이션을 사용해도 위처럼 등록해서 돌아갔다.

# 03-ExecutionContext
배치 처리는 특성상 상태를 가지고 있다. 이러한 잡 상태는 JobExecution 의 ExecutionContext 에 저장된다.
일반적으로 웹은 상태를 HttpSession 을 이용해서 저장한다. 배치에서의 이 역할을 ExecutionContext 가 한다고 생각하면된다.
웹과의 한가지 차이점은 ExecutionContext 는 여러개가 존재할 수 있다는 점이다. JobExecution 처럼 각 StepExecution 도 마찬가지로 ExecutionContext 를 가진다.
ExecutionContext 가 담고 있는 모든 것이 JobRepository 에 저장되므로 안전하다.

