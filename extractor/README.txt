Examples of curl

Extract files from directory:
curl.exe -G http://localhost:8080//extract/directory/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm

Extract single file:
curl.exe -G http://localhost:8080/extract/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm

Extract metadata:
curl.exe -G http://localhost:8080/metadata/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm

How to run chm extraction server:

mvn clean install dependency:copy-dependencies exec:java -Dexec.mainClass="org.ocrix.chm.server.ChmServer"
