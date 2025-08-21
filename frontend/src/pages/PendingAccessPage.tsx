import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { useClerk } from "@clerk/clerk-react";
import { Clock } from "lucide-react";

// A simple page to inform users their account is under review
export default function PendingAccessPage() {

    const { signOut } = useClerk();

    const handleSignOut = () => {
        signOut({ redirectUrl: "/" });
    };


    return (
        <div className="flex items-center justify-center min-h-[calc(100vh-100px)] px-4 py-12">
            <Card className="w-full max-w-md text-center">
                <CardHeader className="flex flex-col items-center">
                    <Clock className="h-12 w-12 mb-4" />
                    <CardTitle className="text-2xl font-bold">Access Pending</CardTitle>
                    <CardDescription className="mt-2">
                        Your account is currently under review by an administrator.
                        You will be granted access shortly.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <p className="text-sm text-muted-foreground mt-4">
                        Thank you for your patience. If you have any questions, please contact support.
                    </p>
                    <Button onClick={handleSignOut} className="mt-6" variant="outline">
                        Sign Out
                    </Button>
                </CardContent>
            </Card>
        </div>
    );
}
