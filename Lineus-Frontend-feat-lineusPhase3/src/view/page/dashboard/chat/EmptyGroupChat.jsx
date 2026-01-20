import { useTranslation } from 'react-i18next';
import { MessageIcon } from '../../../../components/common/Icon';

/**
 * @returns {JSX.Element}
 */
export default function EmptyGroupChat() {
  const { t } = useTranslation();
  return (
    <div className='my-auto flex flex-1 flex-col items-center justify-center gap-4'>
      <MessageIcon />
      <h4 className='text-bold mt-4'>{t('chat-room.welcome-msg')}</h4>
      <p className='mt-4 w-[50%] text-center text-sm text-gray-400'>{t('chat-room.welcome-instruction')}</p>
    </div>
  );
}
