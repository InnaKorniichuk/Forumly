# Forumly

A Reddit-style forum built with Java, Spring Boot and MySQL. Users can create communities, publish posts, comment, and upvote or downvote content.

## Features

- Communities, posts and comments
- Upvote / downvote with live score
- Roles: USER and ADMIN
- Only authors or admins can edit or delete content
- CSRF protection and POST-only state changes
- Custom error pages with proper HTTP status codes

## Tech stack

Java 19, Spring Boot, Spring MVC, Spring Security, Spring Data JPA (Hibernate), Thymeleaf, MySQL, Docker

## Run with Docker

Requires Docker and Docker Compose. No database setup needed.

```bash
git clone <your-repo-url>
cd forumly
docker compose up --build
```

Open http://localhost:8080

Demo admin: `admin@gmail.com` / `Admin12345`, or register a new user.

Stop: `docker compose down`
Stop and delete data: `docker compose down -v`

## Configuration

Defaults live in `docker-compose.yml`. To use your own values, create a `.env` file next to it:

```env
DB_ROOT_PASSWORD=your_root_password
ADMIN_EMAIL=you_email@gmail.com
ADMIN_PASSWORD=YourPassword123
```

## Run without Docker

1. Install Java 19 and MySQL.
2. Set environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`.
3. Run `mvn spring-boot:run`.

## Project structure

```
controller/     web layer (Spring MVC)
service/        business logic
repository/     Spring Data JPA repositories
model/          JPA entities
dto/            request/response DTOs and mappers
exception/      custom exceptions and global handler
configuration/  security config and admin initializer
```
## Endpoints

The app is server-rendered (Thymeleaf), so most `GET` endpoints return a page and `POST` endpoints perform an action and redirect.

### Auth

| Method | Path | Description | Access |
|---|---|---|---|
| GET | `/login` | Login page | Public |
| POST | `/login` | Log in (`email`, `password`) | Public |
| POST | `/logout` | Log out | Authenticated |

### Users

| Method | Path | Description | Access |
|---|---|---|---|
| GET | `/users/create` | Registration page | Public |
| POST | `/users/create` | Register a new user | Public |
| GET | `/users/{id}/read` | User profile and posts | Owner or admin |
| GET | `/users/{id}/update` | Edit profile page | Owner |
| POST | `/users/{id}/update` | Save profile changes | Owner |
| GET | `/users/all` | List all users | Admin |
| POST | `/users/{id}/delete` | Delete a user with all their content | Admin |

### Communities

| Method | Path | Description | Access |
|---|---|---|---|
| GET | `/communities/getAll` | List of communities (home page) | Public |
| GET | `/communities/{id}/read` | Community page with its posts | Public |
| GET | `/communities/create` | Create community page | Authenticated |
| POST | `/communities/create` | Create a community | Authenticated |
| GET | `/communities/{userId}/getByUserId` | Communities owned by a user | Authenticated |
| POST | `/communities/{id}/delete` | Delete a community with its posts | Owner or admin |

### Posts

| Method | Path | Description | Access |
|---|---|---|---|
| GET | `/posts/create/{communityId}` | Create post page | Authenticated |
| POST | `/posts/create/{communityId}` | Create a post in a community | Authenticated |
| GET | `/posts/{id}/read` | Post page with comments | Public |
| GET | `/posts/{id}/update` | Edit post page | Author |
| POST | `/posts/{id}/update` | Save post changes | Author |
| POST | `/posts/{id}/delete` | Delete a post | Author or admin |

### Comments

| Method | Path | Description | Access |
|---|---|---|---|
| POST | `/posts/{postId}/comments/create` | Add a comment to a post | Authenticated |
| POST | `/posts/{postId}/comments/{id}/update` | Edit a comment | Author |
| POST | `/posts/{postId}/comments/{id}/delete` | Delete a comment | Author or admin |

### Votes

| Method | Path | Description | Access |
|---|---|---|---|
| POST | `/votes/posts/{id}/add` | Vote on a post (`voteType=UP` or `DOWN`) | Authenticated |
| POST | `/votes/comments/{id}/add` | Vote on a comment (`voteType=UP` or `DOWN`) | Authenticated |
