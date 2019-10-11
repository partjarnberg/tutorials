# Postgres example

## Run it
    docker-compose -f stack.yml up

Then

    ➜  ~ docker ps
    CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
    2ed0a0562609        postgres            "docker-entrypoint.s…"   5 minutes ago       Up 4 minutes        5432/tcp                 postgres-example_db_1
    dfe7b939dbc2        adminer             "entrypoint.sh docke…"   5 minutes ago       Up 4 minutes        0.0.0.0:8080->8080/tcp   postgres-example_adminer_1

Login to container and create a database

    ➜  ~ docker exec -it 2ed0a0562609 bash
    root@2ed0a0562609:/# psql -U postgres
    psql (11.5 (Debian 11.5-1.pgdg90+1))
    Type "help" for help.
    
    postgres=# CREATE DATABASE imdb;
    CREATE DATABASE
    postgres=# \c imdb
    You are now connected to database "imdb" as user "postgres".
    imdb=#

## Download and import data from IMDB
Download name.basics.tsv.gz and title.basics.tsv.gz from [IMDB](https://datasets.imdbws.com/).

Extract the packages. They will contain tab separated values (.tsv-file).
Place the files in the __data__ folder of this project (the volume is mounted in the container).

Create table for names.

    imdb=# CREATE TABLE imdb_name (nconst VARCHAR, primaryName VARCHAR, birthYear VARCHAR, deathYear VARCHAR, primaryProfession VARCHAR, knownForTitles VARCHAR);
    CREATE TABLE
    imdb=# COPY imdb_name FROM '/opt/data/name-data.tsv';
    COPY 9613244
    
Create table for titles.

    imdb=# CREATE TABLE imdb_title (tconst VARCHAR, titleType VARCHAR, primaryTitle VARCHAR, originalTitle VARCHAR, isAdult VARCHAR, startYear VARCHAR, endYear VARCHAR, runtimeMinutes VARCHAR, genres VARCHAR);
    CREATE TABLE
    imdb=# COPY imdb_title FROM '/opt/data/title-data.tsv';
    COPY 6205364

## Start querying
Select some films.  

    imdb=# SELECT p.primaryname,
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
                    
Takes forever? Create a couple of indices.                     

    imdb=# CREATE INDEX IF NOT EXISTS name_index ON imdb_name (primaryname, birthyear, deathyear, primaryprofession);
    CREATE INDEX
    imdb=# CREATE INDEX IF NOT EXISTS title_index ON imdb_title (tconst);
    CREATE INDEX

Then run the same query again.

    imdb=# SELECT p.primaryname,
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
    
Voila! It's only 10 rows, but try with 1000, 10000 or even 100000. The query will still perform pretty fast. 

But what about full text searches? ;) Maybe its time to look into [Elasticsearch](https://www.elastic.co) or similar?

## Inspect the database using adminer 
Inspect the database - open a browser and inspect database at [http://localhost:8080](http://localhost:8080)
    
__System:__ PostgreSQL 
__Server:__ db
__Username:__ postgres 
__Password:__ example
__Database:__ imdb

Assignment | Size | Competence areas
:--- | :---: | :---
[CTS next generation](https://github.com/cygni/competence/tree/master/cts-nextgen) | ![large](https://img.shields.io/badge/size-large-red.svg?longCache=true&style=flat)  | ![frontend](https://img.shields.io/badge/Frontend-blueviolet.svg?longCache=true&style=flat) ![fullstack](https://img.shields.io/badge/Fullstack-red.svg?longCache=true&style=flat) ![javascript](https://img.shields.io/badge/JavaScript-informational.svg?longCache=true&style=flat) ![single page application](https://img.shields.io/badge/Single_Page_Application-important.svg?longCache=true&style=flat) ![css](https://img.shields.io/badge/CSS-9cf.svg?longCache=true&style=flat) ![tdd](https://img.shields.io/badge/Test_Driven_Development-success.svg?longCache=true&style=flat)
[Movie search service](https://github.com/cygni/competence/tree/master/movie-search-service) | ![large](https://img.shields.io/badge/size-large-red.svg?longCache=true&style=flat) | ![backend](https://img.shields.io/badge/Backend-blue.svg?longCache=true&style=flat) ![fullstack](https://img.shields.io/badge/Fullstack-red.svg?longCache=true&style=flat) ![Database Design](https://img.shields.io/badge/Database_Design-darkred.svg?longCache=true&style=flat) ![Data indexing](https://img.shields.io/badge/Data_Indexing-darkgreen.svg?longCache=true&style=flat) ![API Design](https://img.shields.io/badge/API_Design-purple.svg?longCache=true&style=flat) ![REST API](https://img.shields.io/badge/REST_API-darkblue.svg?longCache=true&style=flat)
[Consultant map](https://github.com/cygni/competence/tree/master/consultant-map) | ![large](https://img.shields.io/badge/size-large-red.svg?longCache=true&style=flat) | ![fullstack](https://img.shields.io/badge/Fullstack-red.svg?longCache=true&style=flat) ![frontend](https://img.shields.io/badge/Frontend-blueviolet.svg?longCache=true&style=flat) ![backend](https://img.shields.io/badge/Backend-blue.svg?longCache=true&style=flat) ![Database Design](https://img.shields.io/badge/Database_Design-darkred.svg?longCache=true&style=flat) ![NoSQL](https://img.shields.io/badge/NoSQL-yellowgreen.svg?longCache=true&style=flat) ![API Design](https://img.shields.io/badge/API_Design-purple.svg?longCache=true&style=flat) ![REST API](https://img.shields.io/badge/REST_API-darkblue.svg?longCache=true&style=flat) ![javascript](https://img.shields.io/badge/JavaScript-informational.svg?longCache=true&style=flat) ![single page application](https://img.shields.io/badge/Single_Page_Application-important.svg?longCache=true&style=flat) ![css](https://img.shields.io/badge/CSS-9cf.svg?longCache=true&style=flat)
