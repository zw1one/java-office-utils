# java-office-utils

java操作office的代码 - web

### 一、功能

**Office Demo**
```
1、todo java操作word-doc demo
2、todo java操作word-docx demo
3、todo java操作excel-xlsx demo（用阿里的excel工具包实现）
```
**Feature**
```
1、excel文件导入到mongoDB √
```
**Web Demo**
```
1、todo 加上接口下载（输出流）文档  
```

等待填坑中...（填不上了）

### 二、运行

* IDEA运行(必须加)
```
IDEA的Active profiles处，添加`dev`
```

##### 部署Web

* Maven打包命令
```
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Pdev
mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Prelease
```

打包完idea启动项目报错，Rebuild project或者mvn clean

* jar包运行命令
```
(java -server -Xmx1024m -Xms256m -XX:+UseParallelGC -XX:ParallelGCThreads=20  -jar File-To-Database-Web-1.0-SNAPSHOT.jar &)
```


* web入口  
<a>http://127.0.0.1:10086/</a>

* swagger接口文档  
<a>http://127.0.0.1:10086/swagger-ui.html</a>

