import ButtonWithIcon from '@/components/common/ButtonWithIcon';
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { twMerge } from 'tailwind-merge';
const DialogFooter = ({
  onClose,
  saveLabel,
  btnStyles,
  cancelLabel,
  saveDisable,
  saveType = 'submit',
  onSubmit = () => {},
  className,
  submitLabel,
}) => {
  const { t } = useTranslation();

  const submitLabels = {
    save: t('common.save'),
  };
  return (
    <div className={twMerge(`flex justify-end space-x-4`, className)}>
      <ButtonWithIcon
        label={t(cancelLabel || 'assignmentGroup.cancel')}
        variant='btn-bordered'
        onClick={onClose}
        icon={faXmark}
        iconPosition='right'
        btnStyles={btnStyles}
      />
      <ButtonWithIcon
        label={saveLabel || submitLabels[submitLabel]}
        variant='btn-success'
        iconPosition='right'
        type={saveType}
        icon={faCheck}
        btnStyles={btnStyles}
        onClick={onSubmit}
        disabled={saveDisable}
      />
    </div>
  );
};

export default DialogFooter;
