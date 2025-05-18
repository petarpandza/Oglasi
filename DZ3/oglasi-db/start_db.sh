#!/bin/bash

CONTAINER_NAME=oglasi_container
POSTGRES_USER=oglasi-user
POSTGRES_PASSWORD=admin123
POSTGRES_DB=oglasi-db
SQL_DIR=$(pwd)/scripts
PORT=5432

docker rm -f $CONTAINER_NAME > /dev/null 2>&1

docker run --name $CONTAINER_NAME \
  -e POSTGRES_USER=$POSTGRES_USER \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -e POSTGRES_DB=$POSTGRES_DB \
  -p $PORT:5432 \
  -v $SQL_DIR:/docker-entrypoint-initdb.d \
  -d postgres > /dev/null

docker logs -f $CONTAINER_NAME

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to start..."
until docker exec $CONTAINER_NAME pg_isready -U $POSTGRES_USER -d $POSTGRES_DB > /dev/null 2>&1; do
  sleep 1
done
