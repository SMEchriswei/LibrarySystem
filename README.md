# Library System

Build an online library system using JAVA, SpringBoot Framework, and third-party serivces such as AWS S3 and AWS SQS.

## Building Library System

Deploy PostgreSQL on a Docker container

	docker pull postgres
	docker run
	-name [container_name]
	-e POSTGRES_DB=[server_name]
	-e POSTGRES_USER=[admin]
	-e POSTGRES_PASSWORD=[password]
	-p 5433:5432
	-d postgres

Use Flyway database migration tool to create your database schema.

	mvn flyway:clean -Ddb_url=[DATABASE_URL]:[PORT_NUMBER]/[DATABASE_NAME] -Ddb_username=[DATABASE_USERNAME] -Ddb_password=[DATABASE_PASSWORD]
	mvn flyway:migrate -Ddb_url=[DATABASE_URL]:[PORT_NUMBER]/[DATABASE_NAME] -Ddb_username=[DATABASE_USERNAME] -Ddb_password=[DATABASE_PASSWORD]

Set your environment variables in your IDE

```bash
  -Ddatabase.driver=org.postgresql.Driver
  -Ddatabase.dialect=org.hibernate.dialect.PostgreSQL9Dialect
  -Ddatabase.url=jdbc:postgresql://[LOCALHOST_URL]
  -Ddatabase.user=[DATABASE_USERNAME]
  -Ddatabase.password=[DATABASE_DRIVER]
  -Dlogging.level.org.springframework=INFO
  -Dlogging.level.com.ascending=TRACE
  -Dsecret.key=[SECRETKEY]
  -Daws.accessKeyId=[AWS_ACCESSKEYID]
  -Daws.secretKey=[AWS_SECRETKEY]
```

Pull the latest Maven image in docker and run it

```bash
  docker pull maven:3.6.0-jdk-8
  docker run -it maven:3.6.0-jdk-8 /bin/bash
```

Find this container's internal IP address

```bash
  docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ${CONTAINER_ID}
```

Create your database schema within this container and make sure all of your unit tests are successful

```bash
  mvn clean compile flyway:migrate -Ddb_url=[LOCALHOST_URL]:[PORT_NUMBER]/[DATABASE_NAME] -Ddb_username=[USERNAME] -Ddb_password=[PASSWORD]

  mvn test -Ddatabase.driver=org.postgresql.Driver -Ddatabase.dialect=org.hibernate.dialect.PostgreSQL9Dialect -Ddatabase.url=jdbc:postgresql://[LOCALHOST_URL]:[PORT_NUMBER]/[DATABASE_NAME] -Ddatabase.user=[USERNAME] -Ddatabase.password=[PASSWORD] -Dlogging.level.org.springframework=INFO -Dlogging.level.com.ascending=INFO -Dsecret.key=[SECRET_KEY] -Daws.accessKeyId=[AWS_ACCESSKEYID] -Daws.secretKey=[AWS_SECRETKEY]
```

Take the compiled code and package it in its distributable format, such as a JAR.

```bash
  mvn compile package -DskipTests=true -q
```



## Request & Response Examples

