package kr.lineedu.lms.config.constant;

public class CanvasConstants {

    public static String canvasUrl = CanvasConstantUtil.CANVAS_URL;

    public final static class ACCOUNT {
        // 학과 정보
        public final static String ENROLLMENT_ROOT_ACCOUNT_INFO_API = "/accounts/{account_id}?per_page=200";
        // 유저 삭제
        public final static String DELETE_CANVAS_USER = "accounts/{account_id}/users/{user_id}";
        // 단과대학과 과 정보
        public final static String GET_DIVISION_AND_SUBDIVISION = "/accounts/{account_id}/sub_accounts/?recursive=true&per_page=50&page={page}";
        // 단과대학 정보
        public final static String GET_DIVISION = "/accounts/{account_id}/sub_accounts/?recursive=false&per_page=200";
        public static final String GET_COURES_BY_ACCOUNT_ID = "/accounts/{account_id}/courses?enrollment_term_id={term_id}&per_page=100&page={page}&include[]=teachers&include[]=account_name&include[]=course_image&sort=id&order=desc";
        public static final String GET_COURES_BY_ACCOUNT_ID_ITER = "/accounts/{account_id}/courses?per_page=50&page={page}&include[]=course_image";
    }

    public final static class ANNOUNCEMENT {
        // 과목에 등록된 공지사항 정보(최근만 불러옴)
        public final static String COURSE_NOTICE_LIST_INFO_API = "/announcements?context_codes=course_{course_id}&per_page=200";
//        public final static String ALL_NOTICE_LIST_INFO_API = "/announcements?context_codes=course_{course_id}";
        public final static String ALL_NOTICE_LIST_INFO_API = "courses/{course_id}/discussion_topics?only_announcements=true&per_page=40&page=1&filter_by=all&no_avatar_fallback=1&include%5B%5D=sections_user_count&include%5B%5D=sections";
        // 과목에 등록된 공지사항을 유저가 읽었는지 확인하기 위한 정보
        public final static String COURSE_NOTICE_INFO_WITH_USER_API = "/courses/{course_id}/discussion_topics?only_announcements=true&per_page=200&page=1&filter_by=all&as_user_id={user_id}";
    }

    public final static class ASSIGNMENT {
        // 과목의 과제 정보
        public final static String COURSE_ASSIGNMENT_LIST_INFO_API = "/courses/{course_id}/assignments?per_page=200";
        // 유저가 수강하고 있는 과목들의 과제 정보
        public final static String USER_ENROLLMENT_ASSIGNMENT_LIST_INFO_API = "/courses/{course_id}/assignments?per_page=200";
        //과목에 해당하는 유저 과제(퀴즈포함) 정보
        public static final String GET_ALL_ASSIGNMENTS = "/users/{user_id}/courses/{course_id}/assignments?per_page=200&include[]=submission";
        //for portal
        public final static String ALL_COURSE_ASSIGNMENT_API = "/courses/{course_id}/assignments";
        public final static String GET_COURSE_ASSIGNMENT_API = "/courses/{course_id}/assignments?include[]=all_dates&include[]=submission&include[]=overrides";
        //for portal
        public final static String SPECIFIC_COURSE_ASSIGNMENT_API = "/courses/{course_id}/assignments/{id}?include[]=overrides";

        public final static String ASSIGNMENT_OVERRIDES = "/courses/{course_id}/assignments/{assignment_id}/overrides";

        public final static String GET_ASSIGNMENT_OVERRIDES = "/courses/{course_id}/assignments/{assignment_id}/overrides?per_page=50";

        public final static String DELETE_ASSIGNMENT_OVERRIDES = "/courses/{course_id}/assignments/{assignment_id}/overrides/{id}";

        public final static String UPDATE_ASSIGNMENT_OVERRIDES = "/courses/{course_id}/assignments/{assignment_id}/overrides/{id}";

        public final static String ASSIGNMENT_DELETE = "/courses/{course_id}/assignments/{id}";

        public final static String ASSIGNMENT_UPDATE = "/courses/{course_id}/assignments/{id}";

        public final static String CREATE_CONTENT_MIGRATION="/courses/{course_id}/content_migrations";

        public final static String QUERY_PROGRESS="/progress/{id}";

        public final static String DUPLICATE_ASSIGNEMENT_API="/courses/{course_id}/assignments/{assignment_id}/duplicate";

