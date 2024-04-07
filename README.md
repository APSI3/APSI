# Analiza i projektowanie systemów informacyjnych, 2024L

## Uruchomienie aplikacji

Aby uruchomić aplikację w środowisku docker należy wykonać polecenie:
`docker compose up`

Za pierwszym razem lub po zmianach w kodzie może być konieczne:
`docker compose up --build`

Uruchomienie w trybie "detached":
`docker compose up -d`

## Tworzenie migracji Flyway

Aby utworzyć migrację należy dodać plik SQL do katalogu `src/main/resources/db/migration` 
zgodnie z konwencją nazewniczą. Mini poradnik dostępny jest na stronie [Bealdung](https://www.baeldung.com/database-migrations-with-flyway). Odpalamy backend i działa.

### Generowanie plików sql migracji 

Polecam dodatek JPA Buddy do Intellij IDEA, który umożliwia generowanie plików migracji na podstawie zmian w encjach. Wystarczy kliknąć opcję "Create Flyway Versioned Migration" w panelu JPA Explorer. 