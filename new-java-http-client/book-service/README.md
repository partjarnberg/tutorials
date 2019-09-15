# Book service

## API documentation

### Rate limit
The API have rate limitation allowing only 60 requests per hour. 

*X-RateLimit-Limit* - The maximum number of requests you're permitted to make per hour.
*X-RateLimit-Remaining* - The number of requests remaining in the current rate limit window.
*X-RateLimit-Reset* - The time at which the current rate limit window resets in UTC epoch seconds.

### GET /api/books?query={query}
Find all books with a title containing the queried string.

E.g.
```
curl -i --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/books?query=Learn%20Functional%20Programming%20with%20Elixir
```      
```
HTTP/1.1 200 OK
X-RateLimit-Limit: 60
X-RateLimit-Remaining: 59
X-RateLimit-Reset: 1568548997343
Connection: keep-alive
Content-Type: application/json;charset=utf-8
Content-Length: 1908
Date: Sun, 15 Sep 2019 11:03:21 GMT

{"books":[{"id":"1a7d75fc-542f-4be8-a451-4625d61e92f2","title":"Learn Functional Programming with Elixir","isbn":"9781680502459","description":"Elixir's straightforward syntax and this guided tour give you a clean, simple path to learn modern functional programming techniques. No previous functional programming experience required! This book walks you through the right concepts at the right pace, as you explore immutable values and explicit data transformation, functions, modules, recursive functions, pattern matching, high-order functions, polymorphism, and failure handling, all while avoiding side effects. Don't board the Elixir train with an imperative mindset! To get the most out of functional languages, you need to think functionally. This book will get you there. Functional programming offers useful techniques for building maintainable and scalable software that solves today's difficult problems. The demand for software written in this way is increasing - you don't want to miss out. In this book, you'll not only learn Elixir and its features, you'll also learn the mindset required to program functionally. Elixir's clean syntax is excellent for exploring the critical skills of using functions and concurrency.Start with the basic techniques of the functional way: working with immutable data, transforming data in discrete steps, and avoiding side effects. Next, take a deep look at values, expressions, functions, and modules. Then extend your programming with pattern matching and flow control with case, if, cond, and functions. Use recursive functions to create iterations. Work with data types such as lists, tuples, and maps. Improve code reusability and readability with Elixir's most common high-order functions. Explore how to use lazy computation with streams, design your data, and take advantage of","imageUrl":"/static/img/learn-functional-programming-with-elixir.jpg"}]}      
```      

### GET /api/books/{id}