        public final static String GET_SINGLE_SUBMISSION="/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}";
        public static final String GET_ALL_SUBMISSION =
                "/courses/{course_id}/assignments/{assignment_id}/submissions";
        public static final String GET_SINGLE_ASSIGNMENT="/courses/{course_id}/assignments/{id}?all_dates=true&include[]=submission&include[]=assignment_visibility&include[]=overrides&incluce[]=observed_users&include[]=can_edit&include[]=score_statistics&include[]=ab_guid";
    }

    public final static class ACCOUNT_NOTIFICATION {
        public final static String CREATE_GLOBAL_ACCOUNT_NOTIFICATION = "/accounts/{account_id}/account_notifications";

        public final static String GET_SINGLE_ACCOUNT_NOTIFICATION = "/accounts/{account_id}/account_notifications/{id}";

        public final static String DELETE_ACCOUNT_NOTIFICATION = "/accounts/{account_id}/account_notifications/{id}";

        public final static String UPDATE_ACCOUNT_NOTIFICATION = "/accounts/{account_id}/account_notifications/{id}";

        public static final String GET_ACCOUNT_NOTIFICATIONS = "/accounts/{account_id}/account_notifications";
    }

    public final static class ASSIGNMENT_GROUP {
        // 과제의 그룹에 대한 정보
        public static final String GET_ASSIGNMENT_GROUPS = "/courses/{course_id}/assignment_groups?exclude_assignment_submission_types[]=wiki_page&exclude_response_fields[]=description&exclude_response_fields[]=rubric&include[]=assignments&include[]=discussion_topic&include[]=all_dates&include[]=module_ids&override_assignment_dates=false&per_page=200";
        // 과제의 그룹 수정
        public static final String MODIFY_ASSIGNMENT_GROUP = "/courses/{course_id}/assignment_groups/{assignment_group_id}";

        public static final String CREATE_ASSIGNMENT_GROUP = "/courses/{course_id}/assignment_groups";

        public static final String DELETE_ASSIGNMENT_GROUP = "/courses/{course_id}/assignment_groups/{assignment_group_id}";

    }

    public final static class COLOR{
        public final static String COLOR_LIST_API = "/users/{user_id}/colors";

        public final static String COLOR_UPDATE = "/users/{user_id}/colors/{asset_string}";
    }

    public final static class COURSE {
        // 유저가 수강하고 있는 과목의 정보
        public final static String USER_ENROLLMENT_LIST_API = "/users/{user_id}/courses?state[]=available&per_page=200";
        // 유저가 등록되어 있는 게시된 과목의 정보와 총 학생 수
        public final static String USER_ENROLLMENT_DETAIl_INFO_LIST_API = "/users/{user_id}/courses?include[]=total_students&state[]=available&per_page=200";
        // 모든 과목정보
        public final static String COURSE_ALL_LIST = "/courses?enrollment_type=teacher&include[]=teachers&per_page=100";
        // 과목에 등록된 유저 목록
        public final static String COURSE_ENROLLMENT_USER_LIST_INFO_API = "/courses/{course_id}/users?include[]=enrollments&per_page=200";
        // 과목을 수강하는 학생 정보
        public final static String COURSE_ENROLLMENT_STUDENT_LIST_INFO_API = "/courses/{course_id}/students?per_page=200";
        // 특정 과목의 정보
        public final static String COURSE_DETAIL_INFO_API = "/courses/{course_id}?per_page=200";

        public final static String COURSE_DETAIL = "/courses/{course_id}?include[]=account&include[]=total_students&include[]=teachers&include[]=current_grading_period_scores&include[]=grading_periods&include[]=course_image&include[]=public_description";

        // 과목별 총학생수
        public final static String COURSE_STUDENT_COUNT = "/users/{user_id}/courses/?include[]=total_students&state[]=available&per_page=200";

        public final static String COURSES_BY_USER = "/users/{user_id}/courses?include[]=teachers&include[]=public_description&include[]=course_progress&include[]=account&include[]=course_image&include[]=term&per_page=50";
        // 과목 진도율
        public final static String COURSE_PROGRESS = "/courses/{course_id}/users/{user_id}/progress?per_page=200";

        public static final String GET_COURSE_INFO = "/courses/{course_id}?include[]=term";

        //과목에 해당하는 유저 과제(퀴즈포함) 정보
        public static final String GET_ALL_ASSIGNMENTS = "/users/{user_id}/courses/{course_id}/assignments";

        // upcoming assignments for portal
        public static final String GET_ALL_UPCOMING_ASSIGNMENTS = "/courses/{course_id}/assignments?bucket=upcoming&order_by=due_at&include[]=submission";

