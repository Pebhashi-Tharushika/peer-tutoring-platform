## Peer Tutoring Platform

### Project Overview
The Peer Tutoring Platform is a full-stack web application that connects learners with peer mentors in a one-on-one tutoring marketplace. This application is built for learners seeking support to prepare for and achieve AWS, Microsoft, Cisco, Google, and other professional certifications. The platform makes it easy for learners to find qualified tutors, schedule flexible 1-on-1 sessions, and track their learning journey, while enabling mentors to share expertise and manage tutoring sessions efficiently.

**Key Features**
* **Class Discovery**: Effortlessly browse and enroll in available classes with detailed information.
* **Mentor Discovery**: A detailed mentor profiles with experience, certifications, and student ratings to make informed decisions.
* **Session Management**: A robust system for scheduling, booking and managing all tutoring sessions.
* **Personalized Dashboards**: Dedicated profiles for both students and admins with tailored functionality to simplify their workflows.
* **Intuitive User Interface**: A seamless, responsive design that guides users through every steps of the platform.
* **Role-Based Access Control**: Securely manages user permissions to ensure each user can only access the content and features relevant to them.


### Technology Stack  

**Frontend** 
- ⚛️ **React + Vite** (TypeScript)  
- 🎨 **TailwindCSS** + **shadcn/ui**  

**Backend**
- ☕ **Spring Boot (Java, Maven)**  
- 🗄️ **PostgreSQL**  

**Authentication & Authorization** 
- 🔑 **Clerk** for user authentication  
- 🛡️ **Spring Security + JWT** for backend authorization and role-based access  

**Deployment & Hosting**  
- ☁️ Backend: **Render**  
- 🌐 Frontend: **Vercel**  
- 🗄️ Database Hosting: **Supabase**  
- 📦 File Uploads: **AWS S3 Bucket**  



### Installation

#### Prerequisites
- Node.js & npm
- Java 11+  
- Maven  
- PostgreSQL

#### Setup  

- Backend (Spring Boot)  
```bash
# Navigate to backend
cd backend  

# Install dependencies & build
mvn clean install  

# Run the application
mvn spring-boot:run
```
- Frontend (React + Vite + TypeScript) 
```bash
# Navigate to frontend
cd frontend  

# Install dependencies
npm install  

# Run the app
npm run dev

```

### Live Demo
Check out the platform here: [https://peer-tutoring-platform.vercel.app](https://peer-tutoring-platform.vercel.app/)


### Contributing
Contributions, issues and feature requests are welcome! Feel free to fork the repo, submit a PR or open an issue.

### License
This project is licensed under the [MIT License](LICENSE.txt).

