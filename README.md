# kajappka-game-service

FIXME

## Usage

The following environment variables can be used to customize the response:

VERIFIER_URI - the URL of authentication service, defaults to  http://localhost:8000/success,
PORT - the port which the service listens on, defaults to 9001,
DB_PATH - the path of the Sqlite database used, defaults to `$HOME/.games`.

### Running the application locally

`lein api`

### Building standalone JAR

`lein build`

It can be run with `java -jar <jar>`.

### Packaging as war

`lein with-profile api ring uberwar`

### Running tests

`lein test`

### Running local Docker image

For development, you can use provided Docker Compose startup file as follows:

```
$ docker-compose -f docker/development/docker-compose.yml up
```

It boots the Mock Verifier container and the game
service itself for local testing and exposes the service at `http://localhost:9001`
