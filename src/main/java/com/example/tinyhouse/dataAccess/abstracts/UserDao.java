package com.example.tinyhouse.dataAccess.abstracts;

import com.example.tinyhouse.entities.concretes.User;
import com.example.tinyhouse.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User getByEmail(String email);
    int countByRole(UserRole role);

    @Query(value = "SELECT dbo.fn_GetUserCountByRole(:role)", nativeQuery = true)
    Integer getUserCountByRoleFn(@Param("role") String role);

}


