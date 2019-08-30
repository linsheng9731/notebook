# Channel、Pipeline、Handler、Context 之间的关系
1. Channel 关联一个 pipeline，pipeline 里包含多个 handler，每个 handler 与一个 context 关联。
2. Channel 和 pipeline 是一对一的关系，handler 和 pipeline 是 多对多的关系。
3. Pipeline 维护了 handler 之间的关系，同时维护了 channel 和 handler 列表之间的关系。
4. Context 维护了 handler 和 pipeline 之间的关系，通过 context 可以触发对应的 handler，事件会从对应的 handler 开始往相应的顺序传递下去，从而获得了更短的事件流，效率更高。