        public static final String SELF_ENROLLMENT_INFO_LIST_API = "/courses/{course_id}/enrollments?user_id=self&type[]=StudentEnrollment&state[]=active";

        public final static String COURSE_ALL_STUDENT_LIST_API = "/courses/{course_id}/students";

        public final static String COURSE_ALL_USERS = "/courses/{course_id}/users?per_page=200&include[]=avatar_url&include[]=enrollments&enrollment_state[]=active&enrollment_state[]=inactive&enrollment_state[]=invited&enrollment_state[]=completed";

        public static final String TERMS = "/accounts/{id}/terms";

        public final static String COURSE_SUMMARY_API = "/courses/{course_id}/activity_stream/summary";

        public final static String COURSE_CREATE="/accounts/{account_id}/courses";

        public final static String COURSE_DELETE="/courses/{course_id}";

        public final static String COURSE_UPDATE="/courses/{id}";

        public final static String SETTINGS="/courses/{course_id}/settings";

        public final static String FEATURES ="/courses/{course_id}/features";

        public final static String CHANGE_FEATURE_FLAG = "/courses/{course_id}/features/flags/{feature}";

        public final static String ENROLLMENTS="/courses/{course_id}/enrollments?state[]=active&state[]=inactive&state[]=creation_pending&state[]=invited&state[]=rejected&state[]=completed";

        public final static String SEARCH_USER_BY_SEARCH_TERM="/accounts/self/users";
        
        // Search users in course by search term
        public final static String SEARCH_COURSE_USERS = "/courses/{course_id}/users?search_term={search_term}&include[]=avatar_url&per_page=300";

        public final static String ENROLL_USER ="/sections/{section_id}/enrollments";
        public static final String Search_SHARE_USER = "courses/{course_id}/content_share_users";
        public static final String GET_SUB_ACC = "/accounts/{account_id}/sub_accounts?include[]=course_count&include[]=sub_account_count";
        public static final String GET_MANAGEABLE_ACC="/manageable_accounts";
        public static final String CREATE_SUB_ACC="/accounts/{account_id}/sub_accounts";
        public static final String UPDATE_SUB_ACC="/accounts/{account_id}";
        public static final String DELETE_SUB_ACC="/accounts/{account_id}/sub_accounts/{id}";
        public static final String UPDATE_COURSE_SETTINGS="/courses/{course_id}/settings";
    }

    public final static class DISCUSSION_TOPIC {
        // 과목에 등록된 모든 공지사항 정보(기한 무관하게 모든 공지를 불러옴)
        public final static String COURSE_ALL_NOTICE_LIST_INFO_API = "courses/{course_id}/discussion_topics?only_announcements=true&per_page=200&page=1&filter_by=all&as_user_id={user_id}";
        // 과목에 등록된 회의 정보
        public final static String GET_DISCUSSION_INFO = "/courses/{course_id}/discussion_topics&per_page=10000";


    }

    public final static class ENROLLMENT_TERM {
        // 학기
        public final static String TERM_LIST_INFO_API = "/accounts/{account_id}/terms?per_page=200";
    }

    public final static class ENROLLMENT {
        public final static String STATE = "/courses/{course_id}/enrollments/{id}";

        public final static String REACTIVATE = "/courses/{course_id}/enrollments/{id}/reactivate";

        public final static String COURSE_ENROLLMENT = "/courses/{course_id}/enrollments";
        public static final String ACCEPT_INVITE = "/courses/{course_id}/enrollments/{id}/accept";
        public static final String REJECT_INVITE = "/courses/{course_id}/enrollments/{id}/reject";
    }

    public final static class GRADING_STANDARD {
        public final static String CREATE_GRADING_STANDARD = "/courses/{course_id}/grading_standards";
        public static final String GET_SINGLE_GRADING_STANDARD = "/courses/{course_id}/grading_standards/{grading_standard_id}";
        public static final String GET_GRADING_STANDARDS = "/courses/{course_id}/grading_standards/{grading_standard_id}";
    }


    public final static class MODULE {
        // 해당 과목에서 해당 유저가 완료한 모듈 정보
        public final static String USER_SUBJECT_STATE_LIST_INFO_API = "/courses/{course_id}/modules?include[]=items&content_details&per_page=200";
        // 과목 모듈 정보와 모듈에 속해 있는 과제 및 자료 정보
        public final static String USER_ENROLLMENT_MODULE_LIST_INFO_API = "/courses/{course_id}/modules?include[]=items&per_page=200";
        // 과목 모듈 정보와 학생 아이디로 View 여부를 확인하기 위한 정보
        public final static String USER_ENROLLMENT_MODULE_LIST_API = "/courses/{course_id}/modules?include[]=items&student_id={student_id}&per_page=200";
        // 과목에 등록된 유저들 정보
        public static final String MODULE_LIST = "/courses/{course_id}/modules?per_page=200";

