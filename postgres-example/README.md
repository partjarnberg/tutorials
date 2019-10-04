# Postgres example

## Run it
    docker-compose -f stack.yml up

For failcheck - open a browser and inspect database at
    http://localhost:8080 System: PostgreSQL user:postgres password: example

Then open a terminal and run

    ➜  ~ docker ps
    CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
    2ed0a0562609        postgres            "docker-entrypoint.s…"   5 minutes ago       Up 4 minutes        5432/tcp                 postgres-example_db_1
    dfe7b939dbc2        adminer             "entrypoint.sh docke…"   5 minutes ago       Up 4 minutes        0.0.0.0:8080->8080/tcp   postgres-example_adminer_1

Login to container and create a database

    ➜  ~ docker exec -it 114465764dcd bash
    root@2ed0a0562609:/# psql -U postgres
    psql (11.5 (Debian 11.5-1.pgdg90+1))
    Type "help" for help.
    
    postgres=# CREATE DATABASE imdb;
    CREATE DATABASE
    postgres=# \q

## Download data from IMDB
Download name.basics.tsv.gz and title.basics.tsv.gz from [IMDB](https://datasets.imdbws.com/).

Extract the packages and place files in `data/name-data.tsv` and `data/title-data.tsv`. They will contain tab separated values (.tsv-file).

Create table for names.

    postgres=# CREATE TABLE imdb_name (nconst VARCHAR, primaryName VARCHAR, birthYear VARCHAR, deathYear VARCHAR, primaryProfession VARCHAR, knownForTitles VARCHAR);
    CREATE TABLE
    postgres=# COPY imdb_name FROM '/opt/data/name-data.tsv';
    COPY 9613244
    
Create table for titles.

    postgres=# CREATE TABLE imdb_title (tconst VARCHAR, titleType VARCHAR, primaryTitle VARCHAR, originalTitle VARCHAR, isAdult VARCHAR, startYear VARCHAR, endYear VARCHAR, runtimeMinutes VARCHAR, genres VARCHAR);
    CREATE TABLE
    postgres=# COPY imdb_name FROM '/opt/data/title-data.tsv';
    COPY 6205364

Select some films  

    postgres=# SELECT p.primaryname,
                  p.birthyear,
                  p.deathyear,
                  p.primaryprofession,
                  array_agg(t.primarytitle) as famoustitles
        FROM imdb_name p JOIN imdb_title t
                          ON t.tconst = any(string_to_array(p.knownfortitles, ','))
           group by primaryname,
                    p.birthyear,
                    p.deathyear,
                    p.primaryprofession limit 10;
                    
Takes forever? Create index                     

    postgres=# CREATE INDEX IF NOT EXISTS name_index ON imdb_name (primaryname, birthyear, deathyear, primaryprofession);
    CREATE INDEX
    postgres=# CREATE INDEX IF NOT EXISTS title_index ON imdb_title (tconst);
    CREATE INDEX

Then run the same query again

    postgres=# SELECT p.primaryname,
              p.birthyear,
              p.deathyear,
              p.primaryprofession,
              array_agg(t.primarytitle) as famoustitles
    FROM imdb_name p JOIN imdb_title t
                      ON t.tconst = any(string_to_array(p.knownfortitles, ','))
       group by primaryname,
                p.birthyear,
                p.deathyear,
                p.primaryprofession limit 10;
      primaryname   | birthyear | deathyear |        primaryprofession         |                                      famoustitles
    ----------------+-----------+-----------+----------------------------------+----------------------------------------------------------------------------------------
     +/-            |           |           | soundtrack                       | {"Wicker Park",Rakenrol}
     0010x0010      |           |           | director,editor,composer         | {"Wir Sind","Dark Edges","The Prelude","Quod Licet Iovi, Non Licet Bovi"}
     070 Shake      |           |           | actress,soundtrack,composer      | {"070 Shake: Nice to Have","The Tonight Show Starring Jimmy Fallon","Trust Nobody"}
     091            |           |           | soundtrack                       | {"Tal cual"}
     ½ Pint         |           |           |                                  | {"Shefik presents Invocation: Journeyers"}
     100%           |           |           | camera_department,director,actor | {"Handsy Daughter","Girlsway Originals","Fantasy Factory Wastelands","Girls Try Anal"}
     10,000 Maniacs |           |           | soundtrack                       | {"Bringing Out the Dead",Stones,"Cold Case","Breaking Bad"}
     1000 Robota    |           |           | composer                         | {"Utopia Ltd."}
     1000Tinho      |           |           |                                  | {Coruja}
     100 Derece     |           |           | composer                         | {"Günesin Oglu"}
    (10 rows)
    
Voila! But what about full text searches? ;)