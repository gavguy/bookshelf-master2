/*
 * Copyright (c) 2018 Dimitrijs Fedotovs
 * This code is licensed under MIT license
 * (see LICENSE.txt for details)
 */

package lv.tsi.javacourses.bookshelf.auth.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "User")
@Table(name = "users")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "login_name", length = 100, unique = true, nullable = false)
    private String loginName;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String name) {
        this.loginName = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public Role getRoleName() {
        return roleName;
    }

    public void setRoleName(Role roleName) {
        this.roleName = roleName;
    }
}
