Przypisane przypadki użycia:

- UC1: Przeglądanie listy wydarzeń
- UC2: Wyszukanie wydarzenia
- UC3: Wyświetlenie strony wydarzenia
- UC4: Stworzenie prywatnego konta 
- UC5: Zakup biletu
- UC6: Zwrot biletu
- UC7: Wyświetlanie historii zakupionych biletów

<br>

| __Nazwa__              | Przeglądanie listy wydarzeń  
| :----------------------| :-------------------------- 
| __ID__                 | UC1                          
| __Aktor główny__       | Użytkownik prywatny          
| __Aktorzy__            | Użytkownik prywatny          
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik może przeglądać wszystkie dostępne wydarzenia                                      
| __Cel__                | Pozwolenie użytkownikowi na poznanie pełnej oferty planowanych wydarzeń                        
| __Wyzwalanie__         | Użytkownik postanawia przejrzeć listę wydarzeń                                                  
| __War. początkowe__    | <ul><li>System posiada niezerową liczbę aktywnych wydarzeń</li></ul>                           
| __War. końcowe__       | <ul><li>Użytkownik przejrzał listę wydarzeń</li></ul>                                          
| __W. funkcjonalne__    | <ul><li>System wyświetla wszystkie aktualne wydarzenia zarejestrowane w systemie</li></ul>     
| __W. niefunkcjonalne__ | <ul><li>Widok wydarzeń powinien być estetyczny</li></ul>                                       
| __Scenariusz__         | 1. Użytkownik otwiera widok listy wydarzeń <br> 2. System wyświetla ofertę wydarzeń            

<br>

| __Nazwa__              | Wyszukanie wydarzenia  
| :----------------------| :--------------------------  
| __ID__                 | UC2                         
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Opcjonalne                       
| __Opis__               | Użytkownik może wyszukać po nazwie konkretne wydarzenie                                     
| __Cel__                | Pozwolenie użytkownikowi na szybsze znajdowanie interesujących go wydarzeń                      
| __Wyzwalanie__         | Użytkownik postanawia wyszukać wydarzenie w systemie                                               
| __War. początkowe__    | <ul><li> Użytkownik jest w stanie przeglądać listę wydarzeń (UC1) </li><li> System posiada niezerową liczbę aktywnych wydarzeń </li> </ul> 
| __War. końcowe__       | <ul><li> Użytkownik pomyślnie wyszukał wydarzenie </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System posiada wyszukiwarkę umożliwiającą wyszukanie wydarzenia </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Wyszukiwarka powinna być umiejscowiona w widocznym i intuicyjnym miejscu na stronie listy wydarzeń </li></ul>                                       
| __Scenariusz__         | 1. Użytkownik wprowadza nazwę konkretnego wydarzenia <br> 2. System aktualizuje prezentowaną listę wydarzeń <span style="color:red"> [E1: Wyszukanie nie zwraca żadnych wyników] </span>.
| __Scenariusz wyjątku__ | E1. Wyszukanie nie zwraca żadnych wyników <br> &nbsp;&nbsp;&nbsp;a. Nie zostało odnalezione wydarzenie zawierające podaną frazę. System informuje użytkownika o braku wyników wyszukiwania           

<br>

| __Nazwa__              | Wyświetlenie strony wydarzenia  
| :----------------------| :--------------------------  
| __ID__                 | UC3                        
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik może wyświetlić stronę poświęconą konkretnemu wydarzeniu                                    
| __Cel__                | Umożliwienie użytkownikowi poznania szczegółów dotyczących wydarzenia                     
| __Wyzwalanie__         | Użytkownik postanawia dowiedzieć się więcej o konkretnym wydarzeniu                                              
| __War. początkowe__    | <ul><li> Użytkownik jest w stanie przeglądać listę wydarzeń (UC1) </li><li> System posiada niezerową liczbę aktywnych wydarzeń </li> </ul> 
| __War. końcowe__       | <ul><li> Użytkownik pomyślnie wyświetlił stronę wydarzenia  </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System wyświetla szczegółowe dane o wydarzeniu </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Widok wydarzenia powinien być estetyczny </li><li> Dane wydarzenia powinny być przedstawione w sposób czytelny </li></ul>                                       
| __Scenariusz__         | 1. Użytkownik znajduje interesujące go wydarzenie w liście wydarzeń i wybiera je  <br> 2. System wyświetla użytkownikowi dane dotyczące wybranego wydarzenia
  
       
<br>

