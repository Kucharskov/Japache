# Japache
Aplikacja zrealizowana w ramach projektu na studiach w języku Java. Jest to prosty konsolowy serwer plików statycznych w postaci zasobu sieciowego. Główną zaletą jest jego prostota umożliwająca działanie pod systemem zarówno Windows jak i Linux.

## Autorzy
Projekt został wykonany przez studentów Politechniki Śląskiej na wydziale Wydział Inżynierii Materiałowej i Metalurgii na kierunku Informatyka Przemysłowa:
- **Michał Kucharski**, główne zadanie to chorobliwie paniczna i nadmierna optymalizacja skutkująca ogromem poprawek.
- **Monika Husar**, główne zadanie to ogrom przeróżnych pomysłów, których nie było czasu zaimplemetnować.

## Funkcjonalność
- Serwowanie plików z zadanego katalogu pod zadanym adresem i portem poprzez generowanie odpowiedzi na zapytania typu "GET" i "HEAD"
- Możliwość konfiguracji z pliku (plik **config.properties**) opcji takich jak:
  - katalog z danymi
  - adres i port na którym serowana będzie usługa
  - tryb debugowania (rozumiany jako tryb "verbose")
  - listowanie katalogów
- Generowanie skromnych ale czytelnych błędów typu 501, 404 zgodnych w formatowaniu z Internet Explorer 9
- Zabezpieczenia przed m. in:
  - wyjściem dostępem poza zadeklarowany katalog z danymi
  - skasowaniem fragmentu konfiguracji
  - skasowaniem całego pliku kofnguracyjnego
- Wielowątkowość i zdolność do pracy na systemach Windows i Linux nawet pod sporym obciążeniem
- Zwracanie w konsoli prostych i czytelnych, formatowanych komunikatów
- Skalowalność projektu prosta w rozbudowie o kolejne możliwości w pliku oraz obsłudze po stronie kodu

## Niedoskonałości i ewentualne możliwości rozbudowy
- Strony HTML wrzucane w podkatalogi muszą mieć linkowane adresy absoltunie względem głównego katalogu zamiast relatywnych, ponieważ w obecnie użytym rozwiązaniu (klasa HttpServer) nie istnieje możliwość pilnowania ścieżki podzapytania.
- Dodanie w kodzie obsługi innych zaimplementowanych już możliwych do wygenerowania stron z błędami (na 30 opisanych kodów zwracane jest zaledwie 5)
- Dodanie graficznego interfejsu użytkownika (z ciągłym wsparciem dla tekstowych konsoli systemu Linux)
- Dodanie systemu wielu języków
- Dodanie obsługi SSL