        // 모듈 차시(item) 리스트
        public final static String ITEM_LIST = "/courses/{course_id}/modules/{module_id}/items?student_id={student_id}&per_page=200";
        // 모듈 완료 정보
        public final static String USER_MODULE_COMPLETE_INFO_API = "/courses/{course_id}/modules?student_id={student_id}&per_page=200";

        public final static String ITEMS_LIST = "/courses/{course_id}/modules/{module_id}?include[]=items";
        // 학생의 모듈별 완료 여부 정보
        public final static String STUDENT_MODULE_COMPLETE_INFO_API = "/courses/{course_id}/modules/{module_id}?include[]=content_details";
    }

    public final static class SUBMISSION {
        // 과제 제출일 정보
        public final static String USER_ASSIGNMENT_COMPLETE_INFO_API = "/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}?per_page=200";
        // 과제의 점수에 대한 정보
        public static final String GET_ASSIGNMENT_SCORE = "/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}?per_page=200";
        // 특정 과목의 특정 과제 채점 정보
        public final static String ASSIGNMENT_GRADE_INFO_API = "/courses/{course_id}/assignments/{assignment_id}/submission_summary?per_page=200";
        // 특정 과목의 특정 과제의 학생별 과제 진행상태 정보(미제출/제출/채점완료)
        public final static String ASSIGNMENT_SUBMISSION_WORKFLOW_INFO = "/courses/{course_id}/assignments/{assignment_id}/submissions?per_page=200";

        public final static String NEW_SUBMISSION_API = "/courses/{course_id}/assignments/{assignment_id}/submissions";

        public final static String USER_SUBMISSION_INFO = "/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}?include[]=submission_comments";

        public final static String ALL_SUBMISSION = "/courses/{course_id}/students/submissions?per_page=200";

        public final static String USER_GRADED_SUBMISSIONS = "/users/{user_id}/graded_submissions";

    }

    public final static class USER {
        //유저 정보 저장
        public final static String CREATE_CANVAS_USER_API = "/accounts/{account_id}/users";
        // 기존 사용자를 관리자 계정으로 변경
        public final static String UPDATE_TO_ADMIN_API = "/accounts/{account_id}/admins";
        // 관리자 role 부여
        public final static String UPDATE_TO_ADMIN_ROLE_API = "/accounts/1/admins";
        // 관리자 role 삭제
        public final static String DELETE_ADMIN_ROLE = "/accounts/1/admins/{user_id}?role_id={role_id}";
        // 관리자 role id
        public final static int CANVAS_ADMIN_ROLE_ID = 25;

        public final static String USERS_API = "/accounts/{account_id}/users?include[]=email&per_page=50";

        public final static String PAGE_USERS_API = "/accounts/{account_id}/users?[]=last_login&include[]=login_id&include[]=email";
        public static final String USER_SETTINGS_API = "/users/{id}/settings";
        
        // Get user by ID
        public static final String GET_USER_BY_ID = "/users/{user_id}?include[]=avatar_url";
    }

    public final static class UTIL {
        // 해당 과목의 강의실 주소
        public final static String COURSE_LECTURE_ROOM_URL = canvasUrl + "/courses/";
        // 학교의 달력 정보(학사일정 등)
        public final static String ACCOUNT_CALENDAR_INFO_API = "/calendar_events?context_codes[]=account_{account_id}&start_date={start_at}&end_date={end_at}&include[]=web_conference&include[]=series_natural_language&per_page=200";
        // 개인 일정 달력 정보(개인 이벤트 등)
        public final static String USER_EVENT_CALENDAR_INFO_API = "/calendar_events?context_codes[]=user_{user_id}&start_date={start_at}&end_date={end_at}&include[]=reserved_times&include[]=web_conference&include[]=series_natural_language&per_page=200";
        // 개인이 수강하고 있는 과목에 대한 달력 정보(상담 시간 등)
        public final static String USER_ENROLLMENT_CALENDAR_INFO_API = "/calendar_events?context_codes[]=course_{course_id}&start_date={start_at}&end_date={end_at}&include[]=reserved_times&include[]=web_conference&include[]=series_natural_language&per_page=200";
        // 개인이 수강하고 있는 과목 과제에 대한 달력 정보
        public final static String USER_ASSIGNMENT_CALENDAR_INFO_API = "/calendar_events?type=assignment&context_codes[]=user_{user_id}&context_codes[]=course_{course_id}&start_date={start_at}&end_date={end_at}&include[]=reserved_times&include[]=web_conference&include[]=series_natural_language&per_page=200";
        // 개인에게 온 메세지 정보
        public final static String USER_MESSAGE_INFO_API = "/conversations?scope=inbox&filter_mode=and&include_private_conversation_enrollments=false&as_user_id={user_id}&per_page=200";
    }

