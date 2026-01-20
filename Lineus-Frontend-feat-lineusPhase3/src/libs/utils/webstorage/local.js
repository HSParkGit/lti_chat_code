export const setLocalStorage = (name, data, type = 'string') => {
  try {
    if (type === 'string') {
      localStorage.setItem(name, data);
      return;
    }
    if (type === 'object') {
      localStorage.setItem(name, JSON.stringify(data));
      return;
    }
    return;
  } catch (error) {
    console.error('error', error);
  }
};

export const getLocalStorage = (name, type = 'string') => {
  try {
    const localData = localStorage.getItem(name);
    if (type === 'string') {
      return localData;
    }
    if (type === 'object') {
      return JSON.parse(localData);
    }
  } catch (error) {
    console.error('error', error);
  }
};

export const removeLocalStorage = (name) => {
  try {
    localStorage.removeItem(name);
  } catch (error) {
    console.error('error', error);
  }
};
