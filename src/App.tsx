import { Calendar } from "@/components/ui/calendar"
import { useState } from "react"

function App() {
  const [date, setDate] = useState<Date>();
  return (
    <>
      <Calendar
        mode="single"
        selected={date}
        onSelect={setDate}
        className="rounded-lg border"
      />
    </>
  )
}

export default App
