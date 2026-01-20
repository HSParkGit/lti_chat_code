import Modal from '../../Modal';
import { useTranslation } from 'react-i18next';

const CreateDialog = ({
  open,
  onClose,
  dialogPosition,
  title,
  description,
  className = 'w-[33rem] rounded-lg',
  children,
  closeIcon = '',
  renderIcons = () => {},
}) => {
  const { t } = useTranslation();
  return (
    <Modal
      open={open}
      onClose={onClose}
      CloseIcon={closeIcon}
      modalClassname='relative'
      className={className}
      position={dialogPosition}
    >
      <div className='flex justify-between rounded-t-xl border-b bg-neutral400 p-6'>
        <div className=''>
          <h2 className='break-all text-lg font-bold text-slate-800'>{title}</h2>
          {description && <p className='break-all text-sm text-secondary400'>{description}</p>}
        </div>
        {renderIcons()}
      </div>
      <div className='flex flex-col px-6 pb-6 pt-2'>{children}</div>
    </Modal>
  );
};

export default CreateDialog;
