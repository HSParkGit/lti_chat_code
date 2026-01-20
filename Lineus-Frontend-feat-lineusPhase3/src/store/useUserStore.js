import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useUserStore = create(
  persist(
    (set, get) => ({
      user: null,
      currentUser: null,
      totalUser: 0,
      invitations: [],
      notiCount: 0,
      courseNotificationCount: 0,
      searchResults: [],
      userSettings: {},
      courseId: null,
      getUserId: () => get().user?.lmsUserId ?? undefined,
      getUserNumber: () => get().user?.userNumber ?? undefined,
      getMe: () => get().user,
      getMyProfilePicture: () => get().user?.profileImage ?? undefined,
      getMyRole: () => get().user?.role ?? get().user?.userRole ?? undefined,
      getCourseId: () => get().user?.courseId ?? undefined,
      setCurrentUser: (newUser) => set(() => ({ currentUser: newUser })),
      setUser: (newUser) => set(() => ({ user: newUser })),
      setSearchResults: (results) => set(() => ({ searchResults: results })),
      removeUser: () => set(() => ({ user: null })),
      removeAll: () =>
        set(() => ({
          user: null,
          notiCount: 0,
          searchResults: [],
        })),
      setNotiCount: (notiCount) => set(() => ({ notiCount })),
      setCourseNotificationCount: (count) => set(() => ({ courseNotificationCount: count })),
      setUserSettings: (settings) => set(() => ({ userSettings: settings })),
      setInvitations: (invitations) => set(() => ({ invitations })),
    }),
    { name: 'user-storage' }
  )
);

export const useUserListsStore = create((set) => ({
  userLists: null,
  setUserLists: (users) => set(() => ({ userLists: users })),
}));
