/*
 * Copyright (c) 2018 Dimitrijs Fedotovs
 * This code is licensed under MIT license
 * (see LICENSE.txt for details)
 */

package lv.tsi.javacourses.bookshelf.auth.boundary;

import lv.tsi.javacourses.bookshelf.auth.control.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.Serializable;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Named
@ViewScoped
public class LoginBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginBean.class);

    @Inject
    private SecurityContext securityContext;
    @Inject
    private CurrentUser currentUser;
    @Inject
    private UserDAO userDAO;
    @NotBlank(message = "Login must not be blank")
    private String loginName;
    @NotBlank(message = "Password must not be blank")
    private String password;

    public String login() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Credential credential = new UsernamePasswordCredential(loginName, password);
        AuthenticationStatus status = securityContext.authenticate(
                requestFrom(facesContext),
                responseFrom(facesContext),
                withParams().credential(credential));
        if (status == AuthenticationStatus.SUCCESS) {
            LOGGER.info("User [{}] is logged in", loginName);
            currentUser.setUser(userDAO.findUser(loginName).orElse(null));
            return null;
        } else {
            LOGGER.info("User [{}] is NOT logged in", loginName);
            FacesMessage msg = new FacesMessage("Password or login name is incorrect");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
    }

    public String logout() {
        try {
            LOGGER.debug("Logging out");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            requestFrom(facesContext).logout();
            currentUser.setUser(null);
            return "/goodbye.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            LOGGER.error("Cannot logout", e);
            return null;
        }
    }

    private HttpServletResponse responseFrom(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        return (HttpServletResponse) externalContext.getResponse();
    }

    private HttpServletRequest requestFrom(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        return (HttpServletRequest) externalContext.getRequest();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
