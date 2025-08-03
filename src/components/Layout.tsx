import { Footer } from "./Footer";
import { Navigation } from "./Navigation";
import type { ReactNode } from "react";
import { Toaster } from "./ui/sonner";

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <>
      <section className="min-h-screen flex flex-col">
        <Navigation />
        <main>{children}</main>
        <Footer />
      </section>
      <Toaster richColors closeButton duration={1700} />
    </>
  );
}
