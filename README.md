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

