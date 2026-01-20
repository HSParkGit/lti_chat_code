// utils/dateUtils.js

/**
 * Converts API datetime format to datetime-local input format
 * @param {string} apiDateTime - DateTime in API format (YYYY-MM-DD HH:MM:SS)
 * @returns {string} DateTime in datetime-local format (YYYY-MM-DDTHH:MM)
 */
export const convertToDateTimeLocal = (apiDateTime) => {
  if (!apiDateTime) return '';
  return apiDateTime.replace(' ', 'T').slice(0, 16);
};

/**
 * Converts datetime-local input format to API format
 * @param {string} dateTimeLocal - DateTime in datetime-local format (YYYY-MM-DDTHH:MM)
 * @returns {string} DateTime in API format (YYYY-MM-DD HH:MM:SS)
 */
export const convertToAPIFormat = (dateTimeLocal) => {
  if (!dateTimeLocal) return '';
  return dateTimeLocal.replace('T', ' ') + ':00';
};

/**
 * Extracts date part from API datetime format
 * @param {string} apiDateTime - DateTime in API format (YYYY-MM-DD HH:MM:SS)
 * @returns {string} Date in YYYY-MM-DD format
 */
export const convertToDateOnly = (apiDateTime) => {
  if (!apiDateTime) return '';
  return apiDateTime.split(' ')[0];
};

/**
 * Converts date to API datetime format with 00:00:00 time
 * @param {string} date - Date in YYYY-MM-DD format
 * @returns {string} DateTime in API format (YYYY-MM-DD 00:00:00)
 */
export const convertDateToAPIFormat = (date) => {
  if (!date) return '';
  return date + ' 00:00:00';
};

/**
 * Adds specified number of days to a date
 * @param {string} dateString - Date in YYYY-MM-DD format
 * @param {number} days - Number of days to add
 * @returns {string} New date in YYYY-MM-DD format
 */
export const addDaysToDate = (dateString, days) => {
  if (!dateString || !days) return '';

  const date = new Date(dateString);
  date.setDate(date.getDate() + parseInt(days) - 1);
  return date.toISOString().split('T')[0];
};

/**
 * Gets the next day date
 * @param {string} dateString - Date in YYYY-MM-DD format
 * @returns {string} Next day date in YYYY-MM-DD format
 */
export const getNextDayDate = (dateString) => {
  if (!dateString) return '';

  const date = new Date(dateString);
  date.setDate(date.getDate() + 1);
  return date.toISOString().split('T')[0];
};

/**
 * Calculates duration between two dates (inclusive)
 * @param {string} startDateTime - Start date in API format
 * @param {string} endDateTime - End date in API format
 * @returns {number} Duration in days (minimum 1)
 */
export const calculateDuration = (startDateTime, endDateTime) => {
  if (!startDateTime || !endDateTime) return 1;

  const start = new Date(startDateTime);
  const end = new Date(endDateTime);

  const diffTime = end.getTime() - start.getTime();
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

  return Math.max(1, diffDays + 1);
};

/**
 * Validates if a date string is valid
 * @param {string} dateString - Date string to validate
 * @returns {boolean} True if valid date
 */
export const isValidDate = (dateString) => {
  if (!dateString) return false;

  const date = new Date(dateString);
  return date instanceof Date && !isNaN(date);
};

/**
 * Formats date for display
 * @param {string} dateString - Date string to format
 * @param {string} locale - Locale for formatting (default: 'en-US')
 * @returns {string} Formatted date string
 */
export const formatDateForDisplay = (dateString, locale = 'en-US') => {
  if (!dateString || !isValidDate(dateString)) return '';

  const date = new Date(dateString);
  return date.toLocaleDateString(locale);
};

/**
 * Gets current date in YYYY-MM-DD format
 * @returns {string} Current date
 */
export const getCurrentDate = () => {
  return new Date().toISOString().split('T')[0];
};

/**
 * Checks if first date is before second date
 * @param {string} date1 - First date
 * @param {string} date2 - Second date
 * @returns {boolean} True if date1 is before date2
 */
export const isDateBefore = (date1, date2) => {
  if (!date1 || !date2) return false;

  return new Date(date1) < new Date(date2);
};

/**
 * Checks if first date is after second date
 * @param {string} date1 - First date
 * @param {string} date2 - Second date
 * @returns {boolean} True if date1 is after date2
 */
export const isDateAfter = (date1, date2) => {
  if (!date1 || !date2) return false;

  return new Date(date1) > new Date(date2);
};

export const formatMinutes = (totalMinutes) => {
  const totalSeconds = totalMinutes * 60;
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = Math.floor(totalSeconds % 60)
    .toString()
    .padStart(2, '0');

  const pad = (num) => String(num).padStart(2, '0');

  return `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`;
};
