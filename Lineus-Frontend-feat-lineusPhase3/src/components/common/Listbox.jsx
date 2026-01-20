import { Listbox as Lists, ListboxButton, ListboxOption, ListboxOptions } from '@headlessui/react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';
import { useState } from 'react';
import { CalendarIcon } from './Icon';
import { twMerge } from 'tailwind-merge';
import { useRef } from 'react';

export default function ListBoxAutoTranslate({ lists, ...props }) {
  const { t } = useTranslation();
  const translatedLists = lists.map((list) => ({ ...list, name: t(list.name) }));
  return <ListBox lists={translatedLists} {...props} />;
}

export function ListBox({
  lists,
  selected,
  setSelected,
  className = 'w-full bg-transparent ',
  disabled = false,
  autoSelected,
  selectedIcon,
  caretClassName,
  boxSize = 'sm',
  rounded = 'none',
  border = 'border',
  handleScroll,
  scrollRef,
  dialogClassName,
  optionStyle = '',
  placeholder,

  ...props
}) {
  const { t } = useTranslation();
  const [selectedData, setSelectedData] = useState(selected ?? lists?.[0]);
  const selectedClassColor = {
    present: 'bg-blue-500 text-white',
    late: 'bg-yellow-300',
    absent: 'bg-red-500 text-white',
  };

  const boxSizes = {
    sm: 'w-32',
    smd: 'w-40 ',
    md: 'w-48',
    lg: 'w-64',
    xl: 'w-[32rem]',
    full: 'w-full',
    rounded: '!rounded-sm w-32',
  };

  const roundedStyles = {
    none: '!rounded-none',
    sm: '!rounded-sm',
    md: '!rounded-md',
    lg: '!rounded-lg',
    xl: '!rounded-xl',
  };

  const borderStyles = {
    none: 'border-none',
    border: 'border border-gray-300 rounded-md',
  };
  return (
    <Lists value={selected} onChange={autoSelected ? setSelectedData : setSelected} disabled={disabled} {...props}>
      <div className={twMerge('relative', dialogClassName)}>
        <ListboxButton
          disabled={disabled} // Add the disabled prop
          className={twMerge(
            `flex items-center justify-between border border-[#DFE1E6] px-3 py-2 text-sm text-[#666D80] transition-all focus:outline-none focus:ring-2 ${borderStyles[border]} ${disabled ? 'cursor-not-allowed opacity-50' : 'cursor-pointer'} ${autoSelected ? selectedClassColor[selectedData?.name || selectedData?.courseName] : className}`
          )} // Adjust styles when disabled
          style={{ borderRadius: '9999px' }}
        >
          {selectedIcon && <CalendarIcon className='mr-2' />}{' '}
          <div className='flex min-w-0 items-center space-x-2'>
            {selected?.listIcon}
            <p className='max-w-[200px] truncate text-left'>
              {autoSelected
                ? t(
                    selectedData?.name ||
                      selectedData?.loginId ||
                      selectedData?.courseName ||
                      selectedData?.title ||
                      selectedData
                  )
                : t(selected?.name || selected?.loginId || selected?.courseName || selected?.title || selected)}
            </p>
          </div>
          {!selected && placeholder && <span className='text-gray-400'>{placeholder}</span>}
          <FontAwesomeIcon icon={faChevronDown} className={twMerge('ml-2 text-xs', caretClassName)} />
        </ListboxButton>
        {!disabled && (
          <ListboxOptions
            ref={scrollRef}
            onScroll={handleScroll}
            className={`absolute z-50 mt-1 max-h-60 overflow-y-auto rounded border bg-white shadow-lg ${boxSizes[boxSize]} ${roundedStyles[rounded]}`}
          >
            {lists?.map((list, index) => (
              <ListboxOption
                key={index}
                value={list}
                className={({ active }) =>
                  `flex cursor-pointer select-none items-center gap-2 px-3 py-1.5 ${active ? 'bg-gray-100' : ''}`
                }
              >
                <span className={twMerge(optionStyle, `flex items-center gap-2 break-all text-xs text-gray-600`)}>
                  {list?.listIcon && list?.listIcon}

                  {t(list?.name || list?.loginId || list?.courseName || list?.title || list)}
                </span>
                {(list?.count === 0 || list?.count) && (
                  <span className={twMerge(optionStyle, `flex-gap-2 items-center text-xs text-gray-600`)}>
                    {list?.listIcon && list?.listIcon}({list?.count})
                  </span>
                )}
              </ListboxOption>
            ))}
          </ListboxOptions>
        )}
      </div>
    </Lists>
  );
}
