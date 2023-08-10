#!/bin/bash
./gradlew clean
./gradlew shadowJar
java -Djava.net.useSystemProxies=true -jar build/libs/KSVAPIServices-1.0.* -conf build/resources/main/ksv-backend-conf.json
