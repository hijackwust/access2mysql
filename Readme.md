原理是：
用工具 mdb-tools 和 java代码 将 access数据库导出 表结构和数据 的sql语句

1. mdb-tools 开源 但是导出 表字段为 blob 时存在问题无法生成正确的sql语句。

./to_mysql.sh Architecture.mdb > Architecture.sql




2. 用java代码 ExportAccess2mysql 处理 导出图片失败的问题

ExportAccess2mysql 依赖  ucanaccess 库
http://ucanaccess.sourceforge.net/site.html#home

javac -cp .:./lib/ucanaccess-4.0.4.jar:./lib/jackcess-2.1.11.jar:./lib/hsqldb-2.4.0.jar:./lib/commons-lang-2.6.jar:./lib/commons-logging-1.1.3.jar:./lib/commons-io-2.6.jar ExportAccess2mysql.java

java -cp .:./lib/ucanaccess-4.0.4.jar:./lib/jackcess-2.1.11.jar:./lib/hsqldb-2.4.0.jar:./lib/commons-lang-2.6.jar:./lib/commons-logging-1.1.3.jar:./lib/commons-io-2.6.jar ExportAccess2mysql Architecture.mdb  ATT_A3 "" ""
