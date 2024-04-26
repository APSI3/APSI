Przypisane przypadki użycia:

- UC15: Rejestrowanie organizatora
- UC16: Usuwanie organizatora

<br>

| __Nazwa__              | Rejestrowanie organizatora
| :----------------------| :--------------------------  
| __ID__                 | UC15                       
| __Aktor główny__       | Administrator        
| __Aktorzy__            | Administrator, organizator      
| __Priorytet__          | Główne                       
| __Opis__               | Administrator tworzy konto dla organizatora                                   
| __Cel__                | Umożliwienie zewnętrznym firmom wykorzystanie systemu do promowania swoich wydarzeń i sprzedaży biletów                   
| __Wyzwalanie__         | Organizator składa wniosek o utworzenie profilu                                          
| __War. początkowe__    | <ul><li> Administrator jest zalogowany (UC17) </li><li> Istnieje co najmniej jeden nierozwiązany wniosek </li></ul> 
| __War. końcowe__       | <ul><li> Utworzony zostaje profil firmy wewnątrz systemu </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System umożliwia administratorowi tworzenie nowych kont organizatorów </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Zgłoszenia o rejestrację powinny być odporne na nadużycia </li><li> Kontakt z administratorem powinien być szybki </li></ul>                                       
| __Scenariusz__         | 1. Administrator otwiera widok listy wniosków <br> 2. Administrator weryfikuje wniosek <span style="color:red"> [E1: Odrzucenie wniosku] </span><br> <span style="color:blue"> [O1: Dodatkowa komunikacja] </span> <br> 3. Administrator inicjuje tworzenie konta <br> 4. System prezentuje formularz tworzenia konta uzupełniony danymi podanymi przez wnioskodawcę <br> 5. Administrator przesyła formularz <br> 6. System waliduje zgłoszenie <span style="color:red"> [E2: Dane nie przechodzą walidacj]</span> <br> 7. System tworzy konto
| __Scenariusz opcjonalny__ |  O1: Dodatkowa komunikacja <br> &nbsp;&nbsp;&nbsp;a. Administrator kontaktuje się z organizatorem za pośrednictwem zewnętrznych narzędzi w celu uszczegółowienia warunków współpracy.
| __Scenariusz wyjątku__ |   E1: Odrzucenie wniosku <br> &nbsp;&nbsp;&nbsp;a. Administrator odrzuca wniosek. <br> &nbsp;&nbsp;&nbsp;b. System wysyła do wnioskodawcy wiadomość informującą o odrzuceniu zgłoszenia. <br> E2: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Administrator podaje dane, które nie przechodzą walidacji lub są niepełne.  <br> &nbsp;&nbsp;&nbsp;b. System informuje administratora o błędzie. <br> &nbsp;&nbsp;&nbsp;c. Administrator informuje organizatora o wadliwych danych.

<br>

| __Nazwa__              | Usuwanie organizatora
| :----------------------| :--------------------------  
| __ID__                 | UC16                       
| __Aktor główny__       | Administrator        
| __Aktorzy__            | Administrator, organizator      
| __Priorytet__          | Drugorzędny                       
| __Opis__               | Administrator usuwa konto organizatora, na jego prośbę lub z innych przyczyn                                  
| __Cel__                | Umożliwienie usunięcia konta organizatora administratorowi
| __Wyzwalanie__         | <ul><li> Organizator wysyła prośbę do administratora o usunięcie profilu </li><li> Administrator podejmuje decyzję o usunięciu profilu z innych przyczyn </li></ul>                                        
| __War. początkowe__    | <ul><li> Administrator jest zalogowany (UC17) </li><li> Istnieje konto organizatora w systemie (UC15) </li></ul> 
| __War. końcowe__       | <ul><li> Profil organizatora zostaje usunięty z systemu </li><li> Wszystkie wydarzenia powiązane z organizatorem zostają usunięte </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System umożliwia administratorowi usuwanie kont organizatorów </li><li> System umożliwia automatyczne usuwanie wydarzeń powiązanych z organizatorem </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> System powinien być spójny przed i po usunięciu wydarzenia </li><li> System powinien być bezpieczny, aby uniknąć niepożądanego usunięcia profilu </li></ul>                                       
| __Scenariusz__         | 1. Administrator otwiera widok listy kont użytkowników <br> 2. Administrator wybiera opcję usunięcia konta organizatora <br> 3. System usuwa konto  <br> <span style="color:blue">   [O1: Usunięcie wydarzeń] </span>
| __Scenariusz opcjonalny__ |  O1: Usunięcie wydarzeń  <br> &nbsp;&nbsp;&nbsp;a. System usuwa również wszystkie wydarzenia powiązane z organizatorem, jeśli istnieją. <br> &nbsp;&nbsp;&nbsp;b. System inicjuje zwrot środków dla posiadaczy biletów