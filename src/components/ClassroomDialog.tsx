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
import { useAuth } from "@clerk/clerk-react"
import { BACKEND_URL } from "@/config/env"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog"
import { ClassRoom, MentorClass } from "@/lib/types"
import { useEffect, useState } from "react"


const baseSchema = z.object({
  className: z.string().min(2, {
    message: "Class name must be at least 2 characters.",
  }),
});

const createSchema = baseSchema.extend({
  classImage: z.custom<File>((file) => file instanceof File && file.size > 0, {
    message: "Image is required",
  })
    .refine((file) => file.size > 0, "Image is required."),
});

const editSchema = baseSchema.extend({
  classImage: z
    .union([
      z.custom<File>((file) => file instanceof File && file.size > 0, {
        message: "Image is required",
      }),
      z.url(),
    ])
    .optional(),
});

type FormValues = z.infer<typeof createSchema> | z.infer<typeof editSchema>;

type ClassroomDialogProps = {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  mode: "create" | "edit";
  initialData?: ClassRoom | undefined;
  onSaveSuccess: (newMentorClass: MentorClass, mode: "create" | "edit") => void;
};

export function ClassroomDialog({
  isOpen,
  onOpenChange,
  mode,
  initialData,
  onSaveSuccess
}: ClassroomDialogProps) {
  const { getToken } = useAuth();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const form = useForm<FormValues>({
    resolver: zodResolver(mode === "create" ? createSchema : editSchema),
    defaultValues: {
      className: initialData?.title ?? ""
    },
  });

  useEffect(() => {
    if (isOpen) {
      setIsSubmitting(false);

      if (initialData) {
        form.reset({
          className: initialData.title,
          classImage: initialData.class_image,
        });
      } else {
        form.reset({
          className: "",
          classImage: undefined,
        });
      }
    }
  }, [isOpen, initialData, form]);


  async function onSubmit(data: FormValues) {

    setIsSubmitting(true);

    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const formData = new FormData();
      formData.append("title", data.className);

      if (data.classImage instanceof File) {
        formData.append("class_image", data.classImage);
      } else if (data.classImage && typeof data.classImage === "string") {
        formData.append("existing_image_url", data.classImage);
      }

      const method = mode === "create" ? "POST" : "PUT";
      const url = mode === "create" ?
        `${BACKEND_URL}/academic/classroom` :
        `${BACKEND_URL}/academic/classroom/${initialData?.class_room_id}`;

      const response = await fetch(url, {
        method: method,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error(`Failed to ${mode === "create" ? "save" : "update"} classroom`);

      const newMentorClass = await response.json();

      onSaveSuccess(newMentorClass, mode);
      
      toast.success(`Classroom ${mode === "create" ? "created" : "updated"} successfully`);

    } catch (error) {
      toast.error(`Failed to ${mode === "create" ? "save" : "update"} classroom`);
      setIsSubmitting(false);
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{mode === "create" ? "Create New Class" : "Edit Classroom"}</DialogTitle>
          <DialogDescription>
            {mode === "create" ? "Enter class name and upload an image." : "Update class details."}
          </DialogDescription>
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
                    <Input
                      type="file"
                      accept="image/*"
                      onChange={(e) => field.onChange(e.target.files?.[0])}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter className="pt-4">
              <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                Cancel
              </Button>
              <Button 
                type="submit"
                disabled={isSubmitting}
              >
                {isSubmitting ? mode === "create" ? "Saving..." : "Updating..." : mode === "create" ? "Save" : "Update"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  )

}