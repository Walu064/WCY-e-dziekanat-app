WCY-e-dziekanat-app

## Klonowanie Repozytorium
1. Sklonuj repozytorium przy użyciu Git:
```
git clone https://github.com/Walu064/WCY-e-dziekanat-app
```

## Instalacja Android Studio
2. Pobierz i zainstaluj Android Studio (wersja Hedgehog) ze strony oficjalnej Android Studio.

## Instalacja i Uruchomienie API
3. Uruchom Windows PowerShell (lub inny terminal), przejdź do katalogu `WCY-e-dziekanat-app/backend`.
4. Zainstaluj `venv` (środowisko wirtualne Python’a):
- Windows PowerShell:
  ```
  python -m venv .venv
  ```
- Linux terminal:
  ```
  python3 -m venv .venv
  ```
5. Aktywuj środowisko wirtualne i zainstaluj wymagane pakiety:
- Windows PowerShell:
  ```
  .venv/Scripts/Activate
  pip install -r requirements.txt
  ```
- Linux terminal:
  ```
  source .venv/bin/activate
  pip3 install -r requirements.txt
  ```
6. Przejdź do katalogu ze skryptem uruchamiającym API:
```
cd app
```

Copy code
7. Uruchom API:
- Windows PowerShell:
  ```
  python main.py
  ```
- Linux terminal:
  ```
  python3 main.py
  ```
8. Dokumentacja API dostępna jest pod adresem `http://localhost:8000/docs`.

## Instalacja i Uruchomienie Aplikacji
9. Otwórz Android Studio w wersji Hedgehog, otwórz projekt `WCY-e-dziekanat-app`.
10. Utwórz emulator (zalecany to „Medium Phone API 34”).
11. Zaloguj się numerem albumu i hasłem utworzonego wcześniej użytkownika.
12. Sprawdzaj, czy dane wprowadzone do bazy wyświetlają się w aplikacji prawidłowo.
