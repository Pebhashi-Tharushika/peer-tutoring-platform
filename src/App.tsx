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

function App() {

  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/redirect" element={<AuthRedirect />} />
          <Route path="/pending-access" element={<PendingAccessPage />} />
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
                    <AdminDashboardPage />
                  </RoleGate>
                </SignedIn>
                <SignedOut>
                  <LoginPage />
                </SignedOut>
              </>
            }
          />
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
