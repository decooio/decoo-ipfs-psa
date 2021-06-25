# Decoo IPFS Pinning Services

This is an implementation of [IPFS Pinning Services API](https://ipfs.github.io/pinning-services-api-spec/) provided by [Decoo](https://decoo.io). A live PSA service endpoint is also maintained which you check the [wiki page](https://wiki.decoo.io/pinningServicesApi) for usage.

Basically, Decoo PSA sevice pin files in the following way:
1. Pin the file to an [IPFS Cluster](https://cluster.ipfs.io/)
2. Place an *Order* to [Crust Network](https://wiki.crust.network/)
3. Unpin the file from IPFS Cluster after the file is successfully pulled and replicated to Crust Network


# Prerequisites

The PSA service depends on a couple of external applications or services. To build and run the service, you need to:
1. Create an .env file based on the sample [.env-example](https://github.com/decooio/decoo-ipfs-psa/blob/main/.env-example)
2. Deploy each of the dependent service (or find an existing one that is ready to use), and configure required info in .env file
3. Build and run the PSA service

# Deployment and Configurations

## Setup MySQL

1. Setup a MySQL Community Server 8.xx

2. Create a *database* named *decoo* with following SQL command:
```sql
CREATE database decoo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

3. Configure .env file for MySQL connection
```batch
MYSQL_HOST=
MYSQL_PORT=
MYSQL_USERNAME=
MYSQL_PASSWORD=
```

## Setup RabbitMQ

1. Setup RabbitMQ server 3.5.1

2. Configure .env file for RabbitMQ connection
```batch
RABBITMQ_HOST=
RABBITMQ_PORT=
RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=
```

## Setup IPFS Cluster

1. Setup [IPFS Cluster](https://cluster.ipfs.io/)

2. Configure .env file for IPFS Cluster connection
```batch
IPFS_CLUSTER_HOST=
IPFS_NODE_PORT=
```

## Setup Decoo Crust Service

1. Check and setup a [decoo-crust-service](https://github.com/decooio/decoo-crust-service)

2. Configure .env file for Decoo Crust Service connection
```sh
CRUST_SEEDS=
CRUST_ORDER_URL=
```

## Compile and run the PSA Service

```sh
$ mvn spring-boot:run
```

# License
[AGPL 3.0](https://github.com/decooio/decoo-ipfs-psa/blob/main/LICENSE)
