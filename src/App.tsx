import { SignedIn, SignedOut } from "@clerk/clerk-react";
import { BrowserRouter, Routes, Route } from "react-router";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import PaymentPage from "./pages/PaymentPage";
import Layout from "./components/Layout";

function App() {

  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/dashboard"
            element={
              <>
                <SignedIn>
                  <DashboardPage />
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
