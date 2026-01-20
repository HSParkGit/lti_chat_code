import React from 'react';
import PropTypes from 'prop-types';
import { twMerge } from 'tailwind-merge';

const sizeClasses = {
  sm: 'h-4 w-4',
  md: 'h-8 w-8',
  lg: 'h-12 w-12',
};

const Loader = ({ size = 'md', className }) => {
  return (
    <div className={twMerge('flex items-center justify-center', className)}>
      <div className={`animate-spin rounded-full border-b-2 border-gray-900 ${sizeClasses[size]}`}></div>
    </div>
  );
};

Loader.propTypes = {
  size: PropTypes.oneOf(['sm', 'md', 'lg']),
};

export default Loader;
