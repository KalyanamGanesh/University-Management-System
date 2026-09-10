# University Management System

CampusFlow is a Vite frontend and Spring Boot backend for managing student, faculty, and worker records.

## Run locally

Start MySQL and create the `university_management_system` database using the credentials in `backend/src/main/resources/application.yaml`.

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

In a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## Authentication

The app includes registration and sign-in with JWT-protected APIs. Registration requires a name, email, and an 8-character password. The JWT is stored in browser local storage and sent with each CRUD request.

Set a unique Base64-encoded `JWT_SECRET` before deploying. The value in `application.yaml` is only a local-development fallback.

## Enable Google sign-in

1. Create a Google OAuth client of type **Web application** in Google Cloud Console.
2. Add this authorized redirect URI: `http://localhost:8081/login/oauth2/code/google`.
3. Set these environment variables before starting the backend:

```powershell
$env:SPRING_PROFILES_ACTIVE = 'google'
$env:GOOGLE_CLIENT_ID = 'your-google-client-id'
$env:GOOGLE_CLIENT_SECRET = 'your-google-client-secret'
$env:GOOGLE_OAUTH_SUCCESS_REDIRECT_URI = 'http://localhost:5173/'
```

4. Start the backend as usual. The Google button on the login screen becomes active automatically.

For deployment, replace the localhost URLs with your real frontend and backend URLs in both Google Cloud and the environment variables.
