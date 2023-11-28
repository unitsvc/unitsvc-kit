#h2.datasource.url=jdbc:h2:mem:h2_db;mode=mysql;database_to_upper=false;non_keywords=user;auto_reconnect=true;
h2.datasource.url=jdbc:h2:file:./db/h2;mode=mysql;database_to_upper=false;non_keywords=user;file_lock=socket;auto_server=true;auto_reconnect=true;
h2.datasource.username=root
h2.datasource.password=root
# 控制台配置，只能关闭，若需开启，须在代码里面开启注解。
h2.console.enabled=false
h2.webPort=8082
h2.webAdminPassword=admin