    public final static class ROLE {
        public final static String ROLE_LIST = "/accounts/1/roles";
    }

    public final static class CALENDAR_EVENT {
        public final static String CALENDAR_EVENTS = "calendar_events";
        public final static String SPECIFIC_CALENDAR_EVENT = "calendar_events/{eventId}";
    }

    public final static class PERFORMANCE_STATUS{
        public final static String COURSE_ENROLLMENT_INFO_API = "/courses/{course_id}/enrollments";
        public final static String COURSE_SUBMISSIONS_INFO_API = "/courses/{course_id}/students/submissions?response_fields[]=score&response_fields[]=submission_type&response_fields[]=user_id&response_fields[]=assignment_id";
        public final static String ASSIGNMENT_GROUP_API = "/courses/{course_id}/assignment_groups?include[]=assignments";
    }

    public final static class DASHBOARD_CARD{
        public final static String GET_DASHBOARD_CARDS = "/dashboard/dashboard_cards";
        public final static String GET_USER_ENROLLMENT = "/users/{user_id}/enrollments?per_page=100";
    }

    public final static class PLANNER_ITEMS{
        public final static String GET_PLANNER_ITEMS_API = "/planner/items";
    }

    public final static class PLANNER_NOTES{
        public final static String PLANNER_NOTES_API = "/planner_notes";
        public final static String SPECIFIC_PLANNER_NOTE_API = "/planner_notes/{id}";
    }

    public final static class QUIZZES{
        public final static String QUIZ_API = "/courses/{course_id}/quizzes";

        public final static String GET_QUIZ_API = "/courses/{course_id}/quizzes?per_page=550";
        public final static String SPECIFIC_QUIZ_API = "/courses/{course_id}/quizzes/{id}";
        public final static String QUIZ_SUBMISSION = "/courses/{course_id}/quizzes/{id}/submissions";
        public final static String QUIZ_SUBMISSIONS = "/courses/{course_id}/quizzes/{id}/submissions?include[]=submission&include[]=quiz&include[]=submission_history";
        public final static String SPECIFIC_QUIZ_SUBMISSION = "/courses/{course_id}/quizzes/{id}/submissions/{id}";
        public final static String QUIZ_DETAILS = "/courses/{course_id}/quizzes/{id}";
        public final static String CREATE_CONTENT_MIGRATION="/courses/{course_id}/content_migrations";
        public final static String QUERY_PROGRESS="/progress/{id}";

        public static final String GET_SINGLE_QUIZ = "/courses/{course_id}/quizzes/{id}";
        public static final String TAKE_QUIZ = "/courses/{course_id}/quizzes/{quiz_id}/submissions";
        public static final String GET_QUIZ_QUESTIONS = "/quiz_submissions/{quiz_submission_id}/questions";
        public static final String COMPLETE_SUBMISSION = "/courses/{course_id}/quizzes/{quiz_id}/submissions/{id}/complete";
        public static final String GET_SINGLE_QUIZ_SUBMISSION = "/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}?include[]=submission_history&include[]=user";
        public static final String EXPORT_CONTENT="/courses/{course_id}/content_exports";
        public static final String IMPORT_CONTENT="/courses/{course_id}/content_migrations";
        public static final String QUIZ_SUBMISSION_TIME = "/courses/{course_id}/quizzes/{quiz_id}/submissions/{id}/time";
        public static final String GET_EXPORT_CONTENT="/courses/{course_id}/content_exports";
        public static final String GET_IMPORT_CONTENT="/courses/{course_id}/content_migrations?per_page=50";
        public static final String GET_EXPORT_PROGRESS="/courses/{course_id}/content_exports/{content_export_id}";
        public static final String ANSWER_QUIZ_SUBMISSION_QUESTIONS = "/quiz_submissions/{quiz_submission_id}/questions";
        public static final String GET_MIGRATION_ISSUE="/courses/{course_id}/content_migrations/{id}/migration_issues";
        public static final String GET_SELECTIVE_DATA="/courses/{course_id}/content_migrations/{c_id}/selective_data?per_page=50";
        public static final String GET_PROGRESS_MIGRATION="/progress/{progressId}";
        public static final String UPDATE_SELECTIVE_DATA="/courses/{course_id}/content_migrations/{id}";

