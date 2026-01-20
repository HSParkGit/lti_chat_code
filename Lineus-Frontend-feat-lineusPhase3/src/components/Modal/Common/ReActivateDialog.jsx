import { RedBlockUser, RedTrashIcon, TrashIcon, UserIcon } from '@/components/common/Icon';
import Modal from '../../Modal';
import ButtonWithIcon from '@/components/common/ButtonWithIcon';
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';

const ReActivateDialog = ({ isOpen, onClose, onActivate, title, label, className = 'w-[25rem]' }) => {
  const { t } = useTranslation();
  return (
    <Modal onClose={onClose} open={isOpen} CloseIcon={''} className={className}>
      <div className='w-[25rem] space-y-3'>
        <div className='flex h-12 w-12 items-center justify-center rounded-full bg-green-50 transition hover:bg-green-100'>
          <UserIcon />
        </div>
        <section className='space-y-4 text-left'>
          <h2 className='text-lg font-bold text-[#1E293B]'>{title}</h2>
          <p className='text-[#475569]'>{label}</p>
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
            label={t('deactivate.activate')}
            variant='btn-success'
            onClick={onActivate}
            icon={faCheck}
            iconPosition='right'
          />
        </div>
      </div>
    </Modal>
  );
};

export default ReActivateDialog;
