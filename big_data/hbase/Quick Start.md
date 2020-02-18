# Quick Start

## Hbase shell
连接 hbase：
```
$ ./bin/hbase shell
hbase(main):001:0>
```

退出：
```
exit
```
## ddl 命令
创建一个 table：
```
# 语法
create '表名', {NAME => '列族名1'}, {NAME => '列族名2'}, {NAME => '列族名3'}
# 此种方式是上上面的简写方式，使用上面方式可以为列族指定更多的属性，如VERSIONS、TTL、BLOCKCACHE、CONFIGURATION等属性
create '表名', '列族名1', '列族名2', '列族名3'

create '表名', {NAME => '列族名1', VERSIONS => 版本号, TTL => 过期时间, BLOCKCACHE => true}


# 示例
create 'tbl_user', 'info', 'detail'
create 't1', {NAME => 'f1', VERSIONS => 1, TTL => 2592000, BLOCKCACHE => true}
```

查看 table 详细信息：
```
describe 'test'
```
添加一个列族
```
# 语法 
alter '表名', '列族名'

# 示例
alter 'tbl_user', 'address'
```

删除一个列族
```
# 语法 
alter '表名', {NAME=> '列族名', METHOD=> 'delete'}

# 示例
alter 'tbl_user', {NAME=> 'address', METHOD=> 'delete'}
```

获取表的描述describe
```
# 语法 
describe '表名'

# 示例
describe 'tbl_user'
```

命名空间 
```
# 列举所有命名空间
list_namespace
# 列举空间里所有 table
list_namespace_tables
# 创建空间
create_namespace
```
## dml 命令
插入或者修改数据put
```
# 语法
# 当列族中只有一个列时'列族名:列名'使用'列族名'
put '表名', '行键', '列族名', '列值'
put '表名', '行键', '列族名:列名', '列值'

# 创建表
create 'tbl_user', 'info', 'detail', 'address'

# 第一行数据
put 'tbl_user', 'mengday', 'info:id', '1'
put 'tbl_user', 'mengday', 'info:name', '张三'
put 'tbl_user', 'mengday', 'info:age', '28'
```

插入数据：
```
hbase(main):003:0> put 'test', 'row1', 'cf:a', 'value1'
0 row(s) in 0.0850 seconds
```

查看数据：
```
# 获取表的所有数据语法
scan '表名'
# 示例
scan 'tbl_user'

# 扫描整个列簇语法
scan '表名', {COLUMN=>'列族名'}
# 示例
scan 'tbl_user', {COLUMN=>'info'}

# 扫描整个列簇的某个列语法
scan '表名', {COLUMN=>'列族名:列名'}
# 示例
scan 'tbl_user', {COLUMN=>'info:age'}

# 获取数据get语法
get '表名', '行键'
# 示例
get 'tbl_user', 'mengday'

# 根据某一行某列族的数据语法
get '表名', '行键', '列族名'
# 示例
get 'tbl_user', 'mengday', 'info'
```