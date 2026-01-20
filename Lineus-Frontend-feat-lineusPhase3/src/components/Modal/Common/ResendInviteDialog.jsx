import { LetterIcon, RedTrashIcon, TrashIcon } from '@/components/common/Icon';
import Modal from '../../Modal';
import ButtonWithIcon from '@/components/common/ButtonWithIcon';
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';

const ResendInviteDialog = ({
  isOpen,
  onClose,
  onResend,
  studentName,
  description,
  isLoading,
  className = 'w-[25rem]',
}) => {
  const { t } = useTranslation();
  return (
    <Modal onClose={onClose} open={isOpen} CloseIcon={''} className={className}>
      <div className='w-[25rem] space-y-3'>
        <div className='flex h-12 w-12 items-center justify-center rounded-full bg-[#E4FFEA] transition'>
          <LetterIcon />
        </div>
        <section className='space-y-4 text-left'>
          <h2 className='text-lg font-bold text-[#1E293B]'>{t('resendInvite.title')}</h2>
          <p className='text-sm text-[#475569]'>
            <span dangerouslySetInnerHTML={{ __html: description }} />
          </p>
          <p className='text-sm font-semibold text-[#475569]'>
            {t('resendInvite.notReceived')}{' '}
            <span className='text-green-600 underline'>{t('resendInvite.resendButton')}</span>
          </p>
        </section>

        <div className='flex justify-end space-x-4 text-sm'>
          <ButtonWithIcon
            label={t('common.cancel')}
            variant='btn-bordered'
            onClick={onClose}
            icon={faXmark}
            iconPosition='right'
          />
          <ButtonWithIcon
            label={t('common.done')}
            variant='btn-success'
            onClick={onResend}
            icon={faCheck}
            disabled={isLoading}
            iconPosition='right'
          />
        </div>
      </div>
    </Modal>
  );
};

export default ResendInviteDialog;
