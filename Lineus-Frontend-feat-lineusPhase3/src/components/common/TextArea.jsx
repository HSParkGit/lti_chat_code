import React, { forwardRef, useEffect } from 'react';
import { inputBoxAutoHeightAdjust } from '@/utils/index.js';

const TextArea = forwardRef(({ messageInput, user, onInputChange, handleKeyDown, placeholder, sent }, ref) => {
  const textareaRef = React.useRef(null);
  useEffect(() => {
    if (sent && textareaRef.current) {
      textareaRef.current.style.height = '42px'; // Reset to min height before measuring
    }
  }, [sent]);
  const handleChange = (userId, value) => {
    onInputChange(userId, value);

    // Auto-resize logic
    const textarea = textareaRef.current;
    if (textarea) {
      inputBoxAutoHeightAdjust(textareaRef, '42px', 180);
    }
  };
  return (
    <textarea
      ref={textareaRef}
      value={messageInput}
      onChange={(e) => handleChange(user?.id, e.target.value)}
      className='no-scrollbar overflow-wrap-anywhere p3-4 h-[42px] max-h-[180px] w-full resize-none break-before-auto overflow-auto rounded-xl border py-2 pr-8 indent-4 shadow-md placeholder:text-sm focus:border-green-400 focus:outline-none'
      placeholder={placeholder}
      onKeyDown={handleKeyDown}
      style={{ wordWrap: 'break-word', overflowY: 'auto' }}
    />
  );
});
TextArea.displayName = 'TextArea';
export default TextArea;
