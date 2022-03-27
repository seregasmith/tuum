Account Management Application (assognment of Tuum)

Allows users to create an account or transaction and get account with available currencies and list of all account's transactions.

NOTE.
There is a docker-compose.yml. It doesn't work, because I didn't find a solution how replace a host from localhost to service name.
I'd like to test by functional tests using https://www.testcontainers.org/modules/docker_compose/
but I was stuck with docker_compose itself.

RUN

before execute on terminal:

docker pull postgres

docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword postgres

docker pull rabbitmq

docker run -d -p 5672:5672 15672:15672 rabbitmq

then build a jar file for gateway module and rpc-server

run gateway and rpc-server in docker using Dockerfile inside of modules.

The default port is 8091

Postman collection and endpoints are there:
tuum\tuum.postman_collection.json