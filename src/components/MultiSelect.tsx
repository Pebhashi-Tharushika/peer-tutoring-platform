import * as React from "react";

import {
	CheckIcon,
	XCircle,
	ChevronDown,
	XIcon,
} from "lucide-react";

import { cn } from "@/lib/utils";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
	Popover,
	PopoverContent,
	PopoverTrigger,
} from "@/components/ui/popover";
import {
	Command,
	CommandEmpty,
	CommandGroup,
	CommandInput,
	CommandItem,
	CommandList,
} from "@/components/ui/command";
import { ScrollArea } from "./ui/scroll-area";

// badge styling
const multiSelectClasses = "m-1 transition-all duration-300 ease-in-out border-foreground/10 text-foreground bg-card hover:bg-card/80";

interface MultiSelectOption {
	label: string;
	value: string;
}

interface MultiSelectProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	options: MultiSelectOption[];
	onValueChange: (value: string[]) => void;
	defaultValue?: string[];
	placeholder?: string;
}


export interface MultiSelectRef {
	reset: () => void;
	getSelectedValues: () => string[];
	setSelectedValues: (values: string[]) => void;
	clear: () => void;
	focus: () => void;
}

export const MultiSelect = React.forwardRef<MultiSelectRef, MultiSelectProps>(
	(
		{
			options,
			onValueChange,
			defaultValue = [],
			placeholder = "Select options",
			...props
		},
		ref
	) => {
		// State to track the width of the trigger button for responsive popover
		const [buttonWidth, setButtonWidth] = React.useState(0);
		const [selectedValues, setSelectedValues] = React.useState<string[]>(defaultValue);
		const [isPopoverOpen, setIsPopoverOpen] = React.useState(false);
		const [searchValue, setSearchValue] = React.useState("");

		const prevDefaultValueRef = React.useRef<string[]>(defaultValue);

		// check if two arrays are equal
		const arraysEqual = React.useCallback(
			(a: string[], b: string[]): boolean => {
				if (a.length !== b.length) return false;
				const sortedA = [...a].sort();
				const sortedB = [...b].sort();
				return sortedA.every((val, index) => val === sortedB[index]);
			},
			[]
		);

		// reset the component to default values
		const resetToDefault = React.useCallback(() => {
			setSelectedValues(defaultValue);
			setIsPopoverOpen(false);
			setSearchValue("");
			onValueChange(defaultValue);
		}, [defaultValue, onValueChange]);

		const buttonRef = React.useRef<HTMLButtonElement>(null);

		React.useEffect(() => {
			if (buttonRef.current) {
				setButtonWidth(buttonRef.current.offsetWidth);
			}
		}, [isPopoverOpen, selectedValues]);

		React.useImperativeHandle(
			ref,
			() => ({
				reset: resetToDefault,
				getSelectedValues: () => selectedValues,
				setSelectedValues: (values: string[]) => {
					setSelectedValues(values);
					onValueChange(values);
				},
				clear: () => {
					setSelectedValues([]);
					onValueChange([]);
				},
				focus: () => {
					if (buttonRef.current) {
						buttonRef.current.focus();
						const originalOutline = buttonRef.current.style.outline;
						const originalOutlineOffset = buttonRef.current.style.outlineOffset;
						buttonRef.current.style.outline = "2px solid hsl(var(--ring))";
						buttonRef.current.style.outlineOffset = "2px";
						setTimeout(() => {
							if (buttonRef.current) {
								buttonRef.current.style.outline = originalOutline;
								buttonRef.current.style.outlineOffset = originalOutlineOffset;
							}
						}, 1000);
					}
				},
			}),
			[resetToDefault, selectedValues, onValueChange]
		);

		const [screenSize, setScreenSize] = React.useState<"mobile" | "tablet" | "desktop">("desktop");

		React.useEffect(() => {
			if (typeof window === "undefined") return;
			const handleResize = () => {
				const width = window.innerWidth;
				if (width < 640) {
					setScreenSize("mobile");
				} else if (width < 1024) {
					setScreenSize("tablet");
				} else {
					setScreenSize("desktop");
				}
			};
			handleResize();
			window.addEventListener("resize", handleResize);
			return () => {
				if (typeof window !== "undefined") {
					window.removeEventListener("resize", handleResize);
				}
			};
		}, []);

		const getAllOptions = React.useCallback((): MultiSelectOption[] => {
			return options;
		}, [options]);

		const getOptionByValue = React.useCallback(
			(value: string): MultiSelectOption | undefined => {
				const option = getAllOptions().find((option) => option.value === value);
				return option;
			},
			[getAllOptions]
		);

		const handleInputKeyDown = (
			event: React.KeyboardEvent<HTMLInputElement>
		) => {
			if (event.key === "Enter") {
				setIsPopoverOpen(true);
			} else if (event.key === "Backspace" && !event.currentTarget.value) {
				const newSelectedValues = [...selectedValues];
				newSelectedValues.pop();
				setSelectedValues(newSelectedValues);
				onValueChange(newSelectedValues);
			}
		};

		// Toggle option selection (remove already selected options & add ones if not selected)
		const toggleOption = (optionValue: string) => {
			const newSelectedValues = selectedValues.includes(optionValue)
				? selectedValues.filter((value) => value !== optionValue)
				: [...selectedValues, optionValue];
			setSelectedValues(newSelectedValues);
			onValueChange(newSelectedValues);
		};

		// Clear all selections
		const handleClear = () => {
			setSelectedValues([]);
			onValueChange([]);
		};

		// Toggle popover visibility
		const handleTogglePopover = () => {
			setIsPopoverOpen((prev) => !prev);
		};

		const toggleAll = () => {
			if (selectedValues.length === getAllOptions().length) {
				handleClear(); // Clear all selections
			} else {
				const allValues = getAllOptions().map((option) => option.value); // Select all options
				setSelectedValues(allValues);
				onValueChange(allValues);
			}
		};

		React.useEffect(() => {
			const resetOnDefaultValueChange = true;
			if (!resetOnDefaultValueChange) return;
			const prevDefaultValue = prevDefaultValueRef.current;
			if (!arraysEqual(prevDefaultValue, defaultValue)) {
				if (!arraysEqual(selectedValues, defaultValue)) {
					setSelectedValues(defaultValue);
				}
				prevDefaultValueRef.current = [...defaultValue];
			}
		}, [defaultValue, selectedValues, arraysEqual]);

		const getWidthConstraints = () => {
			const defaultMinWidth = screenSize === "mobile" ? "0px" : "200px";
			const effectiveMinWidth = defaultMinWidth;
			const effectiveMaxWidth = "100%";
			return {
				minWidth: effectiveMinWidth,
				maxWidth: effectiveMaxWidth,
				width: "100%",
			};
		};

		const widthConstraints = getWidthConstraints();

		React.useEffect(() => {
			if (!isPopoverOpen) {
				setSearchValue("");
			}
		}, [isPopoverOpen]);


		return (
			<Popover
				open={isPopoverOpen}
				onOpenChange={setIsPopoverOpen}
				modal={false}>
				<PopoverTrigger asChild>
					<Button
						ref={buttonRef}
						{...props}
						onClick={handleTogglePopover}
						className={cn(
							"flex p-1 rounded-md border min-h-10 h-auto items-center justify-between bg-inherit hover:bg-inherit [&_svg]:pointer-events-auto w-full",
							screenSize === "mobile" && "min-h-12 text-base",
						)}
						style={{
							...widthConstraints,
							maxWidth: `min(${widthConstraints.maxWidth}, 100%)`,
						}}>

						{/* if there is a selected option, display the badge. otherwise display placeholder */}
						{selectedValues.length > 0 ? (
							<div className="flex justify-between items-center w-full">
								<div className={cn("flex items-center gap-1 flex-wrap")} style={{ paddingBottom: "4px" }}>
									{selectedValues
										.map((value) => {
											const option = getOptionByValue(value);
											if (!option) {
												return null;
											}
											return (
												<Badge
													key={value}
													className={cn(
														multiSelectClasses,
														screenSize === "mobile" && "max-w-[120px] truncate",
														"flex-shrink-0 whitespace-nowrap [&>svg]:pointer-events-auto"
													)}
												>
													<span
														className={cn(
															screenSize === "mobile" && "truncate"
														)}>
														{option.label}
													</span>
													<XCircle
														className={cn("ml-2 h-4 w-4 cursor-pointer")}
														onClick={(event) => {
															event.stopPropagation();
															toggleOption(value);
														}}
													/>
												</Badge>
											);
										})
										.filter(Boolean)}
								</div>
								<div className="flex items-center justify-between">
									<XIcon
										className="h-4 mx-2 cursor-pointer text-muted-foreground"
										onClick={(event) => {
											event.stopPropagation();
											handleClear();
										}}
									/>
									<Separator
										orientation="vertical"
										className="flex min-h-6 h-full"
									/>
									<ChevronDown className="h-4 mx-2 cursor-pointer text-muted-foreground" />
								</div>
							</div>
						) : (
							<div className="flex items-center justify-between w-full mx-auto">
								<span className="text-sm text-muted-foreground mx-3">
									{placeholder}
								</span>
								<ChevronDown className="h-4 mx-2 cursor-pointer text-muted-foreground" />
							</div>
						)}
					</Button>
				</PopoverTrigger>
				<PopoverContent
					className={cn(
						"p-0"
					)}
					style={{
						width: buttonWidth,
						touchAction: "manipulation",
					}}
					align="start"
					onEscapeKeyDown={() => setIsPopoverOpen(false)}>
					<Command
						filter={(value, search) => {
							const option = options.find(o => o.value === value);
							if (option && option.label.toLowerCase().includes(search.toLowerCase())) {
								return 1; // Return a truthy value to keep the item
							}
							return 0; // Return a falsy value to hide the item
						}}
					>
						<CommandInput
							placeholder="Search..."
							onKeyDown={handleInputKeyDown}
							onValueChange={setSearchValue}
						/>
						<CommandList>
							<ScrollArea
								className={cn(
									"h-35 overscroll-behavior-y-contain",
									screenSize === "mobile" && "max-h-[50vh]"
								)}
							>
								<CommandEmpty>No results found.</CommandEmpty>

								{/* show select-all opion only when there’s no active search. */}
								{!searchValue && (
									<CommandGroup>
										<CommandItem
											key="all"
											onSelect={toggleAll}
											className="cursor-pointer">
											<div
												className={cn(
													"mr-2 flex h-4 w-4 items-center justify-center rounded-sm border border-primary",
													selectedValues.length === getAllOptions().length
														? "bg-primary text-primary-foreground" //checked select-all option
														: "opacity-50 [&_svg]:invisible" //unchecked select-all option
												)}>
												<CheckIcon className="h-4 w-4 text-primary-foreground" />
											</div>
											<span>Select All</span>
										</CommandItem>
									</CommandGroup>
								)}
								<Separator
									orientation="horizontal"
									className="flex max-w-11/12 h-0.5 m-auto"
								/>
								<CommandGroup>
									{options.map((option) => {
										const isSelected = selectedValues.includes(option.value);
										return (
											<CommandItem
												key={option.value}
												onSelect={() => toggleOption(option.value)}
												className={cn("cursor-pointer")}
												value={option.value}
											>
												<div
													className={cn(
														"mr-2 flex h-4 w-4 items-center justify-center rounded-sm border border-primary",
														isSelected
															? "bg-primary text-primary-foreground" // checked option
															: "opacity-50 [&_svg]:invisible"	// unchecked option
													)}>
													<CheckIcon className="h-4 w-4 text-primary-foreground" />
												</div>
												<span>{option.label}</span>
											</CommandItem>
										);
									})}
								</CommandGroup>
							</ScrollArea>
						</CommandList>
					</Command>
				</PopoverContent>
			</Popover>
		);
	}
);

MultiSelect.displayName = "MultiSelect";
export type { MultiSelectOption, MultiSelectProps };