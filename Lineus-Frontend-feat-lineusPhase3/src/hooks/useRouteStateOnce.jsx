import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useRef } from 'react';

export function useRouteStateOnce() {
  const location = useLocation();
  const navigate = useNavigate();

  const routeState = useRef(location.state);

  useEffect(() => {
    // Clear location.state on mount
    navigate(location.pathname, { replace: true }); // clears state
  }, [location.pathname, navigate]);

  return routeState.current;
}
