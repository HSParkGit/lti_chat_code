import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useGroupStore = create(
  persist(
    (set, get) => ({
      selectedGroup: null,
      selectedGroupAnnouncement: null,
      updateGroup: null,
      getUpdateGroup: () => get().updateGroup,
      setUpdateGroup: (group) => set(() => ({ updateGroup: group })),
      getSelectedGroup: () => get().selectedGroup,
      getSelectedGroupAnnouncement: () => get().selectedGroupAnnouncement || null,
      setSelectedGroup: (group) => set(() => ({ selectedGroup: group })),
      setSelectedGroupAnnouncement: (announcement) => set(() => ({ selectedGroupAnnouncement: announcement })),
      clearSelectedGroup: () => set(() => ({ selectedGroup: null })),
      clearSelectedGroupAnnouncement: () => set(() => ({ selectedGroupAnnouncement: null })),
    }),
    { name: 'group-storage' }
  )
);
