"use client"


import * as React from "react"
import {
  GalleryVerticalEnd,
  LayoutDashboard,
  MessagesSquare,
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
import { Link } from "react-router"


const data = {
  academic: [
    {
      title: "Classroom",
      icon: School,
      isActive: true,
      items: [
        {
          title: "All Classes",
          url: "/admin/classroom/all",
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
      icon: UserPen,
      items: [
        {
          title: "All Mentors",
          url: "/admin/mentor/all",
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
      icon: CalendarClock,
      items: [
        {
          title: "All Sessions",
          url: "/admin/session/all",
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
      icon: NotebookPen,
      items: [
        {
          title: "All Students",
          url: "/admin/student/all",
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
      url: "/setting",
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
              <Link to="#">
                <div className="bg-sidebar-primary text-sidebar-primary-foreground flex aspect-square size-8 items-center justify-center rounded-lg">
                  <GalleryVerticalEnd className="size-4" />
                </div>
                <div className="flex flex-col gap-0.5 leading-none">
                  <span className="font-medium">ADMINISTRATION</span>
                </div>
              </Link>
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
