-- Inicjalizacja bazy danych stock_db dla aplikacji giełdowej

-- Sprawdź czy baza istnieje, jeśli nie - utwórz
SELECT 'CREATE DATABASE stock_db'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'stock_db')\gexec

-- Połącz się z bazą stock_db
\c stock_db;

-- Tabele zostaną utworzone automatycznie przez Hibernate/JPA
-- Ten skrypt służy tylko do upewnienia się, że baza stock_db istnieje

-- Opcjonalnie można dodać użytkownika dedykowanego dla aplikacji
-- CREATE USER stock_user WITH PASSWORD 'stock_password';
-- GRANT ALL PRIVILEGES ON DATABASE stock_db TO stock_user;