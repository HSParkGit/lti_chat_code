import { Cookies } from 'react-cookie';

const cookies = new Cookies();

/**
 * 지정된 쿠키 이름에 해당하는 쿠키 값을 반환합니다.
 * @param {string} name - 쿠키 이름
 * @returns {string} - 쿠키 값
 */
export const getCookie = (name) => {
  return cookies.get(name);
};

/**
 * 쿠키를 설정하는 함수입니다.
 *
 * @param {string} name - 쿠키의 이름
 * @param {string} value - 쿠키의 값
 * @param {object} option - 쿠키의 옵션
 * @returns {void}
 */
export const setCookie = (name, value, option) => {
  return cookies.set(name, value, option);
};

export const setCookieExpireAtEndOfDay = (name, value) => {
  const now = new Date();
  const endOfDay = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);

  return setCookie(name, value, { expires: endOfDay });
};

/**
 * 지정된 쿠키를 제거합니다.
 *
 * @param {string} name - 제거할 쿠키의 이름
 * @returns {void}
 */
export const removeCookies = (name, option) => {
  return cookies.remove(name, option);
};

// export const setUserIdCookie = (name, value) => {
//   return cookies.set(name, value, option);
// };
// export const setPathCookie = (name, value) => {
//   return cookies.set(name, value, option);
// };
