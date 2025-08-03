import { useUser } from '@clerk/clerk-react';
import { useEffect } from 'react'
import { useNavigate } from 'react-router';

export default function AuthRedirect() {
    const { user, isLoaded } = useUser();
    const router = useNavigate();

    useEffect(() => {
        if (!isLoaded) return;

        const role = user?.publicMetadata?.role;

        if (user) { // Only redirect if a user is signed in
            if (role !== "admin") {
                router("/dashboard");
            } else {
                router("/admin");
            }
        }
    }, [isLoaded, user]);

    return null;;
}
