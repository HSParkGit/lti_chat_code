import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import clsx from 'clsx';

/**
 * Renders a button with optional icons on either side of the label.
 */
const ButtonWithIcon = ({
  label,
  btnStyles = '',
  icon,
  iconStyles = 'px-1.5',
  iconPosition = 'left',
  iconFill,
  variant = '',
  type = 'button',
  onClick = () => {},
  disabled = false,
  tooltip,
  ...props
}) => {
  const variantClasses = {
    'btn-primary': 'bg-blue-primary text-white',
    'btn-secondary': 'bg-gray-500 text-white',
    'btn-danger': 'bg-[#FF6683] text-white',
    'btn-warning': 'bg-yellow-500 text-white',
    'btn-bordered': 'border border-gray-300 text-gray-700',
    'btn-darkBordered': 'border border-slate-800 text-slate-800',
    'btn-bordered-dashed': 'border border-dashed',
    'btn-success': 'bg-[#49C487] text-white',
    'btn-lightGreen': 'bg-green-600 text-white',
    'btn-lightGreenBorder': 'bg-[#C7EDDA] text-[#348B60] border border-[#49C487]',
    'btn-black': 'bg-[#1E293B] text-white',
    'btn-action': 'bg-[#49C487] text-white',
    'btn-disabled': 'bg-gray-300 text-gray-500 cursor-not-allowed',
    'btn-lightGray': 'bg-[#F1F5F9] text-[#475569]',
    'bg-blue': 'bg-[#3B8BC5] text-white',
    'btn-blue': 'bg-blue500 text-white',
    link: 'text-slate-800 hover:text-slate-600 underline bg-transparent border-0 shadow-none text-base font-semibold',
    'btn-bordered-warning': 'border border-[#DF900A] text-[#DF900A]',
    'btn-bordered-green': 'border border-[#49C487] text-[#49C487]',
  };

  const buttonClass = clsx(
    'rounded-full flex items-center justify-between p-2 px-4 disabled:cursor-not-allowed space-x-1',
    variantClasses[variant],
    btnStyles,
    disabled && 'opacity-50 cursor-not-allowed'
  );

  const renderIcon = () => {
    if (!icon) return null;
    if (typeof icon === 'object' && icon.prefix && icon.iconName) {
      // FontAwesome Icon Definition
      return <FontAwesomeIcon icon={icon} className={iconStyles} />;
    } else if (typeof icon === 'function') {
      // Custom React component
      const IconComponent = icon;
      return <IconComponent className={iconStyles} fill={iconFill} />;
    }
    return null;
  };

  return (
    <button
      className={buttonClass}
      type={type}
      onClick={() => onClick()}
      disabled={disabled}
      title={tooltip}
      {...props}
    >
      {icon && iconPosition === 'left' && renderIcon()}
      {label && <span>{label}</span>}
      {icon && iconPosition === 'right' && renderIcon()}
    </button>
  );
};

export default ButtonWithIcon;
