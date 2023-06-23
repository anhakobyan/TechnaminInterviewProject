# TechnaminInterviewProject

## Guideline

### Preinstall

[Docker](https://docs.docker.com)

### Run
Start required services using docker.
```
docker-compose build
docker-compose up
```

you can use `docker-compose.yml` configuration file from the project.

Or you can connect to your preinstalled services(MongoDB, RabbitMQ) changing application.properties file.


You need to pass input file path as application argument or place file in default location. (find in application properties)
