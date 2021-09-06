package org.zx.common.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(unique = true)
    String username;
    String nickname;
    String password;
    String role;
    @ElementCollection
    List<Auth> authorities;
    String email;
    @ColumnDefault(value = "true")
    boolean enabled;
}
