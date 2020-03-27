# Flink 源码分析 - Runtime 

```
client - rpc -> JobSubmitHandler.handleRequest
                -> Dispatcher.submitJob -> internalSubmitJob -> persistAndRunJob -> runJob -> startJobManagerRunner
                    -> JobManagerRunner.start 

                        -> LeaderElectionService.start(this) -> LeaderLatch.start() -> LeaderLatch.internalStart -> LeaderLatch.listener.stateChanged -> LeaderLatch.handleStateChange -> LeaderLatch.setLeadership -> LeaderLatchListener.isLeader

                        -> JobManagerRunner.grantLeadership -> verifyJobSchedulingStatusAndStartJobManager -> startJobMaster

                            -> JobMaster.start -> startJobExecution -> resetAndScheduleExecutionGraph -> scheduleExecutionGraph 
                                -> ExecutionGraph.scheduleForExecution -> scheduleEager
                                    -> Execution.deploy 

                                        -> TaskManagerGateway.submitTask
                                            -> TaskExecutor.submitTask 
                                                -> Task.startTaskThread -> loadAndInstantiateInvokable
                                                    -> AbstractInvokable.invoke(StreamTask 包含 headOperator operatorChain  stateBackend)
```