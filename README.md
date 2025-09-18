# BlogApp - Spring Boot Blog Application

A modern, full-featured blog application built with Spring Boot, MySQL, and Bootstrap 5.

## Features

### 🚀 Core Features
- **User Authentication**: Secure login/register with Spring Security and BCrypt password encoding
- **Blog Management**: Full CRUD operations for blog posts
- **Real-time Interactions**: AJAX-powered like button without page reload
- **Comment System**: Dynamic comment loading and posting
- **Social Sharing**: Share posts on Facebook, Twitter, and LinkedIn
- **Responsive Design**: Bootstrap 5 with Blogger-style card layouts

### 👥 User Roles
- **USER**: Can create, edit, delete their own blogs, like posts, and comment
- **ADMIN**: Extended permissions (can be configured for additional features)

### 📱 Pages
- **Home**: List of all blog posts with preview cards
- **About**: Static information page
- **Login/Register**: Authentication pages with Thymeleaf
- **Blog CRUD**: Create, read, update, delete blog posts
- **Single Blog View**: Full content with likes, comments, and social sharing

## Technology Stack

### Backend
- **Spring Boot 3.2.0** (Java 17+)
- **Spring Security** for authentication
- **Spring Data JPA** with Hibernate ORM
- **MySQL** database
- **Maven** for dependency management

### Frontend
- **Thymeleaf** templating engine
- **Bootstrap 5** for responsive UI
- **Font Awesome** icons
- **jQuery** for AJAX functionality

## Database Schema

### Entities
1. **User**
   - id, name, email, password, role (USER/ADMIN)
   - One-to-many relationships with BlogPost and Comment

2. **BlogPost**
   - id, title, content, createdAt, updatedAt, likesCount, imageUrl
   - Many-to-one with User (author)
   - One-to-many with Comment

3. **Comment**
   - id, content, createdAt
   - Many-to-one with User (author) and BlogPost

## Setup Instructions

### Prerequisites
- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+

### Database Setup
1. Install MySQL and create a database:
```sql
CREATE DATABASE blogapp_db;
```

2. Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blogapp_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
1. Clone or download the project
2. Navigate to the project directory
3. Run the application:
```bash
mvn spring-boot:run
```
4. Open your browser and go to `http://localhost:8080`

### Default Port
The application runs on port 8080 by default. You can change this in `application.properties`:
```properties
server.port=8080
```

## Application Structure

```
src/
├── main/
│   ├── java/com/blogapp/
│   │   ├── BlogApplication.java          # Main application class
│   │   ├── config/
│   │   │   └── SecurityConfig.java       # Spring Security configuration
│   │   ├── controller/
│   │   │   ├── AuthController.java       # Login/Register endpoints
│   │   │   ├── BlogController.java       # Blog CRUD, likes, comments
│   │   │   └── HomeController.java       # Home and About pages
│   │   ├── entity/
│   │   │   ├── User.java                 # User entity
│   │   │   ├── BlogPost.java             # Blog post entity
│   │   │   └── Comment.java              # Comment entity
│   │   ├── repository/
│   │   │   ├── UserRepository.java       # User data access
│   │   │   ├── BlogPostRepository.java   # Blog post data access
│   │   │   └── CommentRepository.java    # Comment data access
│   │   └── service/
│   │       ├── UserService.java          # User business logic
│   │       ├── BlogService.java          # Blog business logic
│   │       └── CommentService.java       # Comment business logic
│   └── resources/
│       ├── templates/                    # Thymeleaf templates
│       │   ├── auth/
│       │   │   ├── login.html
│       │   │   └── register.html
│       │   ├── blog/
│       │   │   ├── create.html
│       │   │   ├── edit.html
│       │   │   └── view.html
│       │   ├── home.html
│       │   └── about.html
│       └── application.properties        # Configuration
```

## Key Features Implementation

### Authentication & Security
- BCrypt password encoding
- Session-based authentication
- Role-based access control
- CSRF protection enabled

### AJAX Like System
- Real-time like updates without page refresh
- RESTful endpoint: `POST /blog/like/{id}`
- Returns JSON response with updated like count

### Comment System
- Nested comments display
- Real-time comment posting
- Author and timestamp tracking

### Social Sharing
- Facebook, Twitter, LinkedIn integration
- Dynamic URL and title sharing
- Opens in popup windows

### Responsive Design
- Mobile-first Bootstrap 5 design
- Blogger-style card layouts
- Gradient backgrounds and modern styling
- Hover effects and smooth transitions

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `POST /logout` - Logout

### Blog Management
- `GET /` - Home page with all blogs
- `GET /blog/create` - Create blog form
- `POST /blog/create` - Save new blog
- `GET /blog/view/{id}` - View single blog
- `GET /blog/edit/{id}` - Edit blog form
- `POST /blog/edit/{id}` - Update blog
- `POST /blog/delete/{id}` - Delete blog
- `POST /blog/like/{id}` - Like blog (AJAX)
- `POST /blog/comment/{id}` - Add comment

### Static Pages
- `GET /about` - About page

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For support or questions, please create an issue in the repository.
