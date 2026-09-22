# SecureDesk – Intelligent IT Helpdesk and Incident Management System

A Spring Boot mini project demonstrating 5 GoF Design Patterns in a realistic
IT helpdesk workflow:

| Pattern | Where | Why |
|---|---|---|
| **Factory Method** | `pattern/factory` | Builds the correct ticket type (Hardware/Software/Network/Security/Access) with the right defaults, without if-else in the service layer |
| **Strategy** | `pattern/strategy` | Swaps the priority-calculation algorithm based on ticket category |
| **State** | `pattern/state` | Controls valid ticket lifecycle transitions (OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED) and rejects illegal ones |
| **Observer** | `pattern/observer` | Notifies employee/support staff whenever a ticket event happens |
| **Chain of Responsibility** | `pattern/chain` | Escalates an unresolved ticket through Level1 → Level2 → Network Specialist → Security Specialist |

---

## PHASE 0 — Prerequisites to install on Kali Linux

Run these once, in order.

```bash
# 1. Update package lists
sudo apt update && sudo apt upgrade -y

# 2. Install Java 17 (JDK) — required by Spring Boot 3.x
sudo apt install -y openjdk-17-jdk
java -version        # should print "17.x.x"

# 3. Install Maven — builds the project and downloads dependencies
sudo apt install -y maven
mvn -version

# 4. Install MySQL Server
sudo apt install -y mysql-server
sudo systemctl enable --now mysql
sudo systemctl status mysql          # confirm it's "active (running)"

# 5. (Optional but recommended) Secure MySQL and set root password
sudo mysql_secure_installation

# 6. Install Git
sudo apt install -y git
git --version

# 7. Install Postman
#    Easiest on Kali: download the .tar.gz from postman.com and extract, OR:
sudo snap install postman
#    If snapd isn't installed: sudo apt install -y snapd
```

### Set the MySQL root password to match the project (or edit application.properties)

```bash
sudo mysql
```
Inside the MySQL prompt:
```sql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
FLUSH PRIVILEGES;
EXIT;
```
> If you'd rather use your own MySQL username/password, just edit
> `src/main/resources/application.properties` (`spring.datasource.username`
> and `spring.datasource.password`) to match.

The database `securedesk_db` itself does **not** need to be created manually —
`createDatabaseIfNotExist=true` in the JDBC URL creates it automatically the
first time the app starts, and `spring.jpa.hibernate.ddl-auto=update` creates
all the tables from the JPA entities.

---

## PHASE 1 — Get the project onto your Kali machine

If you received this as a zip:
```bash
cd ~/Desktop
unzip securedesk.zip
cd securedesk
```

If you want it in Git (recommended for the viva/version-control requirement):
```bash
cd ~/Desktop/securedesk
git init
git add .
git commit -m "Initial commit: SecureDesk design patterns mini project"
# then create a repo on GitHub and:
git remote add origin https://github.com/<your-username>/securedesk.git
git branch -M main
git push -u origin main
```

---

## PHASE 2 — Build the project

```bash
cd ~/Desktop/securedesk
mvn clean install
```
This downloads all dependencies (Spring Web, Spring Data JPA, MySQL driver,
Validation) and compiles the code. First run takes a few minutes since Maven
downloads its local repo cache.

If it ends with `BUILD SUCCESS`, you're good. If it fails, the error will
almost always be either (a) MySQL not running, or (b) a Java version
mismatch — re-check Phase 0.

---

## PHASE 3 — Run the application

```bash
mvn spring-boot:run
```
or, after building a jar:
```bash
java -jar target/securedesk-1.0.0.jar
```

Watch the console for:
```
Tomcat started on port(s): 8080 (http)
Started SecureDeskApplication in x.xxx seconds
```

Leave this terminal running — this is your live server at `http://localhost:8080`.

Quick sanity check in a second terminal:
```bash
curl http://localhost:8080/api/tickets
```
An empty array `[]` means it's alive and connected to MySQL correctly.

---

## PHASE 4 — Verify the database tables were created

```bash
mysql -u root -p
```
```sql
USE securedesk_db;
SHOW TABLES;
```
You should see: `users`, `tickets`, `notifications`, `escalation_history`,
`ticket_status_history`.

---

## PHASE 5 — Test everything in Postman

A ready-made collection is included: **`SecureDesk.postman_collection.json`**

1. Open Postman → **Import** → select `SecureDesk.postman_collection.json`.
2. It creates a collection called **SecureDesk API** with a `baseUrl` variable
   already set to `http://localhost:8080`.
