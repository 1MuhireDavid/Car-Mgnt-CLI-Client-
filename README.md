# Car Management CLI

Command-line interface for managing cars and tracking fuel consumption.

![Java](https://img.shields.io/badge/Java-17-orange)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue)

## Features

- Create and manage cars
- Track fuel entries
- Calculate consumption statistics
- Simple command-line interface

## Prerequisites

- Java 17+
- Maven 3.6+
- [Backend Server](https://github.com/1MuhireDavid/Car-management-and-fuel-tracking) running on `http://localhost:8080`

## Installation

```bash
git clone https://github.com/1MuhireDavid/Car-Mgnt-CLI-Client-.git
cd car-management-cli
mvn clean package
```

## Usage

### Create a Car
```bash
java -jar target/cli-1.0.0.jar create-car --brand Toyota --model Corolla --year 2018
```

**Output:**
```
✓ Car created successfully!
Car ID: 1
```

### Add Fuel Entry
```bash
java -jar target/cli-1.0.0.jar add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```

**Output:**
```
✓ Fuel entry added successfully!
Liters: 40.0 L
Price: 52.5
Odometer: 45000 km
```

### View Statistics
```bash
java -jar target/cli-1.0.0.jar fuel-stats --carId 1
```

**Output:**
```
Total fuel: 120.0 L
Total cost: 155.00
Average consumption: 6.4 L/100km
```

## Commands

| Command | Required Arguments | Description |
|---------|-------------------|-------------|
| `create-car` | `--brand --model --year` | Create a new car |
| `add-fuel` | `--carId --liters --price --odometer` | Add fuel entry |
| `fuel-stats` | `--carId` | View statistics |

## Complete Example

```bash
# 1. Create car
java -jar target/cli-1.0.0.jar create-car --brand Honda --model Civic --year 2020

# 2. Add first fuel entry
java -jar target/cli-1.0.0.jar add-fuel --carId 1 --liters 45 --price 60.0 --odometer 10000

# 3. Add second fuel entry
java -jar target/cli-1.0.0.jar add-fuel --carId 1 --liters 40 --price 52.5 --odometer 10500

# 4. View statistics
java -jar target/cli-1.0.0.jar fuel-stats --carId 1
```

## Troubleshooting

**Backend not running:**
```
Error: Connection refused
```
→ Start backend: `cd ../backend && mvn spring-boot:run`

**JAR not found:**
```
Error: Unable to access jarfile
```
→ Build project: `mvn clean package`

**Car not found:**
```
✗ Error: Car with ID 999 not found
```
→ Create the car first using `create-car` command

## Architecture

```
CLI Application (Java)
       ↓ HTTP
Backend API (Spring Boot)
       ↓
In-Memory Storage
```

## Technologies

- Java 17
- Maven
- HttpClient (Java 11+)
- Gson (JSON parsing)

## Related

- [Backend Server](https://github.com/1MuhireDavid/Car-management-and-fuel-tracking)

## License

MIT

---

**Built with Java HttpClient**
