# EXCHANGE_APP - Projekt aplikacji giełdowych (wersja uproszczona)

## Opis projektu

Przygotowujemy projekt aplikacji giełdowych, który będzie służył jako przykład
do nauki Spring Boot, JPA, MongoDB i Kafka. Projekt ma na celu symulację podstawowego systemu handlu giełdowego 
z rekomendacjami akcji. Jest to uproszczona wersja, która ma być zrealizowana w ciągu 4 dni i służy tylko nauczeniu podstaw programowania w javie.

Projekt składa się z dwóch mikroserwisów Spring Boot symulujących podstawowy system handlu giełdowego:

1. **stock-app** - aplikacja do zarządzania akcjami i transakcjami
2. **stock-recommendation** - serwis prostych rekomendacji akcji
3. **docker-stock** - zależności do uruchomienia aplikacji w kontenerach Docker

## Architektura

### stock-app (port 8080)

Główna aplikacja obsługująca handel akcjami.

#### Model danych (tylko podstawowe encje)

**Stock (Akcja)**
```java
@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;          // np. "PKO", "CCC", "KGHM"
    
    @Column(nullable = false)
    private String companyName;     // pełna nazwa spółki
    
    @Column(nullable = false)
    private BigDecimal currentPrice;// aktualna cena
    
    private LocalDateTime lastUpdate;
    
    @ManyToMany(mappedBy = "stocks")
    private Set<Index> indices = new HashSet<>();  // indeksy do których należy akcja
    
    @Transient
    private Integer buyRecommendations = 0;   // liczba rekomendacji kupna
    
    @Transient
    private Integer sellRecommendations = 0;  // liczba rekomendacji sprzedaży
    
    @Transient
    private Integer holdRecommendations = 0;  // liczba rekomendacji trzymaj
}
```

**Client (Klient)**
```java
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private BigDecimal balance;     // środki na koncie
    
    private LocalDateTime registrationDate;
}
```

**Transaction (Transakcja)**
```java
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type;    // BUY, SELL
    
    @Column(nullable = false)
    private Long quantity;
    
    @Column(nullable = false)
    private BigDecimal price;        // cena w momencie transakcji
    
    private LocalDateTime transactionDate;
}
```

**Index (Indeks giełdowy)**
```java
@Entity
@Table(name = "stock_index")  // "index" jest słowem kluczowym w SQL
public class Index {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;           // np. "WIG20", "mWIG40", "sWIG80"
    
    @Column(nullable = false)
    private String name;             // pełna nazwa indeksu
    
    private String description;      // opis indeksu
    
    @ManyToMany
    @JoinTable(
        name = "index_stocks",
        joinColumns = @JoinColumn(name = "index_id"),
        inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private Set<Stock> stocks = new HashSet<>();  // akcje w indeksie
    
    private BigDecimal currentValue; // aktualna wartość indeksu
    
    private LocalDateTime lastUpdate;
}
```

#### Endpointy REST

**Stock Controller**
- `GET /api/stocks` - lista wszystkich akcji
- `GET /api/stocks/{id}` - szczegóły akcji
- `POST /api/stocks` - dodanie nowej spółki
- `PUT /api/stocks/{id}` - aktualizacja ceny

**Client Controller**
- `GET /api/clients` - lista klientów
- `GET /api/clients/{id}` - szczegóły klienta
- `POST /api/clients` - rejestracja nowego klienta
- `PUT /api/clients/{id}/balance` - aktualizacja salda

**Transaction Controller**
- `POST /api/transactions` - wykonanie transakcji
- `GET /api/transactions` - wszystkie transakcje
- `GET /api/transactions/client/{clientId}` - transakcje klienta

**Index Controller**
- `GET /api/indices` - lista wszystkich indeksów
- `GET /api/indices/{id}` - szczegóły indeksu z listą akcji
- `POST /api/indices` - utworzenie nowego indeksu
- `PUT /api/indices/{id}/stocks` - aktualizacja składu indeksu
- `GET /api/indices/stock/{stockId}` - indeksy zawierające daną akcję

#### Serwisy

**StockService**
- findAll() - zwraca akcje z liczbą rekomendacji
- findById()
- createStock()
- updatePrice()
- updateRecommendationCounts() - aktualizuje liczniki rekomendacji

**ClientService**
- findAll()
- findById()
- createClient()
- updateBalance()

**TransactionService**
- executeTransaction() - realizacja kupna/sprzedaży
- findByClient()
- validateTransaction() - sprawdzenie środków i walidacja

**RecommendationCache** (nowy komponent)
- przechowuje w pamięci aktualne rekomendacje dla każdej akcji
- aktualizowany przez Kafka consumer

