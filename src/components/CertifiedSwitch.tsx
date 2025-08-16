import { useState } from 'react';
import { Switch } from "@/components/ui/switch";
import AlertConfirmationDialog from './AlertConfirmationDialog';
import { BACKEND_URL } from '@/config/env';

type CertifiedSwitchProps = {
    initialState: boolean;
    mentorId: number;
};

export function CertifiedSwitch({ initialState, mentorId }: CertifiedSwitchProps) {
    const [isCertified, /**setIsCertified**/] = useState(initialState);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isRevoking, setIsRevoking] = useState(false);

    async function revokeCertification() {
        setIsRevoking(true);
        const url = `${BACKEND_URL}/academic/mentor?id=${mentorId}&certified=${!isCertified}`;
        console.log(url);
        //Todo: Call API to revoke certification

        // setIsCertified(isCertified => !isCertified);
        setIsDialogOpen(false);
        setIsRevoking(false); // Reset revoking state after operation
    };

    return (
        <div className="flex items-center justify-center">
            <Switch
                checked={isCertified}
                onCheckedChange={() => setIsDialogOpen(true)}
            />

            <AlertConfirmationDialog
                isOpen={isDialogOpen}
                onOpenChange={(open) => !isRevoking && setIsDialogOpen(open)}
                onConfirm={revokeCertification}
                isDisabledButton={isRevoking}
                buttonName={isRevoking ? "Revoking" : "Revoke"}
                title="Revoke Certification"
                description="Are you sure you want to revoke this mentor's certification?"
            />
        </div>
    );
}