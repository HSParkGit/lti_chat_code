import { Navigate, useLocation } from 'react-router-dom';

export function PreserveQueryNavigate({ to }) {
  const location = useLocation();
  return <Navigate to={`${to}${location.search}`} replace />;
}
