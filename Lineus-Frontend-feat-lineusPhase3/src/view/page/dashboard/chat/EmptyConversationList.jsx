import { useTranslation } from 'react-i18next';
import ConversationPlaceholder from './ConversationPlaceholder';

export default function EmptyConversationList() {
  const { t } = useTranslation();
  return (
    <div className='flex h-full flex-1 flex-col items-center justify-center gap-4'>
      <ConversationPlaceholder letter='SF' />
      <ConversationPlaceholder letter='VN' className='ms-4' />
      <ConversationPlaceholder letter='MS' className='me-4' />
      <p>{t('chat-room.no-conversations-yet')}</p>
    </div>
  );
}
