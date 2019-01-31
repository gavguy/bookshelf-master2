
package lv.tsi.javacourses.bookshelf.books.boundary;

import lv.tsi.javacourses.bookshelf.books.model.BookEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(BookEntity book) throws URISyntaxException {
        // void - ne vozvroshae nicego
        Optional<BookEntity> duplicateisbn =
        em.createQuery("select b " +
                "from  Book b " +
                "where b.isbn = :isbn", BookEntity.class)
                .setParameter("isbn", book.getIsbn())
                .getResultStream()
                .findFirst().
       em.persist(book);
        return Response
                .status(Response.Status.CREATED)
                .entity(book)
                .build();
        if(duplicateisbn.isPresent()) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .build();
        }

    }


}