# Authorization server

## Build
`$ ./gradlew clean build`

### Run
`$ java -jar build/libs/oauth2-authorization-server-1.0-SNAPSHOT.jar -httpPort 8080`

### Docker

#### Build 
`$ docker build -t programmersbuzz/oauth2-authorization-server .`

#### Run using docker
`$ docker run -p 127.0.0.1:8080:8080 --env SECURITY_OAUTH2_REDIRECTURIS=http://frontend.local/oauthcallback --env SECURITY_JWT_SIGNINGKEY=rV5Kjnrj2gG9Cp5Vn9B3xjEypUXDf5rx programmersbuzz/oauth2-authorization-server` 

#### Run using docker-compose
```
version: '3.7'

services:
  authorization-server:
    image: programmersbuzz/oauth2-authorization-server
    container_name: authorization-server
    environment:
      - SECURITY_OAUTH2_REDIRECTURIS=http://frontend.local/oauthcallback
      - SECURITY_JWT_SIGNINGKEY=rV5Kjnrj2gG9Cp5Vn9B3xjEypUXDf5rx
    ports:
      - '8080:8080'
```

## Security
The Authorization server support OAuth2 with [Authorization Code Grant](https://oauth.net/2/grant-types/authorization-code/) to obtain JWT access tokens.
This is the recommended OAuth2 grant flow for web and mobile apps.

OAuth2 grant flows _Implicit flow_ and _Password grant_ are intentionally not supported when they are [considered legacy](https://oauth.net/2/grant-types/) and should not be your first choice.    

### Users
Users and roles that are registered are the following:

| Username                        | Password     | Role  |
| ------------------------------- |:-------------| :-----|
| admin@programmers.buzz          | 5VmHyLCKxf68 | ADMIN |
| per.elofsson@programmers.buzz   | K6uT2nJmrVVv | USER  |
| markus.hellner@programmers.buzz | r8mX6YAD3j5P | USER  |

### Registered clients
One single client is registered:

| Client ID              | Secret                   | 
| ---------------------- |:------------------------ | 
| v44LXhKkb9             | NGt8YG85Va828xaGxuL87Jwx |

### Access the REST API
The following sections explain how you generate `access_token` for a user to be able to access the REST API resources.

#### 1 - Generate an access token
##### 1.1 Browse to the following URI with Client ID = `v44LXhKkb9`
```
http://localhost:8080/oauth/authorize?response_type=code&client_id=v44LXhKkb9&redirect_uri=http://frontend.local/oauthcallback
```

You are prompted with a form to enter credentials `username` and `password`. 

Response:
You get redirected (301) to `http://frontend.local/oauthcallback?code=XIdjtc`. See the code URL param? That's to be used to fetch the access token.

__HINT__: If you want to be redirected to another URI e.g. http://localhost:8080/oauthcallback change the value of query param `redirect_uri` accordingly.
Valid values are defined in `application.yml` on property `security.oauth2.redirectUris`. This can also be done using docker-compose:
```
authorization-server:
    build: ./authorization-server
    container_name: authorization-server
    environment:
      - SECURITY_OAUTH2_REDIRECTURIS=http://localhost:8080/oauthcallback
```

##### 1.2 For this example use curl to fetch the token:
Using Client ID = `v44LXhKkb9`, Secret = `NGt8YG85Va828xaGxuL87Jwx` and Authorization Code = `XIdjtc`
```
$ curl --request POST -i --insecure http://localhost:8080/oauth/token -H "Authorization: Basic $(echo -n v44LXhKkb9:NGt8YG85Va828xaGxuL87Jwx | base64)" -d code=XIdjtc -d grant_type=authorization_code -d redirect_uri=http://frontend.local/oauthcallback
```

Response:
```         
HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Cache-Control: no-store
Pragma: no-cache
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 25 Sep 2020 07:35:44 GMT

{
  "access_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDEwMjAyNDQsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNWVmMzFiMmUtNjRiYy00Y2RlLTkxMzMtZTc3N2Y4NzVkMDU2IiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.Arjz5q1mwPtty0VXZAEopPnR86E6YoYDRWfjEYx4mMU",
  "token_type" : "bearer",
  "refresh_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImF0aSI6IjVlZjMxYjJlLTY0YmMtNGNkZS05MTMzLWU3NzdmODc1ZDA1NiIsImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDExMDU3NDQsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiMTM0MWUyYzgtYzkzOS00NjE2LWExZGEtNzY4MTMyNjkyNjc0IiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.ZDzQn2v8y28D59lq-1vwbiwniYtlTMoomX890V3N_l0",
  "expires_in" : 899,
  "scope" : "any",
  "first_name" : "Super",
  "last_name" : "Hero",
  "jti" : "5ef31b2e-64bc-4cde-9133-e777f875d056"
}
```

###### Validity of tokens
The `access_token` for this authorization server is configured to be valid for `15 minutes`. The `refresh_token` is valid for `24 hours`. See details how to refresh the access_token further down in this README.

###### Decode the access token
The access token is a Json Web Token (JWT). [Decoded](https://jwt.io/) the above `access_token` contains the following information:
```
{
  "user_name": "admin@programmers.buzz",
  "scope": [
    "any"
  ],
  "last_name": "Hero",
  "exp": 1601020244,
  "first_name": "Super",
  "authorities": [
    "ROLE_ADMIN"
  ],
  "jti": "5ef31b2e-64bc-4cde-9133-e777f875d056",
  "client_id": "v44LXhKkb9"
}
```

#### 2 - Use the token to access resources at the backend REST API
```
$ curl --request GET -i --insecure https://backend.local/api -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDEwMjAyNDQsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNWVmMzFiMmUtNjRiYy00Y2RlLTkxMzMtZTc3N2Y4NzVkMDU2IiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.Arjz5q1mwPtty0VXZAEopPnR86E6YoYDRWfjEYx4mMU"
```

Response:
```     
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 25 Sep 2020 07:36:12 GMT
   
{
    ...
    ...
}
```

#### 3 - Refresh access token
```
$ curl --request POST -i --insecure http://localhost:8080/oauth/token -H "Authorization: Basic $(echo -n v44LXhKkb9:NGt8YG85Va828xaGxuL87Jwx | base64)" -d grant_type=refresh_token -d refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImF0aSI6IjVlZjMxYjJlLTY0YmMtNGNkZS05MTMzLWU3NzdmODc1ZDA1NiIsImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDExMDU3NDQsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiMTM0MWUyYzgtYzkzOS00NjE2LWExZGEtNzY4MTMyNjkyNjc0IiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.ZDzQn2v8y28D59lq-1vwbiwniYtlTMoomX890V3N_l0
```

Response:
```
HTTP/1.1 200
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Cache-Control: no-store
Pragma: no-cache
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 25 Sep 2020 07:40:00 GMT

{
  "access_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDEwMjA1MDAsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiZmVkMzEwYTUtMzhiOS00NmU4LWJhNmMtNGU1NWM4OTdmOGE3IiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.G8PWkhTZmHjNR0_lyJv9eGcnU1FjIfWzhJS45Bs7QmM",
  "token_type" : "bearer",
  "refresh_token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbkBwcm9ncmFtbWVycy5idXp6Iiwic2NvcGUiOlsiYW55Il0sImF0aSI6ImZlZDMxMGE1LTM4YjktNDZlOC1iYTZjLTRlNTVjODk3ZjhhNyIsImxhc3RfbmFtZSI6Ikhlcm8iLCJleHAiOjE2MDExMDYwMDAsImZpcnN0X25hbWUiOiJTdXBlciIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiYWFjNGFkZGYtZTBmMS00NThmLTgwZmQtZjdmOWVhY2I3N2NlIiwiY2xpZW50X2lkIjoidjQ0TFhoS2tiOSJ9.ecLnvUfxecfyhwIh9Kbjel7opa-MCOwnRDFmXxVeRrw",
  "expires_in" : 899,
  "scope" : "any",
  "first_name" : "Super",
  "last_name" : "Hero",
  "jti" : "fed310a5-38b9-46e8-ba6c-4e55c897f8a7"
}
```            

#### 4 - Logout
By design a JWT is self-contained. Meaning it makes little sense to invalidate an access token. For obvious reasons --> since it is self-contained.

For a client to support logout two approaches in combination should be used:
 * Provided by Authorization server - Reasonably short lifetime of JWT access tokens. 15 minutes have been chosen for this service,
 * Provided by Client - Delete a JWT from `local storage` or equivalent when issuing a logout event.
