import { PageHeader } from "@/components/PageHeader";
import { SidebarInset } from "@/components/ui/sidebar";


export default function ClassroomPage() {
  return (
    <div>
      <SidebarInset>
        <PageHeader title="Classeroom" subtitle="All Classes" />

        <div className="p-4">
          <h1>ClassroomPage</h1>
        </div>
      </SidebarInset>
    </div>
  )
}
