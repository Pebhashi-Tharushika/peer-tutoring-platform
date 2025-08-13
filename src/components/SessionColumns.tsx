"use client"

import { FullSession } from "@/lib/types"
import { ColumnDef } from "@tanstack/react-table"
import { Button } from "./ui/button"
import { SortableColumn } from "./SortableColumn"
import { Checkbox } from "./ui/checkbox"
import { StatusPill } from "./StatusPill"

type ColumnActions = {
  approveSession: (id: number) => void;
  markAsCompleted: (id: number) => void;
};
export const SessionColumns = ({ approveSession, markAsCompleted }: ColumnActions): ColumnDef<FullSession>[] => [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={
          table.getIsAllPageRowsSelected() ||
          (table.getIsSomePageRowsSelected() && "indeterminate")
        }
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
    enableSorting: false,
  },
  {
    accessorKey: "session_id",
    header: ({ column }) => <SortableColumn column={column} title="Session ID" />,
    cell: ({ row }) => (
      <div className="text-center">
        {row.getValue("session_id")}
      </div>
    ),
  },
  {
    accessorFn: row => `${row.topic}`,
    id: "class_room",
    header: ({ column }) => <SortableColumn column={column} title="Classroom" />,
  },
  {
    accessorFn: row => `${row.student.first_name} ${row.student.last_name}`,
    id: "student_name",
    header: ({ column }) => <SortableColumn column={column} title="Student Name" />,
  },
  {
    accessorFn: row => `${row.mentor.first_name} ${row.mentor.last_name}`,
    id: "mentor_name",
    header: ({ column }) => <SortableColumn column={column} title="Mentor Name" />,
  },
  {
    accessorFn: row => `${row.start_time.split('T')[0]}`,
    id: "session_date",
    header: ({ column }) => <SortableColumn column={column} title="Session Date" />,
  },
  {
    accessorFn: row => `${new Date(row.start_time).toLocaleTimeString()} - ${new Date(row.end_time).toLocaleTimeString()}`,
    id: "duration",
    header: ({ column }) => <SortableColumn column={column} title="Duration" />,
  },
  {
    accessorKey: "session_status",
    header: ({ column }) => <SortableColumn column={column} title="Status" />,
    cell:({row})=>{
      return(
        <StatusPill status={row.original.session_status} />
      )
    }
  },
  {
    id: "accept/complete",
    header: () => <div className="text-center">Actions</div>,
    cell: ({ row }) => {
      const session = row.original;

      return (
        <div className="flex gap-2">
          <Button
            variant="outline"
            size="sm"
            disabled={session.session_status !== "PENDING"}
            onClick={() => approveSession(session.session_id)}
          >
            Approve
          </Button>
          <Button
            variant="outline"
            size="sm"
            disabled={session.session_status !== "ACCEPTED"}
            onClick={() => markAsCompleted(session.session_id)}
          >
            Complete
          </Button>
        </div>
      )
    },
  },
]