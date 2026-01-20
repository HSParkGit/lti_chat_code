package kr.lineedu.lms.global.error.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //Util
    AES_ERROR(HttpStatus.OK, "U001", "encrypt error"),
    BUSINESS_EXCEPTION(HttpStatus.OK, "U002", "business exception"),
    NOT_FOUND_DIVISION(HttpStatus.OK, "U003", "not found division from canvas exception"),
    INTERNAL_ERROR(HttpStatus.OK, "U004", "server error"),

    //Course
    PROGRESS_EXCEPTION(HttpStatus.OK, "C001", "progress exception, check course module requirement settings"),
    NOT_FOUND_COURSE_EXCEPTION( HttpStatus.NOT_FOUND, "C002" , "not found course exception"),
    NOT_FOUND_COURSE_STUDENT_EXCEPTION(HttpStatus.NOT_FOUND, "C003" , "not found course student exception"),

    //User
    NOT_FOUND_USER_EXCEPTION(HttpStatus.OK, "U001", "not found user exception"),
    NOT_FOUND_USER_AUTHORITY(HttpStatus.OK, "U002", "not found user authority"),
    NOT_FOUND_HAKSA_USER_EXCEPTION(HttpStatus.OK, "U002", "not found haksa user exception"),
    DUPLICATE_USER_NUMBER_EXCEPTION(HttpStatus.OK, "U003", "duplicate user number exception"),
    NOT_LOGGED_IN(HttpStatus.OK, "U004", "not logged in exception"),
    INVALID_SORT_OPTION_OF_STUDENT(HttpStatus.NOT_FOUND, "U005", "invalid sort option of student exception"),

    //Admin
    FAILED_RESET_PASSWORD_EXCEPTION(HttpStatus.OK, "AD001", "failed reset password exception"),
    NOT_FOUND_ROLE_EXCEPTION(HttpStatus.OK, "AD002", "not found role exception"),

    //Attendance
    ATTENDANCE_ONLINE_SETTING_NOT_FOUND_EXCEPTION(HttpStatus.OK, "AT001", "user online setting not found exception"),
    ATTENDANCE_DUPLICATE_EXCEPTION(HttpStatus.OK, "AT002", "attendance duplicate exception"),
    COURSE_ATTENDANCE_NOT_FOUND_EXCEPTION(HttpStatus.OK, "AT003", "course attendance not found exception"),
    USER_ATTENDANCE_NOT_FOUND_EXCEPTION(HttpStatus.OK, "AT004", "user attendance not found exception"),
    ATTENDANCE_ONLINE_SETTING_NOT_FOUND(HttpStatus.OK, "AT005", "attendance online setting not found exception"),
    ATTENDANCE_SETTING_NOT_FOUND(HttpStatus.OK, "AT006", "attendance setting not found exception"),

    //Panopto
    PANOPTO_FOLDER_NOT_FOUND_EXCEPTION(HttpStatus.OK, "P001", "panopto folder not found exception"),
    PANOPTO_ACCESS_TOKEN_EXCEPTION(HttpStatus.OK, "P002", "panopto access token excepiton"),

    //AssignmentGroup
    NOT_FOUND_ASSIGNMENT_GROUP(HttpStatus.OK, "AG001", "aassignmnetGroup not found exception"),

    //AssignmentGroupGrade
    ALREADY_EXIST_ASSIGNMENT_GROUP_GRADE(HttpStatus.OK, "AGG001", "already exeist assignment group grade"),

    //GradeSetting
    ALREADY_EXIST_GRADE_SETTING(HttpStatus.OK, "GS001", "already exitst grade setting"),
    NOT_FOUND_GRADE_SETTING(HttpStatus.OK, "GS002", "not found grade setting"),

    //Grade
    NOT_FOUND_GRADE(HttpStatus.OK, "G001", "not found grade"),

    //Canvas-AssignmentGroup
    NOT_FOUND_ASSIGNMENT_GROUP_FROM_CANVAS(HttpStatus.OK, "CA001", "not found assignmentGroup from canvas"),

    //Token
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.OK, "T001", "not found refresh token"),
    ALREADY_DELETED_REFRESH_TOKEN(HttpStatus.OK, "T002", "already deleted refresh token"),
    INVALID_TOKEN(HttpStatus.OK, "T003", "invalid token"),
    LOGOUT_USER_ACCESS_TOKEN(HttpStatus.OK, "T004", "logout user access token"),
    LOGOUT_USER_REFRESH_TOKEN(HttpStatus.OK, "T005", "logout user refresh token"),
    ONLY_REFRESH_TOKEN(HttpStatus.OK, "T006", "niri"),
    EXPIRED_JWT_EXCEPTION(HttpStatus.OK, "T007", "expired token"),
    NO_TOKEN_EXCEPTION(HttpStatus.OK, "T008", "no exist token"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.OK, "T009", "access denied"),
    EXPIRED_REFRESH_TOKEN_EXCEPTION(HttpStatus.OK, "T010", "expired refresh token"),

    //Feign
    FEIGN_EXCEPTION(HttpStatus.OK, "F001", "feign exception"),

    //UserHistory
    NOT_FOUND_USER_HISTORY_EXCEPTION(HttpStatus.OK, "UH001", "not found user history  exception"),

    //SAML
    SAML_EXCEPTION(HttpStatus.OK, "S001", "saml exception"),
    SAML_XML_DECODE_EXCEPTION(HttpStatus.OK, "S002", "saml xml decode exception"),
    SAX_BUILDER_EXCEPTION(HttpStatus.OK, "S003", "sax builder exception"),
    RESOLVE_ATTRIBUTE_EXCEPTION(HttpStatus.OK, "S004", "attribute resolve failed"),
    INFLATER_EXEPTION(HttpStatus.OK, "S005", "inflater exeption"),

    //bbsctt
    NOT_FOUND_NOTIFICATION(HttpStatus.OK, "B001", "not found notification bbsctt"),
    NOT_FOUND_MANUAL_EXCEPTION(HttpStatus.OK, "B002", "not found manual bbsctt"),
    NOT_FOUND_FAQ_EXCEPTION(HttpStatus.OK, "B003", "not found faq bbsctt"),
    NOT_FOUND_DATA_ROOM_EXCEPTION(HttpStatus.OK, "B004", "not found dataroom bbsctt"),
    NOT_FOUND_BBSCTT_CONTENT_EXCEPTION(HttpStatus.OK, "B005", "not found bbsctt content"),
    ALREADY_DELETED_BBSCTT(HttpStatus.OK, "B006", "already deleted bbsctt"),
    NOT_FOUND_POPUP(HttpStatus.OK, "B007", "not found popup"),
    NOT_FOUND_BBS_EXCEPTION(HttpStatus.BAD_REQUEST, "B008", "not found bbs bbsctt"),
    REQUIRED_BBS_EXCEPTION(HttpStatus.BAD_REQUEST, "B009", "Title , Writer and Description are  required field!"),

    //zoom
    FAIL_TO_CREATE_MEETING(HttpStatus.OK, "Z001", "fail to create meeting"),
    FAIL_TO_UPDATE_MEETING(HttpStatus.OK, "Z002", "fail to update meeting"),
    FAIL_TO_DELETE_MEETING(HttpStatus.OK, "Z003", "fail to delete meeting"),
    FAIL_TO_CREATE_MEETING_CHECK_CANVAS(HttpStatus.OK, "Z004", "해당 주차가 캔버스에 없습니다"),
    FAIL_TO_CHECK_ATTEND(HttpStatus.OK, "Z005", "check passcode"),
    NOT_FOUND_ATTEND_INFO(HttpStatus.OK, "Z006", "출석 정보를 찾을 수 없습니다"),

    //File
    FILE_UPLOAD_FAIL(HttpStatus.OK, "FI001", "file upload fail"),
    NOT_FOUND_THUMBNAIL(HttpStatus.OK, "FI002", "not found thumbnail"),
    FILE_NOT_FOUND(HttpStatus.OK, "FI003", "file not found"),
    MIME_TYPE_NOT_SUPPORTED_EXCEPTION(HttpStatus.BAD_REQUEST, "FI004", "MIME type not supported"),
    FILE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "FI005" , "File delete fail!" ),
    //Role
    ROLE_UPDATE_EXCEPTION(HttpStatus.OK, "R001", "role update fail, check your private token"),

    //Meeting
    NOT_FOUND_MEETING(HttpStatus.OK, "M001", "not found meeting exception"),

    //Jandi
    JANDI_SAVE_ERROR(HttpStatus.OK, "J001", "failed to save jandi data"),
    NOT_FOUND_JANDI_INFO(HttpStatus.OK, "J002", "not found jandi info"),
    FAIL_CONVERT_JANDI_INFO(HttpStatus.OK, "J003", "failed to convert jandi info"),

    //Zoom
    NO_ZOOM_MEETING_DATA(HttpStatus.OK, "Z001", "no zoom meeting data"),

    //Reminder
    NOT_FOUND_REMINDER(HttpStatus.OK, "RMD001", "not found reminder"),

    NOT_FOUND_AGENDA_EXCEPTION(HttpStatus.BAD_REQUEST, "AGD001", "not found agenda exception"),

    NOT_ALLOWED_USER_EXCEPTION(HttpStatus.BAD_REQUEST,"USR001" ,"Permission Denied!" ),

    //chatting
    NOT_FOUND_CHAT_ROOM_EXCEPTION(HttpStatus.OK, "CR001", "not found chat room exception!"),
    ALREADY_EXIST_USER_EXCEPTION(HttpStatus.OK, "CR002", "already exist user exception!"),
    USER_ALREADY_IN_GROUP_EXCEPTION(HttpStatus.OK, "CR003", "user already in group exception!"),
    NOT_FOUND_MESSAGE_EXCEPTION(HttpStatus.OK, "CR004", "not found message exception!"),

    // group announcement
    NOT_FOUND_GROUP_ANNOUNCEMENT_EXCEPTION(HttpStatus.OK, "GA001", "not found group announcement exception!"),
    NOT_FOUND_GROUP_FILE_EXCEPTION(HttpStatus.OK, "GA002", "not found group file exception!"),
    FILE_DELETE_FAIL_EXCEPTION(HttpStatus.OK, "GA003", "file delete fail exception!"),
    NOT_FOUND_GROUP_CALENDAR_EVENT_EXCEPTION(HttpStatus.OK, "GA004", "not found group calendar event exception!"),
    NOT_FOUND_COMMENT_EXCEPTION(HttpStatus.OK, "GA005", "not found comment exception!"),
    GROUP_ALREADY_ASSIGNED_TO_PHASE_EXCEPTION(HttpStatus.OK, "GA006", "group already assigned to phase exception!"),

    START_END_DATE_REQUIRED(HttpStatus.OK, "GA005", "start date and end date required!"),

    // quizzes
    UNSUPPORTED_QUIZ_TYPE_EXCEPTION(HttpStatus.OK, "UQ001","unsupported quiz type!"),
    UNSUPPORTED_QUIZ_SCORING_POLICY_EXCEPTION(HttpStatus.OK,"UQ002","unsupported quiz scoring policy!"),

    // Phase
    NOT_FOUND_GROUP_PROJECT_PHASE_EXCEPTION(HttpStatus.NOT_FOUND, "GPH001", "not found group project phase!"),


    // Group Project Rubric
    NOT_FOUND_GROUP_PROJECT_RUBRIC_EXCEPTION(HttpStatus.NOT_FOUND, "GPR001", "not found group project rubric!"),
    INTERNAL_SERVER_ERROR_GROUP_PROJECT_RUBRIC_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPR002", "internal server error found when  group project rubric related operation is made!"),
    PERSISTENCE_EXCEPTION_GROUP_PROJECT_RUBRIC_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPR003", "persistence error occur when group project rubric query is made!"),


    // Comment prompt
    NOT_FOUND_COMMENT_PROMPT_EXCEPTION(HttpStatus.NOT_FOUND, "CNP001", "Comment prompt not found!"),


    // Rating prompt
    PROMPT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "GPP001", "Prompt not found!"),
    PROMPT_TYPE_MISMATCH_EXCEPTION(HttpStatus.BAD_REQUEST, "GPP002", "Wrong Prompt Type!"),
    NO_UPLOADED_FILE_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "GPR004", "No file provided. Please attach a file and try again."),

    // Group Project tag
    NOT_FOUND_GROUP_PROJECT_TAG_EXCEPTION(HttpStatus.NOT_FOUND, "GPT001", "not found group project tag!"),
    INTERNAL_SERVER_ERROR_GROUP_PROJECT_TAG_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPT002", "internal server error found when related operation is made!"),
    PERSISTENCE_EXCEPTION_GROUP_PROJECT_TAG_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPT003", "persistence error occurred when querying group project tag!"),

    // Group Project Group
    INTERNAL_SERVER_ERROR_GROUP_PROJECT_GROUP_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPG001", "persistence error occurred when querying group!"),
    NOT_FOUND_GROUP_EXCEPTION(HttpStatus.NOT_FOUND, "GPG002", "not found group!"),
    INTERNAL_SERVER_ERROR_GROUP_PROJECT_GROUP_UPLOADED_CSV_FILE_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPG003", "an input/output error occurred while processing the CSV file."),
    NOT_FOUND_GROUP_SETTING_EXCEPTION(HttpStatus.NOT_FOUND, "GPGS001", "not found group setting!"),
    INTERNAL_SERVER_ERROR_GROUP_PROJECT_GROUP_MEMBER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "GPGM001", "persistence error occurred when querying group member!"),
    NOT_FOUND_GROUP_MEMBER_EXCEPTION(HttpStatus.NOT_FOUND, "GPGM002", "not found group member!"),
    // Tag
    NOT_FOUND_TAG_EXCEPTION(HttpStatus.NOT_FOUND, "TAG001", "not found tag exception!"),



    // Common Unsupported argument
    UNSUPPORTED_ARGUMENT(HttpStatus.OK,"USA001","unsupported argument!"),
    REQUIRED_INITIAL_POST(HttpStatus.OK,"RI001","require initial post"),
    FILE_UPLOAD_FAIL_EXCEPTION(HttpStatus.OK, "FI001", "file upload fail"),
    DATA_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "D001", "data not found exception!"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "II001", "invalid input!"),
    MISSING_REQUEST_PARAMETER_EXCEPTION(HttpStatus.BAD_REQUEST, "II002", "required request parameter is missing or cannot be converted"),

    // Group Project
    NOT_FOUND_GROUP_PROJECT(HttpStatus.OK, "GP001" , "not found group project exception!"),
    NOT_FOUND_GROUP_PROJECT_SUBMISSION(HttpStatus.OK, "GP002" , "not found group project submission exception!"),
    NOT_FOUND_PHASE_EXCEPTION(HttpStatus.NOT_FOUND, "GP003" , "not found phase exception!"),
    // Group Project Rubric Template
    NOT_FOUND_RUBRIC_TEMPLATE_EXCEPTION(HttpStatus.NOT_FOUND, "GP003", "not found rubric template exception!"),
    INVALID_PROMPT_TYPE(HttpStatus.BAD_REQUEST, "GP004", "invalid prompt type!"),
    NOT_FOUND_PROMPT_TEMPLATE_EXCEPTION(HttpStatus.NOT_FOUND, "GP005", "not found prompt template exception!"),
    PROMPT_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "GP006", "prompt template not found!"),

    // Group Project Member
    NOT_FOUND_GROUP_MEMBERS_EXCEPTION(HttpStatus.NOT_FOUND, "GM001", "not found group members exception!"),
    ALREADY_EXIST_GROUP_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "GM002", "already exist group member exception!"),
    EMPTY_MEMBER_EXCEPTION(HttpStatus.NO_CONTENT, "GMS003", "There are no available students!"),
    NOT_ENOUGH_MEMBER_EXCEPTION(HttpStatus.BAD_REQUEST, "GMS004", "There are not enough available students to create at least one group!"),
    DUPLICATE_GROUP_MEMBERS_EXCEPTION(HttpStatus.BAD_REQUEST, "GM003" , "duplicate group members exception!"),
    ALREADY_IN_ANOTHER_GROUP_EXCEPTION(HttpStatus.BAD_REQUEST, "GM004" , "already in another group exception!"),
    NOT_ALL_STUDENTS_ASSIGNED_TO_GROUP(HttpStatus.FORBIDDEN, "GM005" , "not all students assigned to group exception!"),
    NOT_FOUND_GROUP_PROJECT_ASSIGNMENT(HttpStatus.NOT_FOUND, "GP007" , "not found group project assignment exception!" ),
    NOT_FOUND_SUBMISSION_ID(HttpStatus.NOT_FOUND, "GP008", "not found submission id exception!"),


    // Attendance Appeal Management
    NOT_FOUND_ATTENDANCE_APPEAL_EXCEPTION(HttpStatus.NOT_FOUND, "AAM001", "not found attendance appeal exception!"),

    // Student Attendance Event
    INVALID_SORT_OPTION_OF_STUDENT_ATTENDANCE(HttpStatus.NOT_FOUND, "SAE001", "invalid sort option of student attendance exception!"),



    // Group Project Progress
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "GP009", "invalid request exception!"),
    NOT_FOUND_STUDENT_SUBMISSION(HttpStatus.NOT_FOUND, "GP010", "not found student submission exception!"),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "GP010", "not found review exception!"),
    ALREADY_SUBMITTED(HttpStatus.BAD_REQUEST, "GP011", "already submitted this assignment."),
    NOT_FOUND_GROUP_PROJECT_FEEDBACK(HttpStatus.NOT_FOUND, "GP012" , "not found group project's feedback" ),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "GP013" , "You are not authorized to comment on this feedback." ),
    NOT_FOUND_GROUP_PROJECT_RATING_LEVEL(HttpStatus.NOT_FOUND, "GP014" , "not found rating level" );



    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
