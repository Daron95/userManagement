package com.qrdn.login;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.qrdn.login.entity.Credentials;
import com.qrdn.login.entity.User;
import com.qrdn.login.entity.UserPassword;
import com.qrdn.login.repository.CredentialsRepository;
import com.qrdn.login.repository.UserRepository;
import com.qrdn.login.service.UserService;
import com.qrdn.login.service.passwordhash.HashingPasswordAargon2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CredentialsRepository credentialsRepository;

    @InjectMocks
    UserService userService;

    User currentUser, user, user1, user2, user3, user4, user5, user6;
    Credentials credentials;
    UserPassword userPassword, userPassword1;

    @BeforeEach
    public void setup() {

        user = new User();
        user.setUserName("user");
        user.setPassword("pass");
        user.setLastNameEn("en");
        user.setFirstNameEn("ar");
        user.setEmail("d@hotmail.com");
        user.setOfficeCode("22");
        user.setOrganizationCode("11");
        user.setMobile("111111");
        user.setPasswordFrequency("Monthly");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setType("user");

        user1 = new User();
        user1.setUserName("user1");
        user1.setPassword("pass1");

        user2 = new User();
        user2.setUserName("");
        user2.setPassword("pashsbcvfhriwoscnnankkkks");
        user2.setLastNameEn("en");
        user2.setFirstNameEn("ar");
        user2.setEmail("");
        user2.setFirstName("firstname");
        user2.setLastName("lastname");
        user2.setOfficeCode("22");
        user2.setOrganizationCode("11");
        user2.setMobile("111888888888811444444");

        user3 = new User();
        user3.setUserName("user3");
        user3.setPassword("pass3");
        user3.setStatus("active");
        user3.setPasswordValidilityDate("2025-03-31");

        user4 = new User();
        user4.setUserName("eee");
        user4.setPassword("pass");
        user4.setLastNameEn("en");
        user4.setFirstNameEn("ar");
        user4.setEmail("d@hotmail.com");
        user4.setOfficeCode("22");
        user4.setOrganizationCode("11");
        user4.setMobile("111888");

        user5 = new User();
        user5.setUserName("");
        user5.setPassword("");
        user5.setStatus("suspended");
        user5.setPasswordValidilityDate("2015-03-31");

        currentUser = new User();
        currentUser.setUserName(user5.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user5.getPassword()));
        currentUser.setStatus(user5.getStatus());
        currentUser.setPasswordValidilityDate(user5.getPasswordValidilityDate());

        userPassword = new UserPassword();
        userPassword.setUserName("user");
        userPassword.setOldPassword("pass");

        userPassword1 = new UserPassword();
        userPassword1.setUserName("333");
        userPassword1.setOldPassword("333");
        userPassword1.setNewPassword("333");
        userPassword1.setConfirmNewPassword("333");

        user6 = new User();
        user6.setUserName(userPassword1.getUserName());
        user6.setPassword(HashingPasswordAargon2.hashPassword(userPassword1.getOldPassword()));
        user6.setStatus("active");
        user6.setPasswordFrequency("Monthly");

        credentials = new Credentials();

    }

    //success create user
    @Test
    public void createUserTest() throws Exception {

        when(userRepository.save(any(User.class))).thenReturn(user);
       
        User created = userService.createUser(user);

        assertEquals(created.getUserName(), user.getUserName());
        assertEquals("SUCCESS", created.getErrorCode().get(0));

    }

    // Test empty inputs
    @Test
    public void createUserTest1() throws Exception {

        User created = userService.createUser(user2);

        assertEquals(created.getUserName(), user2.getUserName());
        assertEquals("EMPTY USERNAME INPUT", created.getErrorCode().get(0));
        assertEquals("EMPTY TYPE INPUT", created.getErrorCode().get(1));
        assertEquals("EMPTY EMAIL INPUT", created.getErrorCode().get(2));
        assertEquals("EMPTY PASSWORD FREQUENCY INPUT", created.getErrorCode().get(3));

    }

    // Test Username already exists
    @Test
    public void createUserTest2() throws Exception {

        when(userRepository.existsById(anyString())).thenReturn(true);
        User created = userService.createUser(user);

        assertEquals(created.getUserName(), user.getUserName());
        assertEquals("USER_NAME_ALREADY_EXISTS", created.getErrorCode().get(0));

    }

    // Invalid inputs
    @Test
    public void createUserTest3() throws Exception {

        user.setUserName("user%%");
        user.setEmail("hotmail.com");
        user.setPasswordFrequency("daily");

        User created = userService.createUser(user);
        assertEquals(created.getUserName(), user.getUserName());
        assertEquals("INCORRECT USER NAME FORMAT", created.getErrorCode().get(0));
        assertEquals("INCORRECT EMAIL FORMAT", created.getErrorCode().get(1));
        assertEquals("INVALID PASSWORD FREQUENCY INPUT", created.getErrorCode().get(2));
    }

    // success login
    @Test
    public void loginUserTest() throws Exception {

        User currentUser = new User();
        currentUser.setUserName(user3.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user3.getPassword()));
        currentUser.setStatus(user3.getStatus());
        currentUser.setPasswordValidilityDate(user3.getPasswordValidilityDate());

        credentials.setUserName(currentUser.getUserName());
        credentials.setPassword(currentUser.getPassword());

        Optional<User> uOptional = Optional.of(currentUser);
        Optional<Credentials> uOptional1 = Optional.of(credentials);
        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.findUserByID(user3);

        assertEquals(created.getUserName(), user3.getUserName());
        assertEquals("SUCCESS", created.getErrorCode().get(0));
    }

    // invalid inputs
    @Test
    public void loginUserTest1() throws Exception {

        User created = userService.findUserByID(user5);

        assertEquals(created.getUserName(), user5.getUserName());
        assertEquals("EMPTY USERNAME INPUT", created.getErrorCode().get(0));
    }

    // user is suspended
    @Test
    public void loginUserTest2() throws Exception {

        user5.setUserName("user5");
        user5.setPassword("pass5");

        currentUser.setUserName(user5.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user5.getPassword()));

        credentials.setUserName(currentUser.getUserName());
        credentials.setPassword(currentUser.getPassword());

        Optional<User> uOptional = Optional.of(currentUser);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.findUserByID(user5);

        assertEquals(created.getUserName(), user5.getUserName());
        assertEquals("STATUS_SUSPEND", created.getErrorCode().get(0));

    }

    // password is expired
    @Test
    public void loginUserTest3() throws Exception {

        user5.setUserName("user5");
        user5.setPassword("pass5");
        user5.setStatus("active");

        currentUser.setUserName(user5.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user5.getPassword()));
        currentUser.setStatus(user5.getStatus());

        credentials.setUserName(currentUser.getUserName());
        credentials.setPassword(currentUser.getPassword());

        Optional<User> uOptional = Optional.of(currentUser);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.findUserByID(user5);

        assertEquals(created.getUserName(), user5.getUserName());
        assertEquals("PASSWORD_EXPIRED", created.getErrorCode().get(0));
    }

    // user doesn't exist
    @Test
    public void loginUserTest4() throws Exception {

        user5.setUserName("user5");
        user5.setPassword("pass5");

        currentUser.setUserName(user5.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user5.getPassword()));

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User created = userService.findUserByID(user5);

        assertEquals(created.getUserName(), user5.getUserName());
        assertEquals("USER_NOT_FOUND", created.getErrorCode().get(0));
    }

    // password is worng
    @Test
    public void loginUserTest5() throws Exception {

        user5.setUserName("user5");
        user5.setPassword("pass5");

        currentUser.setUserName(user5.getUserName());
        currentUser.setPassword(HashingPasswordAargon2.hashPassword(user3.getPassword()));
        currentUser.setStatus("active");

        credentials.setUserName(currentUser.getUserName());
        credentials.setPassword(currentUser.getPassword());

        Optional<User> uOptional = Optional.of(currentUser);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.findUserByID(user5);

        assertEquals(created.getUserName(), user5.getUserName());
        assertEquals("PASSWORD_WRONG", created.getErrorCode().get(0));

    }

    // success change password
    @Test
    public void changePasswordTest() throws Exception {

        user6.setStatus("new");
        credentials.setUserName(user6.getUserName());
        credentials.setPassword(user6.getPassword());

        Optional<User> uOptional = Optional.of(user6);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        when(userRepository.save(any(User.class))).thenReturn(user6);

        User created = userService.changePassword(userPassword1);

        assertEquals("SUCCESS", created.getErrorCode().get(0));
        assertEquals(created.getUserName(), user6.getUserName());
    }

    // user not found
    @Test
    public void changePasswordTest1() throws Exception {


        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User created = userService.changePassword(userPassword1);

        assertEquals("USER_NOT_FOUND", created.getErrorCode().get(0));
    }

    // Invalid username
    @Test
    public void changePasswordTest2() throws Exception {

        userPassword1.setUserName("user@#");

        User created = userService.changePassword(userPassword1);

        assertEquals("INCORRECT USER NAME FORMAT", created.getErrorCode().get(0));

    }

    // empty inputs
    @Test
    public void changePasswordTest3() throws Exception {

        userPassword1.setOldPassword("");
        userPassword1.setNewPassword("");
        userPassword1.setConfirmNewPassword("");

        credentials.setUserName(user6.getUserName());
        credentials.setPassword(user6.getPassword());

        Optional<User> uOptional = Optional.of(user6);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.changePassword(userPassword1);

        assertEquals("EMPTY OLD PASSWORD INPUT", created.getErrorCode().get(0));
        assertEquals("EMPTY NEW PASSWORD INPUT", created.getErrorCode().get(1));
        assertEquals("EMPTY CONFIRM PASSWORD INPUT", created.getErrorCode().get(2));

    }

    // new password and confirm password doesn't match
    @Test
    public void changePasswordTest4() throws Exception {

        userPassword1.setConfirmNewPassword("pass1");

        credentials.setUserName(user6.getUserName());
        credentials.setPassword(user6.getPassword());

        Optional<User> uOptional = Optional.of(user6);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.changePassword(userPassword1);

        assertEquals("UNMATCHED PASSWORD", created.getErrorCode().get(0));
    }

    // old password is wrong
    @Test
    public void changePasswordTest5() throws Exception {

        userPassword1.setOldPassword("pass12");

        credentials.setUserName(user6.getUserName());
        credentials.setPassword(user6.getPassword());

        Optional<User> uOptional = Optional.of(user6);
        Optional<Credentials> uOptional1 = Optional.of(credentials);

        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        User created = userService.changePassword(userPassword1);

        assertEquals("PASSWORD_WRONG", created.getErrorCode().get(0));
    }

    // User not found
    @Test
    public void changePasswordTest6() throws Exception {

        userPassword1.setUserName("user");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User created = userService.changePassword(userPassword1);

        assertEquals("USER_NOT_FOUND", created.getErrorCode().get(0));

    }

    // success update
    @Test
    public void updateUserTest() throws Exception {

        user.setStatus("active");
        credentials.setUserName(user.getUserName());
        credentials.setPassword(user.getPassword());

        Optional<User> uOptional = Optional.of(user);
        Optional<Credentials> uOptional1 = Optional.of(credentials);
        when(userRepository.findById(anyString())).thenReturn(uOptional);
        when(credentialsRepository.findById(anyString())).thenReturn(uOptional1);

        when(userRepository.save(any(User.class))).thenReturn(user);
        User getUser = userService.updateUser(user);

        assertEquals(getUser.getUserName(), user.getUserName());
        assertEquals("SUCCESS", getUser.getErrorCode().get(0));
    }

    // empty userName
    @Test
    public void updateUserTest1() throws Exception {

        User getUser = userService.updateUser(user2);

        assertEquals(getUser.getUserName(), getUser.getUserName());
        assertEquals("EMPTY USERNAME INPUT", getUser.getErrorCode().get(0));

    }

    // user doesn't exist
    @Test
    public void updateUserTest3() throws Exception {

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User getUser = userService.updateUser(user);

        assertEquals(getUser.getUserName(), user.getUserName());
        assertEquals("USER_NOT_FOUND", getUser.getErrorCode().get(0));
    }

    // success delete
    @Test
    public void deleteUserTest() throws Exception {

        Optional<User> uOptional = Optional.of(user);

        when(userRepository.findById(anyString())).thenReturn(uOptional);

        User getUser = userService.deleteUser(user.getUserName());

        assertEquals(getUser.getUserName(), user.getUserName());

    }

    // empty username
    @Test
    public void deleteUserTest1() throws Exception {

        User getUser = userService.deleteUser("");

        assertEquals("EMPTY USERNAME INPUT", getUser.getErrorCode().get(0));

    }

    // user doesn't exist
    @Test
    public void deleteUserTest2() throws Exception {

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        User getUser = userService.deleteUser(user.getUserName());

        assertEquals("USER_NOT_FOUND", getUser.getErrorCode().get(0));

    }
}
