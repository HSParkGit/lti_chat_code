import ReactDOM from 'react-dom/client';
// import App from './App.jsx'
import '@fortawesome/fontawesome-free/css/all.min.css';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { ErrorBoundary } from 'react-error-boundary';
import { BrowserRouter } from 'react-router-dom';
import App from '../src/App';
import { PROJECT_Map } from './constant';
import { NotificationProvider } from './context/notification.context';
import './i18n/config';
import './index.css';
import Login from './view/auth/login';
import { ModalProvider } from './context/modal.context';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // staleTime: 10 * 60 * 1000, // 10분
    },
  },
});

const faviconType = import.meta.env.VITE_PROJECT_TYPE;
const faviconURL = PROJECT_Map[faviconType].favicon || '/assets/kumo/favicon.ico';

const setFavicon = (url) => {
  const link = document.getElementById('favicon');
  if (link) {
    link.href = url;
  }
};

setFavicon(faviconURL);

ReactDOM.createRoot(document.getElementById('root')).render(
  <QueryClientProvider client={queryClient}>
    <BrowserRouter>
      <ModalProvider>
        <NotificationProvider>
          {/* <ErrorBoundary fallback={<Login />}></ErrorBoundary> */}
          <ErrorBoundary>
            <App />
          </ErrorBoundary>
        </NotificationProvider>
      </ModalProvider>
    </BrowserRouter>
    <ReactQueryDevtools initialIsOpen={false} />
  </QueryClientProvider>
);
