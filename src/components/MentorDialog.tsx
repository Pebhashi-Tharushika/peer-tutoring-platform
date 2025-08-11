"use client"

import { useForm, Resolver } from "react-hook-form"
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
import { Textarea } from "@/components/ui/textarea"
import { useAuth } from "@clerk/clerk-react"
import { BACKEND_URL } from "@/config/env"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "./ui/dialog"
import { Mentor, MentorClass, TitleEnum } from "@/lib/types"
import { useEffect, useState } from "react"
import { MultiSelect } from "./MultiSelect"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select"
import { ScrollArea } from "./ui/scroll-area"


const baseSchema = z.object({
  title: z.enum(TitleEnum, { error: "Title is required.", }),
  firstName: z.string().min(2, { message: "First name is required." }),
  lastName: z.string().min(2, { message: "Last name is required." }),
  email: z.email({ message: "Invalid email address." }),
  phoneNumber: z.string().regex(/^\+[1-9]\d{6,14}$/, { message: "Invalid phone number." }),
  address: z.string().min(5, { message: "Address is required." }),
  profession: z.string().min(2, { message: "Profession is required." }),
  qualification: z.string().min(2, { message: "Qualification is required." }),
  sessionFee: z.coerce.number().min(0, { message: "Session fee must be a positive number." }), // Use z.coerce to convert string to number
  subject: z.string().min(10, { message: "Subject must be at least 10 characters." }).max(15, { message: "Subject must not be longer than 750 characters." }),
  classes: z.array(z.string()).nonempty({ message: "At least one class is required." }),
});

const createSchema = baseSchema.extend({
  mentorImage: z.custom<File>((file) => file instanceof File && file.size > 0, {
    message: "Image is required",
  })
    .refine((file) => file.size > 0, "Image is required."),
});

const editSchema = baseSchema.extend({
  mentorImage: z.union([z.custom<File>((file) => file instanceof File && file.size > 0), z.url()]).optional(),
});

type UnifiedFormValues = z.infer<typeof createSchema> | z.infer<typeof editSchema>;

type MentorDialogProps = {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  mode: "create" | "edit";
  initialData?: Mentor | undefined;
  onSaveSuccess: (newMentor: Mentor, mode: "create" | "edit") => void;
};

