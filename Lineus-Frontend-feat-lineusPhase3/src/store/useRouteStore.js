import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useRouteStore = create(
  persist(
    (set, get) => ({
      currentRoute: null,
      setCurrentRoute: (route) => set(() => ({ currentRoute: route })),
      previousRoute: null,
      setPreviousRoute: (route) => set(() => ({ previousRoute: route })),
      routeHistory: [],
      setRouteHistory: (route) =>
        set(() => ({ routeHistory: typeof route === 'function' ? route(get().routeHistory) : route })),
      clearRouteHistory: () => set(() => ({ routeHistory: [] })),
    }),
    {
      name: 'route-store',
    }
  )
);
