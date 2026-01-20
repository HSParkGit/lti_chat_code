import React from 'react';
import PropTypes from 'prop-types';
import { clsx } from 'clsx';

/**
 * IconButton component that renders a button with an icon.
 * It accepts props to customize the icon, size, color, and other button properties.
 *
 * @param {function} [props.onClick] - The function to be called on button click
 * @param {('gray' | 'primary' | 'secondary' | 'danger' | string)} [props.color='gray'] - Button color variant
 * @param {String} [props.size] - Button size variant
 *  @param {String} [props.className] - Additional custom classes to style the iconButton.
 * @param {React.ReactNode} [props.children] - The content (icon component or any other) to be displayed inside the button
 *
 * @returns {JSX.Element} The rendered button component
 */
const IconButton = ({ variant = '', color = '', size = '', className = '', children, ...props }) => {
  // Define the size classes based on the size prop
  const buttonSize = {
    sm: 'p-2 text-sm',
    md: 'p-3 text-base',
    lg: 'p-4 text-lg',
  };

  // Define the color classes based on the color prop
  const predefinedButtonColor = {
    primary: 'bg-blue-500 hover:bg-blue-600 text-white',
    secondary: 'bg-lightGreen hover:bg-green-600 text-white',
    danger: 'bg-red-500 hover:bg-red-600 text-white',
    gray: 'bg-gray-200 hover:bg-gray-300 text-gray-700',
    'dark-blue': 'bg-[#1E293B] text-white',
    dark: 'bg-slate-800 text-white',
    bordered: 'border border-gray-300 text-gray-700',
  };
  const baseStyles = `flex items-center justify-center p-3 rounded-full `;
  const buttonColorClasses = predefinedButtonColor[variant] || '';

  const mergedClasses = clsx(
    baseStyles,
    className,
    color ? `bg-${color}` : buttonColorClasses, // Use custom color directly (e.g., `#f34343` or Tailwind classes)
    buttonSize[size] || ''
  );
  return (
    <button className={mergedClasses} {...props}>
      {children}
    </button>
  );
};

// Define prop types for validation
IconButton.propTypes = {
  color: PropTypes.string, // Button color
  size: PropTypes.string, // Button size
  className: PropTypes.string, // Button color
  children: PropTypes.node, // Any content passed as children (e.g., icon, text, or components)
};

export default IconButton;
