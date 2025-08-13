import { DataTable } from "@/components/DataTable";
import { PageHeader } from "@/components/PageHeader";
import { Button } from "@/components/ui/button";
import { SidebarInset } from "@/components/ui/sidebar";
import { BACKEND_URL } from "@/config/env";
import { FullSession } from "@/lib/types";
import { useAuth } from "@clerk/clerk-react";
import { Download, Plus } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import AlertConfirmationDialog from "@/components/AlertConfirmationDialog";
import { SessionColumns } from "@/components/SessionColumns";


export default function SessionPage() {

  const [sessions, setSessions] = useState<FullSession[]>([]);
  const [isAlertDialogOpen, setIsAlertDialogOpen] = useState(false);
  const [sessionIdToUpdate, setSessionIdToUpdate] = useState<number | null>(null);
  const [sessionStatusToUpdate, setSessionStatusToUpdate] = useState<string>("PENDING");
  const { getToken } = useAuth();


  async function fetchAllSession() {

    try {
      const token = await getToken({ template: "skillmentor-auth-frontend" });
      const response = await fetch(`${BACKEND_URL}/academic/session`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }); //fetch all sessions

      if (!response.ok) {
        throw new Error("Failed to fetch sessions");
      }

      const data = await response.json();
      console.log(data);
      setSessions(data);
    } catch (error) {
      console.error("Error fetching sessions:", error);
    }
  }

  async function updateStatus() {

    const sessionId = sessionIdToUpdate;
    const status = sessionStatusToUpdate;
    try {
      const token = await getToken({ template: "skillmentor-auth-frontend" });
      const response = await fetch(`${BACKEND_URL}/academic/session/${sessionId}?status=${status}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }); //update session status

      if (!response.ok) {
        throw new Error(`Failed to ${status.toUpperCase() === "ACCEPTED" ? "approve session" : "mark session as completed"}`);
      }

      const data = await response.json();

      setSessions(prevSessions => prevSessions.map(s => s.session_id === data.session_id ? data : s));

      toast.success(`Session ${status.toUpperCase() === "ACCEPTED" ? "approved" : "marked as completed"} successfully`);

    } catch (error) {
      console.error(`Error ${status.toUpperCase() === "ACCEPTED" ? "approving session" : "marking session as completed"}: `, error);
      toast.error(`Failed to ${status.toUpperCase() === "ACCEPTED" ? "approve session" : "mark session as completed"} classroom.`)
    }
  }

  async function confirmToUpdateSessionStatus(sessionId: number, status: string) {
    setSessionStatusToUpdate(status);
    setSessionIdToUpdate(sessionId);
    setIsAlertDialogOpen(true);
  }

  useEffect(() => {
    fetchAllSession();
  }, []);


  return (
    <div>
      <SidebarInset>
        <PageHeader title="Sessioneroom" subtitle="All Sessiones" />
        <div className="p-4 bg-white">
          <div className="container mx-auto py-6">

            {/* headings and import button, add new button */}
            <div className="mb-4 flex flex-wrap items-center justify-between space-y-2 gap-x-4">
              <div>
                <h2 className="text-2xl font-bold tracking-tight">sessions</h2>
                <p className="text-muted-foreground">Here's a list of all sessions in the platform</p>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm" className="ml-auto hidden h-8 lg:flex">
                  <Download />
                  Import
                </Button>
                <Button size="sm" className="h-8">
                  <Plus />
                  Add New
                </Button>
              </div>
            </div>

            <DataTable columns={
              SessionColumns({
                approveSession: (sessionId: number) => confirmToUpdateSessionStatus(sessionId, "ACCEPTED"),
                markAsCompleted: (sessionId: number) => confirmToUpdateSessionStatus(sessionId, "COMPLETED"),
              })}
              data={sessions}
              filterKey="student_name" />

            <AlertConfirmationDialog
              isOpen={isAlertDialogOpen}
              onOpenChange={setIsAlertDialogOpen}
              onConfirm={updateStatus}
              title="Are you absolutely sure?"
              description={`This action cannot be undone. This will change the status of the session as ${sessionStatusToUpdate.toUpperCase()}`}
            />

          </div>
        </div>
      </SidebarInset>
    </div>
  )
}
