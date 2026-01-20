import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import en from './locales/en/translations.json';
import kr from './locales/kr/translations.json';
import ch from './locales/ch/translations.json';
import { useLangStore } from '@/store/useLangStore';

// i18n.failedLoadings = [];
// i18n.on("failedLoading", (lng, ns, msg) => {
//   i18n.failedLoadings.push(lng);
//   //throw msg
// });
const getInitialLanguage = () => {
  const savedLanguage = useLangStore.getState().lang;
  // const savedLanguage = getLocalStorage('lang');
  if (savedLanguage && ['kr', 'en', 'ch'].includes(savedLanguage)) {
    return savedLanguage;
  }

  const defaultLang = savedLanguage || import.meta.env.VITE_DEFAULT_LANG;
  return defaultLang;
};

i18n.use(initReactI18next).init({
  fallbackLng: getInitialLanguage(),
  lng: getInitialLanguage(),
  // debug: true,
  resources: {
    en: {
      translations: en,
    },
    kr: {
      translations: kr,
    },
    ch: {
      translations: ch,
    },
  },
  ns: ['translations'],
  defaultNS: 'translations',
});
i18n.languages = ['kr', 'en', 'ch'];

export default i18n;
