import { faXmark } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ExternalLink from '../common/ExternalLink';
import ProfilePhoto from '../common/ProfilePhoto';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { useTranslation } from 'react-i18next';
import { CANVAS_URL } from '../../config/general';
import { useQuickMessage } from '../../hooks/useQuickMessages';
import ErrorState from '../Error';
import Loader from '../Loader';
import { DATETIME_FORMAT_WITHOUT_SECONDS } from '../../config/constants/date';

dayjs.extend(relativeTime);

const QuickMessages = ({ quickMessages }) => {
  const { t } = useTranslation();

  const handleMessageClick = (message) => {
    // If message has a specific URL, use it, otherwise go to inbox
    if (message.url) {
      window.open(message.url, '_blank', 'noopener,noreferrer');
    } else {
      // Default to inbox view
      window.open(`${CANVAS_URL}/conversations#filter=type=inbox`, '_blank', 'noopener,noreferrer');
    }
  };

  return (
    <div className='flex flex-col gap-4 px-6 pb-6'>
      {quickMessages?.length > 0 ? (
        <div className='space-y-2'>
          {quickMessages.map((message, index) => (
            <div key={message.id}>
              <a
                href={message.url || `${CANVAS_URL}/conversations#filter=type=inbox`}
                target='_blank'
                rel='noopener noreferrer'
                className='block rounded-lg px-2 py-1.5 transition-colors hover:bg-gray-50'
                onClick={(e) => {
                  e.preventDefault();
                  handleMessageClick(message);
                }}
              >
                <div className='flex items-start gap-2'>
                  <ProfilePhoto className='mt-0.5' size={32} imageUrl={message?.avatarUrl} />
                  <div className='min-w-0 flex-1'>
                    <div className='truncate text-sm font-medium'>
                      {message.participants?.[0]?.fullName || 'Unknown'}
                    </div>
                    <div className='truncate text-sm text-gray-500'>{message.subject || 'No subject'}</div>
                    <div className='text-xs text-gray-400'>
                      {dayjs(message.lastMessageAt).format(DATETIME_FORMAT_WITHOUT_SECONDS)}
                    </div>
                  </div>
                </div>
              </a>
              {index + 1 !== quickMessages.length && <hr className='my-2 h-px border-0 bg-gray-100' />}
            </div>
          ))}
        </div>
      ) : (
        <p className='text-center text-gray-400'>{t('no_message_available')}</p>
      )}
    </div>
  );
};

export default QuickMessages;
