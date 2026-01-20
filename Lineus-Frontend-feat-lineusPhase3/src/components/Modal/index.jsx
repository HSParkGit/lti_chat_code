import { Dialog, DialogBackdrop, DialogPanel, DialogTitle } from '@headlessui/react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClose } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';

// Implement Later
const Modal = ({
  title = '',
  children,
  className = 'w-[33rem]',
  open,
  onClose,
  CloseIcon,
  shouldCloseIcon = true,
  closeButtonClassName,
  modalClassname = 'relative p-6',
  titleClassname = '',
  position = 'normal',
}) => {
  const { t } = useTranslation();

  const backDropPositions = {
    end: ' bg-gray-500/75 inset-y-0 right-0 w-[40%] max-w-2xl',
    normal: 'bg-gray-500/75 inset-0',
  };

  return (
    <Dialog open={open} onClose={onClose} className='relative'>
      <DialogBackdrop
        transition
        className='bg-gray-500/75 transition-opacity data-[closed]:opacity-0 data-[enter]:duration-300 data-[leave]:duration-200 data-[enter]:ease-out data-[leave]:ease-in'
      />
      <div className={twMerge('fixed overflow-y-auto', backDropPositions[position])}>
        <div className={twMerge('mr-5 flex min-h-full items-center justify-center p-4 text-center sm:p-0')}>
          <DialogPanel
            transition
            className={twMerge(
              'relative w-full transform rounded-3xl bg-white text-left shadow-xl transition-all data-[closed]:translate-y-4 data-[closed]:opacity-0 data-[enter]:duration-300 data-[leave]:duration-200 data-[enter]:ease-out data-[leave]:ease-in sm:my-8 data-[closed]:sm:translate-y-0 data-[closed]:sm:scale-95',
              className
            )}
          >
            <button className={twMerge('absolute right-5 top-4 z-50', closeButtonClassName)} onClick={onClose}>
              {shouldCloseIcon &&
                (CloseIcon !== undefined ? CloseIcon : <FontAwesomeIcon icon={faClose} className='mt-4 h-5 w-5' />)}
            </button>
            <div className={modalClassname}>
              {title && <DialogTitle className={titleClassname}>{t(title)}</DialogTitle>}
              {children}
            </div>
          </DialogPanel>
        </div>
      </div>
    </Dialog>
  );
};

export default Modal;
