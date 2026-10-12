# NEXUS backend

Spring Boot 3 REST API using Java 17, Spring Security/JWT, and JPA. Local development uses a persistent H2 database. Production uses a managed MySQL database.

## Local development

Requirements: Java 17+ and Maven.

```powershell
mvn spring-boot:run
```

The local API health endpoint is `http://localhost:8081/api/health`.

## Production deployment

This repository includes a Dockerfile for deployment as a Render Docker Web Service. Set `SPRING_PROFILES_ACTIVE=prod`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` (at least 32 characters) in the hosting provider's environment settings. Set `CORS_ALLOWED_ORIGINS` to the exact deployed frontend origin; it defaults to `https://nexus-24eg107d60.netlify.app`. The database user must have permission to create tables: production startup applies the idempotent `user_domains` table initializer, and Hibernate updates the remaining entity schema. The host supplies `PORT`; the health check path is `/api/health`.

After deploying the API, set the frontend's `VITE_API_URL` to the backend URL ending in `/api`. Set `CORS_ALLOWED_ORIGINS` to the frontend's exact HTTPS origin. Keep all credentials in the hosting provider's secret environment settings.
