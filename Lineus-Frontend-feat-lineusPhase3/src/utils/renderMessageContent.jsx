import React from 'react';
import { getSessionStorage } from '@/libs/utils/webstorage/session.js';
import service from '@/api/apiService.js';
import ButtonWithIcon from '@/components/common/ButtonWithIcon.jsx';
import { useLocation } from 'react-router-dom';
import toast from 'react-hot-toast';

export const renderMessageContent = (text) => {
  const urlRegex =
    /((https?:\/\/|www\.)[^\s]+)|(\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\b/gi;
  const elements = [];
  let lastIndex = 0;

  for (const match of text.matchAll(urlRegex)) {
    const { index } = match;
    if (lastIndex < index) {
      elements.push(<span key={lastIndex}>{text.slice(lastIndex, index)}</span>);
    }
    let url = match[0];
    if (!url.startsWith('http')) {
      url = 'http://' + url;
    }
    const renderMessage = () => {
      return (
        <a
          key={index}
          href={url}
          target='_blank'
          rel='noopener noreferrer'
          style={{ color: '#1976d2', textDecoration: 'underline' }}
        >
          {match[0]}
        </a>
      );
    };

    elements.push(renderMessage());
    lastIndex = index + match[0].length;
  }
  if (lastIndex < text.length) {
    elements.push(<span key={lastIndex}>{text.slice(lastIndex)}</span>);
  }
  return elements;
};
