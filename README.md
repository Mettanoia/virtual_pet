
# Virtual Pet Management User Manual

This manual provides an overview of the endpoints for managing virtual pets and how the front-end interacts with the back-end. The system is built using Spring Boot with Spring Security for authentication, and the front-end communicates with the back-end via RESTful API calls.

---

### **1. Authentication and Session Management**

Upon login via the default Spring Security `/login` endpoint, the user is authenticated, and the session is maintained via the `JSESSIONID` cookie. After successful login, the user is redirected to the main interface (`index.html`) with their username included as a query parameter in the URL (`/index.html?username={username}`).

---

### **2. Endpoints**

#### **2.1 POST `/login`**

- **Description**: This endpoint logs in the user. The user provides their username and password via a form.
- **Request**:
  ```http
  POST http://localhost:8080/login
  Content-Type: application/x-www-form-urlencoded

  username={username}&password={password}
  ```
- **Response**: Upon successful login, the user is redirected to `index.html?username={username}`.

#### **2.2 GET `/api/pets/{username}`**

- **Description**: Retrieves all the pets associated with the specified user.
- **Request**:
  ```http
  GET http://localhost:8080/api/pets/{username}
  ```
- **Response**:
  ```json
  [
    {
      "id": 1,
      "name": "Fluffy",
      "petType": "FIRE",
      "energy": "HIGH",
      "mood": "HAPPY",
      "petLevel": "HEROIC_LEVEL_1",
      "lastFeedTime": 1696053123000
    }
  ]
  ```

#### **2.3 POST `/api/pets/{username}`**

- **Description**: Creates a new pet for the specified user.
- **Request**:
  ```http
  POST http://localhost:8080/api/pets/{username}
  Content-Type: application/json

  {
    "id": null,
    "name": "Fluffy",
    "petType": "FIRE",
    "energy": "HIGH",
    "mood": "HAPPY",
    "petLevel": "LEVEL_1",
    "lastFeedTime": 1696053123000
  }
  ```
- **Response**: `200 OK` if the pet is successfully created.

#### **2.4 POST `/api/pets/{username}/{id}`**

- **Description**: Feeds a pet, modifying its energy and updating the feeding time.
- **Request**:
  ```http
  POST http://localhost:8080/api/pets/{username}/{id}
  Content-Type: application/json

  {
    "food": "EGGS"
  }
  ```
- **Response**: `200 OK` with the updated pet energy level.

#### **2.5 POST `/api/pets/{username}/{id}/gift`**

- **Description**: Gives a gift to the pet, which may affect its mood or other attributes.
- **Request**:
  ```http
  POST http://localhost:8080/api/pets/{username}/{id}/gift
  Content-Type: application/json

  {
    "gift": "BONE"
  }
  ```
- **Response**: `200 OK` with the updated pet state.

#### **2.6 DELETE `/api/pets/{username}/{id}`**

- **Description**: Deletes a pet by its ID.
- **Request**:
  ```http
  DELETE http://localhost:8080/api/pets/{username}/{id}
  ```
- **Response**: `200 OK` if the pet is successfully deleted.

---

### **3. Front-End Interaction**

The front-end interacts with the above endpoints via jQuery-powered AJAX requests. After login, the front-end extracts the username from the URL and makes requests to the appropriate API endpoints to manage pets.

#### **3.1 Front-End Login Flow**

After the user logs in, Spring Security redirects them to:
```
/index.html?username={username}
```
The front-end then extracts the username and makes authenticated requests to the `/api/pets/{username}` endpoint to manage the user's pets.

---

### **4. Error Handling**

The front-end handles errors returned by the API. If the session expires or the user is not authenticated, the front-end checks for `401 Unauthorized` or `403 Forbidden` status codes and redirects the user back to the login page.

---

### **5. Security Configuration**

The Spring Security configuration ensures that all API endpoints require authentication. The authenticated user's session is tracked using the `JSESSIONID` cookie, which is automatically included in subsequent requests. Upon successful authentication, the `authenticationSuccessHandler` redirects the user to `index.html` with their username in the query string.

**Key Configuration Snippet**:

```java
.authenticationSuccessHandler((webFilterExchange, authentication) -> {
    String username = authentication.getName();
    webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
    webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create("/index.html?username=" + username));
    return webFilterExchange.getExchange().getResponse().setComplete();
})
```

---

### **6. Example Workflow**

1. **Login**:
   - The user logs in using the form at `/login`.
   - After successful login, the user is redirected to `index.html?username={username}`.

2. **View Pets**:
   - The front-end extracts the username from the URL and sends a `GET` request to `/api/pets/{username}` to retrieve the user's pets.

3. **Create a Pet**:
   - The user can create a new pet by filling out the pet creation form. The front-end sends a `POST` request to `/api/pets/{username}`.

4. **Feed a Pet**:
   - The user can feed a pet by clicking the "Feed" button. The front-end sends a `POST` request to `/api/pets/{username}/{id}`.

5. **Give a Gift**:
   - The user can give a gift to a pet by clicking the "Give Gift" button. The front-end sends a `POST` request to `/api/pets/{username}/{id}/gift`.

6. **Delete a Pet**:
   - The user can delete a pet by clicking the "Delete" button. The front-end sends a `DELETE` request to `/api/pets/{username}/{id}`.
