CALL ./gradlew clean
CALL ./gradlew shadowJar
CALL java -Djava.net.useSystemProxies=true -jar build/libs/KSVAPIServices-1.0.jar -conf build/resources/main/ksv-backend-conf.json