FROM postgres:16.2-alpine

COPY initial_script.sql /docker-entrypoint-initdb.d/01_db_create.sql

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB apsidb
