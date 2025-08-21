import { Outlet } from "react-router";
import { AppSidebar } from "@/components/AppSidebar";
import { SidebarProvider } from "./ui/sidebar";

export default function AdminLayout() {
    return (
        <SidebarProvider defaultOpen className="w-auto">
            <AppSidebar side="right" collapsible="icon" className="sticky" />
            <div className="flex-1 overflow-auto p-4 bg-white">
                <Outlet />
            </div>
        </SidebarProvider>
    );
}