| __Nazwa__              |  Stworzenie prywatnego konta
| :----------------------| :--------------------------  
| __ID__                 | UC4                        
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik prywatny tworzy konto w systemie                                   
| __Cel__                | Umożliwienie użytkownikowi szerszej interakcji z systemem                    
| __Wyzwalanie__         | Użytkownik chce uzyskać dostęp do akcji wymagających posiadania konta                                              
| __War. początkowe__    | <ul><li> Użytkownik nie jest zalogowany </li>
| __War. końcowe__       | <ul><li> Użytkownik pomyślnie założył konto </li><li> Użytkownik może zalogować się na konto </li> </ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na założenie konta </li><li> System dokonuje walidacji </li><li> System informuje o błędach lub sukcesach </li> </ul>     
| __W. niefunkcjonalne__ | <ul><li> Formularz musi być prosty i przejrzysty </li></ul>                                       
| __Scenariusz__         | 1. Użytkownik wchodzi na dowolną stronę aplikacji <br> 2. Użytkownik wchodzi w widok rejestracji <br> 3. System wyświetla widok formularza rejestracji <br> 4. Użytkownik wypełnia formularz <br> 5. Użytkownik przesyła formularz <br> 6. System dokonuje walidacji danych <span style="color:red"> [E1: Dane nie przechodzą walidacji] </span> <br> 7. System tworzy konto użytkownika <br> 8. System potwierdza rejestrację <br> 9. System przekierowuje użytkownika do widoku logowania  
| __Scenariusz wyjątku__ | E1: Dane nie przechodzą walidacji <br> &nbsp;&nbsp;&nbsp;a. Użytkownik podaje dane, które nie przechodzą walidacji lub są niepełne <br> &nbsp;&nbsp;&nbsp;b. System wyświetla użytkownikowi informację o błędzie   

<br>

| __Nazwa__              |  Zakup biletu
| :----------------------| :--------------------------  
| __ID__                 | UC5                        
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik prywatny dokonuje zakupu biletu za pośrednictwem systemu                                   
| __Cel__                | Umożliwienie użytkownikowi zdalnego kupna biletów                     
| __Wyzwalanie__         | Użytkownik postanawia kupić bilet na interesujące go wydarzenie                                              
| __War. początkowe__    | <ul><li> Użytkownik posiada konto (UC4) </li><li> Użytkownik jest zalogowany (UC17) </li><li> System posiada niezerową liczbę aktywnych wydarzeń </li> <li>  Dostępne są bilety na wybrane wydarzenie  </li> </ul> 
| __War. końcowe__       | <ul><li> Użytkownik pomyślnie zakupił bilet </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System generuje rezerwację biletu i udostępnia sposób płatności </li><li> System wyświetla informacje o opcjach biletu </li> </ul>    
| __W. niefunkcjonalne__ | <ul><li> Interfejs kupna biletu powinien być przejrzysty </li><li> Transakcja kupna biletu musi być bezpieczna </li> </ul>                                       
| __Scenariusz__         | 1. Użytkownik wchodzi na stronę wydarzenia (UC3) <br> 2. Użytkownik wybiera opcję zakupu biletu <br> 3. System wyświetla widok zamówienia biletu, w tym jego warianty (jeśli istnieją) <br> 4. Użytkownik wybiera bilet i potwierdza zakup <br> 5. Użytkownik realizuje płatność <span style="color:red"> [E1: Błąd w realizacji] </span> <br> 6. System potwierdza dokonanie zakupu  <br> 7. System wysyła użytkownikowi jego kopię biletu
| __Scenariusz wyjątku__ | E1: Błąd w realizacji <br> &nbsp;&nbsp;&nbsp;a. Użytkownikowi nie udało się sfinalizować transakcji  <br> &nbsp;&nbsp;&nbsp;b. System informuje o błędzie transakcji i pozwala na powrót do strony wydarzenia  

