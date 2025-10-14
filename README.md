# 💬 ChatApp - Real-time Chat Application

A modern real-time chat application built with **Spring Boot 3.x**, **WebSocket**, **JWT Authentication**, and **JPA**. Successfully deployed on both Windows and Ubuntu systems with Tomcat 10.

## 🚀 Features

- **Real-time messaging** with WebSocket support
- **JWT-based authentication** and authorization
- **User registration and login** system
- **Message persistence** with JPA/Hibernate
- **Notification system** for real-time updates
- **RESTful API** for all operations
- **Cross-platform deployment** (Windows & Ubuntu)

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.5
- **Security**: Spring Security with JWT
- **Database**: H2 (development) / Oracle (production)
- **WebSocket**: Spring WebSocket + STOMP
- **Build Tool**: Maven
- **Java Version**: 17+
- **Server**: Tomcat 10+

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Tomcat 10+ (for WAR deployment)
- Git

## 🏃‍♂️ Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/rizwan-codes/ChatApp.git
cd ChatApp
```

### 2. Build the Application
```bash
# Build JAR for standalone execution
./mvnw clean package

# Build WAR for Tomcat deployment
./mvnw clean package -DskipTests
```

### 3. Run the Application

**Option A: Standalone (Embedded Tomcat)**
```bash
java -jar target/chatapp-1.0.0.jar
```

**Option B: Deploy to External Tomcat**
```bash
# Copy WAR to Tomcat webapps
cp target/chatapp-1.0.0.war /path/to/tomcat/webapps/chatapp.war
```

### 4. Access the Application
- **Application**: http://localhost:8080/chatapp/
- **Health Check**: http://localhost:8080/chatapp/health
- **API Documentation**: See API section below

## 🔧 Configuration

### Database Configuration
Edit `src/main/resources/application.properties`:

```properties
# H2 Database (Development)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# Oracle Database (Production)
# spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/freeXDB
# spring.datasource.username=system
# spring.datasource.password=your_password
```

### JWT Configuration
```properties
jwt.secret=your-secret-key
jwt.expirationMs=86400000
```

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/auth/verify` - Verify JWT token

### Example API Usage

**Register User:**
```bash
curl -X POST http://localhost:8080/chatapp/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass","displayName":"Test User"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/chatapp/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass"}'
```

## 🚀 Deployment

### Windows Deployment
1. Install Java 17+ and Tomcat 10+
2. Build WAR file: `./mvnw clean package`
3. Copy `target/chatapp-1.0.0.war` to `webapps/chatapp.war`
4. Start Tomcat

### Ubuntu Deployment
See detailed [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for complete Ubuntu setup with Tomcat 10 installation.

## 🧪 Testing

```bash
# Run tests
./mvnw test

# Health check
curl http://localhost:8080/chatapp/health

# Test endpoints
curl http://localhost:8080/chatapp/test
```

## 📁 Project Structure

```
ChatApp/
├── src/main/
│   ├── java/com/aksa/chatapp/
│   │   ├── controller/          # REST Controllers
│   │   ├── model/              # JPA Entities
│   │   ├── repository/         # Data Repositories
│   │   ├── service/            # Business Logic
│   │   ├── security/           # Security Configuration
│   │   ├── handler/            # WebSocket Handlers
│   │   └── ChatAppApplication.java
│   ├── resources/
│   │   ├── static/             # Static web content
│   │   └── application.properties
│   └── webapp/WEB-INF/         # WAR deployment config
├── target/                     # Build output
├── DEPLOYMENT_GUIDE.md         # Detailed deployment guide
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## 🔍 Troubleshooting

### Common Issues

**404 Errors:**
- Ensure Tomcat 10+ is being used (Spring Boot 3.x compatibility)
- Check if Spring Boot application started in logs
- Verify WAR file is properly deployed

**Port Conflicts:**
```bash
# Check what's using port 8080
netstat -ano | findstr :8080  # Windows
sudo ss -tlnp | grep :8080    # Linux
```

**Database Issues:**
- Check H2 console: http://localhost:8080/chatapp/h2-console
- Verify database configuration in application.properties

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Rizwan** - [GitHub Profile](https://github.com/rizwan-codes)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Tomcat for the robust servlet container
- All contributors and testers

---

⭐ **Star this repository if you found it helpful!**