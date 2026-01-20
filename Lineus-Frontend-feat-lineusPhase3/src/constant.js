import { setCookie } from './libs/utils/webstorage/cookie';

export const 줌확인전역변수설정 = import.meta.env.VITE_API_USE_ZOOM === 'true' ? true : false;

export const 온라인출석부사용여부 = import.meta.env.VITE_API_ONLINELECTURE;

export const 메세지주소 = import.meta.env.VITE_API_MESSAGE_URL;
export const 온라인강의실주소 = import.meta.env.VITE_API_ONLINE_LECTURE_URL;
export const SSO주소 = import.meta.env.VITE_API_COOKIE_URL;
export const 쿠키도메인 = import.meta.env.VITE_API_SSO;
export const 컬러들 = ['#B2E5FE', '#3A3C43', '#007BB6'];

export const PANOTO = 'panoto';
export const ZOOM = 'zoom';
export const MIX = 'mix';
export const JUANPAZO = 'juanpazo';

export const PROJECT_Map = {
  LINEUS: {
    favicon: '/assets/kumo/favicon.ico',
    footer: 'Linus Learn (LMS)',
    logo: '/assets/logo.svg',
    loginBack: '/assets/kumo/login-back.webp',
    color: '#00A7FB',
    title: 'Linus Learn (LMS)',
    panotoLink: 'https://kumoh.ap.panopto.com/Panopto/',
    이전버전LMS주소: 'https://elearning.kumoh.ac.kr/',
    ocwAddress: 'https://ocw.kumoh.ac.kr/',
    loginAddress: {
      phone: '02-6012-4555',
      T_phone: '02-6012-4555',
      email: 'info@lineus.kr',
      alarm: true,
    },
  },
  KUMO: {
    favicon: '/assets/kumo/favicon.ico',
    footer: 'Kumoh University',
    logo: '/assets/kumo/logo.svg',
    loginBack: '/assets/kumo/login-back.webp',
    color: '#00A7FB',
    title: '라이너스',
    panotoLink: 'https://kumoh.ap.panopto.com/',
    이전버전LMS주소: 'https://elearning.kumoh.ac.kr/',
    ocwAddress: 'https://ocw.kumoh.ac.kr/',
    loginAddress: {
      phone: '054-478-7356',
      T_phone: '0707-007-0100',
      email: 'lms@kumoh.ac.kr',
      alarm: true,
    },
  },
  JUAN: {
    favicon: '/assets/juan/favicon.ico',
    footer: '주안대학원대학교',
    logo: '/assets/juan/logo.svg',
    loginBack: '/assets/juan/login-back.webp',
    color: '#00A7FB',
    title: '주안대학원대학교',
    panotoHidden: 'HIDDEN',
    이전버전LMS주소: null,
    ocwAddress: null,
    loginAddress: {
      phone: '000-000-0000',
      email: 'email@university.edu',
      alarm: true,
    },
  },
  HANIL: {
    favicon: '/assets/hanil/favicon.ico',
    footer: '한일장신대학교',
    logo: '/assets/hanil/logo.svg',
    loginBack: '/assets/hanil/login-back.webp',
    color: '#074093',
    title: '한일장신대학교',
    panotoLink: 'https://hanil.ap.panopto.com/Panopto',
    이전버전LMS주소: null,
    ocwAddress: null,
    loginAddress: {
      phone: '063-230-5431',
      email: null,
      alarm: false,
    },
  },
};

export const 메인컬러 = PROJECT_Map.KUMO.color || '#0054A6';

// export const 푸터문구 = `© Copyright <a className='font-bold'>${
//   PROJECT_Map[import.meta.env.VITE_PROJECT_TYPE].footer || 'University'
// }</a> All Rights Reserved`;
export const 푸터문구 = `COPYRIGHT(C) Kumoh National Institute of Technology. All Rights Reserved.`;

export const 일부터십이 = [
  '01:00',
  '02:00',
  '03:00',
  '04:00',
  '05:00',
  '06:00',
  '07:00',
  '08:00',
  '09:00',
  '10:00',
  '11:00',
  '12:00',
];

// export const 오부터육십 = Array.from({ length: 12 }, (_, i) => {
//   const a = (i + 1) * 5;
//   return a > 10 ? a : `0${a}`;
// });
export const 오부터육십 = ['00', '05', '10', '15', '20', '25', '30', '35', '40', '45', '50', '55'];

export const 삼초딜레이 = 6_000;
// export const 삼초딜레이 = 4;

export const 오십십오이십 = [5, 10, 15, 20];
export const 오의배수백이십까지 = [
  5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120,
];
const 오배수 = Array.from({ length: 24 }, (_, i) => (i + 1) * 5);
export const 칠십팔십구십백 = [70, 80, 90, 100];

export const 수업시간배수값 = 24;
export const 수업시간배수 = 5;

export const 수업시간관련객체 = {
  배열의갯수: 24,
  배수의값: 5,
};

export const 오배수함수 = (length = 0) => {
  if (!Number(length)) return [];
  if (isNaN(length)) return [];
  return Array.from({ length }, (_, i) => (i + 1) * 수업시간관련객체.배수의값);
};

/**
 * 배수함수는 주어진 길이에 대해 주어진 배수의 배열을 반환합니다.
 *
 * @param {number} length - 배열의 길이입니다.
 * @param {number} 배수 - 배열의 각 요소에 곱해질 배수입니다.
 * @returns {number[]} 주어진 길이와 배수에 따라 생성된 배열입니다.
 */
export const 배수함수 = (length = 0, 배수 = 10) => {
  if (!Number(length)) return [];
  if (isNaN(length)) return [];
  return Array.from({ length }, (_, i) => (i + 1) * 배수);
};

