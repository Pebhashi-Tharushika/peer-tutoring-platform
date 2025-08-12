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
  const [issessionDialogOpen, setIssessionDialogOpen] = useState(false);
  const [dialogInitialData, setDialogInitialData] = useState<FullSession | undefined>(undefined);
  const [isAlertDialogOpen, setIsAlertDialogOpen] = useState(false);
  // const [sessionIdToDelete, setsessionIdToDelete] = useState<number | null>(null);
  const { getToken } = useAuth();


  async function fetchAllSessiones() {
    
    try {
      const token = await getToken({ template: "skillmentor-auth-frontend" });
      const response = await fetch(`${BACKEND_URL}/academic/session`,{
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }); //fetch all sessions

      if (!response.ok) {
        throw new Error("Failed to fetch sessiones");
      }

      const data = await response.json();
console.log(data);
      setSessions(data);
    } catch (error) {
      console.error("Error fetching sessiones:", error);
    }
  }

  useEffect(() => {
    fetchAllSessiones();
  }, []);


  return (
    <div>
      <SidebarInset>
        <PageHeader title="Sessioneroom" subtitle="All Sessiones" />
        <div className="p-4">
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
                <Button size="sm" onClick={() => setIssessionDialogOpen(true)} className="h-8">
                  <Plus />
                  Add New
                </Button>

                {/* Dialog for Creating/updating */}
                {/* <sessionDialog
                  isOpen={issessionDialogOpen}
                  onOpenChange={setIssessionDialogOpen}
                  mode={dialogMode}
                  initialData={dialogInitialData}
                  onSaveSuccess={handleSaveSuccess}
                /> */}
              </div>
            </div>

            <DataTable columns={
              SessionColumns({
                approveSession: (sessionId: number) => setIssessionDialogOpen(true),
                markAsCompleted: (sessionId: number) => setIssessionDialogOpen(true),
              })}
              data={sessions}
              filterKey="session_status" />

            {/* <AlertConfirmationDialog
              isOpen={isAlertDialogOpen}
              onOpenChange={setIsAlertDialogOpen}
              onConfirm={deletesession}
              title="Are you absolutely sure?"
              description="This action cannot be undone. This will permanently delete the session."
            /> */}

          </div>
        </div>
      </SidebarInset>
    </div>
  )
}
