import { twMerge } from 'tailwind-merge';

/**
 *
 * @param {{letter: string, className: string}} props
 * @returns
 */
export default function ConversationPlaceholder({ letter, className }) {
  return (
    <div className={twMerge('border-1 flex w-[200px] items-center gap-4 rounded-lg border bg-gray-50 p-2', className)}>
      <span className='flex h-10 w-10 items-center justify-center rounded-full bg-gray-200 text-gray-500'>
        {letter}
      </span>
      <div className='flex flex-col gap-1'>
        <span className='h-2 w-24 rounded-lg bg-gray-200'></span>
        <span className='h-2 w-20 rounded-lg bg-gray-200'></span>
      </div>
    </div>
  );
}
