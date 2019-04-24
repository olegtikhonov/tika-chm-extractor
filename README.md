# tika-chm-extractor
==================

Uses 7zipjbinding library which is the LGPL license. The rest of the code is Apache License, Version 2.0. 

## What is it all about?

Tika-chm-extractor is an alternative LGPL option extracting context & metadata from compressed html files. “Original” Tika’s chm extractor has some known problems because MS guys could not provide suitable spec of chm files. The algorithm of extracting these files was hacked by error prone approach. However completely compatible with the Apache Software License version 2.0.

## For whom is it intended?

For anyone who does not care about licensing compliances however cares about more precise extraction.

## How to run it?

Before cloning git, please download sevenZipJbining. Preferably for all platforms. Unzip it. There are two jars which you have to install to your local maven directory, like this:

```
cd /sevenzipjbinding_extracted_folder
```

```
mvn install:install-file -Dfile=sevenzipjbinding-AllPlatforms.jar -DgroupId=net.sf.sevenzipjbinding -DartifactId=allplatforms -Dpackaging=jar -Dversion=1.0
```

```
mvn install:install-file -Dfile=sevenzipjbinding.jar -DgroupId=net.sf.sevenzipjbinding -DartifactId=sevenzipjbinding -Dpackaging=jar -Dversion=1.0
```

Next,
```
git clone https://github.com/olegtikhonov/tika-chm-extractor.git
```

```
cd ../tika-chm-extractor/tree/master/extractor
```

```
mvn clean install dependency:copy-dependencies exec:java -Dexec.mainClass="org.ocrix.chm.server.ChmServer"
```
After that, make sure you have installed curl program. Examples of culs may look as follows:


Extract files from directory:
```
curl.exe -G http://localhost:8080//extract/directory/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm
```
Extract single file:
```
curl.exe -G http://localhost:8080/extract/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm
```
Extract metadata:
```
curl.exe -G http://localhost:8080/metadata/file/C:%5CUsers%5Cotikhono%5CDesktop%5Cchm%5Cpython274.chm
```
Drop me a note and let me know what else you’d like to see and what you end up doing with this. I welcome the start of a good discussion.