<br>

| __Nazwa__              |  Zwrot biletu
| :----------------------| :--------------------------  
| __ID__                 | UC6                        
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Drugorzędne                      
| __Opis__               | Użytkownik anuluje swoją rezerwację biletu i otrzymuje zwrot pieniędzy                                  
| __Cel__                | Umożliwienie użytkownikowi sposobu na zwrot biletu                    
| __Wyzwalanie__         | Użytkownik postanawia zwrócić zakupiony w systemie bilet                                              
| __War. początkowe__    | <ul><li> Użytkownik posiada konto (UC4) </li><li> Użytkownik jest zalogowany (UC17) </li> <li> Użytkownik zakupił bilet na wydarzenie (UC5) </li><li> Wydarzenie jeszcze nie odbyło się </li><li> Wydarzenie nie zostało anulowane </li></ul> 
| __War. końcowe__       | <ul><li> Pomyślnie zwrócono bilet </li><li> Następuje zwrot pieniędzy do użytkownika </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System pozwala na usuwanie rezerwacji i zwrot pieniędzy </li></ul>    
| __W. niefunkcjonalne__ | <ul><li> Zwrot pieniędzy powinien być bezpieczny </li><li> Interfejs zwrotu powinien być prosty </li></ul>                                       
| __Scenariusz__         | 1. Użytkownik przechodzi do strony z listą zakupionych biletów <br> 2. Użytkownik wybiera bilet do usunięcia spośród swoich rezerwacji <br> 3. Użytkownik potwierdza intencję zwrotu <span style="color:red"> [E1: Anulowanie] </span> <br> 4. System rejestruje prośbę <br> 5. System potwierdza zwrot biletu <br> 6. System zleca zwrot pieniędzy
| __Scenariusz wyjątku__ | E1: Anulowanie  <br> &nbsp;&nbsp;&nbsp;a.  Użytkownik postanawia nie zwracać biletu

<br>

| __Nazwa__              |  Wyświetlanie historii zakupionych biletów
| :----------------------| :--------------------------  
| __ID__                 | UC7                        
| __Aktor główny__       | Użytkownik prywatny         
| __Aktorzy__            | Użytkownik prywatny         
| __Priorytet__          | Główne                       
| __Opis__               | Użytkownik może przeglądać historię zakupionych biletów                                  
| __Cel__                | Pozwolenie użytkownikowi na przejrzenie historii zakupionych przez niego biletów                    
| __Wyzwalanie__         | Użytkownik postanawia przejrzeć historię zakupionych biletów                                              
| __War. początkowe__    | <ul><li> Użytkownik posiada konto (UC4) </li><li> Użytkownik jest zalogowany (UC17) </li><li> Użytkownik zakupił bilet na wydarzenie (UC5) </li></ul> 
| __War. końcowe__       | <ul><li> Użytkownik przejrzał historię zakupionych biletów </li></ul>                                         
| __W. funkcjonalne__    | <ul><li> System zachowuje historię zakupu biletów dla użytkowników </li><li> Użytkownicy mogą zapoznać się ze swoją historią zakupów </li></ul>    
| __W. niefunkcjonalne__ | -                                       
| __Scenariusz__         | 1.  Użytkownik otwiera widok historii zakupionych biletów <br> 2. System wyświetla listę zakupionych biletów posortowaną w kolejności zakupu, wyświetlającą podstawowe dane dot. biletów <br> <span style="color:blue">  [O1: Wyświetlenie szczegółów] </span>
| __Scenariusz opcjonalny__ |  O1: Wyświetlenie szczegółów  <br> &nbsp;&nbsp;&nbsp;a. Użytkownik wybiera bilet na interesujące go wydarzenie <br> &nbsp;&nbsp;&nbsp;b. System wyświetla użytkownikowi szczegółowe informacje o wybranym wydarzeniu