# Microblog Frontend

A modern React-based microblogging platform built with TypeScript, Firebase Authentication, and Spring Boot backend integration.

## Features

- 🔐 Firebase Authentication (Email/Password, Password Reset)
- 📝 Create, read, and delete posts (280 character limit)
- ❤️ Like/unlike posts
- 👥 Follow/unfollow users
- 🔍 Search users
- 📱 Responsive design
- ⚡ Real-time updates via Server-Sent Events (SSE)
- 🎨 Modern UI with Tailwind CSS and shadcn/ui

## Setup Instructions

### 1. Firebase Configuration

Replace the placeholder values in `src/lib/firebase.ts` with your Firebase project credentials:

```typescript
const firebaseConfig = {
  apiKey: "YOUR_API_KEY",
  authDomain: "YOUR_AUTH_DOMAIN",
  projectId: "YOUR_PROJECT_ID",
  storageBucket: "YOUR_STORAGE_BUCKET",
  messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
  appId: "YOUR_APP_ID"
};
```

**To get these values:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing one
3. Go to Project Settings > General
4. Scroll to "Your apps" section
5. Click "Add app" and select Web (</>) icon
6. Copy the configuration values

### 2. Backend Setup

Make sure your Spring Boot backend is running on `http://localhost:8081`

The backend should have the following endpoints configured:
- Authentication: `/auth/*`
- Users: `/users/*`
- Posts: `/posts/*`
- Feed: `/feed/*`
- Follows: `/follows/*`, `/followers/*`, `/followees/*`
- Likes: `/likes/*`

### 3. Install Dependencies

```bash
npm install
```

### 4. Run Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:8080`

## Project Structure

```
src/
├── components/         # Reusable UI components
│   ├── ui/            # shadcn/ui components
│   ├── Layout.tsx     # Main layout with navigation
│   ├── PostCard.tsx   # Post display component
│   └── ProtectedRoute.tsx
├── contexts/          # React contexts
│   └── AuthContext.tsx
├── lib/              # Utility functions
│   ├── api.ts        # API client functions
│   ├── firebase.ts   # Firebase configuration
│   └── utils.ts
├── pages/            # Page components
│   ├── Register.tsx
│   ├── Login.tsx
│   ├── ForgotPassword.tsx
│   ├── Feed.tsx
│   ├── NewPost.tsx
│   ├── Profile.tsx
│   ├── Search.tsx
│   └── Notifications.tsx
└── index.css         # Global styles and design tokens
```

## Key Technologies

- **React 18** - UI framework
- **TypeScript** - Type safety
- **Vite** - Build tool
- **Firebase Auth** - Authentication
- **Tailwind CSS** - Styling
- **shadcn/ui** - UI components
- **React Router** - Routing
- **Sonner** - Toast notifications

## API Integration

All API calls are made through the `src/lib/api.ts` module, which handles:
- Authentication headers (Firebase JWT tokens)
- Error handling
- Request/response formatting

## Design System

The app uses a semantic token-based design system defined in `src/index.css`:
- Primary color: Cyan/Blue (#0ea5e9)
- Gradients for accents
- Smooth transitions
- Responsive breakpoints

All components use these tokens for consistent theming across light and dark modes.

## Development Notes

- Posts are limited to 280 characters
- Real-time updates via SSE (to be fully implemented)
- Protected routes redirect to login if not authenticated
- Firebase handles all user authentication
- Backend validates all requests with Firebase JWT tokens
