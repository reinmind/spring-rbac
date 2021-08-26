package org.zx.common.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    User findUserByUsername(String username);
}
