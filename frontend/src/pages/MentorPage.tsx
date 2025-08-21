import { DataTable } from "@/components/DataTable";
import { PageHeader } from "@/components/PageHeader";
import { Button } from "@/components/ui/button";
import { SidebarInset } from "@/components/ui/sidebar";
import { BACKEND_URL } from "@/config/env";
import { Mentor } from "@/lib/types";
import { useAuth } from "@clerk/clerk-react";
import { Download, Plus } from "lucide-react";
import { useEffect, useState } from "react";
import AlertConfirmationDialog from "@/components/AlertConfirmationDialog";
import { MentorColumns } from "@/components/MentorColumns";
import { MentorDialog } from "@/components/MentorDialog";


export default function MentorPage() {

  const [mentors, setMentors] = useState<Mentor[]>([]);
  const [isMentorDialogOpen, setIsMentorDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<"create" | "edit">("create");
  const [dialogInitialData, setDialogInitialData] = useState<Mentor | undefined>(undefined);
  const [isAlertDialogOpen, setIsAlertDialogOpen] = useState(false);
  const [mentorIdToDelete, setMentorIdToDelete] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const { getToken } = useAuth();

  async function confirmToDeleteMentor(mentorId: number) {
    setMentorIdToDelete(mentorId);
    setIsAlertDialogOpen(true);
  }

  async function deleteMentor() {
    setIsDeleting(true);
    //Todo: Implement delete mentor functionality
    console.log("deleted Mentor with mentorId:", mentorIdToDelete);
    setIsAlertDialogOpen(false);
    setMentorIdToDelete(null);
    setIsDeleting(false); // Reset deleting state after operation
  }


  async function fetchAllMentors() {

    try {
      const token = await getToken({ template: "skillmentor-auth-frontend" });
      const response = await fetch(`${BACKEND_URL}/academic/mentor`, {
        headers: {
          Authorization: `Bearer ${token}`,
        }
      }); //fetch all mentors

      if (!response.ok) {
        throw new Error("Failed to fetch mentors");
      }

      const data = await response.json();

      setMentors(data);
    } catch (error) {
      console.error("Error fetching mentors:", error);
    }
  }

  useEffect(() => {
    fetchAllMentors();
  }, []);

  async function handleSaveSuccess(mentor: Mentor, mode: "create" | "edit") {
    if (mode === "create") {
      setMentors(prevMentors => [...prevMentors, mentor]);
    } else {
      setMentors(prevMentors => prevMentors.map(c => c.mentor_id === mentor.mentor_id ? mentor : c));
    }
    setIsMentorDialogOpen(false); // Close dialog after save or update success
  }

  return (
    <div>
      <SidebarInset>
        <PageHeader title="Mentor" subtitle="All Mentors" />
        <div className="p-4 bg-white">
          <div className="container mx-auto py-6">

            {/* headings and import button, add new button */}
            <div className="mb-4 flex flex-wrap items-center justify-between space-y-2 gap-x-4">
              <div>
                <h2 className="text-2xl font-bold tracking-tight">Mentors</h2>
                <p className="text-muted-foreground">Here's a list of all mentors in the platform</p>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm" className="ml-auto hidden h-8 lg:flex">
                  <Download />
                  Import
                </Button>
                <Button size="sm" onClick={() => { setIsMentorDialogOpen(true); setDialogMode("create"); setDialogInitialData(undefined); }} className="h-8">
                  <Plus />
                  Add New
                </Button>

                {/* Dialog for Creating */}
                <MentorDialog
                  isOpen={isMentorDialogOpen}
                  onOpenChange={setIsMentorDialogOpen}
                  mode={dialogMode}
                  initialData={dialogInitialData}
                  onSaveSuccess={handleSaveSuccess}
                />
              </div>
            </div>

            <DataTable columns={
              MentorColumns({
                editMentor: (mentor: Mentor) => { setIsMentorDialogOpen(true); setDialogMode("edit"); setDialogInitialData(mentor); },
                confirmToDeleteMentor: (mentorId: number) => confirmToDeleteMentor(mentorId),
              })}
              data={mentors}
              filterKey="mentor_name" />

            <AlertConfirmationDialog
              isOpen={isAlertDialogOpen}
              onOpenChange={(open) => !isDeleting && setIsAlertDialogOpen(open)}
              onConfirm={deleteMentor}
              isDisabledButton={isDeleting}
              buttonName={isDeleting ? "Deleting" : "Delete"}
              title="Are you absolutely sure?"
              description="This action cannot be undone. This will permanently delete the mentor."
            />

          </div>
        </div>
      </SidebarInset>
    </div>
  )
}
