package org.zx.common.security;

import org.springframework.data.repository.CrudRepository;

public interface SessionTokenRepo extends CrudRepository<SessionToken,String> {
}
