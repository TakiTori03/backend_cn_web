package com.hust.Ecommerce.repositories.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hust.Ecommerce.entities.authentication.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByActivationKey(String activationKey);

    Optional<User> findOneByResetKey(String resetKey);

    // tim kiem so luong account co role va da acctivated
    @Query("SELECT COUNT(u.id) FROM User u JOIN u.role r WHERE r.name = :role AND u.status != 0 ")
    int countAccountActivated(@Param("role") String role);

}
