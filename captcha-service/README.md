# captcha-service

Spring Boot + Java 20 + MySQL CAPTCHA microservice.

## Tech stack

- Java 20
- Spring Boot 3.5.5
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- IntelliJ IDEA

## 1. Create MySQL database

Run:

```sql
CREATE DATABASE captcha_db;
```

The application creates the `captchas` table automatically because:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## 2. Configure MySQL password

Open:

`src/main/resources/application.properties`

Change:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

to your actual MySQL password.

## 3. Run in IntelliJ IDEA

1. Open the `captcha-service` folder in IntelliJ IDEA.
2. Open `pom.xml` and let IntelliJ import Maven dependencies.
3. Make sure Project SDK is Java 20.
4. Run `CaptchaServiceApplication`.

Application URL:

`http://localhost:8081`

## 4. Test health

GET:

`http://localhost:8081/api/captcha/health`

Expected:

```json
{
  "service": "captcha-service",
  "status": "UP"
}
```

## 5. Generate CAPTCHA

POST:

`http://localhost:8081/api/captcha/generate`

No request body.

Example response:

```json
{
  "captchaId": "generated-uuid",
  "captchaText": "AB23XY",
  "imageBase64": "iVBORw0KGgoAAA...",
  "expiresAt": "2026-09-25T12:30:00"
}
```

`imageBase64` contains a PNG image encoded as Base64.

## 6. Validate CAPTCHA

POST:

`http://localhost:8081/api/captcha/validate`

Body:

```json
{
  "captchaId": "generated-uuid",
  "captchaCode": "AB23XY"
}
```

Successful response:

```json
{
  "captchaId": "generated-uuid",
  "valid": true,
  "message": "CAPTCHA validation successful"
}
```

The CAPTCHA becomes unusable after successful validation and expires after 5 minutes.

## IntelliJ API testing

You can create:

`captcha-service.http`

with:

```http
### Health
GET http://localhost:8081/api/captcha/health

### Generate CAPTCHA
POST http://localhost:8081/api/captcha/generate
Content-Type: application/json

### Validate CAPTCHA
POST http://localhost:8081/api/captcha/validate
Content-Type: application/json

{
  "captchaId": "PUT_CAPTCHA_ID_HERE",
  "captchaCode": "PUT_CAPTCHA_CODE_HERE"
}
```

## Important production note

For a real public-facing CAPTCHA, do not return the plaintext CAPTCHA code in the API response. This starter project returns it only to make local development and testing easy. In production, store a hash or otherwise protect the verification secret and add rate limiting, abuse protection, and appropriate image-generation controls.
