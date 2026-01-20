import { createContext, useContext, useState } from 'react';
import toast, { Toaster } from 'react-hot-toast';

export const NotificationContext = createContext({
  showNotification: (msg, type = 'success', options = {}) => {},
  hideNotification: () => {},
});

export const NotificationProvider = ({ children }) => {
  const [toastId, setToastId] = useState(null);

  const showNotification = (msg, type = 'success', options = {}) => {
    let instance = toast[type];
    if (instance) {
      const id = instance(msg, {
        ...options,
      });
      setToastId(id);
    }
  };
  const hideNotification = () => {
    if (toastId) {
      toast.dismiss(toastId);
    }
    setToastId(null);
  };
  return (
    <NotificationContext.Provider
      value={{
        showNotification,
        hideNotification,
      }}
    >
      {children}
      <Toaster position='top-right' reverseOrder={false} />
    </NotificationContext.Provider>
  );
};

export const useNotification = () => {
  const context = useContext(NotificationContext);
  const noop = () => {};
  if (context !== null) {
    return {
      show: (msg, type = 'success', options = {}) => context.showNotification(msg, type, options),
      hide: () => context.hideNotification(null),
    };
  }
  return { open: noop, close: noop };
};
