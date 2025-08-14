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
          <Route path="/redirect" element={<AuthRedirect />} />
          <Route path="/pending-access" element={<PendingAccessPage />} />
          <Route path="/mentor/:id" element={<MentorProfilePage />} />
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
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={
              <RoleGate requiredRole="admin">
                <AdminDashboardPage />
              </RoleGate>
            } />
            <Route path="/admin/classroom/all" element={<ClassroomPage />} />
            <Route path="/admin/session/all" element={<SessionPage />} />
            <Route path="/admin/mentor/all" element={<MentorPage />} />
            <Route path="/admin/student/all" element={<StudentPage />} />
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
