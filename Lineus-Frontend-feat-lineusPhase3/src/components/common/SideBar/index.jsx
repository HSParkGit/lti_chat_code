import { twMerge } from 'tailwind-merge';

export function Sidebar({ children, className }) {
  return (
    <aside
      className={twMerge([
        `fixed inset-y-0 z-10 flex max-h-screen w-[16rem] flex-shrink-0 transform flex-col overflow-hidden border-r bg-[#F6F6F6] transition-all hover:overflow-y-auto lg:static lg:z-auto lg:shadow-none`,
        className,
      ])}
    >
      {children}
    </aside>
  );
}
