package com.playground.jeq.springtestapp.Repository;

import com.playground.jeq.springtestapp.Model.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByDescription(String description);
}
