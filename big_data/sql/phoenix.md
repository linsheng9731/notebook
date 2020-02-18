# Quick Start

## Phoenix 特性
Phoenix实现了相同或更好的性能（更不用说用更少的代码了）：

- 将 SQL 查询编译为本地 HBase 扫描
- 自动计算 scan 键的最佳开始和停止位置
- 协调 scan 操作的并行执行
- 通过以下方式将计算引入数据
    - 将 where 子句中的谓词推送到 hbase 端进行过滤
    - 通过 hbase server 端的协处理器执行聚合查询

除了上面这些特性之外，还有额外的性能优化：
- 二级索引，以提高非行键列查询的性能
- 收集统计信息以改善并行化并指导如何优化
- 跳过 scan filter 以优化IN，LIKE 和 OR 查询
- 对行键进行可选的 salt 化，以平均分配写负载

## 快速开始
只需要三步即可开始在 hbase 集群上使用 phoenix：
 - 复制 phoenix 服务端 jar 包到 hbase 每个区域服务器的 lib 目录中
 - 重新启动 hbase 区域服务器
 - 将 phoenix 客户端 jar 包添加到 HBase 客户端的类路径中
使用 phoenix 自带的命令行工具连接 hbase 集群进行临时 sql 查询：
```
./sqlline.py  <your_zookeeper_quorum>
```
