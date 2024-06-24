Przypisane przypadki użycia:

- UC7: Stworzenie wniosku o utworzenie profilu
- UC8: Stworzenie wydarzenia
- UC9: Edycja danych wydarzenia
- UC10: Anulowanie wydarzenia
- UC11: Przeglądanie statystyk wydarzenia
- UC12: Dodanie lokalizacji

<br>

| __Nazwa__              |  Stworzenie wniosku o utworzenie profilu
| :----------------------| :--------------------------  
| __ID__                 | UC7                       
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
| __Scenariusz__         | 1. Organizator wchodzi na stronę rejestracji <br> 2. Organizator wybiera opcję konta organizatora, używając przycisku `Formularz organizatora`. <br> 3. System prezentuje formularz tworzenia wniosku <br> 4. Organizator wypełnia formularz <br> 5. Organizator przesyła formularz <br> 6. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 7. System zapisuje wniosek
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne. <br> &nbsp;&nbsp;&nbsp;b. System wyświetla powiadomienie o błędzie

<br>

| __Nazwa__              |  Stworzenie wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC8                       
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
| __Scenariusz__         | 1. Organizator używa przycisku z ikoną plusa, znajdującego się w prawym dolnym rogu strony, służącego do tworzenia wydarzenia,<br> 2. System wyświetla okno z kreatorem wydarzenia <br> 3. Organizator wypełnia formularz danymi dot. wydarzenia <br> 4. Organizator przesyła formularz <br> 5. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 6. System rejestruje wydarzenie w bazie danych
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne <br> &nbsp;&nbsp;&nbsp;b. System wyświetla powiadomienie o błędzie

<br>

| __Nazwa__              | Edycja danych wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC9                       
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
| __Scenariusz__         | 1. Organizator otwiera stronę listy swoich wydarzeń przy użyciu przycisku `Twoje wydarzenia` na pasku nawigacyjnym. <br> 2. System przygotowuje widok i prezentuje go organizatorowi <br> 3. Organizator wybiera wydarzenie, z listy <br> 4. Organizator używa przyciskuz ikoną ołówka, służącego do edycji <br> 5. System wyświetla organizatorowi okno z formularzem edycji <br> 6. Organizator edytuje wybrane informacje <br> 7. Organizator przesyła formularz <br> 8. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 9. System aktualizuje dane wydarzenia <br> 10. System wyświetla organizatorowi powiadomienie o pomyślnej edycji <br> 11. System powiadamia mailowo o zaistniałych zmianach klientów posiadających bilety na wydarzenie 
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie

<br>

| __Nazwa__              |  Anulowanie wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC10                       
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
| __Scenariusz__         | 1. Organizator otwiera stronę listy swoich wydarzeń przy użyciu przycisku `Twoje wydarzenia` na pasku nawigacyjnym. <br> 2. System przygotowuje widok i prezentuje go organizatorowi <br> 3. Organizator wybiera wydarzenie, które chce anulować <br> 3. Organizator używa przycisku z ikoną śmietnika, służącego do usuwania wydarzenia  <br> 5. System wyświetla informację o nieodwracalności operacji <br> 6. Organizator potwierdza intencję anulowania wydarzenia, używając przycisku akceptacji <span style="color:red"> [E1: Anulowanie] </span> <br> 7. System ustawia status wydarzenia na anulowane w bazie danych <br> 8. System wyświetla organizatorowi powiadomienie o pomyślnym anulowaniu eventu <br> 7. System powiadamia mailowo klientów posiadających bilety na wydarzenie o zaistniałych zmianach <br> 9. System zleca zwrot środków <br>
| __Scenariusz wyjątku__ |   E1: Anulowanie <br> &nbsp;&nbsp;&nbsp;a. Użytkownik postanawia nie anulować wydarzenia i używa przycisku `Anuluj`

<br>

| __Nazwa__              |  Przeglądanie statystyk wydarzenia
| :----------------------| :--------------------------  
| __ID__                 | UC11                       
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
| __Scenariusz__         | 1. Organizator otwiera stronę listy swoich wydarzeń przy użyciu przycisku `Twoje wydarzenia` na pasku nawigacyjnym. <br> 2. System wyświetla widok listy wydarzeń zawierający informacje o liczbie sprzedanych biletów dla każdego wydarzenia  <br> <span style="color:blue">  [O1: Sprawdzenie danych szczegółowych] </span>
| __Scenariusz opcjonalny__ |  O1: Sprawdzenie danych szczegółowych <br> &nbsp;&nbsp;&nbsp;a. Organizator wybiera wydarzenie, o którego sprzedaży biletów chce dowiedzieć się więcej  <br> &nbsp;&nbsp;&nbsp;b. Organizator używa przycisku z ikoną oka przenoszącego go na stronę wydarzenia  <br> &nbsp;&nbsp;&nbsp;c. System prezentuje organizatorowi stronę wybranego wydarzenia rozszerzoną o szczegółowe statystyki sprzedaży każdej z kategorii biletów

<br>

| __Nazwa__              |  Dodanie lokalizacji
| :----------------------| :--------------------------  
| __ID__                 | UC12                       
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
| __Scenariusz__         | 1. Organizator używa przycisku z ikoną pinezki, znajdującego się w prawym dolnym rogu strony, służącego do tworzenia lokalizacji <span style="color:purple">  [A1: Lokalizacja do wydarzenia ->] </span><br> 2. System prezentuje okno tworzenia lokalizacji <br> <span style="color:purple">  [<- A1] </span> <br> 3. Organizator wypełnia formularz <br> 4. Organizator przesyła formularz <br> 5. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 6. System tworzy lokalizację <br> 7. System wyświetla powiadomienie informujące o sukcesie operacji
| __Scenariusz alternatywny__ |  A1: Lokalizacja do wydarzenia <br> &nbsp;&nbsp;&nbsp;a. Organizator używa przycisku tworzenia lokalizacji umiejcowionego w oknie tworzenia wydarzenia <br> &nbsp;&nbsp;&nbsp;b. System prezentuje okno tworzenia lokalizacji
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Organizator podaje dane, które nie przechodzą walidacji lub są niepełne. <br> &nbsp;&nbsp;&nbsp;b. System informuje organizatora o błędzie.

