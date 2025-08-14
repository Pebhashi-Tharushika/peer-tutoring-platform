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
import { useEffect, useRef, useState } from "react"
import { MultiSelect, MultiSelectRef } from "./MultiSelect"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select"
import { ScrollArea } from "./ui/scroll-area"


const baseSchema = z.object({
  title: z.enum(TitleEnum, { error: "required*" }),
  firstName: z.string().min(2, { message: "required*" }),
  lastName: z.string().min(2, { message: "required*" }),
  email: z.email({ message: "Invalid" }),
  phoneNumber: z.string().regex(/^\+[1-9]\d{6,14}$/, { message: "Invalid" }),
  address: z.string().min(5, { message: "required*" }),
  profession: z.string().min(2, { message: "required*" }),
  qualification: z.string({ error: "required*" }),
  sessionFee: z.coerce.number().min(1, { message: "Invalid" }), // Use z.coerce to convert string to number
  subject: z.string().min(10, { message: "must be at least 10 characters" }).max(750, { message: "Subject must not be longer than 750 characters." }),
  classes: z.array(z.string()).nonempty({ message: "at least one class is required*" }),
});

const createSchema = baseSchema.extend({
  mentorImage: z.custom<File>((file) => file instanceof File && file.size > 0, {
    message: "required*",
  })
    .refine((file) => file.size > 0, "required*"),
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
  const [isSubmitting, setIsSubmitting] = useState(false);

  const options: { label: string; value: string }[] = classes.map(classroom => ({
    value: classroom.class_room_id.toString(),
    label: classroom.title
  }));

  const form = useForm<UnifiedFormValues>({
    resolver: zodResolver(mode === "create" ? createSchema : editSchema) as Resolver<UnifiedFormValues>,
    defaultValues: {
      title: initialData?.title as TitleEnum ?? "",
      firstName: initialData?.first_name ?? "",
      lastName: initialData?.last_name ?? "",
      email: initialData?.email ?? "",
      phoneNumber: initialData?.phone_number ?? "",
      address: initialData?.address ?? "",
      profession: initialData?.profession ?? "",
      qualification: initialData?.qualification.substring(12) ?? "",
      sessionFee: initialData?.session_fee ?? 0,
      subject: initialData?.subject ?? "",
      classes: initialData?.classrooms?.map(String) ?? [],
    },
  });

  console.log(initialData?.qualification);
  console.log(initialData?.qualification.substring(12));
  const multiSelectRef = useRef<MultiSelectRef>(null);

  useEffect(() => {
    fetchClassesNotAssignedMentor();
    if (isOpen) {
      setIsSubmitting(false);
      if (initialData) {
        
        form.reset({
          title: TitleEnum[initialData.title as keyof typeof TitleEnum],
          firstName: initialData.first_name,
          lastName: initialData.last_name,
          email: initialData.email,
          phoneNumber: initialData.phone_number,
          address: initialData.address,
          profession: initialData.profession,
          qualification: initialData.qualification.substring(12),
          sessionFee: initialData.session_fee,
          subject: initialData.subject,
          mentorImage: initialData.mentor_image,
        });

        // set classes to multi-select from the initial data during update
        const existingClassIds = new Set(classes.map(cls => cls.class_room_id));

        const promisesToFetch = initialData.classrooms.filter(id => !existingClassIds.has(id))
          .map(id => fetchClassroomById(id));

        Promise.all(promisesToFetch)
          .then(fetchedClassrooms => {
            const validFetchedClasses = fetchedClassrooms.filter(Boolean);
            const finalClasses = [...classes, ...validFetchedClasses];
            setClasses(finalClasses);

            if (multiSelectRef.current) {
              multiSelectRef.current.setSelectedValues(
                initialData.classrooms.map(String)
              );
            }
          })
          .catch(error => {
            console.error("Error fetching classrooms:", error);
          });


      } else {
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
    }

  }, [isOpen, initialData, form, multiSelectRef]);

  async function fetchClassroomById(classroomId: number) {
    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const response = await fetch(`${BACKEND_URL}/academic/classroom/${classroomId}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error(`Failed to fetch classroom with ID ${classroomId}`);

      const classroom = await response.json();
      return classroom;

    } catch (error) {
      console.error("Error fetching classroom:", error);
      return null;
    }
  }


  async function fetchClassesNotAssignedMentor() {
    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const response = await fetch(`${BACKEND_URL}/academic/classroom/unassigned`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) throw new Error(`Failed to fetch classrooms`);

      const classrooms = await response.json();
      setClasses(classrooms);

    } catch (error) {
      console.error("Error fetching classes:", error);
    }

  }


  async function onSubmit(data: UnifiedFormValues) {
    setIsSubmitting(true);

    const token = await getToken({ template: "skillmentor-auth-frontend" });

    try {
      const titleKey = Object.keys(TitleEnum).find(
        (key) => TitleEnum[key as keyof typeof TitleEnum] === data.title);


      const mentor = {
        title: titleKey,
        first_name: data.firstName,
        last_name: data.lastName,
        email: data.email,
        phone_number: data.phoneNumber,
        address: data.address,
        profession: data.profession,
        qualification: `Tutor since ${data.qualification}`,
        session_fee: data.sessionFee,
        subject: data.subject,
        classrooms: data.classes.map(Number),
      }
      const formData = new FormData();
      const mentorDataBlob = new Blob([JSON.stringify(mentor)], { type: 'application/json' });
      formData.append("mentor", mentorDataBlob);

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
      setIsSubmitting(false);
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
                          {Object.values(TitleEnum).map((title) => (
                            <SelectItem key={title} value={title}>
                              {title}
                            </SelectItem>
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
                          ref={multiSelectRef}
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
                  <Button 
                  type="submit"
                  disabled={isSubmitting}
                  >
                    {isSubmitting ? mode === "create" ? "Saving..." : "Updating..." : mode === "create" ? "Save" : "Update"}
                    </Button>
                </DialogFooter>
              </form>
            </Form>
          </div>
        </ScrollArea>
      </DialogContent>

    </Dialog>
  )

}