import axios from 'axios';
import { useQueryClient } from '@tanstack/react-query';
import { jwtDecode } from 'jwt-decode';
import { useContext, useEffect, useLayoutEffect, useRef, useState } from 'react';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import eyeClose from '../../assets/images/eye_closed.svg';
import eyeOpen from '../../assets/images/eye_open.svg';
import Loader from '../../components/Loader';
import { PROJECT_Map, 선능지미함수, 쿠키도메인, 푸터문구 } from '../../constant';
import { useUserStore } from '../../store/useUserStore';
// import { scrollE } from '../../libs/utils/navigate/scroll';
import { getCookie, removeCookies, setCookie } from '../../libs/utils/webstorage/cookie';
import { getSessionStorage, removeSessionStorage, setSessionStorage } from '../../libs/utils/webstorage/session';
import LOGO_IMG from '../../assets/images/logo.svg';
import { useTranslation } from 'react-i18next';
import toast from 'react-hot-toast';

const Login = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { t } = useTranslation();
  const location = useLocation();

  const [disable, setDisable] = useState(false);
  const [eye, setEye] = useState(false);
  const setUser = useUserStore((state) => state.setUser);
  const [id, setId] = useState(getCookie('id') ? getCookie('id') : '');
  const [isCheck, setIsCheck] = useState(false);
  const pass = useRef();

  const handleShowPass = () => {
    setEye(!eye);
    if (pass.current.type === 'password') {
      pass.current.type = 'text';
    } else {
      pass.current.type = 'password';
    }
  };
  const loginSubmit = async (e) => {
    const option = {
      path: '/',
      domain: 쿠키도메인,
    };
    const user = useUserStore.getState().user;
    if (user) {
      toast.error('You already logged in another tab');
      window.location.reload();
      return;
    }
    removeSessionStorage('accessToken');
    removeCookies('refreshToken');
    removeCookies('bG1zQ291cnNlVXJs', option);
    removeCookies('bG9naW5JZA', option);
    localStorage.removeItem('user-logout');
    e.preventDefault();
    setDisable(true);

    setCookie('id', id);

    const { data } = await axios.post(`${import.meta.env.VITE_API_KEY}/signin`, {
      userNumber: id,
      password: pass.current.value,
    });

    if (data?.errorCode === 'U001' || data?.errorCode) {
      setDisable(false);
      alert('아이디 또는 비밀번호가 일치하지 않습니다.');
      return;
    }
    try {
      setSessionStorage('accessToken', data.accessToken);
      setSessionStorage('userId', data.lmsUserId);
      setCookie('refreshToken', data.refreshToken);
      localStorage.setItem('loggedIn', true);
      setUser(data);
      if (isCheck) {
        setCookie('id', id);
      } else {
        removeCookies('id');
      }

      if (location?.state && location?.state?.from?.pathname !== '/login' && location?.state?.from?.pathname !== '/') {
        // if come form a session expire log out or invite link
        window.location.href = location.state?.from?.pathname + (location.state?.from?.search || '');
      } else {
        window.location.href = '/dashboard';
      }
    } catch (error) {
      setDisable(false);
      console.error(error);
      throw new Error('로그인 실패');
    } finally {
      setDisable(false);
    }
    return;
  };

  const queryClient = useQueryClient();

  useEffect(() => {
    // window.addEventListener('scroll', scrollE);
    queryClient.clear();

    if (searchParams.get('sessionExpired') === 'true') {
      alert(t('sessionExpired'));
    }
  }, []);
  // use effect to redirect to dashboard if user is already logged in
  useEffect(() => {
    const user = useUserStore.getState().user;
    if (user) {
      navigate('/dashboard', { replace: true });
    }
  }, []);
  const accessToken = getSessionStorage('accessToken');
  // useLayoutEffect(() => {
  //   if (accessToken) {
  //     try {
  //       const decode = jwtDecode(accessToken);
  //       setUser(decode);
  //       navigate('/dashboard');
  //     } catch (error) {
  //       removeCookies('refreshToken');
  //       removeSessionStorage('accessToken');
  //       console.error(error);
  //     }
  //   }
  // }, []);

  return (
    <>
      {/* Updated div with white background */}
      <div className='absolute flex min-h-screen w-full flex-col items-center justify-center bg-white'>
        <div className='flex h-full w-full items-center justify-center bg-transparent px-5 py-24'>
          <div className='flex w-full max-w-[468px] flex-col items-center gap-7 rounded-sm bg-white bg-opacity-95 px-[54px] pb-[51.1px] pt-[41px]'>
            <img src={LOGO_IMG} alt='logo login' />
            <span className='text-xl font-medium text-[#414042]'>학습관리시스템(LMS) 로그인</span>
            <form onSubmit={loginSubmit} className='form input flex w-full flex-col gap-5'>
              <label
                htmlFor='numberstudent'
                className='flex w-full flex-col self-start text-xs text-primary lg:text-sm'
              >
                학번/ 교번
                <input
                  type='text'
                  name='userId'
                  onChange={(e) => {
                    setId(e.target.value);
                  }}
                  value={id}
                  className='mt-2 w-full rounded-sm border border-[#CED4DA] bg-white p-2 text-xs text-[#6C757D] lg:text-sm'
                  autoComplete='false'
                  placeholder='학번 또는 교직원코드로 로그인 아이디를 입력하세요'
                />
              </label>
              <label
                htmlFor='numberstudent'
                className='flex w-full flex-col self-start text-xs text-primary lg:text-sm'
              >
                비밀번호
                <div className='relative flex w-full'>
                  <input
                    ref={pass}
                    type='password'
                    name='pin'
                    className='mt-2 w-full rounded-sm border border-[#CED4DA] bg-white p-2 text-xs text-[#6C757D] lg:text-sm'
                    placeholder='비밀번호를 입력하세요'
                  />
                  {eye ? (
                    <img
                      src={eyeOpen}
                      alt=''
                      className='absolute bottom-[11px] right-2 cursor-pointer'
                      onClick={handleShowPass}
                    />
                  ) : (
                    <img
                      src={eyeClose}
                      alt=''
                      className='absolute bottom-[11px] right-2 cursor-pointer'
                      onClick={handleShowPass}
                    />
                  )}
                </div>
              </label>
              <div className='flex w-full justify-between'>
                <label htmlFor='checkbox' className='flex items-center gap-2 text-xs text-primary lg:text-sm'>
                  <input
                    onChange={(e) => setIsCheck(e.target.checked)}
                    checked={isCheck}
                    type='checkbox'
                    className='bg-white'
                  />
                  학번/ 교번 기억하기
                </label>
              </div>
              <button
                disabled={disable}
                type='submit'
                className='w-full rounded-sm bg-[#006e01] py-2 text-base text-white'
              >
                로그인
              </button>
            </form>

            <hr className='h-0.5 w-full rounded-full bg-[#C9CACF]' />
            <ul className='ml-3 list-disc self-start text-secondary lg:ml-4'>
              <li className='ml-1 text-xs text-secondary lg:text-sm'>ID(아이디): 학번 또는 교직원코드</li>
              <li className='ml-1 text-xs text-secondary lg:text-sm'>PW(비밀번호): 개인이 재설정한 비밀번호 입력</li>
              {PROJECT_Map[import.meta.env.VITE_PROJECT_TYPE].loginAddress.alarm && (
                <>
                  <span className='-ml-4 flex items-start gap-1 text-xs text-orange lg:text-sm'>
                    <span className='text-base lg:text-xl'>※</span>
                    초기 로그인 시, 1회적으로 PW 재설정이 필요합니다. <br />
                    (재설정 경로는 로그인 후 공지사항으로 확인 가능)
                  </span>
                  <span className='-ml-4 flex items-center gap-1 text-xs text-orange lg:text-sm'>
                    <span className='text-base lg:text-xl'>※</span>
                    LMS시스템은 교내 통합로그인 비밀번호와 다릅니다.
                  </span>
                </>
              )}
            </ul>
          </div>
        </div>
      </div>
      {disable && (
        <div className='fixed left-0 top-0 flex h-[100vh] w-full items-center justify-center bg-[rgba(30,30,30,0.2)]'>
          <Loader />
        </div>
      )}
    </>
  );
};

export default Login;
