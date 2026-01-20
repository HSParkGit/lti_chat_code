import { useEffect } from 'react';
import { ErrorBoundary } from 'react-error-boundary';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useUserStore } from '../store/useUserStore';

const ErrorFallback = ({ error, resetErrorBoundary }) => {
  return (
    <div role='alert'>
      <h2>Something went wrong:</h2>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  );
};

const ProtectedLayout = () => {
  const user = useUserStore((state) => state.user);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (!user) {
      // navigate('/login', {
      //   state: { from: location },
      //   replace: true,
      // });
    }
  }, [navigate, user]);

  const handleError = (error, info) => {
    console.error('Protected layout error:', error, info);
  };

  return (
    <>
      <ErrorBoundary
        FallbackComponent={ErrorFallback}
        onError={handleError}
        onReset={() => {
          // Reset the state when the user clicks "Try again"
          navigate(0);
        }}
      >
        <Outlet />
      </ErrorBoundary>
    </>
  );
};

export default ProtectedLayout;
