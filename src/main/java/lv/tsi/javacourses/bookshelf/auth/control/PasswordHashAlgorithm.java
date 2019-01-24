/*
 * Copyright (c) 2018 Dimitrijs Fedotovs
 * This code is licensed under MIT license
 * (see LICENSE.txt for details)
 */

package lv.tsi.javacourses.bookshelf.auth.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.PasswordHash;

@ApplicationScoped
public class PasswordHashAlgorithm {
    @Inject
    private PasswordHash passwordHash;

    public PasswordHash get() {
        return passwordHash;
    }
}