3. Run the requests **in this order** (they build on each other's IDs):

### Step-by-step demo script (also proves each pattern is working)

**1. Register users** — run requests #1 and #2
`POST /api/users/register` → creates an Employee (id 1) and a Support Staff
member (id 2). Check the response body for the generated `id`.

**2. Create tickets → proves Factory + Strategy**
Run #4, #5, #6 (Hardware, Security, Network tickets).
- Look at the `priority` field in each response:
  - Hardware ticket with normal wording → `NORMAL`
  - Security ticket → always `CRITICAL` (CriticalPriorityStrategy)
  - Network ticket with "outage" in the description → `CRITICAL`
    (UrgentPriorityStrategy escalates itself)
- Look at `currentSupportLevel`: Hardware → `LEVEL1`, Security →
  `SECURITY_SPECIALIST`, Network → `LEVEL2`. This is the **Factory**
  deciding routing defaults per category.

**3. List / fetch tickets** — #7, #8
`GET /api/tickets` and `GET /api/tickets/1` to see all fields.

**4. Walk ticket #1 through its lifecycle → proves State pattern**
Run in order: #9 (assign) → #10 (start-progress) → #11 (resolve) → #12 (close).
After each call, `status` moves OPEN → ASSIGNED → IN_PROGRESS → RESOLVED → CLOSED.

**5. Prove illegal transitions are blocked → proves State pattern (again)**
Create a fresh ticket (repeat #4), then immediately call the **close**
endpoint (`PUT /api/tickets/{id}/close`) on it while it's still `OPEN`.
You'll get **HTTP 409 Conflict** with a body like:
```json
{ "timestamp": "...", "error": "Cannot CLOSE a ticket in state OPEN" }
```
This is the exact behaviour the faculty will ask you to demonstrate —
"the State pattern rejects invalid transitions instead of an if-else chain."

**6. Escalate a ticket → proves Chain of Responsibility**
Run #14: `POST /api/tickets/{id}/escalate` with a `reason` in the body.
Check the response `currentSupportLevel` — and then query MySQL directly to
show the audit trail the chain produced:
```sql
SELECT * FROM escalation_history WHERE ticket_id = 1;
```
Each row shows `from_level → to_level` and the reason, proving the ticket
was actually passed along the chain (Level1 → Level2 → NetworkSpecialist →
SecuritySpecialist) rather than jumping straight there.

**7. Check notifications → proves Observer**
Run #15: `GET /api/notifications/user/1` (or user 2, the assigned staff).
You'll see one notification row created automatically for every
create/assign/resolve/close/escalate event — this is the `TicketNotifier`
(Subject) pushing to `EmployeeNotificationObserver` and
`SupportStaffNotificationObserver` without the `TicketService` knowing they exist.

---

## PHASE 6 — What to show in the viva, mapped to your evidence

| Ask | What to show |
|---|---|
| "Show me the Factory pattern" | `pattern/factory/TicketFactoryProvider.java` + Postman request #4 response showing category-specific `currentSupportLevel` |
| "Show me the Strategy pattern" | `pattern/strategy/PriorityContext.java` + compare priority field across requests #4, #5, #6 |
| "Show me the State pattern" | `pattern/state/TicketStateContext.java` + Postman request #13 returning 409 |
| "Show me the Observer pattern" | `pattern/observer/TicketNotifier.java` + `GET /api/notifications/user/{id}` results |
| "Show me Chain of Responsibility" | `pattern/chain/EscalationChainBuilder.java` + `escalation_history` table rows |
| "Show layered architecture" | Controller → Service → Design Pattern Layer → Repository → MySQL, visible in `TicketService.java` which is the only class that talks to all 5 pattern packages |

---

## Notes / simplifications made for the academic scope

- `roles` is modeled as an `enum` field on `User` (not a separate join table)
  to keep the schema simple — mention this as a deliberate simplification if asked.
- Authentication/JWT was intentionally left out since the assignment's focus
  is the design patterns, not security — the `role` field is there for you to
  extend into role-based access control if your faculty wants it.
- The escalation endpoint always walks the chain from Level 1 for
  demonstration clarity; in a production system you'd start the chain at the
  ticket's `currentSupportLevel`.

## Project structure

```
securedesk/
├── pom.xml
├── SecureDesk.postman_collection.json
└── src/main/
    ├── resources/application.properties
    └── java/com/securedesk/
        ├── SecureDeskApplication.java
        ├── entity/            (User, Ticket, Notification, EscalationHistory, TicketStatusHistory)
        ├── enums/              (TicketCategory, TicketStatusEnum, PriorityLevel, SupportLevel, UserRole)
        ├── repository/         (Spring Data JPA repositories)
        ├── dto/                (request/response objects)
        ├── pattern/
        │   ├── factory/        (Factory Method pattern)
        │   ├── strategy/       (Strategy pattern)
        │   ├── state/          (State pattern)
        │   ├── observer/       (Observer pattern)
        │   └── chain/          (Chain of Responsibility pattern)
        ├── service/            (wires all 5 patterns together)
        ├── controller/         (REST endpoints)
        └── exception/          (GlobalExceptionHandler)
```
