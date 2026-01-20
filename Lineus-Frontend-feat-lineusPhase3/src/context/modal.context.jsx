import { createContext, useState, useEffect, useContext } from 'react';
import { createPortal } from 'react-dom';

export const ModalContext = createContext({
  modal: null,
  setModal: null,
});

export const ModalProvider = ({ children }) => {
  const [modal, setModal] = useState(null);
  return (
    <ModalContext.Provider
      value={{
        modal,
        setModal,
      }}
    >
      {children}
      {modal !== null ? createPortal(modal, document.body) : null}
    </ModalContext.Provider>
  );
};

export const useModal = () => {
  const context = useContext(ModalContext);
  const noop = () => {};
  if (context !== null) {
    return {
      open: (modalContent) => context.setModal(modalContent),
      close: () => context.setModal(null),
    };
  }
  return { open: noop, close: noop };
};
