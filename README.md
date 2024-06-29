Sample code for the blog article [Faking time in JVM Process](https://madhur.co.in/blog/2024/07/01/mocking-jvm-time.html) 

### Run using Maven
```shell
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentpath:./src/main/resources/libfaketime -XX:+UnlockDiagnosticVMOptions -XX:DisableIntrinsic=_currentTimeMillis -XX:CompileCommand=quiet -XX:CompileCommand=exclude,java/lang/System.currentTimeMillis -XX:CompileCommand=exclude,jdk/internal/misc/VM.getNanoTimeAdjustment"
```


### Get current time

```shell
curl --location 'localhost:8080/time/getTime'

{
    "localDateTime": "2024-06-29T15:42:41.973356",
    "timestamp": 1719655961973,
    "instant": "2024-06-29T10:12:41.973377Z",
    "date": "2024-06-29T10:12:41.973+00:00"
}
```

### Set fake time
```shell
curl --location --request POST 'localhost:8080/time/setTime?dateTime=2024-08-01T00%3A00%3A00'

Time set
````

### Get current time
```shell
curl --location 'localhost:8080/time/getTime'

{
    "localDateTime": "2024-08-01T00:00:35.085",
    "timestamp": 1722450635085,
    "instant": "2024-07-31T18:30:35.085Z",
    "date": "2024-07-31T18:30:35.085+00:00"
}
```

### Reset back to real time
```shell
curl --location --request POST 'localhost:8080/time/resetTime'
```