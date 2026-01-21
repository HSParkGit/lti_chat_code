import { lazy } from 'react';
import AdminLayout from '../layout/admin';
const Chat = lazy(() => import('../view/page/dashboard/chat'));

// Remove the duplicate group route in TEACHER role and use GroupRoute consistently
export const roleBasedRoutes = {
  path: '/dashboard',
  layout: <AdminLayout />,
  routes: [
    { path: '', element: <Chat /> },
    { path: 'chat', element: <Chat /> },
  ],
};
