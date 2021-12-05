# File storage

## Description 

File storage that saves files to local storage. 

In order to clear the directory every time you start, add the value `clearDirectoryAfterStart=true` to [application.properties](src/main/resources/application.properties)

## Build 

```bash
docker build -t file-storage
```

The process may take some time, as gradle has to load all the necessary dependencies

## Run

```bash
docker run -it file-storage
```

