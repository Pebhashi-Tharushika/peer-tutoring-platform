import { columns } from "@/components/Column";
import { DataTable } from "@/components/DataTable";
import { PageHeader } from "@/components/PageHeader";
import { SidebarInset } from "@/components/ui/sidebar";
import { BACKEND_URL } from "@/config/env";
import { MentorClass } from "@/lib/types";
import { useEffect, useState } from "react";




export default function ClassroomPage() {
  const [classes, setClasses] = useState<MentorClass[]>([]);
  
  useEffect(() => {
    async function fetchAllClasses() {
      
      try {
        const response = await fetch(`${BACKEND_URL}/academic/classroom`);
    
        if (!response.ok) {
          throw new Error("Failed to fetch classes");
        }
    
        const data = await response.json();
        console.log("Fetched mentor classes:", data);
        setClasses(data);
      } catch (error) {
        console.error("Error fetching classes:", error);
      }
    }
    fetchAllClasses();
  },[]);
  
  return (
    <div>
      <SidebarInset>
        <PageHeader title="Classeroom" subtitle="All Classes" />

        <div className="p-4">
          <div className="container mx-auto py-6">
            <DataTable columns={columns} data={classes} />
          </div>
        </div>
      </SidebarInset>
    </div>
  )
}
