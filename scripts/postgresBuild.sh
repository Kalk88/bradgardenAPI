#!/bin/bash
docker pull postgres
sudo docker run -d -p 127.0.0.2:5432:5432 --name bradgardenDB -d postgres:latest
echo Sleeping 5s before loading data
sleep 5s
cat sql/bradgardenDB.sql  sql/insert_games.sql sql/insert_members.sql sql/insert_sessions.sql | psql -h 127.0.0.2 -p 5432 -U postgres
echo Data loaded