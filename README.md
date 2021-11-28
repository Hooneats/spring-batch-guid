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