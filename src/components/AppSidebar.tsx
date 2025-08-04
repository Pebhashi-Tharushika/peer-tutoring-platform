"use client"


import * as React from "react"
import {
  Frame,
  GalleryVerticalEnd,
  LayoutDashboard,
  Map,
  MessagesSquare,
  PieChart,
  School,
  UserPen,
  Users,
  NotebookPen,
  CalendarClock,
  Settings,
  CircleQuestionMark,
  HandCoins,
  Receipt,
  Wallet,
} from "lucide-react"

import {
  Sidebar,
  SidebarContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarRail,
} from "@/components/ui/sidebar"
import { SidebarSection } from "./SidebarSection"


const data = {
  academic: [
    {
      title: "Classroom",
      url: "/admin/classroom",
      icon: School,
      isActive: true,
      items: [
        {
          title: "All Classes",
          url: "#",
        },
        {
          title: "Assignments",
          url: "#",
        },
        {
          title: "Grades",
          url: "#",
        },
        {
          title: "Attendance",
          url: "#",
        },
      ],
    },
    {
      title: "Mentor",
      url: "/admin/mentor",
      icon: UserPen,
      items: [
        {
          title: "Mentor Profiles",
          url: "#",
        },
        {
          title: "Certification",
          url: "#",
        },
        {
          title: "Feedback & Reviews",
          url: "#",
        },
      ],
    },
    {
      title: "Bookings",
      url: "/admin/session",
      icon: CalendarClock,
      items: [
        {
          title: "All Sessions",
          url: "#",
        },
        {
          title: "Booking status",
          url: "#",
        },
        {
          title: "Announcements",
          url: "#",
        },
        {
          title: "Resources & Notes",
          url: "#",
        },
      ],
    },
    {
      title: "Student",
      url: "/admin/student",
      icon: NotebookPen,
      items: [
        {
          title: "All Students",
          url: "#",
        },
        {
          title: "Enrollments",
          url: "#",
        },
        {
          title: "Performance",
          url: "#",
        },
        {
          title: "Support",
          url: "#",
        },
      ],
    },
  ],
  finance: [
    {
      title: "Session Fees",
      url: "#",
      icon: Receipt,
    },
    {
      title: "Mentor Payments",
      url: "#",
      icon: Wallet,
    },
    {
      title: "Employee wages",
      url: "#",
      icon: HandCoins,
    },
  ],
  general:[
    {
      title: "Dashboard",
      url: "/admin",
      icon: LayoutDashboard,
    },
    {
      title: "Users",
      url: "#",
      icon: Users,
    },
    {
      title: "Chat",
      url: "#",
      icon: MessagesSquare,
    },
  ],
  other:[
    {
      title: "Setting",
      url: "#",
      icon: Settings,
      items: [
        {
          title: "Account",
          url: "#",
        },
        {
          title: "Appearance",
          url: "#",
        },
        {
          title: "Notification",
          url: "#",
        },
        {
          title: "Display",
          url: "#",
        },
      ],
    },
    {
      title: "Help Center",
      url: "#",
      icon: CircleQuestionMark,
    }
  ]
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar collapsible="icon" {...props}>
      <SidebarHeader>
      <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton size="lg" asChild>
              <a href="#">
                <div className="bg-sidebar-primary text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg">
                  <GalleryVerticalEnd className="size-4" />
                </div>
                <div className="flex flex-col gap-0.5 leading-none">
                  <span className="font-medium">ADMINISTRATION</span>
                </div>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        <SidebarSection items={data.general} title="General"/>
        <SidebarSection items={data.academic} title="Academic"/>
        <SidebarSection items={data.finance} title="Finance"/>
        <SidebarSection items={data.other} title="Other" showMore={true}/>
      </SidebarContent>
      <SidebarRail />
    </Sidebar>
  )
}