        public static final String CREATE_EXTENSION = "/courses/{course_id}/quizzes/{quiz_id}/extensions";
        public static final String UPDATE_QUIZ_SUBMISSION = "/courses/{course_id}/quizzes/{quiz_id}/submissions/{id}";
        public static final String GET_QUIZ_SUBMISSION_BY_ATTEMPT = "/courses/{course_id}/quizzes/{quiz_id}/submissions/{id}";
        public static final String UPLOAD_QUIZ_SUBMISSION_FILE_FIRST_STEP = "/courses/{course_id}/quizzes/{quiz_id}/submissions/self/files";
        public static final String UPLOAD_QUIZ_SUBMISSION_FILE_SECOND_STEP = "/courses/{course_id}/quizzes/{quiz_id}/submissions/self/files/{file_id}";
    }

    public final static class QUIZ_QUESTIONS {
        public final static String QUIZ_QUESTIONS_API = "/courses/{course_id}/quizzes/{quiz_id}/questions?per_page=100";

        public final static String QUIZ_DELETE = "/courses/{course_id}/quizzes/{quiz_id}/questions/{id}";

        public final static String QUIZ_UPDATE = "/courses/{course_id}/quizzes/{quiz_id}/questions/{id}";

        public final static String QUESTION_GROUP = "/courses/{course_id}/quizzes/{quiz_id}/groups";

        public final static String GET_QUESTION_GROUP="courses/{course_id}/quizzes/{quiz_id}/groups/{id}";
        public final static String UPDATE_QUESTION_GROUP="courses/{course_id}/quizzes/{quiz_id}/groups/{id}";
        public final static String DELETE_QUESTION_GROUP="/courses/{course_id}/quizzes/{quiz_id}/groups/{id}";
    }

    public final static class MODULES{
        public final static String MODULE_API = "/courses/{course_id}/modules?include[]=items&include[]=content_details";
        public final static String SPECIFIC_MODULE_API = "/courses/{course_id}/modules/{id}";
        public final static String MODULE_ITEM_API = "/courses/{course_id}/modules/{id}/items";
        public final static String SET_ENABLE_COURSE_PACING_API = "/courses/{course_id}";
        public final static String NEW_COURSE_PACING_API = "/courses/{course_id}/course_pacing/new";
        public final static String SECTION_COURSE_PACING_API = "/courses/{course_id}/course_pacing/new?course_section_id={section_id}";
        public final static String USERS_COURSE_PACING_API = "courses/{course_id}/enrollments?type[]=StudentEnrollment&state[]=active";
        public final static String USER_COURSE_PACING_API = "/courses/{course_id}/course_pacing/new?enrollment_id={user_id}";
        public final static String COURSE_PACING_API = "/courses/{course_id}/course_pacing";
        public final static String SPECIFIC_COURSE_PACING_API = "/courses/{course_id}/course_pacing/{id}";
    }

    public final static class GROUPS{
        public final static String GROUP_API = "/groups";
        public final static String SPECIFIC_GROUP_API = "/groups/{group_id}";
        public final static String GROUP_MEMBERSHIP_API = "/groups/{group_id}/memberships";
        public final static String SPECIFIC_GROUP_MEMBERSHIP_API = "/groups/{group_id}/memberships/{membership_id}";
        public final static String GROUP_FOR_USER_API = "/users/self/groups";
        public final static String GROUP_MEMBERS_API = "/groups/{group_id}/users";
        public final static String CREATE_GROUP_DISCUSSION = "/groups/{group_id}/discussion_topics";
    }
    public final static class GROUP_SET{
        public final static String CREATE_GROUP_SET ="courses/{course_id}/group_categories";
        public final static String UPDATE_GROUP_SET ="group_categories/{group_category_id}";
        public final static String DELETE_GROUP_SET ="group_categories/{group_category_id}";
    }

