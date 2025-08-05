import { PageHeader } from '@/components/PageHeader'
import { SidebarInset } from '@/components/ui/sidebar'

export default function SessionPage() {
  return (
    <div>
      <SidebarInset>
        <PageHeader title="Booking" subtitle="All Sesssions" />

        <div className="p-4">
          <h1>SessionPage</h1>
        </div>
      </SidebarInset>
    </div>
  )
}
