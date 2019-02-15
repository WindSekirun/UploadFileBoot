# UploadFileBoot

Application that serving file powered by Spring Boot 2.x and Kotlin

This project is demo project for publish **Tutorial of RESTful API with Spring Boot and Kotlin**, So DO NOT USE IN PRODUCTION.

This project contains...

* Spring Boot 2.1 + Kotlin 1.3
* Dockerfile for dockerize service (Custom Dockerfile, docker-compose.yml)
* Jenkinsfile for Continous Delivery (NOT IMPLEMENTED)
* Swagger 2 for API Document

## Build & Run

```docker-compose up -d --build```

* Base URL of API: [http://localhost:8080](http://localhost:8080)
* Document URL of API: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## API List

Currently, 'UploadFileBoot' support 3 API(s). You can see document of each api on API Document.

* POST /uploadFile : ```curl -XPOST -F extension=PNG -F file=$localfilename 'http://localhost:8080/uploadFile'```
  * Request to upload single file and return name of file.
* GET /listFile: ```curl -XGET 'http://localhost:8080/listFile'```
  * Get list of uploaded files
* GET /files/{filename} : ```curl -XGET 'http://localhost:8080/files/filename.png'```
  * Get binary data of uploaded file

## License

MIT License, [LICENSE URL](https://github.com/WindSekirun/UploadFileBoot/blob/master/LICENSE)
