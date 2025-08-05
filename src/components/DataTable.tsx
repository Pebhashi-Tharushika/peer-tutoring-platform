"use client"

import {
    ColumnDef,
    ColumnFiltersState,
    flexRender,
    getCoreRowModel,
    getFilteredRowModel,
    getSortedRowModel,
    SortingState,
    useReactTable,
} from "@tanstack/react-table"

import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { DataTablePagination } from "./DataTablePagination"
import { useState } from "react"
import { Input } from "./ui/input"
import { Button } from "./ui/button"
import { DataTableViewOptions } from "./DataTableViewOptions"
import { Download, Plus } from "lucide-react"

interface DataTableProps<TData, TValue> {
    columns: ColumnDef<TData, TValue>[]
    data: TData[]
}

export function DataTable<TData, TValue>({
    columns,
    data,
}: DataTableProps<TData, TValue>) {
    const [sorting, setSorting] = useState<SortingState>([])
    const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([])
    
    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        onSortingChange: setSorting,
        getSortedRowModel: getSortedRowModel(),
        onColumnFiltersChange: setColumnFilters,
        getFilteredRowModel: getFilteredRowModel(),
        state: {
        sorting,
        columnFilters
        },
    })

    return (
        <div>
            <div className="mb-4 flex flex-wrap items-center justify-between space-y-2 gap-x-4">
                <div>
                    <h2 className="text-2xl font-bold tracking-tight">Classrooms</h2>
                    <p className="text-muted-foreground">Here's a list of all classes in the platform!</p>
                </div>
                <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="ml-auto hidden h-8 lg:flex">
                        <Download/>
                        Import
                    </Button>
                    <Button size="sm">
                        <Plus/>
                        Add New 
                    </Button>
                </div>
             </div>
            
            
        
            <div className="space-y-4">
                
                {/* Search, Filter and column toggle */}
                <div className="flex justify-between items-center">
                    <div className="flex items-center">
                        <Input
                        placeholder="Filter class titles..."
                        value={(table.getColumn("title")?.getFilterValue() as string) ?? ""}
                        onChange={(event) =>
                            table.getColumn("title")?.setFilterValue(event.target.value)
                        }
                        className="max-w-sm"
                        />
                    </div>
                    <div>
                        <DataTableViewOptions table={table} />
                    </div>
                </div>

                {/* Table */}
                <div className="overflow-auto rounded-md border">
                    <Table className="min-w-full">
                        <TableHeader>
                            {table.getHeaderGroups().map((headerGroup) => (
                                <TableRow key={headerGroup.id}>
                                    {headerGroup.headers.map((header) => {
                                        return (
                                            <TableHead key={header.id}>
                                                {header.isPlaceholder
                                                    ? null
                                                    : flexRender(
                                                        header.column.columnDef.header,
                                                        header.getContext()
                                                    )}
                                            </TableHead>
                                        )
                                    })}
                                </TableRow>
                            ))}
                        </TableHeader>
                        <TableBody>
                            {table.getRowModel().rows?.length ? (
                                table.getRowModel().rows.map((row) => (
                                    <TableRow
                                        key={row.id}
                                        data-state={row.getIsSelected() && "selected"}
                                    >
                                        {row.getVisibleCells().map((cell) => (
                                            <TableCell key={cell.id}>
                                                {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                            </TableCell>
                                        ))}
                                    </TableRow>
                                ))
                            ) : (
                                <TableRow>
                                    <TableCell colSpan={columns.length} className="h-24 text-center">
                                        No results.
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </div>

                {/* Pagination */}
                <DataTablePagination table={table} />
            </div>
        </div>
    )
}