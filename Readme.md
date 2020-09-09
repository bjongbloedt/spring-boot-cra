### Todo
- Bring up postgres docker container in development, maybe script a dev bootstrap thing?
- Write Component Test
- Write fullstack test


### Things

#### Building Docker Container
```
./gradlew bootBuildImage
```

#### Running Docker Image
```
docker run -p 8080:8080 benjongbloedt/widget-service:0.0.1-SNAPSHOT
```