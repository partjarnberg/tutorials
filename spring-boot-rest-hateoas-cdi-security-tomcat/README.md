# spring-boot-rest-hateoas-cdi-security-tomcat

## Requirements
1. REST framework
2. HATEOAS support
3. Dependency injection
4. Basic Auth
5. Websockets
6. Embedded web server
7. Executable jar

## Technologies being used
- Spring Boot HATEOAS
- Spring Boot Security

## Build and run

### Build
mvn clean package

### Run
java -jar target/spring-boot-rest-hateoas-cdi-security-tomcat-1.0-SNAPSHOT.jar --server.port=8080

#### Basic Auth
User: admin

Password: prepareforglory

#### Example output
Goto http://localhost:8080/my-app/api/persons to list persons:

```javascript
{
  "persons" : [ {
    "firstName" : "William",
    "lastName" : "Dickerson",
    "_links" : {
      "self" : {
        "href" : "http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30"
      }
    }
  }, {
    "firstName" : "Earlene",
    "lastName" : "Walker",
    "_links" : {
      "self" : {
        "href" : "http://localhost:8080/my-app/api/persons/d2391bd5-3bd1-4093-bae0-2e2af81ae8b3"
      }
    }
  }, {
    "firstName" : "Kenneth",
    "lastName" : "Finnegan",
    "_links" : {
      "self" : {
        "href" : "http://localhost:8080/my-app/api/persons/824d8dc9-3598-4550-abe9-07ee2488bb56"
      }
    }
  }, {
    "firstName" : "Kirk",
    "lastName" : "Mason",
    "_links" : {
      "self" : {
        "href" : "http://localhost:8080/my-app/api/persons/ba2aaea6-5ccf-4894-a45e-c5972cb47415"
      }
    }
  }, {
    "firstName" : "John",
    "lastName" : "Doe",
    "_links" : {
      "self" : {
        "href" : "http://localhost:8080/my-app/api/persons/66523a72-abd3-46e1-aaf5-0e201329b8fd"
      }
    }
  } ],
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/my-app/api/persons"
    }
  }
}
```

Follow link http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30 to see more info on that person:

```javascript
{
  "firstName" : "William",
  "lastName" : "Dickerson",
  "email" : "william.dickerson@mail.com",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30"
    },
    "list" : {
      "href" : "http://localhost:8080/my-app/api/persons"
    },
    "jobs" : {
      "href" : "http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30/jobs"
    }
  }
}
```

Follow http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30/jobs to list jobs of that person:

```javascript
{
  "jobs" : [ {
    "profession" : "Musician",
    "employer" : "Coolest company ever",
    "_links" : {
      "employer" : {
        "href" : "http://localhost:8080/my-app/api/employers/529d8f02-9c72-4655-9963-6b9a6f9a76e4"
      }
    }
  }, {
    "profession" : "Engineer",
    "employer" : "Shelf Company",
    "_links" : {
      "employer" : {
        "href" : "http://localhost:8080/my-app/api/employers/5940abce-c427-43f0-a4a2-ea3ddc5b7158"
      }
    }
  }, {
    "profession" : "Youtube Star",
    "employer" : "Software Company",
    "_links" : {
      "employer" : {
        "href" : "http://localhost:8080/my-app/api/employers/201aa9ad-f194-446c-8ef5-cd7f9d40c1e7"
      }
    }
  }, {
    "profession" : "Clerk",
    "employer" : "Fancy Company",
    "_links" : {
      "employer" : {
        "href" : "http://localhost:8080/my-app/api/employers/689dd7c1-2035-4276-9b2b-494f347b71b0"
      }
    }
  }, {
    "profession" : "Software Developer",
    "employer" : "Hipsters",
    "_links" : {
      "employer" : {
        "href" : "http://localhost:8080/my-app/api/employers/5cd913f4-ffa2-474c-b405-661678711b98"
      }
    }
  } ],
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/my-app/api/persons/3a15e084-0d9d-43d0-bb43-722d0c448b30/jobs"
    }
  }
}
```

Follow http://localhost:8080/my-app/api/employers/529d8f02-9c72-4655-9963-6b9a6f9a76e4 to see more information on that employer:

```javascript
{
  "name" : "Coolest company ever",
  "email" : "contact@coolest.com",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/my-app/api/employers/529d8f02-9c72-4655-9963-6b9a6f9a76e4"
    }
  }
}
```