**IndexService**
- findAll()
- findById() - zwraca indeks z listą akcji
- createIndex()
- updateIndexComposition() - aktualizacja składu indeksu
- addStockToIndex() - dodanie akcji do indeksu
- removeStockFromIndex() - usunięcie akcji z indeksu
- calculateIndexValue() - obliczanie wartości indeksu
- findIndicesByStock() - znajdowanie indeksów dla akcji

### stock-recommendation (port 9090)

Prosty serwis rekomendacji akcji.

#### Model danych (MongoDB)

**Recommendation (Rekomendacja)**
```java
@Document(collection = "recommendations")
public class Recommendation {
    @Id
    private String id;
    
    private String stockSymbol;
    
    private RecommendationType type; // BUY, SELL, HOLD
    
    private BigDecimal targetPrice;
    
    private String reason;          // krótkie uzasadnienie
    
    private LocalDateTime createdAt;
    
    private String analyst;         // nazwa analityka
}
```

#### Endpointy REST

**Recommendation Controller**
- `GET /api/recommendations` - wszystkie rekomendacje
- `GET /api/recommendations/stock/{symbol}` - rekomendacje dla akcji
- `POST /api/recommendations` - dodanie rekomendacji
- `DELETE /api/recommendations/{id}` - usunięcie rekomendacji

#### Serwisy

**RecommendationService**
- findAll()
- findByStockSymbol()
- createRecommendation()
- deleteRecommendation()

## Komunikacja między aplikacjami (Kafka)

### Topiki Kafka

**recommendations-topic** - nowe rekomendacje i aktualizacje
```json
{
  "recommendationId": "507f1f77bcf86cd799439011",
  "stockSymbol": "PKO",
  "type": "BUY",
  "targetPrice": 40.00,
  "analyst": "Jan Nowak",
  "reason": "Dobre wyniki kwartalne",
  "createdAt": "2024-01-15T10:30:00"
}
```

**recommendation-deletions** - usunięte rekomendacje
```json
{
  "recommendationId": "507f1f77bcf86cd799439011",
  "stockSymbol": "PKO",
  "deletedAt": "2024-01-15T14:30:00"
}
```

### Przepływ danych

1. **stock-recommendation** publikuje:
   - Nowe rekomendacje do `recommendations-topic`
   - Usunięte rekomendacje do `recommendation-deletions`

2. **stock-app** konsumuje:
   - Rekomendacje z obu topiców
   - Aktualizuje liczniki rekomendacji dla każdej akcji
   - Przechowuje dane w pamięci (RecommendationCache)

## Konfiguracja

### application.properties (stock-app)
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/stock_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Kafka Consumer
spring.kafka.bootstrap-servers=localhost:8097
spring.kafka.consumer.group-id=stock-service
spring.kafka.consumer.auto-offset-reset=earliest
```

### application.properties (stock-recommendation)
```properties
server.port=9090
spring.data.mongodb.uri=mongodb://admin:admin@localhost:27017/recommendations?authSource=admin

# Kafka Producer
spring.kafka.bootstrap-servers=localhost:8097
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

## Docker Compose (uproszczony)

```yaml
version: '3'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: stock_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  mongodb:
    image: mongo:6
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin
    ports:
      - "27017:27017"
  
  kafka:
    image: bitnami/kafka:3.6
    ports:
      - "8097:9092"
    environment:
      - KAFKA_CFG_NODE_ID=0
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER

volumes:
  postgres_data:
```

## Lista zadań do implementacji (4 dni)

### Dzień 1: Struktura i podstawy

**1. Utworzenie projektów (1h)**
- Stworzenie modułu stock-app
- Stworzenie modułu stock-recommendation
- Konfiguracja pom.xml

**2. Model danych stock-app (2h)**
- Encja Stock
- Encja Client
- Encja Transaction
- Encja Index z relacją M:N do Stock
- Konfiguracja JPA i PostgreSQL

**3. Model danych stock-recommendation (1h)**
- Dokument Recommendation
- Konfiguracja MongoDB

**4. Repozytoria (2h)**
- StockRepository, ClientRepository, TransactionRepository w stock-app w trzech wariantach:
  - jdbc
  - jpa
  - mem 
- IndexRepository z metodami do zarządzania relacją M:N
- RecommendationRepository w stock-recommendation
- Podstawowe metody CRUD

**5. Dane testowe (1h)**
- data.sql z przykładowymi akcjami, klientami i indeksami z indeksów WIG-20, WIG-BANKI, WIG-IT
- Inicjalizacja relacji między akcjami a indeksami
- CommandLineRunner dla MongoDB

### Dzień 2: Logika biznesowa

**6. Serwisy stock-app (3h)**
- StockService
- ClientService
- TransactionService z walidacją
- IndexService z zarządzaniem składem

**7. Serwis stock-recommendation (1h)**
- RecommendationService

**8. Obsługa błędów (2h)**
- Custom exceptions
- Global exception handler
- Validation

**9. Testy serwisów (2h)**
- Testy jednostkowe z Mockito
- Podstawowe przypadki testowe

