
# Worker Shifts Service

A Java application serving a rest API for work planning shifts.

#### Project structure
This is a maven project with folowing modules
* **domain** - models and business rules
* **persistence** - storage integration using Spring JDBC Template
* **server** - Rest API server using Spring Boot implementation

#### Models
* Worker - defines worker profile.

  | Fields | Type     | Description                |
  | :-------- | :------- | :------------------------- |
  | `id`      | `string` | **Required**. Worker id defined y user. Ex: `john.doe` |
  | `firstName`      | `string` | **Required**.  |
  | `lastName`      | `string` | **Required**. |
  | `active`      | `boolean` | **Required**. |

* Shift - the definnition of a working shift binded to a worker.

  | Fields | Type     | Description                |
  | :-------- | :------- | :------------------------- |
  | `id`      | `long` | **Required**. The shift start GMT timestamp in seconds |
  | `workerId`      | `string` | **Required**.  |
  | `startTime`      | `timestamp` GMT | **Required**.  Shift start time|

#### Functionnal rules

* The shift is binded to worker
* The shift is 8 hours long
* A worker cannot have more than one shift in a single day
* There are 3 daily shifts: 0-8, 8-16, 16-24

## API Reference

#### Get worker

```http
  GET /api/v1/workers/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id`      | `string` | **Required**. Unique worker id |

response body type: `Worker`

#### Add new worker
The id is of `string` type and shoud be provided in request body by the client.
```http
  POST /api/v1/workers
```

|              | Type     | Description                       |
| :--------    | :------- | :-------------------------------- |
| Request body | `Worker` | Worker payload to be added |

#### Update worker

```http
  POST /api/v1/workers/{workerId}
```

|              | Type     | Description                       |
| :--------    | :------- | :-------------------------------- |
| Request body | `Worker` | Worker payload to be added |


#### Add new shift
The shidft is assigned to worker and it's of 8 hours long. A worker can have one shift/day.
The shift id is computed from starting shift time.
```http
  POST /api/v1/workers/{workerId}/shifts
```

|              | Type     | Description                       |
| :--------    | :------- | :-------------------------------- |
| Request body | `Shift`  | Whift to be added |


#### Find worker shift

```http
  GET /api/v1/workers/{workerId}/shifts/{shiftId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `workerId`      | `string` | **Required**. Unique worker id |
| `shiftId`      | `long` | **Required**. shift id |

response body type: `Shift`

#### Delete worker shift

```http
  DELETE /api/v1/workers/{workerId}/shifts/{shiftId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `workerId`      | `string` | **Required**. Unique worker id |
| `shiftId`      | `long` | **Required**. shift id |

#### Worker shifts lookup

```http
  GET /api/v1/workers/{workerId}/shifts
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `workerId`      | `string` | **Required**. Unique worker id |
| `from`      | `string` | **Required**. Shifts start from (inclusive). Date-time format ISO-8601|
| `to`      | `string` | **Required**. Shifts start to (exclusive). Date-time format ISO-8601|

response body type: `Shift[]`

#### Shifts lookup (all workers)

```http
  GET /api/v1/shifts
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `from`      | `string` | **Required**. Shifts start from (inclusive). Date-time format ISO-8601|
| `to`      | `string` | **Required**. Shifts start to (exclusive). Date-time format ISO-8601|

response body type: `Shift[]`

For detailed API documentation, run application and open:
```http
  http://localhost:8081/api-docs
```
or
```http
  http://localhost:8081/swagger-ui/index.html
```



## Build and Run Locally

Clone the project

Requirements: **Java 11** and **Apache Maven**


Go to the project directory

```bash
  cd work-planning-service
```

Build

```bash
  mvn clean package
```

Start the server

```bash
  java -jar server/target/server-1.0-SNAPSHOT.jar
```

## Build and Run Locally

Clone the project

Requirements: **Java 11** and **Apache Maven**


Go to the project directory

```bash
  cd work-planning-service
```

Build

```bash
  mvn clean package
```

To run tests, run the following command

```bash
  mvn test
```
The command will run:

* Unit tests from the **domain** module
* Unit tests from **persistance** module.
* Integration tests from **server** module


Start the server

```bash
  java -jar server/target/server-1.0-SNAPSHOT.jar
```

Api absolute paths

```http
  POST http://localhost:8081/api/v1/workers
  GET http://localhost:8081/api/v1/workers/{id}
  PUT http://localhost:8081/api/v1/workers/{id}
```
```http
  
  GET http://localhost:8081/api/v1/workers/{workerId}/shifts/{shiftId}
  DELETE http://localhost:8081/api/v1/workers/{workerId}/shifts/{shiftId}
  GET http://localhost:8081/api/v1/workers/{workerId}/shifts?from=2021-12-13T00:00:00Z&to=2021-12-20T00:00:00Z
  POST http://localhost:8081/api/v1/workers/{workerId}/shifts
```


