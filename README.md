# revolut_test

Required Java 11 and Maven 3.6.0

## build before run with:
 
mvn clean package

## run

java -jar target/revolut_test-1.0-jar-with-dependencies.jar

## default listen port is 8080, to change it, start with

java -Dport=18080 -jar target/revolut_test-1.0-jar-with-dependencies.jar


## REST API 

### get account by id

curl -X GET localhost:8080/account/{id}

### create account with currency

curl -X POST -d "currency={CURRENCY}" localhost:8080/account
 
### deposit to account with currency

curl -X POST -d "currency={CURRENCY}&amount={AMOUNT}" localhost:8080/deposit/{id}

### transfer between accounts with currency

curl -X POST -d "currency={CURRENCY}&amount={AMOUNT}&from={ACCOUNT_ID}&to={ACCOUNT_ID}" localhost:8080/transfer

### withdrawal from account with currency

curl -X POST -d "currency={CURRENCY}&amount={AMOUNT}" localhost:8080/withdrawal/{id}


## example

curl -X POST -d "currency=USD" localhost:8080/account

curl -X GET localhost:8080/account/1

curl -X POST -d "currency=USD" localhost:8080/account

curl -X GET localhost:8080/account/2

curl -X POST -d "currency=USD&amount=100" localhost:8080/deposit/1

curl -X POST -d "currency=USD&amount=100&from=1&to=2" localhost:8080/transfer

curl -X POST -d "currency=USD&amount=100" localhost:8080/withdrawal/2