### API Resources

  - [GET /healthcheck] (#get healthcheck info, no authorization required)
  - [GET /auth] (#get a new JSON Web Token for authorization, _**all URIs except '/auth' & '/healthcheck' require authorization**_)<br /><br />


  - [GET /books] (#get the current book list)
  - [GET /books/{$bookTitle}] (#get info on the book with the provided book title)
  - [PUT /books/{$bookId}] (#update info on the book with the provided book ID)
  - [POST /books] (#create info on a new book)
  - [DEL /books/{$bookTitle}] (#delete info on the book with the provided book title)<br /><br />



  - [GET /customers] (#get the current customer list)
  - [GET /customers/{$customerName}] (#get info on the customer with the provided customer name)
  - [PUT /customers/{$customerId}] (#update info on the customer with the provided customer ID)
  - [POST /customers] (#create info on a new customer)
  - [DEL /customers/{$customerName}] (#delete info on the customer with the provided customer name)<br /><br />


  - [GET /issuestatuses] (#get the current issue status list)
  - [GET /issuestatuses/{$issuestatusesId}] (#get info on the issue status with the provided issue status ID)
  - [PUT /issuestatuses?bookId={$bookId}&customerId={customerId}] (#update info on the issue status with the provided issue status ID)
  - [POST /issuestatuses?bookId={$bookId}&customerId={customerId}&issueDate={mm/dd/yyyy}] (#create info on a new issue status)
  - [DEL /issuestatuses/issueStatusId={$issuestatusesId}] (#delete info on the issue status with the provided issue status ID)<br /><br />



  - [DEL /files/{$AWS_S3BucketName}] (#delete the bucket with the provided bucket name)
  - [POST /files] (#create a new bucket with the bucket name provided in the request body)
  - [GET /files] (#download the file from the local server, for local testing only)
  - [POST /files/{$AWS_S3BucketName}] (#upload the files to the S3 bucket with the provided bucket name)<br /><br />



### GET /healthcheck

Example: {{host}}/healthcheck

Response body:

		This is a health check controller

### GET /auth

Example: {{host}}/auth

Response body:

		Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwiaWF0IjoxNTg1MTQ4NjEwLCJpc3MiOiJjb20uYXNjZW5kaW5nIiwiZXhwIjoxNTg2MDEyNjEwLCJhbGxvd2VkUmVhZFJlc291cmNlcyI6Ii9kZXB0cywvZGVwYXJ0bWVudHMsL2VtcGxveWVlcywvZW1zLC9hY250cywvYWNjb3VudHMsL3Rlc3QsL2ZpbGVzLC9ib29rcywvY3VzdG9tZXJzLC9pc3N1ZXN0YXR1c2VzLCIsImFsbG93ZWRDcmVhdGVSZXNvdXJjZXMiOiIvZGVwdHMsL2RlcGFydG1lbnRzLC9lbXBsb3llZXMsL2VtcywvYWNudHMsL2FjY291bnRzLC90ZXN0LC9maWxlcywvYm9va3MsL2N1c3RvbWVycywvaXNzdWVzdGF0dXNlcywiLCJhbGxvd2VkVXBkYXRlUmVzb3VyY2VzIjoiL2RlcHRzLC9kZXBhcnRtZW50cywvZW1wbG95ZWVzLC9lbXMsL2FjbnRzLC9hY2NvdW50cywvdGVzdCwvZmlsZXMsL2Jvb2tzLC9jdXN0b21lcnMsL2lzc3Vlc3RhdHVzZXMsIiwiYWxsb3dlZERlbGV0ZVJlc291cmNlcyI6Ii9kZXB0cywvZGVwYXJ0bWVudHMsL2VtcGxveWVlcywvZW1zLC9hY250cywvYWNjb3VudHMsL3Rlc3QsL2ZpbGVzLC9ib29rcywvY3VzdG9tZXJzLC9pc3N1ZXN0YXR1c2VzLCJ9.ZEu1-P5B9O8Se44sBcYkGtltkSZZyzkwpThNptlZoeM

### GET /issuestatuses  (Here we'll only use /issuestatuses URI as a demonstration for simplicity's sake)  

Example: {{host}}/issuestatuses

Response body:

	[
    	{
        	"id": 1,
        	"issueDate": "2020-01-01T05:00:00.000+0000",
        	"returnDate": null
    	}
  ]

### GET /issuestatuses/{$issuestatusesId}

Example: {{host}}/issuestatuses/{$issuestatusesId}

Response body:

	[
    	{
        	"id": 1,
        	"issueDate": "2020-01-01T05:00:00.000+0000",
        	"returnDate": null
    	}
  ]


### PUT /issuestatuses?bookId={$bookId}&customerId={customerId}

Example: {{host}}/issuestatuses?bookId={$bookId}&customerId={customerId}

Response body:

	The issue status has been updated.

### POST /issuestatuses?bookId={$bookId}&customerId={customerId}&issueDate={mm/dd/yyyy}

Example: {{host}}/issuestatuses?bookId={$bookId}&customerId={customerId}&issueDate={mm/dd/yyyy}

Response body:

	The issue status has been created.

### DEL /issuestatuses/issueStatusId={$issuestatusesId}

Example: {{host}}/issuestatuses/issueStatusId={$issuestatusesId}

Response body:

	The issue status has been deleted.
