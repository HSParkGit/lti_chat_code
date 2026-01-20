import TailwindcssReactAriaComponents from 'tailwindcss-react-aria-components';
import TailwindcssAnimate from 'tailwindcss-animate';
import TailwindcssContainerQueries from '@tailwindcss/container-queries';
import TailwindcssTypography from '@tailwindcss/typography';
/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}', './src/stories/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontSize: {
        // Set '3xl' default to 1.5rem (24px) for screens below the 2xl breakpoint
        '3xl': 'clamp(1.5rem, 1.82vw, 1.75rem)',
        '2sm': '0.95rem',
        '2xs': '0.85rem',
      },
      colors: {
        primary: '#4B505F',
        success: '#42B27B',
        secondary: '#4A4D59',
        // Uncomment and ensure the dynamic color logic works if needed
        // 'blue-primary': PROJECT_Map[import.meta.env.VITE_PROJECT_TYPE]?.color || '#0054A6',
        'blue-primary': '#00A7FB', // Fallback value
        blue50: '#EBF3F9',
        orange: '#fbbf24',
        bgZoom: '#0054A6',
        cZoom: '#333843',
        darkRed: '#8B0000',

        neutral700: '#B1B2B2',
        neutral500: '#F9FAFB',
        neutral300: '#FBFCFC',
        neutral400: '#FAFBFC',
        neutral200: '#FCFDFD',
        neutral100: '#FDFDFE',

        stroke60: '#B9C2CD',
        stroke70: '#EFF2F6',
        stroke500: '#CBD5E1',
        stroke300: '#DCE3EB',
        stroke200: '#E7ECF1',
        stroke100: '#ECF1F6',

        lightGreen: '#49C487',
        green400: '#49C487',

        secondary400: '#4B5462',
        secondary300: '#68707C',
        secondary500: '#1E293B',
        secondary200: '#989DA5',
        secondary100: '#B9BDC2',

        blue500: '#3B8BC5',

        // gray30: '#CBD5E1', slate-300
        // gray20: '#E2E8F0', slate-200
        // gray60: '#475569', slate-600
        // gray80: '#1E293B', slate-800
        // gray10: '#F1F5F9', slate-100
        // gray5: '#F8FAFC', slate-50
        mutedSlate: '#667085',

        titleColor: '#1E293B',

        darkGray: '#252C32',
        lightGray: '#475569',
        lighterGray: '#F7F7F7',
        lighterText: '#555E67',
        mutedGray: '#667085',
      },
      fontFamily: {
        sans: ['Plus Jakarta Sans'],
        body: ['Plus Jakarta Sans'],
      },
    },
  },
  plugins: [TailwindcssReactAriaComponents, TailwindcssAnimate, TailwindcssContainerQueries, TailwindcssTypography],
};
