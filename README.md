# Profile Classification API — Data Persistence Service

A Spring Boot REST API that accepts a name, calls three external classification APIs in parallel, applies business logic, and stores the result in a PostgreSQL database. Built as part of the HNG Internship Stage 1 Backend Assessment.

---

## Live API

**Base URL:** `https://datapersistenceapi.pxxl.click`

**Test it:**
```
POST https://datapersistenceapi.pxxl.click/api/profiles
Content-Type: application/json

{ "name": "john" }
```

---

## What It Does

For every name submitted, the API:

1. Calls **Genderize.io** → gets gender and probability
2. Calls **Agify.io** → gets predicted age
3. Calls **Nationalize.io** → gets top nationality by probability
4. Applies classification logic (age group, top country)
5. Stores the result in PostgreSQL
6. Returns the structured profile — or the existing one if the name was already submitted

All three external API calls happen **in parallel** for fast response times.

---

## Endpoints

### 1. `POST /api/profiles` — Create Profile

**Request body:**
```json
{ "name": "ella" }
```

**Success — 201 Created:**
```json
{
  "status": "success",
  "data": {
    "id": "b3f9c1e2-7d4a-4c91-9c2a-1f0a8e5b6d12",
    "name": "ella",
    "gender": "female",
    "gender_probability": 0.99,
    "sample_size": 1234,
    "age": 46,
    "age_group": "adult",
    "country_id": "NG",
    "country_probability": 0.85,
    "created_at": "2026-04-18T21:40:00Z"
  }
}
```

**Already exists — 200 OK:**
```json
{
  "status": "success",
  "message": "Profile already exists",
  "data": { ...existing profile... }
}
```

---

### 2. `GET /api/profiles/{id}` — Get Single Profile

```
GET /api/profiles/b3f9c1e2-7d4a-4c91-9c2a-1f0a8e5b6d12
```

**Success — 200 OK:**
```json
{
  "status": "success",
  "data": {
    "id": "b3f9c1e2-7d4a-4c91-9c2a-1f0a8e5b6d12",
    "name": "john",
    "gender": "male",
    "gender_probability": 0.99,
    "sample_size": 1234,
    "age": 25,
    "age_group": "adult",
    "country_id": "NG",
    "country_probability": 0.85,
    "created_at": "2026-04-18T21:40:00Z"
  }
}
```

---

### 3. `GET /api/profiles` — Get All Profiles

Supports optional filters via query parameters (case-insensitive).

```
GET /api/profiles?gender=male&country_id=NG&age_group=adult
```

**Success — 200 OK:**
```json
{
  "status": "success",
  "count": 2,
  "data": [
    {
      "id": "id-1",
      "name": "john",
      "gender": "male",
      "age": 25,
      "age_group": "adult",
      "country_id": "NG"
    }
  ]
}
```

**Query Parameters:**

| Parameter    | Description           | Example  |
|--------------|-----------------------|----------|
| `gender`     | Filter by gender      | `male`   |
| `country_id` | Filter by country code | `NG`    |
| `age_group`  | Filter by age group   | `adult`  |

---

### 4. `DELETE /api/profiles/{id}` — Delete Profile

```
DELETE /api/profiles/b3f9c1e2-7d4a-4c91-9c2a-1f0a8e5b6d12
```

Returns **204 No Content** on success.

---

## Classification Rules

**Age groups (from Agify):**

| Age Range | Group     |
|-----------|-----------|
| 0 – 12    | child     |
| 13 – 19   | teenager  |
| 20 – 59   | adult     |
| 60+       | senior    |

**Nationality:** the country with the highest probability from Nationalize is selected.

---

## Error Responses

All errors follow this structure:

```json
{ "status": "error", "message": "<error message>" }
```

| Status | Scenario                                     |
|--------|----------------------------------------------|
| `400`  | Missing or empty name                        |
| `404`  | Profile not found                            |
| `422`  | Invalid type for name parameter              |
| `502`  | External API returned invalid or no data     |
| `500`  | Unexpected server error                      |

**Edge cases that return 502:**
- Genderize returns `gender: null` or `count: 0`
- Agify returns `age: null`
- Nationalize returns no country data

---

## Running Locally

**Prerequisites:** Java 17+, Maven, PostgreSQL

**1. Clone the repo:**
```bash
git clone https://github.com/etoroumoren/data_persistence_api.git
cd data_persistence_api
```

**2. Create a local database:**
```sql
CREATE DATABASE profiles_db;
```

**3. Update `application.properties`:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/profiles_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

**4. Run the app:**
```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`.

---

## Testing Locally

```bash
# Create a profile
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"name": "john"}'

# Get all profiles
curl http://localhost:8080/api/profiles

# Get with filters
curl "http://localhost:8080/api/profiles?gender=male&country_id=NG"

# Get by ID
curl http://localhost:8080/api/profiles/{id}

# Delete
curl -X DELETE http://localhost:8080/api/profiles/{id}
```

---

## Project Structure

```
src/main/java/com/apiPersistence/dataPersistenceApi/
├── config/
│   └── CorsConfig.java               # Global CORS configuration
├── controller/
│   └── ProfileController.java        # All REST endpoints
├── service/
│   ├── ProfileService.java           # Core business logic
│   ├── ExternalApiService.java       # Parallel external API calls
│   └── ProfileClassifier.java        # Age group + nationality logic
├── entity/
│   └── Profile.java                  # JPA entity mapped to profiles table
├── repository/
│   └── ProfileRepository.java        # Spring Data JPA repository
├── dto/
│   └── external/
│       ├── GenderizeResponse.java    # Genderize API response shape
│       ├── AgifyResponse.java        # Agify API response shape
│       ├── NationalizeResponse.java  # Nationalize API response shape
│       ├── ProfileResponseDTO.java   # Full profile response
│       └── ProfileSummaryDTO.java    # Summary for list endpoint
├── mapper/
│   └── ProfileMapper.java            # Entity → DTO mapping
└── exception/
    ├── GlobalExceptionHandler.java   # Centralized error handling
    ├── ExternalApiException.java     # 502 upstream errors
    ├── ProfileNotFoundException.java  # 404 errors
    └── InvalidRequestException.java  # 400 errors
```

---

## Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4
- **Database:** PostgreSQL (Neon — cloud hosted)
- **ORM:** Spring Data JPA + Hibernate
- **HTTP Client:** RestClient (parallel calls via CompletableFuture)
- **UUID:** UUID v7 via uuid-creator
- **Build Tool:** Maven
- **Deployment:** PXXL App
