import { PageHeader } from '@/components/PageHeader'
import { SidebarInset } from '@/components/ui/sidebar'

export default function StudentPage() {
  return (
    <div>
      <SidebarInset>
        <PageHeader title="Student" subtitle="All Students" />

        <div className="p-4">
          <h1>StudentPage</h1>
        </div>
      </SidebarInset>
    </div>
  )
}
