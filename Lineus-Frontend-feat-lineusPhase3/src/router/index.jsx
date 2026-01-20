import { Suspense, useEffect } from 'react';
import { Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import { useUserStore } from '../store/useUserStore';
import Login from '../view/auth/login';
import ProtectedLayout from '../layout/ProtectedLayout';
import { roleBasedRoutes } from './routeConfig';
import { Logout } from '../view/page/Logout';
import Loading from '../components/Loader';
import { useRouteStore } from '@/store/useRouteStore.js';
import LTIStarterPage from '@/view/page/dashboard/LTI';

function Router() {
  const { getMyRole, user } = useUserStore();
  const userRole = getMyRole();
  const navigate = useNavigate();
  const location = useLocation();
  const currentAuthRoutes = roleBasedRoutes || null;
  const { setCurrentRoute, setPreviousRoute, setRouteHistory, currentRoute } = useRouteStore();

  useEffect(() => {
    const fullPath = location.pathname + location.search;
    setPreviousRoute(currentRoute);
    setCurrentRoute(fullPath);
    setRouteHistory((prev) => [...prev, fullPath]);

    if (!user && location.pathname !== '/' && location.pathname !== '/by_pass_authenticate') {
      navigate('/', { state: { from: location } });
    }
  }, [
    location.pathname,
    location.search,
    user,
    navigate,
    setCurrentRoute,
    setPreviousRoute,
    setRouteHistory,
    currentRoute,
  ]);

  const renderRoutes = (routes) => {
    return routes.map((route) => {
      // Handle index routes
      if (route.index) {
        return (
          <Route
            key={`${route.element}-index`}
            index
            element={<Suspense fallback={<Loading />}>{route.element}</Suspense>}
          />
        );
      }

      // Handle all other routes
      return (
        <Route key={route.path} path={route.path} element={<Suspense fallback={<Loading />}>{route.element}</Suspense>}>
          {/* If the route has children, call this function again */}
          {route.children && renderRoutes(route.children)}
        </Route>
      );
    });
  };

  return (
    <Routes>
      <Route path='/' element={<Login />} />
      <Route path='/by_pass_authenticate' element={<LTIStarterPage />} />
      {currentAuthRoutes && (
        <Route element={<ProtectedLayout />}>
          <Route
            path={currentAuthRoutes.path}
            element={<Suspense fallback={<Loading />}>{currentAuthRoutes.layout}</Suspense>}
          >
            {renderRoutes(currentAuthRoutes.routes)}
          </Route>
        </Route>
      )}
      <Route path='/login' element={<Login />} />
      <Route path='/logout' element={<Logout />} />
    </Routes>
  );
}

export default Router;
