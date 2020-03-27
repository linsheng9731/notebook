# Flink 源码分析 - State CheckPoint 模块

- checkpoint 机制，保存对象是什么？对于不同 state backend 有什么不同？

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