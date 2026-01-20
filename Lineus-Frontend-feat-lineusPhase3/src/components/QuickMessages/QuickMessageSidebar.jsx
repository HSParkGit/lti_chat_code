import { useTranslation } from 'react-i18next';

import RightSidebar from '../RightSidebar';
import QuickMessages from '.';
import { CANVAS_URL } from '../../config/general';
import ExternalLink from '../common/ExternalLink';
import RightSidebarTitle from '../RightSidebar/Title';

export const QuickMessageForSidebar = ({ quickMessages, onClose }) => {
  const { t } = useTranslation();

  const titleComponent = (
    <ExternalLink url={`${CANVAS_URL}/conversations#filter=type=inbox`} className='hover:underline'>
      <span>{t('Quick_Messages')}</span>
    </ExternalLink>
  );
  return (
    <>
      <RightSidebarTitle
        title={titleComponent}
        subtitle={t('message_desc')}
        badge={`${quickMessages?.length || 0}`}
        handleClick={onClose}
      />
      <QuickMessages quickMessages={quickMessages} />
    </>
  );
};

const QuickMessageSidebar = ({ quickMessages, isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <RightSidebar isOpen={isOpen} onClose={onClose}>
      <QuickMessageForSidebar quickMessages={quickMessages} onClose={onClose} />
    </RightSidebar>
  );
};

export default QuickMessageSidebar;
