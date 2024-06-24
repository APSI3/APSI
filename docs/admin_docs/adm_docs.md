## Cel i zakres
Poniższy rozdział ma na celu przedstawienie informacji wymaganych do efektywnego administrowania *Systemem Rezerwacji Biletów*. Tekst obejmuje zagadnienia dotyczące zarządzania od strony technicznej oraz obsługę dokumentacji systemu. 

## Opis systemu

### Software systemu
| __Software__              | Opis                       | Technologie
| :----------------------| :--------------------------| :--------------------------  
| __Web UI__             | Graficzny interfejs użytkownika w postaci aplikacji webowej, front-end.     | React, TypeScript
| __Serwer API__         | Serwer back-end serwujący dane systemowe poprzez API.                       | Java, Spring Boot  
| __Baza danych__        | Instancja bazy danych, używana w celu osiągnięcia persystencji danych.      | PostgreSQL  
| __Narzędzie konteneryzacji__   | Służy do automatyzacji wdrożenia oraz uruchomienia systemu          | Docker
| __Generator dokumentacji__     | Narzędzie służące do budowy dokumentacji w postaci statycznych stron internetowych   | MkDocs 

### Hardware systemu
System docelowo instalowany oraz uruchamiany jest w ramach jednej fizycznej maszyny, na której konteneryzowane są poszczególne moduły systemu.

## Komunikacja i dostępność
Aplikacja kliencka domyślnie dostępna jest pod portem `:3000` oraz osiągalna przy pomocy przeglądarki internetowej z dowolnej maszyny z dostępem do internetu. Komunikacja z serwerem API (domyślnie port `:8080`) odbywa się protokołem HTTP. Serwer bazy danych znajduje się domyślnie pod portem `:5432`.

## Instalacja systemu
Instalacja systemu sprowadza się do umieszczenia plików źródłowych systemu w docelowym katalogu na maszynie fizycznej.

W celu uruchomienia systemu należy zainstalować odpowiednie oprogramowanie. Wymagana jest instalacja narzędzia [*Docker*](https://www.docker.com/) w wersji na system operacyjny maszyny fizycznej. 

Dokumentacja serwowana jest użytkownikom przy pomocy narzędzi [*MkDocs*](https://pypi.org/project/mkdocs/) oraz [*MkDocs Material*](https://pypi.org/project/mkdocs-material/). Istnieje również możliwość eksportowania dokumentacji do pliku PDF za pomocą [*MkDocs with PDF*](https://pypi.org/project/mkdocs-with-pdf/). Powyższe narzędzia muszą zostać przed użyciem zainstalowane na docelowej maszynie.

## Uruchomienie systemu
W ramach pierwszego uruchomienia systemu lub po dokonaniu zmian w kodzie źródłowym należy użyć [komendy](https://docs.docker.com/reference/cli/docker/compose/up/) w terminalu:  
` docker compose up --build `  
, budującej obrazy poszczególnych modułów systemu oraz uruchamiającej skonteneryzowane oprogramowanie.   
Przy pierwszym uruchomieniu baza danych wypełniana jest danymi inicjalizującymi, m.in. tworzone jest konto administratora.

Każde kolejne uruchomienie systemu w wersji niezmodyfikowanej odbywać się będzie poprzez użycie [komendy](https://docs.docker.com/reference/cli/docker/compose/up/):  
` docker compose up `

W wyniku powyższych komend uruchamiane są trzy kontenery zawierające odpowiednio: front-end, back-end oraz bazę danych.

## Zatrzymanie systemu
W celu zatrzymania działania instancji systemu należy użyć [komendy](https://docs.docker.com/reference/cli/docker/compose/stop/):   
` docker compose stop`

W wyniku powyższej komendy wstrzymane zostaną uruchomione kontenery, bez utraty danych.

Możliwe również jest użycie [komendy](https://docs.docker.com/reference/cli/docker/compose/down/):   
` docker compose down`

W wyniku powyższej komendy wstrzymane oraz usunięte zostaną wszystkie kontenery, wraz z ich danymi (w tym dane bazy danych) oraz wewnętrzną wirtualną infrastrukturą sieciową.

## Dziennik zdarzeń
W ramach każdego z kontenerów dokonywane jest logowanie użytecznych danych opisujących zdarzenia występujące w systemie. Aby wyświetlić dziennik zdarzeń kontenera należy użyć [komendy](https://docs.docker.com/reference/cli/docker/container/logs/):   
` docker logs {backend / frontend / database} `

## Użytkowanie dokumentacji
W celu uruchomienia serwera dokumentacji należy użyć [komendy](https://www.mkdocs.org/user-guide/cli/#mkdocs-serve):   
` mkdocs serve `

Dokumentacja zostanie wtedy udostępniona (domyślnie pod portem `:8000`) w postaci strony internetowej.

Aby wygenerować plik PDF zawierający dokumentację systemu należy użyć [komendy](https://www.mkdocs.org/user-guide/cli/#mkdocs-build):   
` mkdocs build `

Wynikowy plik zapisany zostanie pod ścieżką `./docs/dokumentacja.pdf`.

## Rozwój dokumentacji
Dokumentacja systemu rozwijana jest poprzez edycję plików w formacie Markdown znajdujących się pod ścieżką `./docs/` w odpowiednich katalogach oraz edycję pliku konfiguracyjnego `mkdocs.yml`. Mapowanie rozdziałów na odpowiednie pliki Markdown deklarowane jest w pliku konfiguracyjnym w sekcji `nav`. W celu stworzenia nowego rozdziału należy utworzyć plik Markdown i dodać wiersz w sekcji `nav` pliku konfiguracyjnego. [Więcej informacji o użytkowaniu narzędzia *MkDocs*.](https://www.mkdocs.org/user-guide/)

## Instrukcja
Instrukcja użytkowania systemu dla administratora dostępna jest w rozdziale *Instrukcja użytkowania - Instrukcja administratora*.
