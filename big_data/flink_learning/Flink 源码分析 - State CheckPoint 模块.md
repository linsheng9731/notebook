# Flink 源码分析 - State CheckPoint 模块


```
AbstractStreamOperator.initializeState -> StreamTask.createStreamTaskStateInitializer
StreamTask.createStateBackend // 创建 statbackend
```
```
	private StateBackend createStateBackend() throws Exception {
		final StateBackend fromApplication = configuration.getStateBackend(getUserCodeClassLoader());

		return StateBackendLoader.fromApplicationOrConfigOrDefault(
				fromApplication,
				getEnvironment().getTaskManagerInfo().getConfiguration(),
				getUserCodeClassLoader(),
				LOG);
	}
```