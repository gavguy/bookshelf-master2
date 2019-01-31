
package lv.tsi.javacourses.bookshelf.books.boundary;

import lv.tsi.javacourses.bookshelf.books.model.BookEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/books")
@Stateless
public class BooksApi {
    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookEntity> getBooks() {
        return em.createQuery("select b from Book b")
                .getResultList();
    }

    @GET
@Produces(MediaType.APPLICATION_JSON)
    @Path("/{bookId}")
    public BookEntity getBookById(@PathParam("bookId") long bookid) {
        return em.find(BookEntity.class, bookid);
    }
}