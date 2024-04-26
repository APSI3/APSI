Przypisane przypadki użycia:

- UC8: Stworzenie wniosku o utworzenie profilu
- UC9: Stworzenie wydarzenia
- UC10: Edycja danych wydarzenia
- UC11: Anulowanie wydarzenia
- UC12: Przeglądanie statystyk wydarzenia
- UC13: Stworzenie wniosku o usunięcie profilu
- UC14: Dodanie lokalizacji

<br>

| __Nazwa__              |  Stworzenie wniosku o utworzenie profilu
| :----------------------| :--------------------------  
| __ID__                 | UC8                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Główne                       
| __Opis__               | Podmiot składa wniosek o utworzenie konta z uprawnieniami organizatora wydarzeń                                   
| __Cel__                | Złożenie wniosku o założenie konta                    
| __Wyzwalanie__         | Organizator postanawia złożyć wniosek o konto w systemie                                             
| __War. początkowe__    | <ul><li> Organizator nie posiada konta w systemie </li></ul> 
| __War. końcowe__       | <ul><li> Powstanie wniosku </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na utworzenie nowego wniosku </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Interfejs tworzenia wniosku musi być przejrzysty </li></ul>                                       
| __Scenariusz__         | 1. Organizator wchodzi w widok tworzenia wniosku <br> 2. System prezentuje formularz tworzenia wniosku <br> 3. Organizator wypełnia formularz <br> 4. Organizator przesyła formularz <br> 5. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 6. System zapisuje wniosek
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne. <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie.

<br>

| __Nazwa__              |  Stworzenie wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC9                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Główne                       
| __Opis__               | Organizator tworzy wydarzenie dla swojej organizacji                                   
| __Cel__                | Stworzenie nowego wydarzenia                    
| __Wyzwalanie__         | Organizator wchodzi na stronę aplikacji w celu stworzenia wydarzenia                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto w systemie (UC15) </li><li> Organizator jest zalogowany (UC17) </li> </ul> 
| __War. końcowe__       | <ul><li> Powstanie nowego wydarzenia w systemie </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na utworzenie nowego wydarzenia </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Interfejs tworzenia wydarzenia musi być przejrzysty </li><li> Wydarzenie powinno być tworzone z jak najmniejszym opóźnieniem </li></ul>                                       
| __Scenariusz__         | 1. Organizator wybiera opcję utworzenia wydarzenia <br> 2. System prezentuje kreator wydarzenia <br> 3. Organizator podaje informacje o wydarzeniu <br> 4. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 5. System rejestruje wydarzenie
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie

<br>

| __Nazwa__              | Edycja danych wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC10                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Drugorzędne                      
| __Opis__               | Organizator może edytować dane dot. wydarzenia                                   
| __Cel__                | Umożliwienie organizatorowi zmianę szczegółów eventu                    
| __Wyzwalanie__         | Pojawiły się zmiany dot. organizacji eventu, które muszą zostać wprowadzone w systemie                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto (UC15) </li><li> Organizator jest zalogowany (UC17) </li><li> Organizator ma utworzone co najmniej jedno aktywne wydarzenie (UC9) </li><li> Wydarzenie się jeszcze nie odbyło </li><li> Wydarzenie nie zostało anulowane </li></ul> 
| __War. końcowe__       | <ul><li> Organizator pomyślnie dokonał edycji danych wydarzenia </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System udostępnia widok edycji wydarzenia </li><li> Wydarzenia mogą być w pewnym stopniu edytowalne </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Prezentowany widok edycji powinien być intuicyjny i łatwo dostępny </li></ul>                                       
| __Scenariusz__         | 1. Organizator otwiera widok listy swoich wydarzeń <br> 2. System przygotowuje widok i prezentuje go organizatorowi <br> 3. Organizator wybiera wydarzenie, które chce edytować <br> 4. System wyświetla organizatorowi widok edycji <br> 5. Organizator edytuje wybrane informacje <br> 6. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 7. System aktualizuje wydarzenie <br> 8. System powiadamia organizatora o pomyślnej edycji <br> 9. System powiadamia o zaistniałych zmianach klientów posiadających bilety na wydarzenie 
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie

<br>

| __Nazwa__              |  Anulowanie wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC11                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator, Użytkownik prywatny        
| __Priorytet__          | Drugorzędne                      
| __Opis__               | Organizator może anulować wydarzenie                                   
| __Cel__                | Umożliwienie organizatorowi anulowanie eventu                    
| __Wyzwalanie__         | Wydarzenie zostało odwołane                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto (UC15) </li><li> Organizator jest zalogowany (UC17) </li><li> Organizator ma utworzone co najmniej jedno aktywne wydarzenie (UC9) </li><li> Wydarzenie się jeszcze nie odbyło </li><li> Wydarzenie nie zostało anulowane </li></ul> 
| __War. końcowe__       | <ul><li> Organizator pomyślnie anulował wydarzenie </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> Wydarzenia mogą być anulowane </li><li> System dokonuje zwrotu środków dla posiadaczy biletów </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Zwroty powinny być pełne </li><li> Zwroty pieniędzy powinny być bezpieczne </li></ul>                                       
| __Scenariusz__         | 1. Organizator otwiera widok listy swoich wydarzeń <br> 2. System przygotowuje widok i prezentuje go organizatorowi <br> 3. Organizator wybiera wydarzenie, które chce anulować <br> 4. Użytkownik potwierdza intencję anulowania wydarzenia <span style="color:red"> [E1: Anulowanie] </span> <br> 5. System anuluje wydarzenie <br> 6. System powiadamia organizatora o pomyślnym anulowaniu eventu <br> 7. System powiadamia klientów posiadających bilety na wydarzenie o zaistniałych zmianach <br> 8. System zleca zwrot środków <br>
| __Scenariusz wyjątku__ |   E1: Anulowanie <br> &nbsp;&nbsp;&nbsp;a. Użytkownik postanawia nie anulować wydarzenia 

