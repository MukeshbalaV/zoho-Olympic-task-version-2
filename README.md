
---

# Olympics Data Management System

## Project Overview
This project is a simple **Olympics Data Management System** that allows you to:

1. **Add Olympic athletes data** via CSV upload.
2. **Fetch leaderboard** data by sport and year.

The project is implemented in **Java (Servlets + JDBC)** and uses **MySQL** as the backend database.  
It performs validation to ensure data integrity while inserting into the database.

---

## Technologies Used
- Java 17+ (Servlets)
- Jakarta Servlet API
- MySQL
- Gson (for JSON responses)
- CSV file upload
- Maven (optional for dependencies)
- Eclipse IDE (or any Java IDE)

---

## Database Setup
1. Start MySQL and create a database called `olympics_app`.
2. Create the following tables:

**Teams Table**
```sql
CREATE TABLE teams (
    id INT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(255),
    year INT
);
````

**Sports Table**

```sql
CREATE TABLE sports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sport_name VARCHAR(255),
    year INT
);
```

**Olympic Players Table**

```sql
CREATE TABLE olympic_players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sno INT,
    name VARCHAR(255),
    age INT,
    sex VARCHAR(10),
    teamName VARCHAR(255),
    sportName VARCHAR(255),
    games VARCHAR(255),
    year INT,
    season VARCHAR(50),
    medal VARCHAR(10),
    row_hash CHAR(64),
    UNIQUE KEY uniq_row_hash (row_hash)
);
```

---

## API Endpoints

### 1. POST `/addOlympicsInfo`

* **Description:** Upload a CSV file to insert athlete data.
* **CSV Columns:**
  `SNo, Name, Sex, Age, Team, Games, Year, Season, Sport, Medal`
* **Validation Rules:**

  1. Mandatory fields: Name, Sex, Games, Season, Year.
  2. Duplicate rows are skipped based on SHA-256 hash of row data.
  3. If **Year exists**, validates Team and Sport for that year.
  4. If **Year does not exist**, row is inserted without checking Team or Sport.
  5. Medal must be one of `0, 1, 2, 3`; else row is skipped.
* **Sample Request:**

```
CSV file with columns:
SNo,Name,Sex,Age,Team,Games,Year,Season,Sport,Medal
139,srrrm,M,23,Spain,2016 Summer,2027,Summer,Swimming,2
```

* **Sample Response:**

```json
{
    "status": "success",
    "insertedCount": 1,
    "skippedRows": [
        {"reason": "invalid team", "name": "srrrm"},
        {"reason": "invalid medal 5", "name": "John Doe"}
    ]
}
```

---

### 2. GET `/getOlympicsInfo?sportName=<sport>&year=<year>`

* **Description:** Fetch leaderboard for a specific sport and year.
* **Query Parameters:**

  * `sportName` (string)
  * `year` (int)
* **Sample Response:**

```json
[
    {"name": "Athlete1", "team": "USA", "medal": "Gold"},
    {"name": "Athlete2", "team": "China", "medal": "Silver"}
]
```

* **Error Example:**

```json
{"message": "Both sport name and year are required"}
```

---

## CSV File Format

* Must have **exact headers**:
  `SNo, Name, Sex, Age, Team, Games, Year, Season, Sport, Medal`
* Fields should be **comma-separated**.
* Example row:

```
139,srrrm,M,23,Spain,2016 Summer,2027,Summer,Swimming,2
```

---



## How to Run Locally

1. Start MySQL and create the database/tables.
2. Import your **team** and **sports** data for relevant years.
3. Place the project in **Eclipse** or any Java IDE.
4. Build and deploy to **Tomcat/Jetty**.
5. Test using **Postman** or frontend:

   * POST CSV: `/addOlympicsInfo`
   * GET leaderboard: `/getOlympicsInfo?sportName=Swimming&year=2027`

---

## Project Folder Structure

```
OfflineTask-Submission/
│
├── src/
│   └── com/
│       └── olympictask/
│           ├── addOlympicsInfo.java
│           ├── getOlympicsInfo.java
│           ├── OlympicsDao.java
│           ├── Athlete.java
│           └── LeaderboardDao.java
│
├── README.md
├── web.xml   (if any)
├── deployment.yaml  (if any)
```

---



