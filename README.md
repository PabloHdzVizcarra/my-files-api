# My Files API

This API is start following the idea of the book Designing Web Apis.

The idea of this project is make an application with which any developer can make a UI application
for handle the file's storage.

## Run Project

This project uses maven to run as it, is a spring application you can run the project with the
following command in the root directory:

```shell
mvn spring-boot:run
```

Create package

```shell
mvn packge
```

run application with the package created

```shell
java -jar target/todoapi-0.0.1-SNAPSHOT.jar
```

You need to have installed JRE or JDK to you can run this project

## Setup

You need add a file application.yml in the route **src/main/resources** for can start this
application. The next Yamel file contains the structure of the file

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create
  datasource:
    username: <your username database>
    password: <your password database>
    url: <the URL for you database>
  mail:
    host: <the host>
    username: <username for your host>
    password: <password for your host>
    port: <port host>
    protocol: smtp

jwt-token:
  secret-key: <the secret with wich the token will be signed>
  expiration-time: <time expiration for tokens. milliseconds>

aws:
  bucket:
    name: <bucket name in aws>
    prefix-name: <prfix to save files in the bucket>

webhook:
  url: <the URL used for the webhook>
```

### Technologies used

- MySQL: database used.
- Mail Service: MailTrap use to receive a mail with info for activate the user before signup.
- AWS S3 Bucket: to save CSV files.
- WebHook: simple API writes with any language that receive some events.
- Spring Security: Controls all security in the application
- JsonWebToken: handle credentials

## API Design

This API was designed trying following REST principles.

The next image is a basic design for this API.
![System Design_ My Files API](https://user-images.githubusercontent.com/55292284/147143280-5e868ef3-d9f5-4ee5-bc9f-3e01ecd6dbf6.jpg)

The next link is the API Specification for this API.

[API Specification](https://github.com/PabloHdzVizcarra/my-files-api/wiki/API-Technical-Specification-MyFiles)

## API Endpoints

The API have the functionalities of CRUD operations in the next link contains the all endpoints with
some examples:

[End Points](https://github.com/PabloHdzVizcarra/my-files-api/wiki/Endpoints-Page)

## API Errors

The API has a specified behavior to handle the errors. The next page contains details about how to
handle errors.

[Error Page](https://github.com/PabloHdzVizcarra/my-files-api/wiki/Error-Page)

## GitHub Actions

**Java Continuous Integration** GitHub Action name

This project has Continuous Delivery (CI) through a GitHub Action, this action simply runs the test
and makes sure that the test always pass.

The GitHub Action **Java Continuous Integration** will be triggered every time a pull request is
made to the main branch of the project.

## WebHook

This API contains a basic webhook which send events, when a file that was tracking is modified. You
can see the webhook specification here:

[WebHook](https://github.com/PabloHdzVizcarra/my-files-api/wiki/WebHook-Events)

Made with love by Pablo Hernandez