export const 올 = 'ALL';
export const 학생 = 'STUDENT';
export const 선생 = 'TEACHER';
export const 관리자 = 'ADMIN';

/**
 * 기준 밀리초
 */
// export const 딜레이시간초 = 4;
export const 딜레이시간초 = 1_750;

// 출석
export const ATTENDANCE = 'ATTENDANCE';
// 지각
export const LATE = 'LATE';
// 결석
export const ABSENCE = 'ABSENCE';
export const ABSENCES = 'ABSENCES';
export const NO_PANOPTO_ITEM = 'NO_PANOPTO_ITEM';
export const NO_ATTEND_SETTING = 'NO_ATTEND_SETTING';

export const 변환함수 = (object) => {
  // const {
  //   eighth,
  //   eleventh,
  //   fifteenth,
  //   fifth,
  //   first,
  //   fourteenth,
  //   fourth,
  //   ninth,
  //   second,
  //   seventh,
  //   sixth,
  //   tenth,
  //   third,
  //   thirteenth,
  //   twelfth,
  // } = object;
  // const newArr = [
  //   first,
  //   second,
  //   third,
  //   fourth,
  //   fifth,
  //   sixth,
  //   seventh,
  //   eighth,
  //   ninth,
  //   tenth,
  //   eleventh,
  //   twelfth,
  //   thirteenth,
  //   fourteenth,
  //   fifteenth,
  // ];
  // return newArr;
};

export const delay = (ms) => new Promise((res) => setTimeout(res, ms));

export const getRandomColor = () => {
  const randomIndex = Math.floor(Math.random() * 컬러들.length);
  return `bg-[${컬러들[randomIndex]}]`;
};

export const 필터함수 = (arr, string) => {
  const filteredData = arr
    .map((module) => {
      return {
        moduleNumber: module.moduleNumber,
        videoAttendanceDtos: module.videoAttendanceDtos.filter((video) => video.attendanceState === string),
      };
    })
    .filter((module) => module.videoAttendanceDtos.length > 0);

  return filteredData;
};
export const 줌만쓰는학생디테일필터함수 = (arr = [], string = '') => {
  const filteredData = arr?.map((el) => {
    return el?.filter((item) => item?.isAttend === string);
  });
  return filteredData;
};

// export const 갯수함수 = (arr, string) => {
//   arr.reduce(
//     (acc, current) => acc + current.videoAttendanceDtos?.filter((item) => item.attendanceState === string).length,
//     0
//   );
// };

export const countAttendanceStates = (data) => {
  const counts = {
    ATTENDANCE: 0,
    ABSENCE: 0,
    LATE: 0,
  };

  data.forEach((module) => {
    module.videoAttendanceDtos.forEach((video) => {
      // eslint-disable-next-line no-prototype-builtins
      if (counts.hasOwnProperty(video.attendanceState)) {
        counts[video.attendanceState]++;
      }
    });
  });

  return counts;
};
export const 줌만쓰는학생디테일갯수구하는함수 = (data) => {
  const counts = {
    ABSENCE: 0,
    ATTENDANCE: 0,
  };

  data.forEach((module) => {
    module.forEach((video) => {
      // eslint-disable-next-line no-prototype-builtins
      if (counts.hasOwnProperty(video.isAttend)) {
        counts[video.isAttend]++;
      }
    });
  });

  return counts;
};
export const 반복문 = async () => {
  let a = '';
  for (let i = 0; i < 100_000; i++) {
    a += 1;
  }
  return a;
};

export const 객체배열변환함수 = (obj) => {
  const {
    first,
    second,
    third,
    fourth,
    fifth,
    sixth,
    seventh,
    eighth,
    ninth,
    tenth,
    eleventh,
    twelfth,
    thirteenth,
    fourteenth,
    fifteenth,
  } = obj;

  const newArr = [
    first,
    second,
    third,
    fourth,
    fifth,
    sixth,
    seventh,
    eighth,
    ninth,
    tenth,
    eleventh,
    twelfth,
    thirteenth,
    fourteenth,
    fifteenth,
  ];

  return newArr;
};

export const 강의시작쿠키저장함수 = (duratuon) => {
  const options = {
    path: '/',
    expires: new Date(new Date().getTime() + 1000 * duratuon),
  };
  setCookie('alarm', true, options);
};

export const 시간변환함수분을시간으로 = (minute) => {
  const hour = Math.floor(minute / 60);
  const min = minute % 60;
  const newData = {
    hour,
    min,
  };
  return newData;
};

export const countValues = (obj) => {
  return Object.values(obj).reduce((acc, value) => {
    acc[value] = (acc[value] || 0) + 1;
    return acc;
  }, {});
};

export const 배열15아이템 = [
  'first',
  'second',
  'third',
  'fourth',
  'fifth',
  'sixth',
  'seventh',
  'eighth',
  'ninth',
  'tenth',
  'eleventh',
  'twelfth',
  'thirteenth',
  'fourteenth',
  'fifteenth',
];

export const 매우맛있는함수 = (str) => {
  return str ? str : '-';
};

export const 선능지미함수 = (str, arg) => (str ? str : arg);

export const HIDDEN = 'HIDDEN';

/**
 * openWindowPopup
 *
 * @param {string} item - url
 * @returns {void}
 */
export const openWindowPopup = (item) => {
  window.open(`/${item}`);
};

export const 가장큰숫자를만드는함수 = (number) => {
  if (typeof number !== 'number') return 0;
  return BigInt(Number.MAX_SAFE_INTEGER);
};

export const 팝업에러 = 'popup-001';

export const 어드민로그인아이디 = 'administrator';

export const DEFAULT_DATETIME_FORMAT = 'YYYY-MM-DD h:mm A';
