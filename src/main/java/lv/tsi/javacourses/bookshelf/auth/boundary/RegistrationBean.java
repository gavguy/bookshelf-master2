/*
 * Copyright (c) 2018 Dimitrijs Fedotovs
 * This code is licensed under MIT license
 * (see LICENSE.txt for details)
 */

package lv.tsi.javacourses.bookshelf.auth.boundary;

import lv.tsi.javacourses.bookshelf.auth.control.PasswordHashAlgorithm;
import lv.tsi.javacourses.bookshelf.auth.control.UserDAO;
import lv.tsi.javacourses.bookshelf.auth.model.Role;
import lv.tsi.javacourses.bookshelf.auth.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Named
@ViewScoped
public class RegistrationBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginBean.class);
    @Inject
    private PasswordHashAlgorithm passwordHash;
    @Inject
    private CurrentUser currentUser;
    @Inject
    private UserDAO userDAO;
    @NotBlank(message = "Login must not be blank")
    @Size(min = 4, message = "Login name should be at least 4 characters long")
    @Size(max = 100, message = "Login name is too long")
    private String loginName;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    @Size(max = 200, message = "Password is too long")
    private String password1;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    @Size(max = 200, message = "Password is too long")
    private String password2;

    public String register() {
        if (!Objects.equals(password1, password2)) {
            FacesMessage msg = new FacesMessage("Passwords do not match");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        try {
            UserEntity user = new UserEntity();
            user.setLoginName(loginName);
            String hashedPassword = passwordHash.get().generate(password1.toCharArray());
            user.setPasswordHash(hashedPassword);
            user.setRoleName(Role.USER);
            userDAO.createUser(user);
            LOGGER.debug("User {} registered. Hashed password: {}", loginName, hashedPassword);
            return "success";
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage("Registration unsuccessful. Try another login name");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            LOGGER.warn("Registration unsuccessful", e);
            return null;
        }
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
