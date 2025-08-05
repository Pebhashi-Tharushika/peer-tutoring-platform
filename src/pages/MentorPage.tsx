import { PageHeader } from '@/components/PageHeader'
import { SidebarInset } from '@/components/ui/sidebar'

export default function MentorPage() {
  return (
    <div>
      <SidebarInset>
        <PageHeader title="Mentor" subtitle="All Mentors" />

        <div className="p-4">
          <h1>MentorPage</h1>
        </div>
      </SidebarInset>
    </div>
  )
}
