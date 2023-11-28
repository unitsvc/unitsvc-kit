# 通用工具类

<a target="_blank" href="https://search.maven.org/artifact/com.unitsvc.kit/unitsvc-all">
    <img src="https://img.shields.io/maven-central/v/com.unitsvc.kit/unitsvc-all.svg?label=Maven%20Central" />
</a>

`安装`

```xml
  <repositories>
        <repository>
            <id>maven-snapshots</id>
            <name>maven-snapshots</name>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <releases>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </releases>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
        <repository>
            <id>central</id>
            <url>https://maven.aliyun.com/repository/central</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
        <repository>
            <id>public</id>
            <url>https://maven.aliyun.com/repository/public</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>

    </repositories>
```
```xml
<!-- 常用工具包，只支持poi4.x、5.x版本，需要引入不同版本的依赖 -->
<dependency>
    <groupId>com.unitsvc.kit</groupId>
    <artifactId>unitsvc-all</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

`4.x版本poi依赖`

```xml

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>4.1.2</version>
</dependency>
```

```xml

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>4.1.2</version>
</dependency>
```

`5.x版本poi依赖`

```xml

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>5.2.3</version>
</dependency>
```

```xml

<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>
```

`facade扩展工具包`

```xml
<dependency>
    <groupId>com.unitsvc.kit</groupId>
    <artifactId>unitsvc-facade</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

```xml
<!-- 依赖版本 -->
<dependency>
    <groupId>org.antlr</groupId>
    <artifactId>antlr4-runtime</artifactId>
    <version>4.13.1</version>
</dependency>
```