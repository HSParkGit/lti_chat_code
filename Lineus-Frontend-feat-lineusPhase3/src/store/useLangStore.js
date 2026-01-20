import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useLangStore = create(
  persist(
    (set, get) => ({
      lang: null,
      setLang: (lang) => set(() => ({ lang })),
    }),
    {
      name: 'lang-store',
    }
  )
);
