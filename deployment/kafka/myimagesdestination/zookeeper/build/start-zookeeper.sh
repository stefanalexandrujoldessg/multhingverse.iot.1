#!/bin/sh
echo inside
exec "/kafka/bin/zookeeper-server-start.sh" "/kafka/config/zookeeper.properties"