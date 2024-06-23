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
| __Scenariusz__         | 1. Administrator otwiera stronę listy wniosków używając przycisku `Wnioski` na pasku nawigacyjnym. <br> 2. Administrator wybiera z listy wniosek o rejestrację <br> 3. System wyświetla okno zawierające dane z wniosku <br> 4. Administrator weryfikuje dane <span style="color:red"> [E1: Odrzucenie wniosku] </span><br> <span style="color:blue"> [O1: Dodatkowa komunikacja] </span> <br> 5. Administrator inicjuje tworzenie konta poprzez użycie przycisku akceptacji wniosku <br> 6. System tworzy konto <br> 7. Organizator zostaje powiadomiony mailowo o stworzenia konta.
| __Scenariusz opcjonalny__ |  O1: Dodatkowa komunikacja <br> &nbsp;&nbsp;&nbsp;a. Administrator kontaktuje się z organizatorem za pośrednictwem zewnętrznych narzędzi w celu uszczegółowienia warunków współpracy.
| __Scenariusz wyjątku__ |   E1: Odrzucenie wniosku <br> &nbsp;&nbsp;&nbsp;a. Administrator odrzuca wniosek. <br> &nbsp;&nbsp;&nbsp;b. System wysyła do wnioskodawcy wiadomość mailową informującą o odrzuceniu zgłoszenia. 

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
| __Scenariusz__         | 1. Administrator otwiera stronę listy wniosków używając przycisku `Wnioski` na pasku nawigacyjnym. <br> 2. Administrator wybiera z listy wniosek o usunięcie konta <br> 3. System wyświetla okno zawierające dane z wniosku <br> 4. Administrator otwiera stronę listy kont użytkowników używając przycisku `Użytkownicy` na pasku nawigacyjnym. <br> 5. Administrator wybiera użytkownika z wyświetlonej listy <br> 6. Administrator używa przycisku z ikoną kosza, odpowiadającego za usunięcie konta organizatora <br> 7. System usuwa konto organizatora <br> <span style="color:blue">   [O1: Usunięcie wydarzeń] </span>
| __Scenariusz opcjonalny__ |  O1: Usunięcie wydarzeń  <br> &nbsp;&nbsp;&nbsp;a. System usuwa również wszystkie wydarzenia powiązane z organizatorem, jeśli istnieją. <br> &nbsp;&nbsp;&nbsp;b. System inicjuje zwrot środków dla posiadaczy biletów <br> &nbsp;&nbsp;&nbsp;c. Posiadacze biletów otrzymują powiadomienia e-mail o odwołaniu wydarzeń.