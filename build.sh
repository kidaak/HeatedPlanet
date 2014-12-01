#!/bin/sh
# build.sh
rm -rf bin
mkdir bin 2>/dev/null

find src -name "*.java" |grep -v "Test.java" > sources.txt
javac -source 1.6 -d bin -cp 'bin:lib/h2-1.4.182.jar' @sources.txt
cp -r ./resource ./bin/resource
cp -r ./lib ./bin/lib
