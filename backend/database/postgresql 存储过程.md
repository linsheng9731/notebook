# PG 存储过程

## 函数定义

```
CREATE FUNCTION somefunc(integer, text) RETURNS integer
AS 'function body text'
LANGUAGE plpgsql;
```
存储过程定义使用关键字 CREATE FUNCTION，PL/pgSQL是块结构（block-structured）语言，上面示例里的function body必须是一个块（block），块结构如下
```
[ <<label>> ]
[ DECLARE
    declarations ]
BEGIN
    statements
END [ label ];
```

总体结构如下：
```
CREATE FUNCTION somefunc() RETURNS integer AS $$
<< outerblock >>
DECLARE
    quantity integer := 30;
BEGIN
    RAISE NOTICE 'Quantity here is %', quantity;  -- Prints 30
    quantity := 50;
    RETURN quantity;
END;
$$ LANGUAGE plpgsql;
```
* PL/pgSQL的BEGIN/END与事务无关，只起分隔块的作用；
* Function和trigger不能发起或提交一个事务，只能运行在外部查询发起的事务里，因为其没有事务的上下文；
* 通过 select function(); 调用
* 可以定义函数返回 void 类型，表示不返回数据

## 变量声明使用 

`变量名 类型` 的格式，默认值使用 `:=` 赋值
```
i integer;
quantity numeric(5);
quantity integer DEFAULT 32;
url varchar := 'http://mysite.com';
id_user CONSTANT integer := 10;
metric:=(select id from push_metrics where project_id=projectId);
```
直接使用变量名使用变量：
```
insert into table values(1, quantity);
```

## 函数形参

函数形参主要有两种方式调用

1) 命名参数，如：
```
CREATE FUNCTION sales_tax(subtotal real) RETURNS real AS $$
BEGIN
    RETURN subtotal * 0.06;
END;
$$ LANGUAGE plpgsql;
```
通过 $n 调用：
```
CREATE FUNCTION sales_tax(real) RETURNS real AS $$
DECLARE
    subtotal ALIAS FOR $1;
BEGIN
    RETURN subtotal * 0.06;
END;
$$ LANGUAGE plpgsql;
```

如上的函数形参传参方式都是值传递，可以通过OUT关键字将形参设置为引用传递，示例：
```
CREATE FUNCTION sales_tax(subtotal real, OUT tax real) AS $$
BEGIN
    tax := subtotal * 0.06;
END;
$$ LANGUAGE plpgsql;
```
这里跟前面示例的sales_tax功能一样，只是前面是作为返回值返回，而这里将计算结果记录在了引用传递的参数里，外部可以在调用函数后访问tax变量获取函数计算结果。这就像C里的指针变量一样，也类似C#里参数的ref关键字。

## 数组遍历
支持对结果集合的遍历：
```
  FOR EID IN SELECT ID FROM custom_events WHERE KEY IN ('key') LOOP
        {do something}  
    END LOOP;
```

## 控制流

RETURN 控制：
```
RETURN expression; 直接返回，结束流程
RETURN NEXT expression; 保存到结果集，继续流程
RETURN QUERY query; 保存到结果集，继续流程，可以带动态参数
```

条件控制：
```
IF ... THEN
IF ... THEN ... ELSE
IF ... THEN ... ELSIF ... THEN ... ELSE
```
例子：
```
IF number = 0 THEN
    result := 'zero';
ELSIF number > 0 THEN
    result := 'positive';
ELSIF number < 0 THEN
    result := 'negative';
ELSE
    result := 'NULL';
END IF;
```
```
drop function update_metric_ref();
create function update_metric_ref() returns integer as $$
declare 
	projectId numeric; -- 定义变量
	metric numeric;
    product RECORD;
begin 
     -- 循环语句
     for projectId in select project_id from push_messages where metrics_ref=1 loop 
          -- 查询结果赋值
          metric:=(select id from push_metrics where project_id=projectId);
          -- 更新引用变量
          update push_messages set metrics_ref=metric where metrics_ref=1 ;
     end loop;
     return 1;
end;

$$ language plpgsql;

```


```
create function update_campaign_schemas() returns integer as $$
declare 
    product_id integer[]; -- 定义变量
    c_id numeric;
    ss character varying[];
    campaign RECORD;
begin 
     -- 循环语句
     for campaign in select * from message_campaigns where is_often=true loop 
     	  product_id:=campaign.product_ids;
     	  c_id:=campaign.id;
          -- 查询结果赋值
          ss:=(select ARRAY_AGG(url_schema) from products where id in (select * from unnest(product_id)) )::character varying[];
          -- 更新引用变量
          update message_campaigns set schemas=ss where id=c_id ;
     end loop;
     return 1;
end;

$$ language plpgsql;

```
