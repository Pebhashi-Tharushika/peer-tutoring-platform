import { useState } from 'react';
import { Switch } from "@/components/ui/switch";
import AlertConfirmationDialog from './AlertConfirmationDialog';
import { BACKEND_URL } from '@/config/env';

type CertifiedSwitchProps = {
    initialState: boolean;
    mentorId: number;
};

export function CertifiedSwitch({ initialState, mentorId }: CertifiedSwitchProps) {
    const [isCertified, setIsCertified] = useState(initialState);
    const [isDialogOpen, setIsDialogOpen] = useState(false);

    async function revokeCertification() {

        const url = `${BACKEND_URL}/academic/mentor?id=${mentorId}&certified=${!isCertified}`;
        //Todo: Call API to revoke certification

        setIsCertified(isCertified => !isCertified);
        setIsDialogOpen(false);
    };

    return (
        <div className="flex items-center justify-center">
            <Switch
                checked={isCertified}
                onCheckedChange={() => setIsDialogOpen(true)}
            />

            <AlertConfirmationDialog
                isOpen={isDialogOpen}
                onOpenChange={setIsDialogOpen}
                onConfirm={revokeCertification}
                title="Revoke Certification"
                description="Are you sure you want to revoke this mentor's certification?"
            />
        </div>
    );
}