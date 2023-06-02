package com.qrdn.login.service.passwordhash;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class HashingPasswordAargon2 {
    
    private HashingPasswordAargon2(){}
    
    public static String hashPassword(String password) {
        // Create an instance of Argon2
        Argon2 argon2 = Argon2Factory.create();
        // Generate a hash of the password
        return argon2.hash(10, 65536, 1, password.toCharArray());
    }

    // Verifies a password against a given hash and returns true if the password matches, false otherwise
    public static boolean verifyPassword(String password, String hash) {
        // Create an instance of Argon2
        Argon2 argon2 = Argon2Factory.create();
 
        // Verify the password against the hash
        return argon2.verify(hash, password.toCharArray());

    }
}
