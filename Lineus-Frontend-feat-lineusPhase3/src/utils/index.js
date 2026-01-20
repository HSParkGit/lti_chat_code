import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';
import {
  DATETIME_FORMAT,
  DATETIME_FORMAT_WITHOUT_SECONDS,
  DATETIME_FORMAT_WITH_T,
  DATE_FORMAT,
} from '../config/constants/date';
import { USER_ROLE } from '../config/constants/user';

export const formatDueDate = (isoString) => {
  const { t } = useTranslation();
  if (!isoString) return t('no_due_date');
  return dayjs(isoString).format(DATETIME_FORMAT_WITHOUT_SECONDS);
};

export const getRoleNameByRole = (role, t = () => {}) => {
  if (!role) return '';

  switch (role.toUpperCase()) {
    case USER_ROLE.ADMIN:
      return t('role_name.admin');
    case USER_ROLE.PROFESSOR:
      return t('role_name.professor');
    default:
      return t('role_name.student');
  }
};

export const formatDate = (date) => {
  if (!date) return '-';
  return dayjs(date).format(DATE_FORMAT);
};
export const formatDateTime = (date) => {
  if (!date) return '-';
  return dayjs(date).format(DATETIME_FORMAT_WITH_T);
};

export const clickOutside = (ref, onOutsideClick) => {
  const handleClick = (event) => {
    if (ref.current && !ref.current.contains(event.target) && !event.defaultPrevented) {
      onOutsideClick?.(event);
    }
  };

  document.addEventListener('click', handleClick, true);

  return () => {
    document.removeEventListener('click', handleClick, true);
  };
};

export const formatDateWithTranslation = (date, t, i18n, format = 'discussionDetails.dateFormat') => {
  return dayjs(date)
    .locale(i18n.language === 'kr' ? 'ko' : i18n.language)
    .format(t(format));
};

export const turnDateToISOString = (date) => {
  if (date) {
    const newDate = new Date(date);
    return newDate.toISOString();
  }
};

export const getWordCountFromHtmlOrText = (htmlOrText) => {
  const plainText = htmlOrText
    .replace(/<[^>]*>/g, ' ')
    .replace(/\s+/g, ' ')
    .trim();
  return plainText ? plainText.split(' ').length : 0;
};

// Helper function to convert UTC to local datetime string
export const utcToLocalDatetimeString = (utcDateString) => {
  if (!utcDateString) return '';

  const date = new Date(utcDateString);

  // Adjust for timezone offset
  const localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000);

  // Format to YYYY-MM-DDTHH:mm (without seconds)
  return localDate.toISOString().slice(0, 16);
};

export const inputBoxAutoHeightAdjust = (ref, height = '42px', maxHeight = 180) => {
  ref.current.style.height = height;
  ref.current.style.height = Math.min(ref.current.scrollHeight, maxHeight) + 'px'; // Grow, but not beyond max
};
