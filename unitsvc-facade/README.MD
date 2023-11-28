#### 注意点

```text
H2数据库区分大小写，故实体类映射时，可能会出现列不存在异常，字段设计时要么驼峰，要么下划线，尽量避免列字段首字母大写。

// 若字段存在首字母大写，可以采用以下方法处理。
@Table(value = "数据表名称", camelToUnderline = false)
```

#### 可选配置文件

`clickhouse.properties`

```text
clickhouse.datasource.url=jdbc:clickhouse://play.clickhouse.com:443/blogs?ssl=true
clickhouse.datasource.username=explorer
clickhouse.datasource.password=root

# 【mapper路径】：mapper.clickhouse.**，【xml路径】：mybatis.**
```

`h2.properties`

```text
h2.datasource.url=jdbc:h2:file:./db/h2;mode=mysql;database_to_upper=false;non_keywords=user;file_lock=socket;auto_server=true;auto_reconnect=true;
h2.datasource.username=root
h2.datasource.password=root
# 控制台配置，只能关闭，若需开启，须在代码里面开启注解。
h2.console.enabled=false
h2.webPort=8082
h2.webAdminPassword=admin

# 【mapper路径】：mapper.h2.**，【xml路径】：mybatis.**
```

`mysql.properties`

```text
h2.datasource.url=jdbc:h2:file:./db/h2;mode=mysql;database_to_upper=false;non_keywords=user;file_lock=socket;auto_server=true;auto_reconnect=true;
h2.datasource.username=root
h2.datasource.password=root

# 【mapper路径】：mapper.mysql.**，【xml路径】：mybatis.**
```