import moment from 'moment-timezone';
import { momentLocalizer } from 'react-big-calendar';
import { t } from 'i18next';
import { useTranslation } from 'react-i18next';

export const checkIsToday = (date) => {
  return moment().isSame(date, 'day');
};
export const getWeekDays = (date) => {
  const startOfWeek = moment(date).startOf('week');
  return ['Time', ...Array.from({ length: 7 }, (_, i) => moment(startOfWeek).add(i, 'days'))];
};

const getStartMoment = (type) => moment().startOf(type);

const getEndMoment = (type) => moment().endOf(type);

export const startDate = getStartMoment('day');

export const endDate = getEndMoment('day');

export const localizer = momentLocalizer(moment);

export const startOfMonthString = getStartMoment('month').toISOString();

export const endOfMonthString = getEndMoment('month').toISOString();

export const toDate = (date) => moment(date).toDate();

export const getCurrentDate = (format) => moment().format(format);

export const getDateFormat = ({ date, format }) => {
  return date ? moment(date).format(format) : 'Not set';
};

export const getDayFormat = (date) => (date ? moment(date).format('YYYY MMM DD') : '-');

export const getFormattedDateTime = (date) => {
  const monthName = moment(date).format('MMM');
  const day = moment(date).format('D');
  const time = moment(date).format('h:mm A');
  return `${monthName} ${day} ${time}`;
};

export const getFormattedDateTimeWithYear = (date) => {
  const monthName = moment(date).format('MMM');
  const day = moment(date).format('D');
  const year = moment(date).format('YYYY');
  const time = moment(date).format('h:mm A');
  return `${monthName} ${day} ${year} ${time}`;
};

export const getTimeFormat = (date) => {
  return moment(date).format('h:mm A');
};

export const getToday = () => {
  return moment().format('YYYY-MM-DD');
};

export const getDateISOString = (date) => {
  return moment(date).toISOString();
};

export const getTimezoneLists = () => {
  const now = new Date();
  return moment.tz.names().map((tz) => {
    const currentOffset = moment.tz(now, tz).format('Z');
    const parts = tz.split('/');
    const city = parts[parts.length - 1].replace(/_/g, ' ');
    return {
      value: tz,
      name: `${city} (${currentOffset})`,
    };
  });
};

export const getMonthsChunk = (referenceMoment, count = 12) => {
  const months = [];
  let currentMoment = referenceMoment.clone();

  for (let i = 0; i < count; i++) {
    months.push({
      name: currentMoment.format('MMMM YYYY'), // Placeholder for translation
      value: currentMoment.format('YYYY-MM'),
    });
    currentMoment.subtract(1, 'month'); // Go backward in time
  }

  return {
    months: months,
    nextStart: currentMoment.format('YYYY-MM'),
    hasMore: true,
  };
};

export const getTodayMonth = () => {
  return `${t(`months.${moment().format('MMMM')}`)} ${moment().format('YYYY')}`;
};

export const getMinDate = (startDate) => {
  const date = startDate ? new Date(new Date(startDate).setDate(new Date(startDate).getDate() + 1)) : '';
  return startDate ? moment(date).format('YYYY-MM-DD') : '';
};

export const getDateTime = (date) => {
  return moment(date).format('YYYY-MM-DD HH:mm:ss');
};
