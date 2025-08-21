import { SignedIn, SignedOut } from "@clerk/clerk-react";
import { BrowserRouter, Routes, Route } from "react-router";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import PaymentPage from "./pages/PaymentPage";
import Layout from "./components/Layout";
import AdminDashboardPage from "./pages/AdminDashboardPage";
import AuthRedirect from "./components/AuthRedirect";
import RoleGate from "./components/RoleGate";
import PendingAccessPage from "./pages/PendingAccessPage";
import ClassroomPage from "./pages/ClassroomPage";
import SessionPage from "./pages/SessionPage";
import MentorPage from "./pages/MentorPage";
import StudentPage from "./pages/StudentPage";
import AdminLayout from "./components/AdminLayout";
import MentorProfilePage from "./pages/MentorProfilePage";

function App() {

  return (
    <BrowserRouter>
      <Layout>
      <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/pending-access" element={<PendingAccessPage />} />
          <Route path="/mentor/:id" element={<MentorProfilePage />} />
          <Route 
            path="/redirect" 
            element={
              <>
                <SignedIn>
                  <AuthRedirect />
                </SignedIn>
                <SignedOut>
                  <LoginPage/>
                </SignedOut>
              </>
            } 
          />
          <Route
            path="/dashboard"
            element={
              <>
                <SignedIn>
                  <RoleGate requiredRole="student">
                    <DashboardPage />
                  </RoleGate>
                </SignedIn>
                <SignedOut>
                  <LoginPage />
                </SignedOut>
              </>
            }
          />
          <Route 
            path="/admin" 
            element={
              <>
                <SignedIn>
                  <RoleGate requiredRole="admin">
                    <AdminLayout />
                  </RoleGate>
                </SignedIn>
                <SignedOut>
                  <LoginPage />
                </SignedOut>
              </>
            }
            >
            <Route 
              index 
              element={
                <RoleGate requiredRole="admin">
                  <AdminDashboardPage />
                </RoleGate>
              } 
            />
            <Route
              path="classroom/all"
              element={
                <RoleGate requiredRole="admin">
                  <ClassroomPage />
                </RoleGate>
              }
            />
            <Route
              path="session/all"
              element={
                <RoleGate requiredRole="admin">
                  <SessionPage />
                </RoleGate>
              }
            />
            <Route
              path="mentor/all"
              element={
                <RoleGate requiredRole="admin">
                  <MentorPage />
                </RoleGate>
              }
            />
            <Route
              path="student/all"
              element={
                <RoleGate requiredRole="admin">
                  <StudentPage />
                </RoleGate>
              }
            />
          </Route>
          <Route
            path="/payment/:sessionId"
            element={
              <>
                <SignedIn>
                  <PaymentPage />
                </SignedIn>
                <SignedOut>
                  <LoginPage />
                </SignedOut>
              </>
            }
          />
          <Route path="*" element={<LoginPage />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}

export default App
