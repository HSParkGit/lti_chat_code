import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';

const useTranslatedDailyDateFormat = () => {
  const { t } = useTranslation();
  const dayFormat = t('dateFormat.calendar.daily');
  return (date) => {
    const dateObj = dayjs(date);
    const formattedDate = dateObj.format(dayFormat);
    const day = dateObj.format('dddd');
    const translatedDay = t(`days.${day}`);
    return formattedDate.replace(day, translatedDay);
  };
};

export default useTranslatedDailyDateFormat;
