import { ClassroomColumns } from "@/components/ClassroomColumns";
import { ClassroomDialog } from "@/components/ClassroomDialog";
import { DataTable } from "@/components/DataTable";
import { PageHeader } from "@/components/PageHeader";
import { Button } from "@/components/ui/button";
import { SidebarInset } from "@/components/ui/sidebar";
import { BACKEND_URL } from "@/config/env";
import { ClassRoom, MentorClass } from "@/lib/types";
import { useAuth } from "@clerk/clerk-react";
import { Download, Plus } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import AlertConfirmationDialog from "@/components/AlertConfirmationDialog";


export default function ClassroomPage() {

  const [classes, setClasses] = useState<MentorClass[]>([]);
  const [isClassroomDialogOpen, setIsClassroomDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState<"create" | "edit">("create");
  const [dialogInitialData, setDialogInitialData] = useState<ClassRoom | undefined>(undefined);
  const [isAlertDialogOpen, setIsAlertDialogOpen] = useState(false);
  const [classroomIdToDelete, setClassroomIdToDelete] = useState<number | null>(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const { getToken } = useAuth();

  async function confirmToDeleteClassroom(classroomId: number) {
    setClassroomIdToDelete(classroomId);
    setIsAlertDialogOpen(true);
  }

  async function deleteClassroom() {

    if (classroomIdToDelete === null) return;

    setIsDeleting(true);

    try {
      const token = await getToken({ template: "skillmentor-auth-frontend" });
      const response = await fetch(`${BACKEND_URL}/academic/classroom/${classroomIdToDelete}`, { // delete a classroom
        method: 'DELETE',
        headers: ({
          Authorization: `Bearer ${token}`,
        })
      });
      
      if (!response.ok){ 
        const errorData = await response.json();
        toast.error(errorData.message); 
        throw new Error(errorData.message);
      }
      setClasses(classes.filter(c => c.class_room_id !== classroomIdToDelete));
      toast.success("Classroom deleted successfully");

    } catch (error) {
      console.error(error);
      
    } finally {
      setIsAlertDialogOpen(false);
      setClassroomIdToDelete(null);
      setIsDeleting(false); // Reset deleting state after operation
    }
  }

  async function fetchAllClasses() {

    try {
      const response = await fetch(`${BACKEND_URL}/academic/classroom`); //fetch all classrooms

      if (!response.ok) {
        throw new Error("Failed to fetch classes");
      }

      const data = await response.json();

      setClasses(data);
    } catch (error) {
      console.error("Error fetching classes:", error);
    }
  }

  useEffect(() => {
    fetchAllClasses();
  }, []);

  async function handleSaveSuccess(classroom: MentorClass, mode: "create" | "edit") {
    if (mode === "create") {
      setClasses(prevClasses => [...prevClasses, classroom]);
    } else {
      setClasses(prevClasses => prevClasses.map(c => c.class_room_id === classroom.class_room_id ? classroom : c));
    }
    setIsClassroomDialogOpen(false); // Close dialog after save or update success
  }

  return (
    <div>
      <SidebarInset>
        <PageHeader title="Classeroom" subtitle="All Classes" />
        <div className="p-4 bg-white">
          <div className="container mx-auto py-6">

            {/* headings and import button, add new button */}
            <div className="mb-4 flex flex-wrap items-center justify-between space-y-2 gap-x-4">
              <div>
                <h2 className="text-2xl font-bold tracking-tight">Classrooms</h2>
                <p className="text-muted-foreground">Here's a list of all classrooms in the platform</p>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm" className="ml-auto hidden h-8 lg:flex">
                  <Download />
                  Import
                </Button>
                <Button size="sm" onClick={() => { setIsClassroomDialogOpen(true); setDialogMode("create"); setDialogInitialData(undefined); }} className="h-8">
                  <Plus />
                  Add New
                </Button>

                {/* Dialog for Creating/updating */}
                <ClassroomDialog
                  isOpen={isClassroomDialogOpen}
                  onOpenChange={setIsClassroomDialogOpen}
                  mode={dialogMode}
                  initialData={dialogInitialData}
                  onSaveSuccess={handleSaveSuccess}
                />
              </div>
            </div>

            <DataTable columns={
              ClassroomColumns({
                editClassroom: (classroom: ClassRoom) => { setIsClassroomDialogOpen(true); setDialogMode("edit"); setDialogInitialData(classroom); },
                confirmToDeleteClassroom: (classroomId: number) => confirmToDeleteClassroom(classroomId),
              })}
              data={classes}
              filterKey="title" />

            <AlertConfirmationDialog
              isOpen={isAlertDialogOpen}
              onOpenChange={(open) => !isDeleting && setIsAlertDialogOpen(open)}
              // onOpenChange={setIsAlertDialogOpen}
              onConfirm={deleteClassroom}
              isDisabledButton={isDeleting}
              buttonName={isDeleting ? "Deleting" : "Delete"}
              title="Are you absolutely sure?"
              description="This action cannot be undone. This will permanently delete the classroom."
            />

          </div>
        </div>
      </SidebarInset>
    </div>
  )
}
