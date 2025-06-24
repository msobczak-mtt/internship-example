# 📈 Stock Trading Application - Spring Boot Training

## 🎯 Cel szkolenia

Aplikacja **stock-app** to przykład edukacyjny Spring Boot demonstrujący nowoczesne techniki programowania aplikacji webowych. Pokazuje różne poziomy zaawansowania - od prostych rozwiązań dla początkujących do zaawansowanych praktyk produkcyjnych.

## 🏗️ Architektura aplikacji

```
stock-app/
├── 📱 Kontrolery REST (Web Layer)
├── 🔧 Serwisy biznesowe (Service Layer)  
├── 💾 Repozytoria danych (Repository Layer)
└── 📊 Model danych (Domain Model)
```

### Model domeny giełdowej

- **Stock** - akcje spółek (symbol, nazwa, cena)
- **Client** - klienci maklerzy (dane osobowe, saldo)
- **Transaction** - transakcje kupna/sprzedaży
- **Index** - indeksy giełdowe (WIG20, S&P500)

## 🚀 Uruchamianie aplikacji

### Szybki start (zero konfiguracji!)
```bash
mvn spring-boot:run -f stock-app/pom.xml
```

### Dostępne endpointy
- 🌐 **Aplikacja**: http://localhost:8080
- 🔍 **API**: http://localhost:8080/stocks  
- 📊 **H2 Console**: http://localhost:8080/h2-console

### Konfiguracje baz danych
```bash
# H2 (domyślna - brak setup)
mvn spring-boot:run -f stock-app/pom.xml

# PostgreSQL (wymaga Docker)
mvn spring-boot:run -f stock-app/pom.xml -Dspring.profiles.active=postgres
```

## 📚 Komponenty edukacyjne

### 1. 🎭 Kontrolery REST - Różne podejścia

**Prosty sposób** (dla początkujących):
```java
@GetMapping
public List<Stock> getAllStocks() {
    return stockService.getAllStocks();
}
```

**Zaawansowany sposób** (ResponseEntity):
```java
@GetMapping("/{id}")
public ResponseEntity<Stock> getStock(@PathVariable Long id) {
    Stock stock = stockService.getStockById(id);
    return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
}
```

**Pokazane koncepty:**
- 🔄 Różnice między prostym zwrotem obiektu a ResponseEntity
- 📋 Właściwe kody odpowiedzi HTTP (200, 201, 404, 400)
- 🛡️ Obsługa błędów i wyjątków

### 2. 🔧 Serwisy biznesowe

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceBean implements StockService {
    
    private final StockRepository stockRepository;
    
    public List<Stock> getAllStocks() {
        log.info("Finding all stocks...");
        return stockRepository.findAll();
    }
}
```

**Pokazane wzorce:**
- 📦 Dependency Injection z Lombok
- 📝 Logowanie operacji biznesowych
- 🔒 Enkapsulacja logiki biznesowej

### 3. 💾 Repozytoria - Spring Data JPA Magic!

**Przed** (setki linii kodu):
```java
public class JpaStockDao implements StockDao {
    // Ręczne implementacje CRUD...
    // findById(), save(), delete() etc.
}
```

**Po** (Spring robi za nas!):
```java
public interface StockRepository extends ListCrudRepository<Stock, Long> {
    Optional<Stock> findBySymbol(String symbol);
    List<Stock> findByCompanyNameContaining(String companyName);
}
```

**Pokazane koncepty:**
- ✨ Spring Data JPA automatyczne implementacje
- 🔍 Query methods z naming conventions
- 🎯 Custom queries z @Query

### 4. 📊 Model danych z JPA

```java
@Entity
@Data
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;
    
    @ManyToMany(mappedBy = "stocks")
    private Set<Index> indices = new HashSet<>();
}
```

**Pokazane adnotacje:**
- 🏷️ JPA Entity mapping
- 🔗 Relacje między encjami (M:N)
- 📋 Walidacja i constraints

## 🎪 Różne implementacje DAO (edukacyjne)

Aplikacja pokazuje **3 sposoby** dostępu do danych:

### 1. 🔷 JPA (domyślny)
```properties
stock.dao=jpa
```
- Hibernate ORM
- Automatyczne query generation
- Object-relational mapping

### 2. 🔶 JDBC (template)
```properties  
stock.dao=jdbc
```
- Spring JDBC Template
- SQL queries w Javie
- Mapowanie manualne

### 3. 🔸 Memory (testowy)
```properties
stock.dao=mem
```
- In-memory collections
- Brak bazy danych
- Idealne do testów

## 🛠️ Przykłady API

### Podstawowe operacje CRUD

```bash
# 📋 Lista wszystkich akcji
curl http://localhost:8080/stocks

