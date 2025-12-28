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
В `src/main/resources/application.yml`:
```yml
app:
  security:
    jwt:
      secret: "CHANGE_ME_TO_A_LONG_RANDOM_SECRET_32+_CHARS"
```

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
