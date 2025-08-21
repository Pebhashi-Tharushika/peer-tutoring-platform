"use client"

import { Mentor } from "@/lib/types"
import { ColumnDef } from "@tanstack/react-table"
import { PreviewDialog } from "./PreviewDialog"
import { Button } from "./ui/button"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuSeparator, DropdownMenuTrigger } from "./ui/dropdown-menu"
import { MoreHorizontal } from "lucide-react"
import { SortableColumn } from "./SortableColumn"
import { Checkbox } from "./ui/checkbox"
import { CertifiedSwitch } from "./CertifiedSwitch"

type ColumnActions = {
  editMentor: (mentor: Mentor) => void;
  confirmToDeleteMentor: (id: number) => void;
};
export const MentorColumns = ({ editMentor, confirmToDeleteMentor }: ColumnActions): ColumnDef<Mentor>[] => [
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
    accessorKey: "mentor_id",
    header: ({ column }) => <SortableColumn column={column} title="Mentor ID" />,
    cell: ({ row }) => (
      <div className="text-center">
        {row.getValue("mentor_id")}
      </div>
    ),
  },
  {
    accessorFn: row => `${row.title} ${row.first_name} ${row.last_name}`,
    id: "mentor_name",
    header: ({ column }) => <SortableColumn column={column} title="Mentor Name" />,
  },
  {
    accessorKey: "profession",
    header: ({ column }) => <SortableColumn column={column} title="Profession" />,
  },
  {
    accessorKey: "email",
    header: ({ column }) => <SortableColumn column={column} title="Email" />,
  },
  {
    accessorKey: "phone_number",
    header: ({ column }) => <SortableColumn column={column} title="Phone Number" />,
    cell: ({ row }) => (
      <div className="text-right">
        {row.getValue("phone_number")}
      </div>
    ),
  },
  {
    accessorKey: "session_fee",
    header: ({ column }) => <SortableColumn column={column} title="Session Fee" />,
    cell: ({ row }) => {
      const amount = parseFloat(row.getValue("session_fee"))
      const formatted = new Intl.NumberFormat("si-LK", {
        style: "currency",
        currency: "LKR",
      }).format(amount)

      return <div className="text-right">{formatted}</div>
    },
  },
  {
    accessorKey: "is_certified",
    header: ({ column }) => <SortableColumn column={column} title="Certification" />,
    cell: ({ row }) => {
      const { mentor_id, is_certified } = row.original;

      return (
        <CertifiedSwitch
          initialState={is_certified}
          mentorId={mentor_id}
        />
      );
    }
  },
  {
    id: "mentor_image",
    header: "Mentor Image",
    cell: ({ row }) => {
      const imageUrl = row.original.mentor_image
      return (
        <div className="flex items-center justify-center">
          {typeof imageUrl === "string" ? (
            <PreviewDialog imageUrl={imageUrl} />
          ) : (
            <span className="text-muted-foreground">No image</span>
          )}
        </div>
      )
    },
  },
  {
    id: "actions",
    cell: ({ row }) => {
      const cls = row.original

      return (
        <DropdownMenu>

          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="h-8 w-8 p-0">
              <span className="sr-only">Open menu</span>
              <MoreHorizontal className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>

          <DropdownMenuContent align="end">
            <DropdownMenuItem onClick={() => navigator.clipboard.writeText(cls.title)}>Copy Mentor Name</DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={() => editMentor(cls)}>Edit Mentor</DropdownMenuItem>
            <DropdownMenuItem onClick={() => confirmToDeleteMentor(cls.mentor_id)} className="text-destructive">Delete Mentor</DropdownMenuItem>
            <DropdownMenuItem>View Mentor Details</DropdownMenuItem>
          </DropdownMenuContent>

        </DropdownMenu>
      )
    },
  },
]