<br>

| __Nazwa__              |  Przeglądanie statystyk wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC12                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Drugorzędne                      
| __Opis__               | Organizator widzi dla swoich wydarzeń liczbę sprzedanych biletów                                   
| __Cel__                | Umożliwienie organizatorowi obserwacji popularności swoich wydarzeń                    
| __Wyzwalanie__         | Organizator postanawia przejrzeć statystyki swoich wydarzeń                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto (UC15) </li><li> Organizator jest zalogowany (UC17) </li><li> Organizator ma utworzone co najmniej jedno aktywne wydarzenie (UC9) </li></ul> 
| __War. końcowe__       | <ul><li> Organizator widzi statystyki sprzedaży biletów na swoje wydarzenia </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System udostępnia informacje o sprzedanych biletach </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Informacje o biletach i wydarzeniach powinny być prezentowane w sposób przejrzysty </li></ul>                                       
| __Scenariusz__         | 1. Organizator otwiera widok listy swoich wydarzeń <br> 2. System wyświetla widok listy wydarzeń zawierający informacje o liczbie sprzedanych biletów dla każdego wydarzenia  <br> <span style="color:blue">  [O1: Sprawdzenie danych szczegółowych] </span>
| __Scenariusz opcjonalny__ |  O1: Sprawdzenie danych szczegółowych <br> &nbsp;&nbsp;&nbsp;a. Organizator wybiera wydarzenie, o którego sprzedaży biletów chce dowiedzieć się więcej  <br> &nbsp;&nbsp;&nbsp;b. System prezentuje organizatorowi widok wybranego wydarzenia rozszerzony o szczegółowe statystyki sprzedaży każdej z kategorii biletów

<br>

| __Nazwa__              | Stworzenie wniosku o usunięcie profilu
| :----------------------| :--------------------------  
| __ID__                 | UC13                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Główne                       
| __Opis__               | Podmiot składa wniosek o usunięcie konta z uprawnieniami organizatora wydarzeń                                   
| __Cel__                | Złożenie wniosku o usunięcie konta                    
| __Wyzwalanie__         | Organizator postanawia złożyć wniosek usunięcie konta w systemie                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto w systemie (UC15) </li><li> Organizator jest zalogowany (UC17) </li> </ul> 
| __War. końcowe__       | <ul><li> Powstanie wniosku </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na utworzenie nowego wniosku </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Proces składania wniosku musi być intuicyjny </li></ul>                                       
| __Scenariusz__         | 1. Organizator zgłasza wniosek o usunięcie aktywnego konta <br> 2. System wyświetla zapytanie o potwierdzenie intencji złożenia wniosku <br> 3. Organizator potwierdza intencję złożenia wniosku <br> 4. System rejestruje wniosek <br> 5. System informuje o złożeniu wniosku 

<br>

| __Nazwa__              |  Dodanie lokalizacji
| :----------------------| :--------------------------  
| __ID__                 | UC14                       
| __Aktor główny__       | Organizator         
| __Aktorzy__            | Organizator        
| __Priorytet__          | Główne                       
| __Opis__               | Podmiot dodaje do systemu informacje o lokalizacji przyszłych eventów                                   
| __Cel__                | Dodanie lokalizacji do systemu oraz umożliwienie jej użycia przy tworzeniu przyszłych wydarzeń                    
| __Wyzwalanie__         | Organizator postanawia dodać lokalizację do systemu                                              
| __War. początkowe__    | <ul><li> Organizator posiada konto (UC15) </li><li> Organizator jest zalogowany (UC17) </li></ul> 
| __War. końcowe__       | <ul><li> Powstanie wniosku </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na utworzenie nowego wniosku </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Interfejs tworzenia wniosku musi być przejrzysty </li></ul>                                       
| __Scenariusz__         | 1. Organizator wybiera opcję dodania lokalizacji <span style="color:purple">  [A1: Lokalizacja do wydarzenia ->] </span><br> 2. System prezentuje formularz tworzenia lokalizacji <br> <span style="color:purple">  [<- A1] </span> <br> 3. Organizator wypełnia formularz <br> 4. Organizator przesyła formularz <br> 5. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 6. System zapisuje lokalizację <br> 7. System wyświetla potwierdzenie sukcesu operacji
| __Scenariusz alternatywny__ |  A1: Lokalizacja do wydarzenia <br> &nbsp;&nbsp;&nbsp;a. Organizator dodaje lokalizację w trakcie tworzenia wydarzenia  <br> &nbsp;&nbsp;&nbsp;b. System prezentuje widok tworzenia lokalizacji
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne. <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie.

