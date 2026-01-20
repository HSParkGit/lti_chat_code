/**
 *
 * @param {string} token 세션에 저장할 토큰값
 * @param {string} name 세션의 이름 ** 기본값 accessToken
 * @returns
 */
export const setSessionStorage = (name = 'accessToken', token) => {
  sessionStorage.setItem(name, token);
  return;
};

/**
 *
 * @param {string} name 지울 세션의 이름 ** 기본값 accessToken
 * @returns
 */
export const removeSessionStorage = (name = 'accessToken') => {
  sessionStorage.removeItem(name);
  return;
};

/**
 *
 * @param {string} name 가져올 세션의 이름값 ** 기본값 accessToken
 * @returns
 */
export const getSessionStorage = (name = 'accessToken') => {
  try {
    const getSession = sessionStorage.getItem(name);
    return getSession;
  } catch (error) {
    console.error('error', error);
  }
};
