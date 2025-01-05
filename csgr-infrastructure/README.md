# CSGR-Infrastructure

Here you find the docker-compose file for the initial database setup.

## Technical Requirements

- docker (https://docs.docker.com/get-started/get-docker/)
- docker-compose (installed with Docker Desktop) (https://docs.docker.com/compose/install/)

## Setup

To start the docker containers for local development run the following command in the infrastructure folder:

```
docker-compose up -d
```

# Decisions

Why postgresql version 17.2?
- open source
- no licensing fees
- newest stable version