export function MentorDialog({
  isOpen,
  onOpenChange,
  mode,
  initialData,
  onSaveSuccess
}: MentorDialogProps) {
  const [classes, setClasses] = useState<MentorClass[]>([]);
  const { getToken } = useAuth();

  const options: { label: string; value: string }[] = classes.map(classroom => ({
    value: classroom.class_room_id.toString(),
    label: classroom.title
  }));

  const form = useForm<UnifiedFormValues>({
    resolver: zodResolver(mode === "create" ? createSchema : editSchema) as Resolver<UnifiedFormValues>,
    defaultValues: {
      title: initialData?.title as TitleEnum ?? undefined,
      firstName: initialData?.first_name ?? "",
      lastName: initialData?.last_name ?? "",
      email: initialData?.email ?? "",
      phoneNumber: initialData?.phone_number ?? "",
      address: initialData?.address ?? "",
      profession: initialData?.profession ?? "",
      qualification: initialData?.qualification ?? "",
      sessionFee: initialData?.session_fee ?? 0,
      subject: initialData?.subject ?? "",
      classes: initialData?.classroom_id_list?.map(String) ?? [],
    },
  });

  useEffect(() => {
    fetchClassesNotAssignedMentor();
    if (isOpen && initialData) {
      form.reset({
        title: initialData.title as TitleEnum,
        firstName: initialData.first_name,
        lastName: initialData.last_name,
        email: initialData.email,
        phoneNumber: initialData.phone_number,
        address: initialData.address,
        profession: initialData.profession,
        qualification: initialData.qualification,
        sessionFee: initialData.session_fee,
        subject: initialData.subject,
        classes: initialData.classroom_id_list.map(String),
        mentorImage: initialData.mentor_image,
      });
    } else if (isOpen) {
      form.reset({
        title: undefined,
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        address: "",
        profession: "",
        qualification: "",
        sessionFee: 0,
        subject: "",
        classes: [],
        mentorImage: undefined,
      });
    }
  }, [isOpen, initialData, form]);

  async function fetchClassesNotAssignedMentor() {
    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const response = await fetch(`${BACKEND_URL}/academic/classroom`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error(`Failed to fetch classrooms`);

      const classrooms = await response.json();
      setClasses(classrooms.filter((classroom: MentorClass) => classroom.mentor === null));

    } catch (error) {
      console.error("Error fetching classes:", error);
    }

  }

  async function onSubmit(data: UnifiedFormValues) {
    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const formData = new FormData();
      formData.append("title", data.title);
      formData.append("first_name", data.firstName);
      formData.append("last_name", data.lastName);
      formData.append("email", data.email);
      formData.append("phone_number", data.phoneNumber);
      formData.append("address", data.address);
      formData.append("profession", data.profession);
      formData.append("qualification", data.qualification);
      formData.append("session_fee", data.sessionFee.toString());
      formData.append("subject", data.subject);
      formData.append("classroom_id_list", JSON.stringify(data.classes.map(Number)));

      if (data.mentorImage instanceof File) {
        formData.append("mentor_image", data.mentorImage);
      } else if (data.mentorImage && typeof data.mentorImage === "string") {
        formData.append("existing_image_url", data.mentorImage);
      }

      const method = mode === "create" ? "POST" : "PUT";
      const url = mode === "create" ?
        `${BACKEND_URL}/academic/mentor` :
        `${BACKEND_URL}/academic/mentor/${initialData?.mentor_id}`;

      const response = await fetch(url, {
        method: method,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error(`Failed to ${mode === "create" ? "save" : "update"} mentor`);

      const newMentor = await response.json();
      onSaveSuccess(newMentor, mode);
      toast.success(`Mentor ${mode === "create" ? "created" : "updated"} successfully`);

    } catch (error) {
      toast.error(`Failed to ${mode === "create" ? "save" : "update"} mentor`);
    }
  }

  const startYear = new Date().getFullYear() - 20;
  const endYear = new Date().getFullYear();

  const years = Array.from(
    { length: endYear - startYear + 1 },
    (_, i) => startYear + i
  );

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>

      <DialogContent className="sm:max-w-3xl">
        <ScrollArea className="h-[92vh]">
          <div className="px-4">
            <DialogHeader>
              <DialogTitle>{mode === "create" ? "Create New Mentor" : "Edit Mentor"}</DialogTitle>
              <DialogDescription>
                {mode === "create" ? "Enter mentor details." : "Update mentor details."}
              </DialogDescription>
            </DialogHeader>

            <Form {...form}>
              <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">

                <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
                  <FormField control={form.control} name="title" render={({ field }) => (
                    <FormItem className="col-span-2">
                      <FormLabel>Title</FormLabel>
                      <Select onValueChange={field.onChange} defaultValue={field.value}>
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue placeholder="Title" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {Object.entries(TitleEnum).map(([key, value]) => (
                            <SelectItem key={key} value={key}>{value}</SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )} />

                  <FormField control={form.control} name="firstName" render={({ field }) => (
                    <FormItem className="col-span-5">
                      <FormLabel>First Name</FormLabel>
                      <FormControl><Input placeholder="Enter first name" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>
                  )} />

                  <FormField control={form.control} name="lastName" render={({ field }) => (
                    <FormItem className="col-span-5">
                      <FormLabel>Last Name</FormLabel>
                      <FormControl><Input placeholder="Enter last name" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>
                  )} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <FormField control={form.control} name="email" render={({ field }) => (
                    <FormItem>
                      <FormLabel>Email</FormLabel>
                      <FormControl><Input type="email" placeholder="Enter email" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />

                  <FormField control={form.control} name="phoneNumber" render={({ field }) => (
                    <FormItem>
                      <FormLabel>Phone Number</FormLabel>
                      <FormControl><Input placeholder="Enter phone number" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
                  <FormField control={form.control} name="address" render={({ field }) => (
                    <FormItem>
                      <FormLabel>Address</FormLabel>
                      <FormControl><Input placeholder="Enter Address" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-12 gap-4">
                  <FormField control={form.control} name="profession" render={({ field }) => (
                    <FormItem className="col-span-5">
                      <FormLabel>Profession</FormLabel>
                      <FormControl><Input placeholder="Enter Profession" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />
                  <FormField control={form.control} name="qualification" render={({ field }) => (
                    <FormItem className="col-span-4">
                      <FormLabel>Qualification: Since Tutor</FormLabel>
                      <Select onValueChange={field.onChange} defaultValue={field.value}>
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue placeholder="Select Year" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          <ScrollArea className="h-48">
                            {years.map((year) => (
                              <SelectItem key={year} value={String(year)}>
                                {year}
                              </SelectItem>
                            ))}
                          </ScrollArea>
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>)} />
                  <FormField control={form.control} name="sessionFee" render={({ field }) => (
                    <FormItem className="col-span-3">
                      <FormLabel>Session Fee: (LKR)</FormLabel>
                      <FormControl><Input type="number" placeholder="Enter session fee" {...field} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
                  <FormField control={form.control} name="subject" render={({ field }) => (
                    <FormItem>
                      <FormLabel>Subject/Bio</FormLabel>
                      <FormControl>
                        <Textarea placeholder="Tell student a little bit about yourself" className="resize-none" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
                  <FormField control={form.control} name="mentorImage" render={({ field }) => (
                    <FormItem><FormLabel>Mentor Image</FormLabel><FormControl>
                      <Input type="file" accept="image/*" onChange={(e) => field.onChange(e.target.files?.[0])} /></FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-1 gap-4">
                  <FormField control={form.control} name="classes" render={({ field }) => (
                    <FormItem>
                      <FormLabel>Classes</FormLabel>
                      <FormControl>
                        <MultiSelect
                          options={options}
                          onValueChange={field.onChange}
                          defaultValue={field.value}
                          placeholder="Select classes"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>)} />
                </div>

                <DialogFooter className="pt-4">
                  <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancel</Button>
                  <Button type="submit">{mode === "create" ? "Save" : "Update"}</Button>
                </DialogFooter>
              </form>
            </Form>
          </div>
        </ScrollArea>
      </DialogContent>

    </Dialog>
  )

}