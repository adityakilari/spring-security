package com.iralik.spring_security.repository;

import com.iralik.spring_security.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

  Users findByUserName(String userName);
  Optional<Users> findById(int id);

}
