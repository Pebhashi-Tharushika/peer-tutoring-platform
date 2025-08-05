"use client"

import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog"
import { Eye } from "lucide-react"
import { Button } from "@/components/ui/button"

interface PreviewDialogProps {
  imageUrl: string
}

export function PreviewDialog({ imageUrl }: PreviewDialogProps) {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon">
          <Eye className="h-4 w-4" />
        </Button>
      </DialogTrigger>
      <DialogContent className="w-[30vw] h-[50vh] max-w-3xl p-4">
      <div className="w-full h-full overflow-hidden flex items-center justify-center">
        <img
            src={imageUrl}
            alt="Class Image"
            className="max-w-full max-h-full object-contain rounded-md"
            />
            </div>
      </DialogContent>
    </Dialog>
  )
}
