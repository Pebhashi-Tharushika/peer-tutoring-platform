"use client"

import { useForm } from "react-hook-form"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { toast } from "sonner"

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { useNavigate } from "react-router"
import {useAuth } from "@clerk/clerk-react";
import { BACKEND_URL } from "@/config/env"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog"

const FormSchema = z.object({
    className: z.string().min(2, {
        message: "Class name must be at least 2 characters.",
    }),
    classImage: z
        .instanceof(FileList)
        .refine((files) => files.length > 0, {
            message: "Image is required",
        }),
})

type CreateClassDialogProps = {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
  };

export function CreateClassDialog({ isOpen, onOpenChange }: CreateClassDialogProps) {
    const navigate = useNavigate();
    const { getToken } = useAuth();

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            className: "",
            classImage: undefined,
        },
    })

    async function onSubmit(data: z.infer<typeof FormSchema>) {
        console.log("Form submitted:", data)

        try {
            const file = data.classImage[0]
            const base64Image = await toBase64(file)
            const payload = {
                title: data.className,
                class_image: base64Image,
                enrolled_student_count: 0,
            }

            console.log("Payload to send:", payload)
            const token = await getToken({ template: "skillmentor-auth-frontend" });

            const response = await fetch(`${BACKEND_URL}/academic/classroom`, { //create a new classroom
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(payload),
            })

            if (!response.ok) throw new Error("Failed to save classroom")

            toast.success("Classroom created successfully!")
            console.log(response);
            setTimeout(() => {
                onOpenChange(false) // Close dialog after save
                navigate("/dashboard");
              }, 2000);

        } catch (error) {
            toast.error("Failed to create classroom.")
        }
    }

    function toBase64(file: File): Promise<string> {
        return new Promise((resolve, reject) => {
            const reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = () => resolve(reader.result as string)
            reader.onerror = reject
        })
    }

    return (
        <Dialog open={isOpen} onOpenChange={onOpenChange}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Create New Class</DialogTitle>
              <DialogDescription>Enter class name and upload an image.</DialogDescription>
            </DialogHeader>
    
            <Form {...form}>
              <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                  control={form.control}
                  name="className"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Class Name</FormLabel>
                      <FormControl>
                        <Input placeholder="Enter class name" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
    
                <FormField
                  control={form.control}
                  name="classImage"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Class Image</FormLabel>
                      <FormControl>
                        <Input type="file" accept="image/*" onChange={(e) => field.onChange(e.target.files)} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
    
               
                <DialogFooter className="pt-4">
                  <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                    Cancel
                  </Button>
                  <Button type="submit">Save</Button>
                </DialogFooter>
              </form>
            </Form>
          </DialogContent>
        </Dialog>
      )

}