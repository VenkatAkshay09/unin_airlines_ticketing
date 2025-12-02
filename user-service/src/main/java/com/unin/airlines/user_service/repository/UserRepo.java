package com.unin.airlines.user_service.repository;

import com.unin.airlines.user_service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Long> {

}
