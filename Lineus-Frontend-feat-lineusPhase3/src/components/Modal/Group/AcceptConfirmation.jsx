import React from 'react';
import CreateDialog from '@/components/Modal/Common/CreateDialog.jsx';
import { useTranslation } from 'react-i18next';
import ButtonWithIcon from '@/components/common/ButtonWithIcon.jsx';
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';
import service from '@/api/apiService.js';
import toast from 'react-hot-toast';
import useAcceptInvite from '@/hooks/group/groupMember/useAcceptInvite.js';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import { useUserStore } from '@/store/useUserStore.js';
import { USER_ROLE } from '@/config/constants/user.js';

const AcceptConfirmation = ({ open, onClose, url }) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const { user } = useUserStore();
  const navigate = useNavigate();
  const { mutateAsync: acceptGroupInvite } = useAcceptInvite();
  const inviteCode = url.split('?')[1];
  const redirectUrl = url.split(window.location.origin);
  const handleAccept = async () => {
    try {
      await acceptGroupInvite(inviteCode, {
        onSuccess: (data) => {
          queryClient.invalidateQueries(['groupsDashboard']);
          if (user.userRole === USER_ROLE.STUDENT) {
            if (data?.data?.errorCode) {
              const groupId = data.data?.errorMessage?.split(':')[1];
              console.log(data.data, groupId);

              navigate(`/dashboard/group/${groupId}?${inviteCode}`);
            } else {
              navigate(`/dashboard/group/${data.data?.data?.groupId}?${inviteCode}`);
            }
          } else {
            navigate(redirectUrl[1]);
          }
          if (data?.data?.errorCode) {
            toast.error(t('group.invite.already_joined'));
          } else {
            toast.success(t('group.invite.accept_success'));
          }
        },
        onError: (error) => {
          console.log(error);
          toast.error(error.message.split(':')[1]);
        },
      });
      onClose();
    } catch (e) {
      if (user.userRole === USER_ROLE.STUDENT) {
        console.log(e);
        navigate(`/dashboard/group?${inviteCode}`);
      } else {
        navigate(redirectUrl[1]);
      }
    } finally {
      onClose();
    }
  };
  return (
    <CreateDialog open={open} onClose={onClose} title={t('group.invite.title')} className={'w-[33rem] rounded'}>
      <p>{t('group.invite.accept_confirm')}</p>
      <div className={'my-4 flex justify-end gap-4'}>
        <ButtonWithIcon
          label={t('common.cancel')}
          variant='btn-bordered'
          onClick={onClose}
          icon={faXmark}
          iconPosition='right'
        />
        <ButtonWithIcon
          label={t('common.accept')}
          variant='btn-success'
          onClick={handleAccept}
          icon={faCheck}
          iconPosition='right'
        />
      </div>
    </CreateDialog>
  );
};
export default AcceptConfirmation;
