import { RedTrashIcon, TrashIcon } from '@/components/common/Icon';
import Modal from '../../Modal';
import ButtonWithIcon from '@/components/common/ButtonWithIcon';
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { FaCheck, FaCheckCircle } from 'react-icons/fa';
import { GoCheckCircle } from 'react-icons/go';

const SuccessDialog = ({ isOpen, onClose, title, label }) => {
  const { t } = useTranslation();
  return (
    <Modal onClose={onClose} open={isOpen} CloseIcon={''} className={'w-[30rem]'}>
      <div className='space-y-5'>
        <div className='flex h-12 w-12 items-center justify-center rounded-full bg-green-100 transition'>
          <GoCheckCircle className='h-5 w-5 font-bold text-[#27925C]' />
        </div>
        <section className='space-y-2 text-left'>
          <h2 className='text-xl font-bold text-[#1E293B]'>{title}</h2>
          <p className='text-[#475569]'>{label}</p>
        </section>

        <div className='flex justify-end space-x-4 text-sm'>
          <ButtonWithIcon
            label={t('Done')}
            variant='btn-success'
            onClick={onClose}
            icon={faCheck}
            iconPosition='right'
            btnStyles='!py-2.5'
          />
        </div>
      </div>
    </Modal>
  );
};

export default SuccessDialog;
