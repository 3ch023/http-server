# HTTP Web Server
Simple HTTP Web Server supporting GET and HEAD methods.

## Build & Run

Build the project with maven to generate executable jar
```
mvn clean package
```

Run generated jar. This will start the server.
```
java -jar target/HttpServer-1.0-SNAPSHOT-jar-with-dependencies.jar
```

##### Running in the IDE
Make sure you build the project before running it in Intellij Idea or Eclipse. Application looks for resources in target/classes folder.

### Configuration

The server will start on port 8081 by default. You can change this value in config.properties.
Served resources are packed within the jar. Configuring the Web Server root is not supported.

### Limitations
1. File types served are limited to the capabilities of URLConnection.guessContentTypeFromStream(is), so Web Server is not serving PDF, MS Word formats etc.
2. absoluteURIs are not supported, so next request will not be served properly
```
GET http://www.w3.org/pub/WWW/TheProject.html HTTP/1.1
...
```

### Troubleshooting