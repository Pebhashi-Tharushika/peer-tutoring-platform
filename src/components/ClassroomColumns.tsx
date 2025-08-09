"use client"

import { ClassRoom, MentorClass } from "@/lib/types"
import { ColumnDef } from "@tanstack/react-table"
import { PreviewDialog } from "./PreviewDialog"
import { Button } from "./ui/button"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator, DropdownMenuTrigger } from "./ui/dropdown-menu"
import { MoreHorizontal } from "lucide-react"
import { SortableColumn } from "./SortableColumn"
import { Checkbox } from "./ui/checkbox"

type ColumnActions = {
  editClassroom: (classroom: ClassRoom) => void;
  deleteClassroom: (id: number) => void;
};
export const ClassroomColumns = ({ editClassroom, deleteClassroom }: ColumnActions): ColumnDef<MentorClass>[] => [
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
    accessorKey: "class_room_id",
    header: ({ column }) => <SortableColumn column={column} title="Classroom ID" />,
  },
  {
    accessorKey: "title",
    header: ({ column }) => <SortableColumn column={column} title="Title" />,
  },
  {
    accessorKey: "enrolled_student_count",
    header: ({ column }) => <SortableColumn column={column} title="Enrolled Students" />,
  },
  {
    accessorFn: row => row.mentor !== null ? `${row.mentor.first_name} ${row.mentor.last_name}` : "Unassigned",
    id: "mentor_name",
    header: ({ column }) => <SortableColumn column={column} title="Mentor Name" />,
  },
  {
    id: "class_image",
    header: "Class Image",
    cell: ({ row }) => {
      const imageUrl = row.original.class_image
      if (typeof imageUrl === "string") {
        return <PreviewDialog imageUrl={imageUrl} />
      }
      return <span className="text-muted-foreground">No image</span>
    },
  },
  {
    id: "actions",
    cell: ({ row }) => {
      const cls = row.original

      const classroomData: ClassRoom = {
        class_room_id: cls.class_room_id,
        title: cls.title,
        enrolled_student_count: cls.enrolled_student_count,
        class_image: cls.class_image,
      };

      return (
        <DropdownMenu>

          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="h-8 w-8 p-0">
              <span className="sr-only">Open menu</span>
              <MoreHorizontal className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>

          <DropdownMenuContent align="end">
            <DropdownMenuItem onClick={() => navigator.clipboard.writeText(cls.title)}>Copy Classroom Title</DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={() => editClassroom(classroomData)}>Edit Classroom</DropdownMenuItem>
            <DropdownMenuItem onClick={() => deleteClassroom(cls.class_room_id)}>Delete Classroom</DropdownMenuItem>
            <DropdownMenuItem>View Mentor Details</DropdownMenuItem>
          </DropdownMenuContent>

        </DropdownMenu>
      )
    },
  },
]