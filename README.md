# Oglasi
 
Sustav za objavu i pregled oglasa

---

## Pokretanje aplikacije:

### Baza podataka:
    ```bash
    $ cd DZ3/oglasi-db
    ```
    ```bash
    $ ./start_db.sh
    ```

### Backend:
    ```bash
    $ cd DZ3/oglasi-backend
    ```
    ```bash
    $ gradle bootRun
    ```

### Frontend:
    ```bash
    $ cd DZ3/oglasi-frontend
    ```
    ```bash
    $ npm install
    ```
    ```bash
    $ ng serve
    ```

## Prije pokretanja potrebno stvoriti env datoteku u DZ3 folderu:
    ```bash
    $ touch DZ3/env.properties
    ```

## I popunit ju slijedećim sadržajem:
```
DB_NAME=
DB_USER=
DB_PASSWORD=
DB_PORT=
```