    public final static class DISCUSSION_TOPICS{
        public final static String COURSE_DISCUSSION_API = "/courses/{course_id}/discussion_topics?include[]=sections&include_assignment=true&per_page=300";
        public final static String GROUP_DISCUSSION_API = "/groups/{group_id}/discussion_topics?include[]=sections&include_assignment=true";
        public final static String NEW_GROUP_DISCUSSION_API = "/groups/{group_id}/discussion_topics";
        public final static String NEW_COURSE_DISCUSSION_API = "/courses/{course_id}/discussion_topics";
        public final static String SPECIFIC_COURSE_DISCUSSION_API = "/courses/{course_id}/discussion_topics/{topic_id}";
        public final static String UPDATE_COURSE_DISCUSSION="/courses/{course_id}/discussion_topics/{topic_id}";
        public final static String SPECIFIC_GROUP_DISCUSSION_API = "/groups/{group_id}/discussion_topics/{id}";
        public final static String ENTRIES_COURSE_DISCUSSION_API = "/courses/{course_id}/discussion_topics/{id}/entries";
        public final static String ENTRIES_GROUP_DISCUSSION_API = "/groups/{group_id}/discussion_topics/{id}/entries";
        public final static String MARK_DISCUSSION_READ = "/{type}/{type_id}/discussion_topics/{id}/read";
        public final static String GET_DISCUSSION_DETAILS = "/courses/{course_id}/discussion_topics/{topic_id}";
        public final static String GET_REPLY = "/courses/{course_id}/discussion_topics/{topic_id}/entries/{entry_id}/replies";
        public final static String DELETE_COURSE_DISCUSSION = "/courses/{course_id}/discussion_topics/{topic_id}";
        public final static String DELETE_GROUP_DISCUSSION = "groups/{group_id}/discussion_topics/{topic_id}";
        public final static String CREATE_CONTENT_MIGRATION="/courses/{course_id}/content_migrations";
        public final static String QUERY_PROGRESS="/progress/{id}";
        public static final String SPECIFIC_COURSE_DISCUSSION_REORDER_API = "/courses/{course_id}/discussion_topics/reorder";
        public static final String DUPLICATE_COURSE_DISCUSSION_API = "/courses/{course_id}/discussion_topics/{topic_id}/duplicate";
        public static final String GET_FULL_DISCUSSION_TOPIC = "/courses/{course_id}/discussion_topics/{topic_id}/view";
        public static final String POST_ENTRY_TO_DISCUSSION_TOPIC = "/courses/{course_id}/discussion_topics/{topic_id}/entries";
        public static final String POST_REPLY_TO_ENTRY = "/courses/{course_id}/discussion_topics/{topic_id}/entries/{entry_id}/replies";
        public static final String MARK_ENTRY_READ = "/courses/{course_id}/discussion_topics/{topic_id}/entries/{entry_id}/read";
        public static final String RATING_ENTRY = "/courses/{course_id}/discussion_topics/{topic_id}/entries/{entry_id}/rating";
        public static final String READ_DISCUSSION = "/courses/{course_id}/discussion_topics/{topic_id}/read";
        public static final String UPDATE_ENTRY = "/courses/{course_id}/discussion_topics/{topic_id}/entries/{id}";
    }

    public final static class SECTIONS{
        public final static String SECTION_API = "/courses/{course_id}/sections?include[]=students&include[]=total_students&include[]=enrollments&per_page=50";
        public final static String CREATE = "/courses/{course_id}/sections";
        public final static String DELETE = "/sections/{section_id}";
        public final static String UPDATE = "/sections/{section_id}";
        public final static String GET_STUDENT_BY_SECTION="/sections/{id}?include[]=students&include[]=avatar_url";
        public final static String ENROLLED_USERS="/sections/{section_id}/enrollments?per_page=100&include[]=avatar_url";
        public final static String ENROLL_USER="/sections/{section_id}/enrollments";
        public static final String SECTION_INFO = "/courses/{course_id}/sections/{id}";
        public static final String GET_USERS_WITH_ENROLLMENT_STATUS="/sections/{id}/enrollments?include[]=can_be_removed&page=1&per_page=50";
    }

