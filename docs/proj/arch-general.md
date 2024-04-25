![architecture](../assets/arch_general.png "Ogólny model architektury![alt text](image.png)")

Architektura systemu oparta jest na prosty modelu trójwarstwowym, składającym się z aplikacji klienckiej, serwera API oraz bazy danych, co zapewnia prosty podział obowiązków każdego z komponentów i modularność systemu. 

Aplikacja kliencka odpowiada za prezentację danych systemu oraz zapewnia możliwość interakcji klientów z systemem. Jej głównym elementem jest graficzny interfejs wyświetlany użytkownikom końcowym korzystającym z platformy przy pomocy przeglądarki internetowej.

Serwer API odpowiada za realizację całej logiki biznesowej systemu. Odpowiada na żądania użytkowników korzystających z aplikacji klienckiej oraz udostępnia punkty końcowe pozwalające na pobranie lub modyfikację danych gromadzonych w systemie. Serwer API jest zaimplementowany w stylu REST i może współpracować z innymi systemami niż aplikacja kliencka zrealizowana w tym projekcie. 

Baza danych przechowuje oraz organizuje wszystkie dane systemowe. Komunikuje się jedynie z serwerem API.
