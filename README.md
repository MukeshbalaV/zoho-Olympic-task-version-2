

---

# Zoho Olympic Task

This project is a **Java Servlet-based application** for managing Olympic player data, including validation and leaderboard retrieval. It allows bulk upload of athletes from CSV, as well as insertion of teams and sports for specific years.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Setup & Configuration](#setup--configuration)
4. [Project Structure](#project-structure)
5. [APIs](#apis)
6. [How to Run](#how-to-run)
7. [Sample Requests](#sample-requests)
8. [Notes](#notes)

---

## Project Overview

The application allows:

* Bulk upload of Olympic athletes via CSV.
* Validation for mandatory fields, team, sport, and duplicate records.
* Leaderboard retrieval by sport and year.
* Insert teams and sports for a given year from raw text input.

---

## Prerequisites

* **Java JDK 17+**
* **Apache Tomcat 10+** or compatible servlet container
* **MySQL 8+**
* **Required JARs:**

  * `mysql-connector-java-x.x.x.jar` (MySQL JDBC driver)
  * `gson-x.x.x.jar` (Gson for JSON serialization)

> Place the JARs in the `WEB-INF/lib` folder of your project.

---

## Setup & Configuration

1. **Database Setup**

```sql
CREATE DATABASE olympics_app;
```

Create supporting tables:

```sql
CREATE TABLE teams (
    team_name VARCHAR(100),
    year INT,
    PRIMARY KEY (team_name, year)
);

CREATE TABLE sports (
    sport_name VARCHAR(100),
    year INT,
    PRIMARY KEY (sport_name, year)
);
```

* The `olympic_players` table is automatically created/reset on server startup by `OlympicsDao.resetOlympicPlayersTable()`.

2. **Configure Database Connection**

Update credentials in `OlympicsDao.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/olympics_app";
private static final String USER = "root";
private static final String PASSWORD = "mukesh24";
```

3. **Add Required Libraries**

Copy `mysql-connector-java-x.x.x.jar` and `gson-x.x.x.jar` to `WEB-INF/lib`.

---

## Project Structure

```
/zoho-olympic-task
│
├─ src/main/java/com/olympictask/
│   ├─ AddOlympicsInfo.java       # POST CSV upload API
│   ├─ GetOlympicsInfo.java       # GET leaderboard API
│   ├─ LoadTeams.java             # POST teams insert API
│   ├─ LoadSports.java            # POST sports insert API
│   ├─ OlympicsDao.java           # DAO for all database operations
│   ├─ LeaderboardDao.java        # DAO for leaderboard retrieval
│   ├─ Athlete.java               # Entity representing Olympic player
│   └─ LeaderboardEntry.java      # Entity for leaderboard response
│
├─ WEB-INF/lib/                   # Place JDBC and Gson JARs here
├─ web.xml                        # Servlet mappings (if not using annotations)
└─ README.md
```

---

## APIs

### 1. Upload Olympic Players

**URL:**

```
POST /zoho-olympic-task/addOlympicsInfo
```

**Request Body:** CSV file (text/plain)
**Headers:** `Content-Type: text/csv`

**CSV Format:**

| SNo | Name | Sex | Age | Team | Games | Year | Season | Sport | Medal |
| --- | ---- | --- | --- | ---- | ----- | ---- | ------ | ----- | ----- |

**Response Example:**

```json
{
  "status": "success",
  "insertedCount": 5,
  "invalidRows": [
    { "rowNumber": "3", "reason": "Invalid sport for year 2024" }
  ]
}
```

---

### 2. Retrieve Leaderboard

**URL:**

```
GET /zoho-olympic-task/getOlympicsInfo?sportName=Swimming&year=2024
```

**Response Example:**

```json
[
  { "name": "Michael Phelps", "teamName": "USA", "medal": "3" },
  { "name": "Ryan Lochte", "teamName": "USA", "medal": "2" }
]
```

---

### 3. Insert Teams

**URL:**

```
POST /zoho-olympic-task/loadTeams?year=2024
```

**Request Body:** Raw text, one team per line

**Response Example:**

```json
{
  "status": "success",
  "insertedCount": 10
}
```

---

### 4. Insert Sports

**URL:**

```
POST /zoho-olympic-task/loadSports?year=2024
```

**Request Body:** Raw text, one sport per line

**Response Example:**

```json
{
  "status": "success",
  "insertedCount": 8
}
```

---

## How to Run

1. Copy the project folder to your Tomcat `webapps` directory.
2. Place `mysql-connector-java-x.x.x.jar` and `gson-x.x.x.jar` inside `WEB-INF/lib`.
3. Start **Tomcat server**.
4. Access APIs using `http://localhost:2025/zoho-olympic-task/...`

---

## Notes

* Duplicate entries in `olympic_players` are silently skipped.
* Teams and sports must have a valid `year` parameter.
* CSV header validation ensures correct order of columns.
* Raw text insert APIs are temporary for quick setup; you can replace them later with proper files or DB seed.

---



---


