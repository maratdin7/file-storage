# File storage

[![master](https://github.com/maratdin7/file-storage/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/maratdin7/file-storage/actions/workflows/gradle.yml)
[![dev](https://github.com/maratdin7/file-storage/actions/workflows/gradle.yml/badge.svg?branch=dev)](https://github.com/maratdin7/file-storage/actions/workflows/gradle.yml)
## Description 

File storage that saves files to local storage. 

* In order to clear the directory every time you start, add the value `clearDirectoryAfterStart=true` to
[application.properties](src/main/resources/application.properties)

* You can download a file stored in the repository by executing a request

    ```bash
    curl http://localhost:8080/download/{name}
    ```

* When downloading files, the degree of download is displayed

## Build 

```bash
docker build -t file-storage .
```

The process may take some time, as gradle has to load all the necessary dependencies

## Run

```bash
docker run -it -p8080:8080 file-storage
```