# 🔍 Konkretna akcja po ID  
curl http://localhost:8080/stocks/1

# 🔎 Szukanie po symbolu
curl http://localhost:8080/stocks/symbol/AAPL

# ➕ Dodanie nowej akcji
curl -X POST http://localhost:8080/stocks \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "AAPL",
    "companyName": "Apple Inc.",
    "currentPrice": 150.00
  }'

# 💰 Aktualizacja ceny
curl -X PUT "http://localhost:8080/stocks/1/price?newPrice=155.50"
```

### Różne odpowiedzi HTTP

```bash
# ✅ 200 OK
curl -i http://localhost:8080/stocks/1

# ❌ 404 Not Found  
curl -i http://localhost:8080/stocks/999

# ✨ 201 Created
curl -X POST http://localhost:8080/stocks -H "Content-Type: application/json" -d '{...}'
```

## 🧪 Testowanie

### Testy jednostkowe (Mockito)
```bash
mvn test -Dtest=StockServiceBeanTest
```

### Testy integracyjne (H2)
```bash
mvn test -Dspring.profiles.active=h2
```

**Pokazane techniki:**
- 🎭 Mockowanie dependencies
- 🔍 AssertJ dla asercji
- 📝 Snake_case naming dla testów

## 🔧 Konfiguracja środowisk

### Development (H2)
```properties
# application.properties (domyślny)
spring.datasource.url=jdbc:h2:mem:stockdb
spring.h2.console.enabled=true
```

### Production (PostgreSQL)  
```properties
# application-postgres.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stock_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 📈 Poziomy zaawansowania

### 🥉 Poziom podstawowy
- Proste kontrolery REST
- Bezpośredni zwrot obiektów
- Spring Data JPA repositories
- H2 in-memory database

### 🥈 Poziom średni  
- ResponseEntity z kodami HTTP
- Obsługa błędów i wyjątków
- Custom query methods
- Profile konfiguracyjne

### 🥇 Poziom zaawansowany
- Różne implementacje DAO
- Dependency injection patterns  
- Testowanie z Mockito
- Production-ready configuration

## 🎓 Cele szkoleniowe

Po szkoleniu uczestnicy będą umieli:

✅ **Tworzyć aplikacje Spring Boot** od podstaw  
✅ **Projektować REST API** z właściwymi kodami HTTP  
✅ **Używać Spring Data JPA** do operacji na danych  
✅ **Konfigurować różne bazy danych** (H2, PostgreSQL)  
✅ **Pisać testy jednostkowe** z Mockito  
✅ **Stosować wzorce** Service-Repository  
✅ **Zarządzać konfiguracją** przez profiles  

## 🚀 Następne kroki

- 🔐 **Security** - Spring Security z JWT
- 📨 **Messaging** - Kafka consumers/producers  
- 🐳 **Containerization** - Docker & Docker Compose
- ☁️ **Cloud** - deployment na platformy chmurowe

---

*Aplikacja stock-app to kompleksowy przykład nowoczesnej aplikacji Spring Boot, idealny do nauki i prezentacji różnych technik programistycznych.*