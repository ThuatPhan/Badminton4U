# Badminton4U Microservices

## Overview
Badminton4U is a microservices-based system designed to explore modern software architecture. It brings together key technologies such as service discovery, database management, caching, messaging, and API gateways, all orchestrated with Docker Compose.

While the core functionalities are in place, aspects like logging, API documentation (Swagger), and authentication can be further refined. Future improvements may include enhanced monitoring, security, and scalability.

### System Architecture

![Badminton4U System Architecture](https://res.cloudinary.com/dmz26yafu/image/upload/v1743402689/tytd787cswas7mylxm0p.png)

## Key Components
- **Eureka Server** for service discovery.
- **PostgreSQL** databases for `product`, `cart`, and `order` services.
- **Redis** for caching.
- **RabbitMQ** for messaging.
- **Microservices** for product, cart, order, payment, and notifications.
- **API Gateway** as a single entry point.

## Getting Started

### Start Services
```sh
docker-compose up -d
```

### Stop Services
```sh
docker-compose down
```

### View Logs
```sh
docker-compose logs -f <service-name>
```

## Work In Progress
- **Logging** needs proper integration.
- **Swagger API documentation** is incomplete.
- **Authentication** requires better security.

## Final Thoughts
This project showcases microservices architecture in action. While functional, it continues to evolve with room for improvements!

