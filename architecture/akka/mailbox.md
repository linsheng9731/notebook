# 邮箱
邮箱是存储 actor 相关消息的对象。通常一个 actor 只有一个邮箱，在某些特殊情况下比如使用 BalancingPool 的 router，所有子 actor 使用同一个邮箱。

## 邮箱类型
