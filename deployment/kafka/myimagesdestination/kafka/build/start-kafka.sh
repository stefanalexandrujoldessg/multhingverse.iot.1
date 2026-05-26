#!/bin/sh
echo inside
exec "/kafka/bin/kafka-server-start.sh" "/kafka/config/server.properties"