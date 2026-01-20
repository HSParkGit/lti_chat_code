import { useState } from 'react';
import { Tab, TabGroup, TabList } from '@headlessui/react';
import { twMerge } from 'tailwind-merge';
import ChatInterface from './ChatInterface';
import { useTranslation } from 'react-i18next';
import { useChatType } from './providers/useChatTypeProvider';

const TABS = [
  { title: 'chat-room.all', value: 'all' },
  { title: 'chat-room.unread', value: 'unread' },
];

export default function ChatSection() {
  const { t } = useTranslation();
  const { messageType, setMessageType } = useChatType();

  return (
    <TabGroup
      as='div'
      selectedIndex={messageType}
      onChange={setMessageType}
      className='flex h-full min-h-0 w-full flex-1 flex-col'
    >
      <TabList className='flex flex-nowrap'>
        {TABS.map((tab) => (
          <Tab
            key={tab.title}
            className={twMerge(
              `border-b-2 px-3 py-2 text-sm/6 font-semibold text-slate-700 data-[selected]:border-b-slate-800 data-[focus]:outline-1 focus:outline-none`,
              tab.className
            )}
          >
            {tab.icon}
            <p>{t(tab.title)}</p>
          </Tab>
        ))}
      </TabList>

      <div className='flex min-h-0 w-full flex-1 pb-6'>
        <ChatInterface messageType={TABS[messageType].value} />
      </div>
    </TabGroup>
  );
}
