package com.qrdn.login.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.qrdn.login.constants.UserConstant;
import com.qrdn.login.entity.Credentials;
import com.qrdn.login.entity.User;
import com.qrdn.login.entity.UserPassword;
import com.qrdn.login.repository.CredentialsRepository;
import com.qrdn.login.repository.UserRepository;
import com.qrdn.login.service.passwordhash.HashingPasswordAargon2;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    Endpoints endpoints;

    private HttpClient httpClient;

    public UserService() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    /**
     * Saving user's information in the database
     * 
     * @param user
     * @return User
     * @throws InterruptedException
     * @throws IOException
     */
    public User createUser(User user) {

        try {
            user = checkNullAndFormat(user);
            if (!user.getErrorCode().isEmpty()) {
                return user;
            }

            if (userRepository.existsById(user.getUserName())) {

                user.setErrorCode(UserConstant.USER_NAME_ALREADY_EXISTS);
                user.setErrorDescription(UserConstant.USER_NAME_ALREADY_EXISTS_MESSAGES);
                return user;
            }

            HashMap<String, Object> addToJson = new HashMap<>();
            addToJson.put("user_name", user.getUserName());
            addToJson.put("password", user.getPassword());
            addToJson.put("type", user.getType());
            addToJson.put("created_date", null);
            addToJson.put("created_by", user.getCreatedBy());

            JSONObject obj = new JSONObject(addToJson);

            endpoints = new Endpoints("Credentials", "createCredentials");

            User getUser = doPost(endpoints.getUrl(), obj);

            user.setPassword(getUser.getPassword());
            user.setType(getUser.getType());
            user.setCreatedDate(getUser.getCreatedDate());
            user.setCreatedBy(getUser.getCreatedBy());

            user.setStatus(UserConstant.NEW);
            user.setErrorCode(UserConstant.SUCCESS);
            user.setErrorDescription(UserConstant.SUCCESS_MESSAGE);
            if (user.getPasswordFrequency().equalsIgnoreCase(UserConstant.DEFAULT)) {
                user.setPasswordValidilityDate("Unlimited");
            } else {
                user.setPasswordValidilityDate(calculateUserValidilityDate(user.getPasswordFrequency()));
            }
            // rollback , if one database operations failed
            userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e);
            // handleServiceException(Priority.High, user, "error occured while creating the
            // user ", e,
            // new HashMap<>());
        }
        return user;
    }

    /**
     * Searching the database for user. The search is done using user's username
     * 
     * @param user
     * @return User
     */
    public User findUserByID(User user) {

        try {
            user = checkUserNameNullOrInvalid(user);
            if (!user.getErrorCode().isEmpty()) {
                return user;
            }

            User currentUser;
            Credentials credentials;
            Optional<User> checkUser = userRepository.findById(user.getUserName());
            Optional<Credentials> checkCredentials = credentialsRepository.findById(user.getUserName());

            if (checkUser.isPresent() && checkCredentials.isPresent()) {

                currentUser = checkUser.get();
                credentials = checkCredentials.get();

                currentUser = checkDeletedAndSuspended(currentUser);
                if (!currentUser.getErrorCode().isEmpty()) {
                    return currentUser;
                }

                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    user.setErrorCode(UserConstant.EMPTY_PASSWORD_INPUT);
                    user.setErrorDescription(UserConstant.EMPTY_PASSWORD_INPUT_MESSAGE);
                    return user;
                }

                if (!HashingPasswordAargon2.verifyPassword(user.getPassword(), credentials.getPassword())) {

                    currentUser.setErrorCode(UserConstant.PASSWORD_WRONG);
                    currentUser.setErrorDescription(UserConstant.PASSWORD_WRONG_MESSAGE);
                }

                if (!currentUser.getPasswordValidilityDate().equalsIgnoreCase(UserConstant.UNLIMITED) && java.sql.Date
                        .valueOf(currentUser.getPasswordValidilityDate()).getTime() < new java.util.Date().getTime()) {

                    currentUser.setErrorCode(UserConstant.PASSWORD_EXPIRED);
                    currentUser.setErrorDescription(UserConstant.PASSWORD_EXPIRED_MESSAGE);

                }
                if (currentUser.getStatus().equalsIgnoreCase(UserConstant.NEW)) {

                    currentUser.setErrorCode(UserConstant.STATUS_NEW);
                    currentUser.setErrorDescription(UserConstant.STATUS_NEW_MESSAGE);

                }
                if (currentUser.getErrorCode().isEmpty()
                        && currentUser.getStatus().equalsIgnoreCase(UserConstant.ACTIVE)) {

                    currentUser.setErrorCode(UserConstant.SUCCESS);
                    currentUser.setErrorDescription(UserConstant.SUCCESS_MESSAGE);
                }

                currentUser.setPassword(credentials.getPassword());
                currentUser.setType(credentials.getType());

                return currentUser;

            } else {
                user.setErrorCode(UserConstant.USER_NOT_FOUND);
                user.setErrorDescription(UserConstant.USER_NOT_FOUND_MESSAGES);
            }
        } catch (Exception e) {
            System.out.println(e);
            // handleServiceException(Priority.High, user, "error occured while sign in ",
            // e,
            // new HashMap<>());
        }
        return user;
    }

    /**
     * Delete the user by changing the status to Deleted
     * 
     * @param userName
     * @return User
     */
    public User deleteUser(String userName) {

        User user = new User();
        user.setUserName(userName);

        try {
            user = checkUserNameNullOrInvalid(user);
            if (!user.getErrorCode().isEmpty()) {
                return user;
            }

            Credentials credentials;
            Optional<User> checkUser = userRepository.findById(userName);
            Optional<Credentials> checkCredentials = credentialsRepository.findById(userName);

            if (checkUser.isPresent() && checkCredentials.isPresent()) {
                credentials = checkCredentials.get();

                checkUser.get().setStatus(UserConstant.DELETED);
                checkUser.get().setPassword(credentials.getPassword());
                checkUser.get().setType(credentials.getType());
                return userRepository.save(checkUser.get());

            } else {
                user.setErrorCode(UserConstant.USER_NOT_FOUND);
                user.setErrorDescription(UserConstant.USER_NOT_FOUND_MESSAGES);
            }
        } catch (Exception e) {
            // handleServiceException(Priority.High, user, "error occured while deleting the
            // user ", e,
            // new HashMap<>());
        }
        return user;
    }

    /**
     * Update existing user's information in the database
     * 
     * @param user
     * @return User
     */
    @Transactional
    public User updateUser(User user) {

        try {

            user = checkNullAndFormat(user);
            user = checkStatus(user);
            if (!user.getErrorCode().isEmpty()) {
                return user;
            }

            Credentials credentials;
            Optional<User> checkUser = userRepository.findById(user.getUserName());
            Optional<Credentials> checkCredentials = credentialsRepository.findById(user.getUserName());

            if (checkUser.isPresent() && checkCredentials.isPresent()) {

                credentials = checkCredentials.get();

                if (checkUser.get().getStatus().equalsIgnoreCase(UserConstant.DELETED)) {
                    user.setErrorCode(UserConstant.STATUS_DELETED);
                    user.setErrorDescription(UserConstant.UPDATE_STATUS_DELETED_MESSAGE);
                    return user;
                }

                else {
                    user.setPassword(credentials.getPassword());
                    user.setType(credentials.getType());
                    user.setUpdatedDate(new java.sql.Date(System.currentTimeMillis()));

                    if (user.getPasswordFrequency().equalsIgnoreCase(UserConstant.DEFAULT)) {
                        user.setPasswordValidilityDate("Unlimited");
                    } else {
                        user.setPasswordValidilityDate(calculateUserValidilityDate(user.getPasswordFrequency()));
                    }

                    if (user.getUpdatedBy() == null || user.getUpdatedBy().isEmpty()) {
                        user.setUpdatedBy("api");
                    }

                    user.setFirstNameEn(checkUser.get().getFirstNameEn());
                    user.setLastNameEn(checkUser.get().getLastNameEn());
                    user.setCreatedBy(checkUser.get().getCreatedBy());
                    user.setCreatedDate(checkUser.get().getCreatedDate());

                    user.setGroupCode(checkUser.get().getGroupCode());
                    user.setOfficeCode(checkUser.get().getOfficeCode());
                    user.setOrganizationCode(checkUser.get().getOrganizationCode());
                    user.setRole(checkUser.get().getRole());

                    user.setErrorCode(UserConstant.SUCCESS);
                    user.setErrorDescription(UserConstant.SUCCESS_MESSAGE);
                    userRepository.save(user);
                }
            } else {
                user.setErrorCode(UserConstant.USER_NOT_FOUND);
                user.setErrorDescription(UserConstant.USER_NOT_FOUND_MESSAGES);
                return user;
            }

        } catch (Exception e) {
            System.out.println(e);
            // handleServiceException(Priority.High, user, "error occured while updating the
            // user ", e,
            // new HashMap<>());
        }
        return user;
    }

    /**
     * Change password of existing user in the database
     * 
     * @param userPassword
     * @return User
     * @throws InterruptedException
     * @throws IOException
     */
    @Transactional
    public User changePassword(UserPassword userPassword) throws IOException, InterruptedException {

        User user = new User();
        user.setUserName(userPassword.getUserName());
        try {
            user = checkUserNameNullOrInvalid(user);

            if (!user.getErrorCode().isEmpty()) {
                return user;
            }
            Optional<User> checkUser = userRepository.findById(userPassword.getUserName());
            Optional<Credentials> checkCredentials = credentialsRepository.findById(userPassword.getUserName());

            if (checkUser.isPresent() && checkCredentials.isPresent()) {

                user = checkUser.get();
                Credentials credentials = checkCredentials.get();

                user = checkDeletedAndSuspended(user);
                if (!user.getErrorCode().isEmpty()) {
                    return user;
                }
                User check = checkNullAndLenght(userPassword, user);

                if (!check.getErrorCode().isEmpty()) {
                    return check;
                }

                if (!userPassword.getNewPassword().equals(userPassword.getConfirmNewPassword())) {

                    user.setErrorCode(UserConstant.UNMATCHED_PASSWORD);
                    user.setErrorDescription(UserConstant.UNMATCHED_PASSWORD_MESSAGE);

                }
                if (!user.getStatus().equalsIgnoreCase(UserConstant.NEW) &&
                        userPassword.getOldPassword().equals(
                                userPassword.getNewPassword())) {
                    user.setErrorCode(UserConstant.SAME_PASSWORD);
                    user.setErrorDescription(UserConstant.SAME_PASSWORD_MESSAGE);

                }
                if (user.getStatus().equalsIgnoreCase(UserConstant.SUSPEND)) {
                    user.setErrorCode(UserConstant.STATUS_SUSPEND);
                    user.setErrorDescription(UserConstant.STATUS_SUSPEND_MESSAGE);

                }

                if (!user.getStatus().equalsIgnoreCase(UserConstant.NEW) &&
                        !HashingPasswordAargon2.verifyPassword(userPassword.getOldPassword(),
                                credentials.getPassword())) {
                    user.setErrorCode(UserConstant.PASSWORD_WRONG);
                    user.setErrorDescription(UserConstant.PASSWORD_WRONG_MESSAGE);

                }
                if (user.getErrorCode().isEmpty()) {

                    endpoints = new Endpoints("Credentials", "changePassword");

                    HashMap<String, Object> addToJson = new HashMap<>();
                    addToJson.put("user_name", userPassword.getUserName());
                    addToJson.put("password", userPassword.getNewPassword());

                    JSONObject obj = new JSONObject(addToJson);

                    User getUser = doPost(endpoints.getUrl(), obj);

                    user.setPassword(getUser.getPassword());
                    user.setType(getUser.getType());
                    user.setCreatedDate(getUser.getCreatedDate());
                    user.setCreatedBy(getUser.getCreatedBy());

                    user.setStatus(UserConstant.ACTIVE);
                    user.setErrorCode(UserConstant.SUCCESS);
                    user.setErrorDescription(UserConstant.SUCCESS_MESSAGE);
                    user.setUpdatedDate(new java.sql.Date(System.currentTimeMillis()));

                    if (user.getUpdatedBy() == null || user.getUpdatedBy().isEmpty()) {
                        user.setUpdatedBy("api");
                    }
                    if (user.getPasswordFrequency().equalsIgnoreCase(UserConstant.DEFAULT)) {
                        user.setPasswordFrequency(UserConstant.DEFAULT);
                        user.setPasswordValidilityDate(UserConstant.UNLIMITED);
                    } else {
                        user.setPasswordValidilityDate(calculateUserValidilityDate(user.getPasswordFrequency()));
                    }
                    userRepository.save(user);
                }
            } else {
                user.setErrorCode(UserConstant.USER_NOT_FOUND);
                user.setErrorDescription(UserConstant.USER_NOT_FOUND_MESSAGES);
            }

        } catch (Exception e) {

            // handleServiceException(Priority.High, user, "error occured while changing the
            // user's password ", e,
            // new HashMap<>());
        }
        return user;
    }

    /**
     * Check if user username, password, email, phone, type and passwordFrequency
     * are
     * notnull
     * also checking format validity for: userName, email, mobile and
     * passwordFrequency
     * 
     * @param user
     * @return user
     */
    public User checkNullAndFormat(User user) {

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_USERNAME_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_USERNAME_INPUT_MESSAGE);

        } else if (!checkUserName(user.getUserName())) {
            user.setErrorCode(UserConstant.INCORRECT_USER_NAME_FORMAT);
            user.setErrorDescription(UserConstant.INCORRECT_USER_NAME_FORMAT_MESSAGE);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_PASSWORD_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_PASSWORD_INPUT_MESSAGE);
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_FIRST_NAME_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_FIRST_NAME_INPUT_MESSAGE);
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_LAST_NAME_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_LAST_NAME_INPUT_MESSAGE);
        }

        if (user.getType() == null || user.getType().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_TYPE_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_TYPE_INPUT_MESSAGE);
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_EMAIL_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_EMAIL_INPUT_MESSAGE);
        } else if (!checkEmail(user.getEmail())) {
            user.setErrorCode(UserConstant.INCORRECT_EMAIL_FORMAT);
            user.setErrorDescription(UserConstant.INCORRECT_EMAIL_FORMAT_MESSAGE);
        }
        if (user.getMobile() == null || user.getMobile().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_MOBILE_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_MOBILE_INPUT_MESSAGE);
        } else if (!checkMobile(user.getMobile())) {
            user.setErrorCode(UserConstant.INCORRECT_MOBILE_FORMAT);
            user.setErrorDescription(UserConstant.INCORRECT_MOBILE_FORMAT_MESSAGE);
        }

        return checkPasswordFrequency(user);
    }

    /**
     * check if password frequency is one of these values : monthly, quarterly,
     * semiannually , annually
     * 
     * @param user
     * @return User
     */
    public User checkPasswordFrequency(User user) {

        if (user.getPasswordFrequency() == null || user.getPasswordFrequency().isEmpty()) {

            user.setErrorCode(UserConstant.EMPTY_PASSWORD_FREQUENCY_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_PASSWORD_FREQUENCY_MESSAGE);

        } else if (!user.getPasswordFrequency().equalsIgnoreCase(UserConstant.DEFAULT)
                && (!user.getPasswordFrequency().equalsIgnoreCase(UserConstant.MONTHLY)
                        && !user.getPasswordFrequency().equalsIgnoreCase(UserConstant.QUARTERLY)
                        && !user.getPasswordFrequency().equalsIgnoreCase(UserConstant.SEMIANNUALLY)
                        && !user.getPasswordFrequency().equalsIgnoreCase(UserConstant.ANNUALLY))) {
            user.setErrorCode(UserConstant.INVALID_PASSWORD_FREQUENCY_INPUT);
            user.setErrorDescription(UserConstant.INVALID_PASSWORD_FREQUENCY_MESSAGE);
        }
        return user;
    }

    /**
     * Check that email has a valid email format
     * 
     * @param email
     * @return boolean
     */
    public boolean checkEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Check that mobile doesnt contain alphabetic letters
     * 
     * @param mobile
     * @return boolean
     */
    public static boolean checkMobile(String mobile) {
        int len = mobile.length();
        for (int i = 0; i < len; i++) {
            if ((Character.isLetter(mobile.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param passwordFrequency
     * @return Date
     */
    public String calculateUserValidilityDate(String passwordFrequency) {

        Date date = new java.sql.Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        if (passwordFrequency.equalsIgnoreCase(UserConstant.MONTHLY)) {
            c.add(Calendar.DATE, 30);
        } else if (passwordFrequency.equalsIgnoreCase(UserConstant.QUARTERLY)) {
            c.add(Calendar.DATE, 91);
        } else if (passwordFrequency.equalsIgnoreCase(UserConstant.SEMIANNUALLY)) {
            c.add(Calendar.DATE, 181);
        } else if (passwordFrequency.equalsIgnoreCase(UserConstant.ANNUALLY)) {
            c.add(Calendar.DATE, 365);
        }
        date = new java.sql.Date(c.getTimeInMillis());
        return date.toString();
    }

    /**
     * @param userName
     * @return boolean
     */
    public boolean checkUserName(String userName) {
        int len = userName.length();
        for (int i = 0; i < len; i++) {
            if ((!Character.isLetterOrDigit(userName.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param userPassword
     * @param user
     * @return User
     */
    public User checkNullAndLenght(UserPassword userPassword, User user) {

        if (!user.getStatus().equalsIgnoreCase(UserConstant.NEW)
                && (userPassword.getOldPassword() == null || userPassword.getOldPassword().isEmpty())) {
            user.setErrorCode(UserConstant.EMPTY_OLD_PASSWORD_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_OLD_PASSWORD_INPUT_MESSAGE);

        }
        if (userPassword.getNewPassword() == null || userPassword.getNewPassword().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_NEW_PASSWORD_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_NEW_PASSWORD_INPUT_MESSAGE);
        }
        if (userPassword.getConfirmNewPassword() == null || userPassword.getConfirmNewPassword().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_CONFIRM_PASSWORD_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_CONFIRM_PASSWORD_INPUT_MESSAGE);
        }

        return user;
    }

    /**
     * check user's username nullity and validity
     * 
     * @param user
     * @return User
     */
    public User checkUserNameNullOrInvalid(User user) {
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_USERNAME_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_USERNAME_INPUT_MESSAGE);

        } else if (!checkUserName(user.getUserName())) {
            user.setErrorCode(UserConstant.INCORRECT_USER_NAME_FORMAT);
            user.setErrorDescription(UserConstant.INCORRECT_USER_NAME_FORMAT_MESSAGE);
        }
        return user;
    }

    public User checkStatus(User user) {

        if (user.getStatus() == null || user.getStatus().isEmpty()) {
            user.setErrorCode(UserConstant.EMPTY_STATUS_INPUT);
            user.setErrorDescription(UserConstant.EMPTY_STATUS_INPUT_MESSAGE);

        } else if (!user.getStatus().equalsIgnoreCase(UserConstant.ACTIVE)
                && !user.getStatus().equalsIgnoreCase(UserConstant.SUSPEND)) {
            user.setErrorCode(UserConstant.INVALID_STATUS_INPUT);
            user.setErrorDescription(UserConstant.INVALID_STATUS_INPUT_MESSAGE);
        }
        return user;
    }

    /**
     * check if the user status is deleted
     * 
     * @param user
     * @return User
     */
    public User checkDeletedAndSuspended(User user) {
        if (user.getStatus().equalsIgnoreCase(UserConstant.DELETED)) {
            user.setErrorCode(UserConstant.STATUS_DELETED);
            user.setErrorDescription(UserConstant.STATUS_DELETED_MESSAGE);
        } else if (user.getStatus().equalsIgnoreCase(UserConstant.SUSPEND)) {
            user.setErrorCode(UserConstant.STATUS_SUSPEND);
            user.setErrorDescription(UserConstant.STATUS_SUSPEND_MESSAGE);
        }
        return user;
    }

    /**
     * @param url
     * @param obj
     * @return User
     * @throws IOException
     * @throws InterruptedException
     */
    public User doPost(String url, JSONObject obj) throws IOException, InterruptedException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(obj);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        return objectMapper.readValue(json, User.class);
    }
}
