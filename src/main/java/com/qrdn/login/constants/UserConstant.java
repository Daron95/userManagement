package com.qrdn.login.constants;

public class UserConstant {

    private UserConstant() {
    }

    // user status definition
    public static final String ACTIVE = "active";
    public static final String SUSPEND = "suspended";
    public static final String NEW = "new";
    public static final String DELETED = "deleted";

    // Password frequency definition
    public static final String DEFAULT = "N/A";
    public static final String MONTHLY = "MONTHLY";
    public static final String QUARTERLY = "QUARTERLY";
    public static final String SEMIANNUALLY = "SEMIANNUALLY";
    public static final String ANNUALLY = "ANNUALLY";
    public static final String UNLIMITED = "UNLIMITED";

    // errors definition
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_NAME_ALREADY_EXISTS = "USER_NAME_ALREADY_EXISTS";
    public static final String PASSWORD_WRONG = "PASSWORD_WRONG";
    public static final String STATUS_SUSPEND = "STATUS_SUSPEND";
    public static final String STATUS_NEW = "STATUS_NEW";
    public static final String STATUS_DELETED = "STATUS_DELETED";
    public static final String PASSWORD_EXPIRED = "PASSWORD_EXPIRED";

    // errors messages
    public static final String USER_NOT_FOUND_MESSAGES = "Username does not exist !";
    public static final String USER_NAME_ALREADY_EXISTS_MESSAGES = "Username already exists !";
    public static final String PASSWORD_WRONG_MESSAGE = "Wrong Password !";
    public static final String STATUS_SUSPEND_MESSAGE = "Account is Blocked";
    public static final String STATUS_DELETED_MESSAGE = "Account is Deleted";
    public static final String STATUS_NEW_MESSAGE = "Change Your Password";
    public static final String PASSWORD_EXPIRED_MESSAGE = "Password Expired ! Change Your Password";
    public static final String UPDATE_STATUS_DELETED_MESSAGE = "Unable to proceed; username is deleted!";

    // Success messages
    public static final String SUCCESS = "SUCCESS";
    public static final String SUCCESS_MESSAGE = "Success";

    // Empty inputs
    public static final String EMPTY_USERNAME_INPUT = "EMPTY USERNAME INPUT";
    public static final String EMPTY_USERNAME_INPUT_MESSAGE = "Username input is not filled";
    public static final String EMPTY_PASSWORD_INPUT = "EMPTY PASSWORD INPUT";
    public static final String EMPTY_PASSWORD_INPUT_MESSAGE = "Password input is not filled";
    public static final String EMPTY_EMAIL_INPUT = "EMPTY EMAIL INPUT";
    public static final String EMPTY_EMAIL_INPUT_MESSAGE = "Email input is not filled";
    public static final String EMPTY_MOBILE_INPUT = "EMPTY MOBILE INPUT";
    public static final String EMPTY_MOBILE_INPUT_MESSAGE = "Mobile input is not filled";
    public static final String EMPTY_FIRST_NAME_EN_INPUT = "EMPTY NAME EN input INPUT";
    public static final String EMPTY_FIRST_NAME_EN_INPUT_MESSAGE = "Name EN input input is not filled";
    public static final String EMPTY_FIRST_NAME_AR_INPUT = "EMPTY NAME AR input INPUT";
    public static final String EMPTY_FIRST_NAME_AR_INPUT_MESSAGE = "Name AR input input is not filled";
    public static final String EMPTY_OLD_PASSWORD_INPUT = "EMPTY OLD PASSWORD INPUT";
    public static final String EMPTY_OLD_PASSWORD_INPUT_MESSAGE = "Old Password input is not filled";
    public static final String EMPTY_NEW_PASSWORD_INPUT = "EMPTY NEW PASSWORD INPUT";
    public static final String EMPTY_NEW_PASSWORD_INPUT_MESSAGE = "New Password input is not filled";
    public static final String EMPTY_CONFIRM_PASSWORD_INPUT = "EMPTY CONFIRM PASSWORD INPUT";
    public static final String EMPTY_CONFIRM_PASSWORD_INPUT_MESSAGE = "Confirm Password input is not filled";
    public static final String EMPTY_ORGANIZATION_CODE_INPUT = "EMPTY ORGANIZATION CODE INPUT";
    public static final String EMPTY_ORGANIZATION_CODE_INPUT_MESSAGE = "Organization Code input is not filled";
    public static final String EMPTY_OFFICE_CODE_INPUT = "EMPTY OFFICE CODE INPUT";
    public static final String EMPTY_OFFICE_CODE_INPUT_MESSAGE = "Office Code input is not filled";
    public static final String EMPTY_PASSWORD_FREQUENCY_INPUT = "EMPTY PASSWORD FREQUENCY INPUT";
    public static final String EMPTY_PASSWORD_FREQUENCY_MESSAGE = "Password Frequency input is not filled";
    public static final String EMPTY_TYPE_INPUT = "EMPTY TYPE INPUT";
    public static final String EMPTY_TYPE_INPUT_MESSAGE = "Type input is not filled";
    public static final String EMPTY_FIRST_NAME_INPUT = "EMPTY FIRST NAME INPUT";
    public static final String EMPTY_FIRST_NAME_INPUT_MESSAGE = "First name input is not filled";
    public static final String EMPTY_LAST_NAME_INPUT = "EMPTY LAST NAME INPUT";
    public static final String EMPTY_LAST_NAME_INPUT_MESSAGE = "Last name input is not filled";
    public static final String EMPTY_STATUS_INPUT = "EMPTY STATUS INPUT";
    public static final String EMPTY_STATUS_INPUT_MESSAGE = "Status input is not filled";

    // Invalid inputs
    public static final String INCORRECT_USER_NAME_FORMAT = "INCORRECT USER NAME FORMAT";
    public static final String INCORRECT_USER_NAME_FORMAT_MESSAGE = "User Name should contain only Letters and digits";
    public static final String INCORRECT_EMAIL_FORMAT = "INCORRECT EMAIL FORMAT";
    public static final String INCORRECT_EMAIL_FORMAT_MESSAGE = "Email format is not correct";
    public static final String UNMATCHED_PASSWORD = "UNMATCHED PASSWORD";
    public static final String UNMATCHED_PASSWORD_MESSAGE = "The Password Confirmation does not match";
    public static final String SAME_PASSWORD = "SAME PASSWORD";
    public static final String SAME_PASSWORD_MESSAGE = "New Password should be different than Old Password";
    public static final String INCORRECT_MOBILE_FORMAT = "INCORRECT MOBILE FORMAT";
    public static final String INCORRECT_MOBILE_FORMAT_MESSAGE = "Mobile should not contain letters";
    public static final String INVALID_PASSWORD_FREQUENCY_INPUT = "INVALID PASSWORD FREQUENCY INPUT";
    public static final String INVALID_PASSWORD_FREQUENCY_MESSAGE = "Password Frequency should be : Monthly or Quarterly or Semiannually or Annually. If it is empty then it will be Unlimited";
    public static final String INVALID_STATUS_INPUT = "INVALID STATUS INPUT";
    public static final String INVALID_STATUS_INPUT_MESSAGE = "Status should be : Active or Suspended";

}
