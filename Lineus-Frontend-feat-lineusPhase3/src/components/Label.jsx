import React from 'react';
import clsx from 'clsx';
import PropTypes from 'prop-types';
/**
 * Label Component with default styling that can be overridden.
 * 
 * @param {string} text - The text to display as the label. Defaults to 'Default Label'.
 * @param {string} className - Custom classes to override the default styles.
 */
const Label = ({ text = 'Default', className = '' }) => {
    // Define default styles
    const defaultClassName = "text-sm font-medium text-gray-700";

    // Use clsx to conditionally combine class names
    const mergedClassName = clsx(defaultClassName, className);

    return (
        <span
            className={mergedClassName} // Apply the merged classes
        >
            {text}
        </span>
    );
};

Label.propTypes = {
    text: PropTypes.string,
    className: PropTypes.string,
};


export default Label;
