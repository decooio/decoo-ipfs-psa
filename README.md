# decoo-ipfs-psa

## Building

#### 1.Install MySQL and create database

Install MySQL version higher than 8.0

Create decoo database

```sql
CREATE database decoo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

#### 2.Install RabbitMQ

Install RabbitMQ version 3.5.1

#### 3.Install Maven

Install Maven

#### 4.Set ENV as .env-example

#### 5.Start project

```shell
mvn spring-boot:run
```