import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle
} from "./ui/alert-dialog";

type AlertConfirmationDialogProps = {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    onConfirm: () => Promise<void>;
    isDisabledButton: boolean;
    buttonName: string;
    title: string;
    description: string;
};

export default function AlertConfirmationDialog({
    isOpen,
    onOpenChange,
    onConfirm,
    isDisabledButton,
    buttonName,
    title,
    description,
}: AlertConfirmationDialogProps) {
    return (
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>{title}</AlertDialogTitle>
                    <AlertDialogDescription>{description}</AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel >Cancel</AlertDialogCancel>
                    <AlertDialogAction 
                        onClick={onConfirm}
                        disabled={isDisabledButton}
                    >
                        {buttonName}
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}


