## Demo mTLS Test for Java Application

A simple demo project to test mTLS using Java 17 and Spring Boot.

### Prerequisites:
Java 17
Docker
Docker-compose

### How to use:
First, you need to create your own private certificate authority and client certificates. Then execute the docker container with the server image with mTLS. Lastly, run the Java application.

### Steps
1) Creating certificates
    - I made some adjustments to the commands, but basically everything is already in the original project file [here](https://github.com/lucascunha/demo-mtls-test/blob/master/server/Read.me).

2) Run the server
    - Run the following command in the server folder:
    ```
    docker-compose up -d
    ```

3) Run the Java application
    - Run the following command in the root folder:
    ```
    mvn spring-boot:run
    ```
4) Test the application
    - You can test the application using the following curl command:
    ```
    curl http://localhost:8080/hello
    ```
    - Or you can test opening the url directly from a web browser

### Credits
Original project: https://github.com/gabrielstyce/java17-mtls-test