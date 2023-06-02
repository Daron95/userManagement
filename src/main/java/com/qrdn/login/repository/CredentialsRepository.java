package com.qrdn.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrdn.login.entity.Credentials;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, String>  {
    
    Optional<Credentials> findById(String userName);

}
