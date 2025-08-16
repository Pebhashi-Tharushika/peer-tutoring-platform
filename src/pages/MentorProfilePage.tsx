import { Button } from '@/components/ui/button';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { BACKEND_URL } from '@/config/env';
import { MentorProfile, TitleEnum } from '@/lib/types';
import { CircleChevronLeft } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router';
import { toast } from 'sonner';

export default function MentorProfilePage() {
    const navigate = useNavigate();
    const { id } = useParams();
    const [mentorProfile, setMentorProfile] = useState<MentorProfile | null>(null);

    async function fetchMentorProfleData() {
        try {
            const response = await fetch(`${BACKEND_URL}/academic/mentor/profile/${id}`);
            if (!response.ok) {
                const errorText = await response.text();
                console.error("Failed to fetch mentor data:", errorText);
                toast.error("Error", { description: "Failed to fetch mentor data. Please try again later." });
                navigate("/");
                return;
            }
            const mentorData: MentorProfile = await response.json();
            setMentorProfile(mentorData);
        } catch (error) {
            console.error("Error fetching mentor data:", error);
        }
    }

    useEffect(() => {
        fetchMentorProfleData();
    }, [id]);

    return (
        <div className="space-y-10 max-w-[85vw] mx-auto py-10">
            <div className='flex justify-between items-center border-b-1 border-gray-300 pb-1'>
                <Button 
                onClick={() => navigate("/")}
className='bg-transparent border-none shadow-none text-gray-600 font-bold transform transition-transform duration-300 hover:scale-105 hover:text-black  hover:bg-transparent hover:cursor-pointer'
                >
                    <CircleChevronLeft />
                    Back
                </Button>
            </div >
            {mentorProfile?.mentor &&
                <div className='grid grid-cols-1 lg:grid-cols-12 gap-6'>
                    <div className='lg:col-span-4 p-4 flex justify-center items-center'>
                        <img
                            src={mentorProfile.mentor.mentor_image}
                            alt={mentorProfile.mentor.first_name}
                            className="w-[90%] h-auto lg:h-full lg:w-auto object-cover shadow-lg border-2 border-gray-200 rounded-2xl p-2 bg-gradient-to-tr from-primary to-35%"
                        />
                    </div>
                    <div className='lg:col-span-8 space-y-4'>
                        <h1 className="text-3xl text-gray-800 font-bold">{TitleEnum[mentorProfile.mentor.title as keyof typeof TitleEnum] + " " + mentorProfile.mentor.first_name + " " + mentorProfile.mentor.last_name}</h1>
                        <p className='text-muted-foreground'>{mentorProfile.mentor.subject}</p>
                        <div className="bg-muted p-6 rounded-xl shadow-lg w-full max-w-2xl hover:shadow-xl transition-shadow duration-300">
                            <h2 className="text-xl font-bold mb-4 text-gray-800">Classroom Enrollment</h2>
                            <Table>
                                <TableHeader>
                                    <TableRow className="border-none">
                                        <TableHead className="font-bold text-gray-700">Classrooms</TableHead>
                                        <TableHead className="font-bold text-gray-700">Enrolled Students</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {mentorProfile.mentor_classes.map((classroom) => (
                                        <TableRow key={classroom.classroom_name} className="border-none">
                                            <TableCell className=" px-4 py-2">{classroom.classroom_name}</TableCell>
                                            <TableCell className="px-4 py-2 text-right pr-16">{classroom.session_count}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </div>
                    </div>
                </div>
            }
        </div>
    )

}

