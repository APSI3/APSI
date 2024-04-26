Przypisane przypadki użycia:

- UC17: Zalogowanie się na konto

<br>

| __Nazwa__              | Zalogowanie się na konto
| :----------------------| :--------------------------  
| __ID__                 | UC17                       
| __Aktor główny__       | Dowolny użytkownik systemu        
| __Aktorzy__            | Dowolny użytkownik systemu      
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik loguje się na swoje konto                                 
| __Cel__                | Umożliwienie użytkownikowi zalogowania się na swoje konto, z którym powiązane są pewnego rodzaju uprawnienia                   
| __Wyzwalanie__         | Użytkownik chce się zalogować                                         
| __War. początkowe__    | <ul><li> Użytkownik posiada konto (UC4) </li><li> Użytkownik nie jest zalogowany </li></ul> 
| __War. końcowe__       | <ul><li> Użytkownik jest zalogowany </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System umożliwia zalogowanie się na wcześniej utworzone konto </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Formularz logowania powinien być intuicyjny </li></ul>                                       
| __Scenariusz__         | 1. Użytkownik wchodzi na dowolną stronę aplikacji <br> 2. Użytkownik wybiera opcję logowania <br> 3. System prezentuje formularz logowania <br> 4. Użytkownik wypełnia formularz <br> 5. Użytkownik przesyła formularz <br> 6.  System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji]</span> <br> 7. System wyświetla potwierdzenie logowania <br> 8. System przekierowuje użytkownika do strony głównej
| __Scenariusz wyjątku__ |   E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Użytkownik podaje dane, które nie przechodzą walidacji lub są niepełne.  <br> &nbsp;&nbsp;&nbsp;b. System wyświetla użytkownikowi informację o błędzie.