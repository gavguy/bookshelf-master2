package lv.tsi.javacourses.bookshelf.books.boundary;

import lv.tsi.javacourses.bookshelf.auth.model.UserEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/user")

@Stateless

public class UserList {
    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public List<UserList> getUser() {
        List<UserEntity> resultList = em.createQuery(
                "select  u " +
                        "from User u", UserEntity.class)
                .getResultList();

        List<UserDto> dtos = new ArrayList<>().
        for (UserEntity o : resultList) {
            UserDto d = new UserDto();
            d.setName(u.getLoginRole())
            d.setRole(u.getRoleNme().toString());
        }
    }
}