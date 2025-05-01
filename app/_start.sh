#!/bin/bash

chmod a+x /app/gradlew
chmod -R a+rwX /app

cd /app

#gradle clean build -i --stacktrace

gradle buildFatJar --no-daemon >/app/__stdout.log 2>/app/__stderr.log

cp /app/build/libs/*.jar /app/ktor-sample.jar
/opt/java/openjdk/bin/java -jar /app/ktor-sample.jar
