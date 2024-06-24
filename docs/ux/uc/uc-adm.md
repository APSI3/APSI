Przypisane przypadki użycia:

- UC14: Rejestrowanie organizatora

<br>

| __Nazwa__              | Rejestrowanie organizatora
| :----------------------| :--------------------------  
| __ID__                 | UC14                       
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
