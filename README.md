
# Worker Shifts Service

This is a application written in Java used for serving a rest API for work planning shifts.




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

http://localhost:8081/api-docs

or

http://localhost:8081/swagger-ui/index.html
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


## Running Tests

To run tests, run the following command

```bash
  mvn test
```
The command will run:

* Unit tests from the **domain** module
* Unit tests from **persistance** module. 
* Integration tests from **server** module
