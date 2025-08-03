import { useUser } from "@clerk/clerk-react";
import { useEffect } from "react";
import { useNavigate } from "react-router";

export default function RoleGate({ children, requiredRole }: { children: React.ReactNode; requiredRole: string }) {
  const { user, isLoaded } = useUser();
  const router = useNavigate();

  useEffect(() => {
    if (!isLoaded) return;

    const userRole = user?.publicMetadata?.role;

    if (userRole !== requiredRole) {
        if (userRole === "admin") {
          router("/admin");
        } else if (userRole === "student") {
          router("/dashboard");
        } else {
          router("/login"); // Redirect to the login page if no role is found 
        }
      }
    }, [isLoaded, user, router, requiredRole]);
  

  // Only render children if the user has the correct role
  const userRole = user?.publicMetadata?.role;
  if (userRole === requiredRole) {
    return <>{children}</>;
  } else {
    return null; // Don't render anything while the redirect is happening
  }
}