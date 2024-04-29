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
    - I made some adjustments to the commands, but basically everything is already in the original project file.

    - Make sure you are positioned in the folder server/

    - Create the subfolders 'certs' and 'clients'
    ```
        mkdir -p nginx/certs/clients
    ```

    - Generates self-signed server certificate
    ```
        openssl genpkey -algorithm RSA -out nginx/certs/localhost.key
        openssl req -new -key nginx/certs/localhost.key -out nginx/certs/localhost.csr -config template.csr.conf
        openssl x509 -req -days 365 -in nginx/certs/localhost.csr -signkey nginx/certs/localhost.key -out nginx/certs/localhost.crt
    ```

    - Generates self-signed client certificate
    ```
        openssl genpkey -algorithm RSA -out nginx/certs/clients/client.key
        openssl req -new -key nginx/certs/clients/client.key -out nginx/certs/clients/client.csr -config template.csr.conf
        openssl x509 -req -days 365 -in nginx/certs/clients/client.csr -signkey nginx/certs/clients/client.key -out nginx/certs/clients/client.crt
    ```

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
