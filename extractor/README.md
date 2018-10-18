# Intention
This project is used as an alternative solution of CHM extraction of Apache Tika.
As Apache project it cannot use LGPL 2.1 licence. However if it's not your concern, this implementation much better (IMO). 

## Examples of curls

### Extract files from directory:
```
curl -G http://localhost:8080//extract/directory/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm
```

### Extract single file:
```
curl -G http://localhost:8080/extract/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm
```

### Extract metadata:
```
curl.exe -G http://localhost:8080/metadata/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm
```

### How to run chm extraction server:
```
mvn clean install dependency:copy-dependencies exec:java -Dexec.mainClass="org.ocrix.chm.server.ChmServer"
```
