# Simple Web Server

This project is a simple web server written in Java that handles HTTP `GET` and `POST` requests. It was developed as part of an academic exercise to learn the basics of web servers and HTTP request handling.

## Features

- **HTTP Request Handling:** Supports `GET` and `POST` requests.
- **Static Files:** Serves static files (HTML, CSS, JS) from the `src/main/resources/static` folder.
- **Dynamic Routes:** Generates personalized responses for routes like `/hello?name=John`.
- **Scalability:** Handles multiple concurrent requests using threads.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 11 or higher:**  
  You can download it from [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source alternative like [OpenJDK](https://openjdk.org/).

- **Apache Maven:**  
  Maven is used for building and managing the project. Download it from the [official Maven website](https://maven.apache.org/download.cgi).

- **Git (optional but recommended):**  
  Git is used for version control. You can download it from [git-scm.com](https://git-scm.com/).

### Verify Installation

1. **Java:**
   ```bash
   java -version

## Setup

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/simple-web-server.git

2. Navigate to the project directory:
    cd simple-web-server

3. Compile the project with Maven:
    mvn clean package

4. Run the server:
    java -cp target/SimpleWebServer-1.0-SNAPSHOT.jar edu.escuelaing.arem.Server

The server will be listening at http://localhost:8080.

## Usage

### GET Requests

#### Static Files:

Access static files in the `src/main/resources/static` folder. For example:

- http://localhost:8080/index.html
- http://localhost:8080/styles.css
- http://localhost:8080/scripts.js

#### Screenshot:
Static File <!-- Replace with the correct path to your screenshot -->

### Dynamic Responses:

Use the `/hello` route with a `name` parameter to receive a personalized greeting:

- http://localhost:8080/hello?name=John

Response: `Hello, John!`

Screenshot:


## POST Requests

Use the `/hellopost` route with a `name` parameter to receive a personalized greeting from a POST request:

### Example request:

```bash
curl -X POST http://localhost:8080/hellopost?name=Jane
```

## Testing:

The project includes unit tests to verify the functionality of the server and request handler. To run the tests, use the following command:

```bash
mvn test
```

### The tests cover:

- Handling of GET and POST requests.

- Serving static files.

- Error handling (404, 500).

### Screenshot:





