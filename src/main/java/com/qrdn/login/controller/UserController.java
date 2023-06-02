package com.qrdn.login.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// import com.qrdn.common.controller.BaseController;
// import com.qrdn.common.log.entity.LogDetails.Priority;
import com.qrdn.login.entity.User;
import com.qrdn.login.entity.UserPassword;
import com.qrdn.login.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@OpenAPIDefinition(info = @Info(title = "User Controller", description = "This controller consist of five operations: create user, login user, update user, delete user, change password of user"))
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    /**
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return new ResponseEntity<>("One of the inputs is incorrect \n" + "Exception: " + exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleException1(HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return new ResponseEntity<>("One of the inputs is incorrect \n" + "Exception: " + exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Create user
     * 
     * @param user
     * @param request
     * @return Map<String, Object>
     */
    @Operation(summary = "Create user and credentilas for user", description = "To create user and credentials for user, user body must be provided having fields: user_name, first_name, last_name, password, email, mobile, password_frequency and type. (the others are optional)")
    @PostMapping("/createUser")
    public Map<String, Object> createUser(@RequestBody User user, HttpServletRequest request) {

        Map<String, Object> userResponse = new TreeMap<>();
        try {
            user = userService.createUser(user);

            userResponse.put("error_code", user.getErrorCode());
            userResponse.put("error_description", user.getErrorDescription());
            userResponse.put("user_name", user.getUserName());
            userResponse.put("first_name", user.getFirstName());
            userResponse.put("last_name", user.getLastName());
            userResponse.put("password_frequency", user.getPasswordFrequency());
            userResponse.put("password_validility_date", user.getPasswordValidilityDate());
            userResponse.put("status", user.getStatus());
            userResponse.put("created_date", String.valueOf(user.getCreatedDate()));

        } catch (Exception e) {

            // handleServiceException(Priority.High, user, "error occured while creating the
            // user ", e,
            // new HashMap<>());
        }
        return userResponse;
    }

    /**
     * Change password of the user
     * 
     * @param userPassword
     * @return Map<String, Object>
     * @throws Exception
     */
    @Operation(summary = "Change Password ", description = "To change password of the existing user , userPassword must be provided having fields: user_name, old_password, new_password, confirm_new_password (note that if User's status is new no need for old password)")
    @PutMapping("/changePassword")
    public Map<String, Object> changePassword(@RequestBody UserPassword userPassword) throws Exception {

        Map<String, Object> userResponse = new TreeMap<>();
        try {
            User user = userService.changePassword(userPassword);

            userResponse.put("error_code", user.getErrorCode());
            userResponse.put("error_description", user.getErrorDescription());
            userResponse.put("user_name", user.getUserName());
            userResponse.put("status", user.getStatus());

        } catch (Exception e) {
            // handleServiceException(Priority.High, userPassword, "error occured while
            // changing the user's password ", e,
            // new HashMap<>());
        }
        return userResponse;
    }

    /**
     * User Login
     * 
     * @param user
     * @return Map<String, Object>
     */
    @Operation(summary = "User Login", description = "To be able to login, user body must be provided having fields: user_name, password")
    @PostMapping("/loginUser")
    public Map<String, Object> loginUser(@RequestBody User user) {

        Map<String, Object> userResponse = new TreeMap<>();
        try {
            user = userService.findUserByID(user);

            userResponse.put("error_code", user.getErrorCode());
            userResponse.put("error_description", user.getErrorDescription());
            userResponse.put("user_name", user.getUserName());
            userResponse.put("status", user.getStatus());
            userResponse.put("password_frequency", user.getPasswordFrequency());
            userResponse.put("password_validility_date", user.getPasswordValidilityDate());

        } catch (Exception e) {
            // handleServiceException(Priority.High, user, "error occured while sign in ",
            // e,
            // new HashMap<>());
        }
        return userResponse;
    }

    /**
     * Delete user
     * 
     * @param user_name
     * @return Map<String, Object>
     */
    @Operation(summary = "Delete User", description = "To delete the user , just fill the parameter user_name")
    @DeleteMapping("/deleteUser")
    public Map<String, Object> deleteUser(@RequestParam String user_name) {

        Map<String, Object> userResponse = new TreeMap<>();
        try {
            User user = userService.deleteUser(user_name);

            userResponse.put("error_code", user.getErrorCode());
            userResponse.put("error_description", user.getErrorDescription());
            userResponse.put("user_name", user.getUserName());
            userResponse.put("status", user.getStatus());

        } catch (Exception e) {
            // handleServiceException(Priority.High, user_name, "error occured while
            // deleting the user ", e,
            // new HashMap<>());
        }
        return userResponse;
    }

    /**
     * Updates user
     * 
     * @param user
     * @return Map<String, Object>
     */
    @Operation(summary = "Update User", description = "To update the information of existing user, user body must be provided  ")
    @PutMapping("/updateUser")
    public Map<String, Object> updateUser(@RequestBody User user) {

        Map<String, Object> userResponse = new TreeMap<>();
        try {
            user = userService.updateUser(user);

            userResponse.put("error_code", user.getErrorCode());
            userResponse.put("error_description", user.getErrorDescription());
            userResponse.put("user_name", user.getUserName());
            userResponse.put("first_name", user.getFirstName());
            userResponse.put("last_name", user.getLastName());
            userResponse.put("password_frequency", user.getPasswordFrequency());
            userResponse.put("password_validility_date", user.getPasswordValidilityDate());
            userResponse.put("status", user.getStatus());
            userResponse.put("updated_date", String.valueOf(user.getUpdatedDate()));

        } catch (Exception e) {
            // handleServiceException(Priority.High, user, "error occured while updating the
            // user ", e,
            // new HashMap<>());
        }
        return userResponse;
    }
}