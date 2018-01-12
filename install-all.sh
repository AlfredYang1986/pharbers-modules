#!/bin/bash

set -e
echo "installing pharber modules"


echo -e "\033[47;30m 1.installing pharber modules \033[0m"
cd base-modules
mvn clean install
cd ..


echo -e "\033[47;30m 2.installing errorcode modul \033[0m"
cd errorcode
mvn clean install
cd ..


echo -e "\033[47;30m 3.installing pharbers-cli-triats \033[0m"
cd pharbers-cli-traits
mvn clean install
cd ..


echo -e "\033[47;30m 4.installing http \033[0m"
cd http
mvn clean install
cd ..


echo -e "\033[47;30m 5.installing mongodb connect \033[0m"
echo "before intall please start mongod instance"
cd mongodb-connect
mvn clean install
cd ..

echo -e "\033[47;30m 6.installing mongodb driver \033[0m"
echo "before intall please start mongod instance"
cd mongodb-driver
mvn clean install
cd ..


echo -e "\033[47;30m 7.installing mongodb manager \033[0m"
echo "before intall please start mongod instance"
cd mongodb-manager
mvn clean install
cd ..


echo -e "\033[47;30m 8.installing ecrypt \033[0m"
echo "before intall please start mongod instance"
cd encrypt
mvn clean install
cd ..


echo -e "\033[47;30m 9.installing auth-token \033[0m"
echo "before intall please start mongod instance"
cd auth-token
mvn clean install
cd ..


echo -e "\033[47;30m 10.installing pharbers-pattern \033[0m"
cd pharbers-pattern
mvn clean install
cd ..


echo -e "\033[47;30m 11.installing pharber-page-memory \033[0m"
cd pharbers-page-memory
mvn clean install
cd ..


echo -e "\033[47;30m 12.installing pharbers-data-parse \033[0m"
cd pharbers-data-parse
mvn clean install
cd ..


echo -e "\033[47;30m 13.installing pharbers-sercurity \033[0m"
cd pharbers-sercurity
mvn clean install
cd ..


echo -e "\033[47;30m 14.installing pharbers-message \033[0m"
cd pharbers-message
mvn clean install
cd ..


echo -e "\033[47;30m 15.installing pharbers-bson-writer \033[0m"
cd pharbers-bson-writer
mvn clean install
cd ..


echo -e "\033[47;30m 16.installing pharbers-memory \033[0m"
cd pharbers-memory
mvn clean install
cd ..


echo -e "\033[47;30m 17.installing pharbers-max-util \033[0m"
cd pharbers-max-util
mvn clean install
cd ..


echo -e "\033[47;30m 18.installing pharbers-redis-driver \033[0m"
cd pharbers-redis-driver
mvn clean install
cd ..

echo -e "\033[47;30m 19.installing xmpp-em \033[0m"
cd xmpp-em
mvn clean install
cd ..

set +e
echo "finish, have fun"