    public final static class FOLDER{
        public final static String FOLDER_BY_PATH = "/courses/{course_id}/folders/by_path/{full_path}";
        public final static String ALL_SUB_FOLDERS_API = "/folders/{folder_id}/folders?per_page=10";
        public final static String ALL_FILES_FOLDERS_API = "/folders/{folder_id}/files?include[]=user&include[]=usage_rights&include[]=enhanced_preview_url&include[]=context_asset_string";
        public final static String SPECIFIC_FILE_API = "/files/{file_id}";
        public final static String UPLOAD_FILE_API = "/folders/{folder_id}/files";
//        public final static String UPLOAD_SUBMISSION_FILE_API = "/courses/:course_id/assignments/:assignment_id/submissions/self/files";
        public final static String UPLOAD_SUBMISSION_FILE_API = "/courses/{course_id}/assignments/{assignment_id}/submissions/{user_id}/files";
        public final static String UPLOAD_COURSE_FILE = "/courses/{course_id}/files";
        public final static String FILES_FOR_COURSES = "/courses/{course_id}/files?include[]=user";
        public final static String FOLDERS_FOR_COURSES = "/courses/{course_id}/folders?include[]=user&per_page=50";
        public final static String FOLDERS_FOR_GROUP = "/groups/{group_id}/folders?include[]=user";
        public final static String FILES_FOR_USER = "/users/{user_id}/files?include[]=user";
        public final static String FOLDERS_FOR_USER = "/users/{user_id}/folders?include[]=user";
        public final static String FOLDERS_FOR_ACCOUNT = "/accounts/1/folders";
        public final static String FILES_FOR_FOLDER_API = "/folders/{folder_id}/files?include[]=user";
        public final static String ACTUAL_UPLOAD = "/files_api";
        public final static String DELETE_FILE = "/files/{file_id}";
        public final static String CREATE_SUCCESS="/files/{file_id}/create_success";

    }

    public final static class PAGES{
        public final static String PAGE_API = "/courses/{course_id}/pages?per_page=200";
        public final static String PUBLISHED_PAGE_API = "/courses/{course_id}/pages?per_page=200&published=true";
        public final static String CREATE = "/courses/{course_id}/pages";
        public final static String UPDATE = "/courses/{course_id}/pages/{id}";
        public final static String DELETE = "/courses/{course_id}/pages/{id}";
        public final static String DETAILS = "/courses/{course_id}/pages/{id}";
    }

    public final static class CONVERSATION{
        public final static String CONVERSATION_API = "/conversations";
        public final static String SPECIFIC_CONVERSATION_API = "/conversations/{id}";
        public final static String ADD_MESSAGE_API = "/conversations/{id}/add_message";
        public final static String GET_UNREAD_COUNT = "/conversations/unread_count";
        public final static String MARK_ALL_READ = "/conversations/mark_all_as_read";
    }

    public final static class BLANK_OUT{
        public final static String BLANK_OUT_API = "/courses/{course_id}/blackout_dates";
        public final static String SPECIFIC_BLANK_OUT_API = "/courses/{course_id}/blackout_dates/{id}";
    }

    public final static class NOTIFICATION_PREFERENCES{
        public final static String ALL = "users/{user_id}/communication_channels/{communication_channel_id}/notification_preferences";
        public final static String CATEGORIES = "users/{user_id}/communication_channels/{communication_channel_id}/notification_preference_categories";
        public final static String MULTI_UPDATE = "users/self/communication_channels/{communication_channel_id}/notification_preferences";
    }

    public final static class COMMUNICATION_CHANNEL{
        public final static String GET_COMMUNICATION_CHANNEL = "/users/{user_id}/communication_channels";
        public final static String CREATE_COMMUNICATION_CHANNEL = "/users/{user_id}/communication_channels";
    }

    public final static class TABS{
        public final static String GET_TABS = "/courses/{course_id}/tabs";
        public final static String UPDATE_TAB = "/courses/{course_id}/tabs/{tab_id}?position={position}&hidden={is_hidden}";
    }

    public final static class CONTENT_SHARE{
        public final static String CREATE = "/users/self/content_shares";
        public final static String SENT_LIST="/users/self/content_shares/sent?per_page=100";
        public final static String RECEIVE_LIST="/users/self/content_shares/received?per_page=100";
        public final static String UPDATE_READ_STATE = "/users/self/content_shares/{content_share_id}";
    }

    public final static class RUBRIC{
        public final static String GET_COURSE_RUBRICS = "/courses/{course_id}/rubrics?per_page=200";
        public static final String CREATE_RUBRIC = "/courses/{course_id}/rubrics";
        public static final String DELETE_RUBRIC = "/courses/{course_id}/rubrics/{rubric_id}";
        public static final String UPDATE_RUBRIC = "/courses/{course_id}/rubrics/{rubric_id}";
        public static final String GET_COURSE_RUBRIC = "/courses/{course_id}/rubrics/{id}?include[]=associations&include[]=assessments";
    }
}
