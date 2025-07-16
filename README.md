# Space Reservation Application

## About

"Space Reservation Application" is a platform where people can find spaces for their events or meetings and reserve them
in demand for a specific period.

## How to Run

### 1. Install Docker

You can either install [Docker Desktop](https://docs.docker.com/get-started/get-docker/) or
[Docker Engine](https://docs.docker.com/engine/install/ubuntu/) for Ubuntu.

### 2. Declare Environment Variables

Copy the [`docker/sample.env`](docker/sample.env) file as `docker/.env` file and change the values with real ones.

### 3. Docker Setup

#### 3.1 Create Network

Create the `sra-net` network running the following command:

`docker network create sra-net`

#### 3.2 Create Volume

Create the `sra-vol` using the following command:

`docker volume create sra-vol`

_Note:_ you may need to run the commands with `sudo`.

#### 3.3 Resolve Conflicts

If you already have MySQL up and running on your local device, change the port in the [`docker-compose.yml`](docker/docker-compose.yml) file.

```yml
services:
  db:
    ...
    ports:
      - '<new_port>:3306'
...
```

### 4. Run The Services

In the root directory of the project run the following command:

`docker compose up -d`
