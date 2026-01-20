import { createContext, useContext, useMemo, useState } from 'react';

// Create the context with a default value (e.g., 'individual' or 'group')
const ChatTypeContext = createContext(null);

export const useChatType = () => {
  const context = useContext(ChatTypeContext);
  if (!context) {
    throw new Error('useChatType must be used within a ChatTypeProvider');
  }
  return context;
};

// Context Provider component
export const ChatTypeProvider = ({ children }) => {
  const [chatType, setChatType] = useState('Individuals');
  const [messageType, setMessageType] = useState(0); // 0 for 'Individuals', 1 for 'Group'

  const value = useMemo(
    () => ({
      chatType,
      setChatType,
      messageType,
      setMessageType,
      isIndividual: chatType === 'Individuals',
      isGroup: chatType === 'Group',
    }),
    [chatType, messageType]
  );

  return <ChatTypeContext.Provider value={value}>{children}</ChatTypeContext.Provider>;
};
