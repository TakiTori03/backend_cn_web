package com.hust.Ecommerce.repositories.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.Ecommerce.entities.authentication.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
