# Slow REST API handling Courses and Students
This is an intentionally slow running REST API :)

The service provides functionality for students to register at the University. Also they can register/unregister for courses they plan to attend.
## Endpoints
All operations enabled by the API and how to interact with the service, in detail, is listed in this section. 
### GET /api/students
Get all students being registered at the University.
```
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/students
``` 
``` 
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 15
Date: Thu, 12 Sep 2019 19:57:17 GMT

{"students":[]}
```
### POST /api/students
Register a student at the University.
```    
curl -i --header "Content-Type: application/json" \
  --request POST \
  --data '{"name":"Johnny Puma"}' \
  http://localhost:8080/api/students
```
```
HTTP/1.1 201 Created
Connection: keep-alive
Location: /api/students/60fbbd6f-c174-4c48-857f-ef42bd601619
Content-Type: application/json;charset=utf-8
Content-Length: 66
Date: Thu, 12 Sep 2019 19:58:42 GMT

{"name":"Johnny Puma","id":"60fbbd6f-c174-4c48-857f-ef42bd601619"}
```
### GET /api/students/{studentId}
Get a specific student details.
```       
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/students/60fbbd6f-c174-4c48-857f-ef42bd601619
```
``` 
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 66
Date: Thu, 12 Sep 2019 20:00:40 GMT

{"name":"Johnny Puma","id":"60fbbd6f-c174-4c48-857f-ef42bd601619"}
```       
### GET /api/students/{studentId}/courses
```  
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/students/bda23d31-6441-45e2-bce6-0081e879043a/courses
```                  
```                  
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 14
Date: Thu, 12 Sep 2019 20:18:47 GMT

{"courses":[]}
```                  
### GET /api/courses
Get all the courses being held at the University.
```  
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/courses
```
```   
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 13763
Date: Thu, 12 Sep 2019 20:01:49 GMT

{"courses":[{"id":"7071336b-28b9-481c-9d6d-fb5939afe9a4","name":"Web Programming","description":"This course builds on the concepts and issues discussed in Web Programming 1 surrounding software development for programs that operate on the web and the Internet. Existing and emerging web development topics to be covered include web applications, web services, enterprise web development, markup languages, and server-side programming."},{"id":"bc463311-b7aa-4ab2-8f7e-91a43c9bd34e","name":"Communications and Networking"},{"id":"4fbe90af-f578-42e4-bcac-adc58bdf90ba","name":"Robotics"},{"id":"252a7704-76ec-4bcb-a744-dcf94c903261","name":"Artificial Intelligence","description":"This course will cover current concepts and techniques in artificial intelligence, including “reasoning”, problem solving, and search optimization."},{"id":"64b1a01c-cacb-40d4-87d3-94caa4897e25","name":"Mobile Applications","description":"The course explores concepts and issues surrounding information system applications to real-time operating systems and wireless networking systems."},{"id":"536e7fd4-5b88
...
...
}
```
### GET /api/courses/{courseId}
Get the details of a specific course.
``` 
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/courses/7071336b-28b9-481c-9d6d-fb5939afe9a4
```
``` 
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 425
Date: Thu, 12 Sep 2019 20:03:48 GMT

{"id":"7071336b-28b9-481c-9d6d-fb5939afe9a4","name":"Web Programming","description":"This course builds on the concepts and issues discussed in Web Programming 1 surrounding software development for programs that operate on the web and the Internet. Existing and emerging web development topics to be covered include web applications, web services, enterprise web development, markup languages, and server-side programming."}
```
### GET /api/courses/{courseId}/participants
Get all the participating student at a course.
```
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/courses/7071336b-28b9-481c-9d6d-fb5939afe9a4/participants
```
```  
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 63
Date: Thu, 12 Sep 2019 20:04:43 GMT

{"id":"7071336b-28b9-481c-9d6d-fb5939afe9a4","participants":[]}
```
### POST /api/courses/{courseId}/participants
Register a student at the University for a course.
```
curl -i --header "Content-Type: application/json" \
  --request POST \
  --data '{"id":"bda23d31-6441-45e2-bce6-0081e879043a"}' \
  http://localhost:8080/api/courses/fc00e98d-2bbc-4a57-ac29-b33326365a4a/participants
```
```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 129
Date: Thu, 12 Sep 2019 20:06:21 GMT

{"id":"7071336b-28b9-481c-9d6d-fb5939afe9a4","participants":[{"name":"Johnny Puma","id":"60fbbd6f-c174-4c48-857f-ef42bd601619"}]}
```
### DELETE /api/courses/{courseId}/participants
Unregister a student from a course.
```
curl -i --header "Content-Type: application/json" \
  --request DELETE \
  --data '{"id":"60fbbd6f-c174-4c48-857f-ef42bd601619"}' \
  http://localhost:8080/api/courses/7071336b-28b9-481c-9d6d-fb5939afe9a4/participants
```
```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 63
Date: Thu, 12 Sep 2019 20:07:39 GMT

{"id":"7071336b-28b9-481c-9d6d-fb5939afe9a4","participants":[]}
```