### Dzień 3: REST API i Kafka

**10. Kontrolery stock-app (2h)**
- StockController
- ClientController
- TransactionController
- IndexController

**11. Kontroler stock-recommendation (1h)**
- RecommendationController

**12. DTO i mappery (2h)**
- Request/Response DTO
- Prosty mapper (bez MapStruct)

**13. Konfiguracja Kafka (1h)**
- Producer w stock-recommendation
- Consumer w stock-app

**14. Publikowanie i konsumowanie eventów (2h)**
- RecommendationEventPublisher w stock-recommendation
- RecommendationConsumer w stock-app
- RecommendationCache do przechowywania danych

### Dzień 4: Integracja i prezentacja

**15. Docker Compose (1h)**
- Uruchomienie infrastruktury
- Testowanie połączeń

**16. Testy integracyjne (2h)**
- REST API testy z MockMvc
- Test z profilem H2

**17. Prosty UI (2h)**
- Jedna strona Thymeleaf z listą akcji
- Formularz transakcji

**18. Dokumentacja (1h)**
- README z instrukcją uruchomienia
- Przykładowe requesty HTTP

**19. Prezentacja (1h)**
- Demonstracja działania
- Omówienie architektury

## Przykładowe scenariusze

1. **Rejestracja klienta i wpłata środków**
2. **Tworzenie indeksów giełdowych**
   - Utworzenie WIG20 z największymi spółkami
   - Utworzenie indeksów branżowych (WIG-BANKI, WIG-IT)
3. **Dodanie rekomendacji w stock-recommendation**
   - Rekomendacja jest publikowana na Kafka
   - stock-app odbiera i aktualizuje liczniki
4. **Przeglądanie akcji z informacją o indeksach i rekomendacjach**
   - Klient widzi do jakich indeksów należy akcja
   - Widzi ile jest rekomendacji BUY/SELL/HOLD
5. **Zarządzanie składem indeksu**
   - Dodawanie/usuwanie akcji z indeksu
   - Aktualizacja wartości indeksu

## Materiały pomocnicze

### Przykładowe requesty HTTP

```http
### Rejestracja klienta
POST http://localhost:8080/api/clients
Content-Type: application/json

{
  "firstName": "Jan",
  "lastName": "Kowalski",
  "email": "jan.kowalski@example.com",
  "balance": 10000.00
}

### Pobranie akcji z rekomendacjami
GET http://localhost:8080/api/stocks

# Przykładowa odpowiedź:
# {
#   "id": 1,
#   "symbol": "PKO",
#   "companyName": "PKO Bank Polski",
#   "currentPrice": 35.50,
#   "buyRecommendations": 3,
#   "sellRecommendations": 1,
#   "holdRecommendations": 2,
#   "indices": ["WIG20", "WIG-BANKI"]
# }

### Utworzenie indeksu
POST http://localhost:8080/api/indices
Content-Type: application/json

{
  "symbol": "WIG20",
  "name": "Warszawski Indeks Giełdowy 20",
  "description": "20 największych spółek na GPW",
  "stockIds": [1, 2, 3, 4, 5]
}

### Dodanie akcji do indeksu
PUT http://localhost:8080/api/indices/1/stocks
Content-Type: application/json

{
  "stockIds": [1, 2, 3, 4, 5, 6, 7]
}

### Pobranie indeksów dla akcji
GET http://localhost:8080/api/indices/stock/1

### Wykonanie transakcji
POST http://localhost:8080/api/transactions
Content-Type: application/json

{
  "clientId": 1,
  "stockId": 1,
  "type": "BUY",
  "quantity": 10
}

### Dodanie rekomendacji (publikuje na Kafka)
POST http://localhost:9090/api/recommendations
Content-Type: application/json

{
  "stockSymbol": "PKO",
  "type": "BUY",
  "targetPrice": 40.00,
  "reason": "Dobre wyniki kwartalne",
  "analyst": "Jan Nowak"
}

### Usunięcie rekomendacji (publikuje na Kafka)
DELETE http://localhost:9090/api/recommendations/{id}
```

## Wskazówki implementacyjne

1. **Zacznij od najprostszej wersji** - podstawowy CRUD
2. **Używaj H2 do testów** - szybsze niż PostgreSQL
3. **Kafka może być opcjonalne** - jeśli brakuje czasu
4. **UI jest opcjonalne** - REST API jest priorytetem
5. **Skup się na happy path** - obsługa błędów może być minimalna

### Zależność Maven dla PostgreSQL
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Rozszerzenia (jeśli zostanie czas)

- Scheduler do automatycznej aktualizacji cen
- Więcej typów transakcji
- Proste statystyki (np. najpopularniejsze akcje)
- Basic Auth dla endpointów
- Wyświetlanie szczegółów rekomendacji przy akcji
- Historia zmian liczby rekomendacji