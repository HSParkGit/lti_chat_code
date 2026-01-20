import { createContext, useContext, useReducer, useCallback, useMemo } from 'react';

const MessageContext = createContext();

const messageReducer = (state, action) => {
  switch (action.type) {
    case 'SET_MESSAGES':
      return {
        ...state,
        messages: action.payload,
      };
    case 'ADD_MESSAGE':
      // Prevent duplicates by checking messageId (handle both snake_case and camelCase)
      const newMsgId = action.payload.message_id || action.payload.messageId;
      if (state.messages.some(m => (m.message_id || m.messageId) === newMsgId)) {
        console.log('Duplicate message blocked:', newMsgId);
        return state;
      }
      console.log('Adding new message:', newMsgId, action.payload.content);
      return {
        ...state,
        messages: [...state.messages, action.payload],
      };
    case 'ADD_MESSAGES':
      return {
        ...state,
        messages: [...action.payload, ...state.messages], // New messages at top
      };
    case 'SET_CONNECTED':
      return {
        ...state,
        isConnected: action.payload,
      };
    default:
      return state;
  }
};

export const MessageProvider = ({ children, initialMessages = [] }) => {
  const [state, dispatch] = useReducer(messageReducer, {
    messages: initialMessages,
    isConnected: false,
  });

  const setMessages = useCallback((messages) => {
    dispatch({ type: 'SET_MESSAGES', payload: messages });
  }, []);

  const addMessage = useCallback((message) => {
    dispatch({ type: 'ADD_MESSAGE', payload: message });
  }, []);

  const addMessages = useCallback((messages) => {
    dispatch({ type: 'ADD_MESSAGES', payload: messages });
  }, []);

  const setConnected = useCallback((status) => {
    dispatch({ type: 'SET_CONNECTED', payload: status });
  }, []);

  const value = useMemo(
    () => ({
      ...state,
      setMessages,
      addMessage,
      addMessages,
      setConnected,
    }),
    [state, setMessages, addMessage, addMessages, setConnected]
  );

  return <MessageContext.Provider value={value}>{children}</MessageContext.Provider>;
};

export const useMessages = () => useContext(MessageContext);
