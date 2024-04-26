W aplikacji klienckiej wykorzystany został framework React w połączeniu z językiem programowania wysokiego poziomu TypeScript. Aplikacja stworzona została przy użyciu generatora Create React App zapewniającego środowisko deweloperskie oraz wykorzystującego bundler Webpack i transpilator Babel.

Do stworzenia serwera API użyto języka Java oraz frameworku Spring Boot. Podmoduły Spring takie jak: web, security, data-jpa i jdbc zapewniają odpowiednio poprawne działanie podstaw aplikacji webowej, bezpieczeństwo oraz mapowanie klas w Javie na rekordy w bazie danych.

Jako bazę danych wykorzystano PostgreSQL w wersji 16.

Za konteneryzację poszczególnych komponentów odpowiedzialne jest narzędzie Docker. Tworzone kontenery zarządzane są przy pomocy narzędzia Docker Compose. Całą aplikację można uruchomić z użyciem polecenia “docker compose up” na dowolnej maszynie wspierającej Dockera.
