#!/bin/bash

echo "installing pharber modules"

echo "1.installing pharber-module"
cd base-modules
mvn clean install
cd ..

echo "2.installing errorcode module"
cd errorcode
mvn clean install
cd ..

echo "11.installing pharbers-cli-triats"
cd pharbers-cli-traits
mvn clean install
cd ..

echo "3.installing http"
cd http
mvn clean install
cd ..

echo "4.installing mongodb connect"
echo "before intall please start mongod instance"
cd mongodb-connect
mvn clean install
cd ..

echo "5.installing mongodb driver"
echo "before intall please start mongod instance"
cd mongodb-driver
mvn clean install
cd ..

echo "6.installing mongodb manager"
echo "before intall please start mongod instance"
cd mongodb-manager
mvn clean install
cd ..

echo "7.installing ecrypt"
echo "before intall please start mongod instance"
cd encrypt
mvn clean install
cd ..

echo "8.installing em-xmpp"
echo "before intall please start mongod instance"
cd xmpp-em
mvn clean install
cd ..

echo "9.installing auth-token"
echo "before intall please start mongod instance"
cd auth-token
mvn clean install
cd ..

echo "10.installing pharbers-pattern"
cd pharbers-pattern
mvn clean install
cd ..

echo "11.installing pharbers-message"
cd pharbers-message
mvn clean install
cd ..

echo "finish, have fun"
