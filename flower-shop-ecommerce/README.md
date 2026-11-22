# Flower Shop E-commerce System (Spring Boot + PostgreSQL)

Тема: **Онлайн-магазин цветов** (E-commerce System)

## ✅ Выполнено по требованиям
- Spring Boot (слои: Entity → Repository → Service → REST Controller)
- DTO (Request/Response) для сущностей (Entity не возвращается из REST)
- MapStruct (Entity ↔ DTO)
- Spring Security + JWT
- 3 роли: **ADMIN / USER / MANAGER**
- Функции безопасности:
  - регистрация
  - аутентификация (JWT)
  - смена пароля
  - редактирование профиля
  - создание пользователей админом
  - блокировка/разблокировка и удаление пользователей админом
  - разграничение доступа по ролям
- PostgreSQL + Spring Data JPA + Hibernate
- Liquibase миграции (schema + seed ролей)
- Unit-тесты сервисов (JUnit + Mockito)
- Postman Collection

## Сущности (12 бизнес-сущностей)
User, Role, Address, Category, Product, Inventory, Cart, CartItem, Order, OrderItem, Payment, Delivery

## Как запустить (IntelliJ IDEA Ultimate)
### 1) Поднять PostgreSQL через Docker
```bash
docker compose up -d
```

### 2) Настроить JWT secret
Set database and JWT signing secrets through environment variables (do not commit real secrets).

### 3) Запустить приложение
- Открой проект в IntelliJ → Maven → `spring-boot:run`
или
```bash
mvn spring-boot:run
```

Приложение: http://localhost:8080

## Демо-аккаунт ADMIN
- email: `admin@flowershop.kz`
- password: `Admin123!`
(создаётся при старте в `DataInitializer`)

## Postman
Импортируй файл:
`postman/Flower-Shop.postman_collection.json`

## Подсказка по ролям и доступу
- Public:
  - GET `/api/products`, GET `/api/categories`
- USER:
  - `/api/me`, `/api/addresses`, `/api/cart`, `/api/orders`
- MANAGER:
  - создание/редактирование товаров и категорий + mark-paid
- ADMIN:
  - полный доступ + `/api/admin/users/**`

## Environment setup (updated November 2026)

Database credentials and the JWT secret are **not stored in source files anymore**. Configure them in the shell before launching the application:

**Windows CMD**
```cmd
set DB_URL=jdbc:postgresql://localhost:5432/flower_shop
set DB_USER=postgres
set DB_PASSWORD=YOUR_LOCAL_DATABASE_PASSWORD
set JWT_SECRET=REPLACE_WITH_YOUR_OWN_RANDOM_SECRET_AT_LEAST_32_CHARACTERS
mvn spring-boot:run
```

**macOS/Linux**
```bash
export DB_URL=jdbc:postgresql://localhost:5432/flower_shop
export DB_USER=postgres
export DB_PASSWORD='your_local_password'
export JWT_SECRET='your_unique_random_secret_at_least_32_chars'
mvn spring-boot:run
```

If older commits contained a real DB password or signing secret, **rotate them**: changing the current file does not erase Git history. Use a dedicated local PostgreSQL database and never expose demo credentials on a public server. Run `mvn test` to check available unit tests; CI build is separate from a real database integration test.
