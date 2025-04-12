
# 🛒 Microservices E-commerce Platform

Welcome! This is a **personal microservices-based e-commerce project** designed for learning and hands-on practice. It's built using modern technologies including:

- 🧠 Spring Boot 3 + Spring Cloud (Eureka, Gateway, Feign Client)
- 🐇 RabbitMQ, 🔄 Redis, 🐘 PostgreSQL
- 🔐 Auth0 for authentication & authorization
- 💳 Stripe for payment processing
- ☁️ Cloudinary for image uploads
- 🐳 Docker & Docker Compose for container orchestration

> ⚠️ **Note:** This project is still under active development and is **not production-ready**. Use it to learn, experiment, and improve!

---

## ⚙️ Environment Configuration

Create a `.env` file in the project root directory with the following content.  
Sensitive values have been masked for security purposes:

```env
# Database
DB_USERNAME=*****
DB_PASSWORD=*****

# Redis
REDIS_HOST=redis
REDIS_PORT=6379

# RabbitMQ
RABBIT_HOST=rabbitmq
RABBIT_PORT=5672
RABBIT_USERNAME=*****
RABBIT_PASSWORD=*****

# Eureka
EUREKA_URL=http://eureka-server:8761/eureka

# Mail
MAIL_HOST=smtp-relay.brevo.com
MAIL_PORT=587
MAIL_USERNAME=*****
MAIL_PASSWORD=*****
SENDER_EMAIL=*****
SENDER_NAME=*****

# Auth0
AUTH0_ISSUER=https://your-domain.us.auth0.com/
AUTH0_AUDIENCE=http://localhost:8080/

# Stripe
STRIPE_SECRET_KEY=*****
STRIPE_WEBHOOK_SECRET=*****
STRIPE_SUCCESS_URL=http://localhost:5000/payment?status=success
STRIPE_CANCEL_URL=http://localhost:5000/payment?status=cancel
STRIPE_CURRENCY=usd

# Cloudinary
CLOUDINARY_CLOUD_NAME=*****
CLOUDINARY_API_KEY=*****
CLOUDINARY_API_SECRET=*****
CLOUDINARY_FOLDER_NAME=Uploads

# Database URLs
PRODUCT_DB_URL=jdbc:postgresql://product-db:5432/product_db
CART_DB_URL=jdbc:postgresql://cart-db:5432/cart_db
ORDER_DB_URL=jdbc:postgresql://order-db:5432/order_db
```

---

## 🐳 Run with Docker Compose

### ✅ Step 1: Clone the repository

```bash
git clone https://github.com/ThuatPhan/Badminton4U.git
cd Badminton4U
```

### ✅ Step 2: Create the `.env` file  
Follow the template above and make sure all values are properly set.

### ✅ Step 3: Start Docker Compose

```bash
docker-compose --env-file .env up --build
```

This will build and start all microservices, databases, RabbitMQ, Redis, and the Eureka registry.

> ⏳ First build might take a few minutes depending on your internet speed and machine performance.

---

## 📌 Notes

- Make sure your `.env` file is **never committed to version control**.
- You can extend this system with additional services like:
  - Centralized logging using **ELK stack**
  - Distributed tracing with **Spring Cloud Sleuth + Zipkin**
  - CI/CD pipelines and Kubernetes support

---

## 🙌 Contribution & Feedback

Feel free to fork the repo, suggest changes, or open an issue.  
This is a fun learning project and I’d love to hear how you're using or improving it!

---

Happy coding! 💻🚀
