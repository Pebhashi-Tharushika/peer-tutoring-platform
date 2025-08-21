import { ChevronsUpDown } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Column } from "@tanstack/react-table"

interface SortableColumnProps<TData> {
  column: Column<TData, unknown>
  title: string
}

export function SortableColumn<TData>({ column, title }: SortableColumnProps<TData>) {
  return (
    <Button
      variant="ghost"
      className="p-0 hover:bg-transparent"
      onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
    >
      {title}
      <ChevronsUpDown className="ml-2" />
    </Button>
  )
}
