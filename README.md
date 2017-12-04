# Maintenance
It's a simple RestAPI project based on Spring Boot. It enables us to manage movies and actors repositories. It's an implementation of task one for a "backend team". 


Movies
-------
### GET
>**`/movies`** - get list of movies
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "title" : "",
>- "uri" : ""

### GET
>**`/movies/{id}`** - get movie selected by id
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "title" : ""

### POST
>**`/movies`** - create movie
>
> Input:
>- "tittle" : "",
>- "category" : "" - (it can be "new", "best", "others" )
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "tittle" : ""

### PUT
>**`/movies/{id}`** - update movie
>
> Input:
>- "tittle"
>
> Response:
>- "id" : "",
>- "actors" : [], 
>- "tittle" : ""

### DELETE
>**`/movies/{id}`** - remove movie selected by id


Actors
-------
### GET
>**`/actors`** - get list of actors
>
> Response:
>- "id" : "",
>- "name"

### GET
>**`/actors/{id}`** - get actor selected by id
>
> Response:
>- "id" : "",
>- "name"

### POST
>**`/actors`** - create actor
>
> Input:
>- "name"
>
> Response:
>- "id" : "",
>- "name" : ""

### PUT
>**`/actors/{id}`** - update movie, allow to bind actor with movies
>
> Input:
>- "name",
>- "movieIds" : [] - optional
>
> Response:
>- "id" : "",
>- "name" : ""

### DELETE
>**`/actors/{id}`** - remove actor selected by id



#TASK TWO FEATURES

Users
-------
### POST
>**`/users`** - create user
> Input:
>- "name"

> Response:
>- "id" : "",
>- "name" : "",
>- "borrowedMovies" : []

Movies
-------
### POST
>**`/movies/borrow`** - borrow movies (combinning all available discount) 
> Input:
>- "userId",
>- "movies" : [
    {"id" : "",
    "title: ""}
  ]

> Response:
>- "userId",
>- "cost", 
>- "movies" : [
    {"id" : "",
    "title: "",
    "uri" : "",
    "actors" : []}
  ]
  
### POST
>**`/movies/return`** - return movies
> Input:
>- "userId",
>- "movies" : [
    "id" : "",
    "title": ""
  ]
  
> Response: 
>- [{"title" : "", "actors" : [], "id":"", "category":"", "available":""}]

### GET
>**`/movies/available`** - return available movies

> Response: 
>- [{"title" : "", "actors" : [], "id":"", "category":"", "available":""}]

### GET
>**`/movies/category/{category}`** - return movies with proper category

> Response: 
>- [{"title" : "", "actors" : [], "id":"", "category":"", "available":""}]

### GET
>**`/movies/user/{userId}`** - return movies borrowed by user

> Response: 
>- [{"title" : "", "actors" : [], "id":"", "category":"", "available":""}]


[Project original](https://github.com/macieg-b/Patronage_RestAPI_SpringBoot)
