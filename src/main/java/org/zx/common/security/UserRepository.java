package org.zx.common.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    int deleteByUsername(String username);
    boolean existsByUsername(String username);
    User findUserByUsername(String username);
}
