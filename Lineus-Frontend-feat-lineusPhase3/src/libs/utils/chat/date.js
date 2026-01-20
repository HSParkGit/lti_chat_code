/**
 * Formats a timestamp for chat messages:
 * - "just now" for < 1 min
 * - "1 min", "2 mins", etc. for < 1hr
 * - "1 hr", "2 hrs", etc. for today
 * - "Yesterday" if message was sent yesterday
 * - "DD/MM" if older
 * @param {string | Date} timestamp
 * @returns {string}
 */
export function formatChatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const now = new Date();

  const diffMs = now.getTime() - date.getTime();
  const diffMin = Math.floor(diffMs / (1000 * 60));
  const diffHr = Math.floor(diffMin / 60);

  const isToday = date.toDateString() === now.toDateString();

  const yesterday = new Date();
  yesterday.setDate(now.getDate() - 1);
  const isYesterday = date.toDateString() === yesterday.toDateString();

  if (isToday) {
    if (diffMin < 1) return 'just now';
    if (diffMin < 60) return `${diffMin} min${diffMin === 1 ? '' : 's'}`;
    return `${diffHr} hr${diffHr === 1 ? '' : 's'}`;
  }

  if (isYesterday) return 'Yesterday';

  return `${date.getMonth() + 1}/${date.getDate()}`; // MM/DD format
}

/**
 * Formats a timestamp for message group labels:
 * - "Today" if the message was sent today
 * - "Yesterday" if the message was sent yesterday
 * - "2 Mar 25" (DD MMM YY) for older dates
 * @param {string | Date} date - The UTC timestamp to format
 * @returns {string}
 */
export function formatMessageGroupLabel(date) {
  const msgDate = new Date(date);
  const now = new Date();

  const isToday = msgDate.toLocaleDateString() === now.toLocaleDateString();

  const yesterday = new Date();
  yesterday.setDate(now.getDate() - 1);
  const isYesterday = msgDate.toLocaleDateString() === yesterday.toLocaleDateString();

  if (isToday) return 'Today';
  if (isYesterday) return 'Yesterday';

  return msgDate.toLocaleDateString(undefined, {
    day: 'numeric',
    month: 'short',
    year: '2-digit',
  });
}

/**
 * Formats UTC timestamp into 12-hour format.
 * @param {string} timestamp - e.g., "2025-04-04T21:59:51.876203"
 * @returns {string} - e.g., "9:59 PM"
 */
export function formatChatTimestampExact12Hour(timestamp) {
  const date = new Date(timestamp);

  // Convert UTC to local time by adding the timezone offset
  let hour = date.getHours();
  const minute = date.getMinutes();
  const period = hour >= 12 ? 'PM' : 'AM';
  hour = hour % 12 || 12;

  return `${hour}:${minute.toString().padStart(2, '0')} ${period}`;
}
