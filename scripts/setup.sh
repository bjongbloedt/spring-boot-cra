#!/bin/sh

docker run --name postgres -e POSTGRES_PASSWORD=mysecretpostgrespassword -p 5432:5432 -d postgres:13-alpine
