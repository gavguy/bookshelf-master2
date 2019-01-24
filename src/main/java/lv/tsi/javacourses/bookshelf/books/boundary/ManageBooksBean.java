package lv.tsi.javacourses.bookshelf.books.boundary;

import lv.tsi.javacourses.bookshelf.books.model.ReservationEntity;
import lv.tsi.javacourses.bookshelf.books.model.ReservationStatus;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class ManageBooksBean implements Serializable {
    @PersistenceContext
    private EntityManager em;
    private List<ReservationEntity> availableResult;
    private List<ReservationEntity> takenResult;

    public void prepare() {
        availableResult = new ArrayList<>();
        List<ReservationEntity> userReservations = em.createQuery(
                "select r from Reservation r " +
                        "where r.status = 'ACTIVE'", ReservationEntity.class)
                .getResultList();

        for (ReservationEntity r : userReservations) {
            Long reservationId = r.getId();
            Optional<ReservationEntity> firstReservation = em.createQuery(
                    "select r from Reservation r " +
                            "where r.book = :book and r.status <> 'CLOSED' " +
                            "order by r.created", ReservationEntity.class)
                    .setParameter("book", r.getBook())
                    .getResultStream()
                    .findFirst();
            if (firstReservation.isEmpty() || firstReservation.get().getId().equals(reservationId)) {
                availableResult.add(r);
            }
        }

        takenResult = em.createQuery("select r from Reservation r " +
                "where r.status = 'TAKEN'", ReservationEntity.class)
                .getResultList();
    }
    @Transactional
    //rabota s bazoj danih
public void giveBook(ReservationEntity reservation) {
     ReservationEntity r = em.merge(reservation);
     //sinhroniz s bazoj danih
r.setStatus(ReservationStatus.TAKEN);
//rezervacija = taken
//taken - na rukah
prepare();
}
@Transactional
    public void backBook(ReservationEntity reservation) {
        ReservationEntity r = em.merge(reservation);
        //sinhroniz s bazoj danih
        r.setStatus(ReservationStatus.CLOSED);
        prepare();
    }

    public List<ReservationEntity> getAvailableBooks() {
        return availableResult;
        // svobodnije knigi
    }

    public List<ReservationEntity> getTakenBooks() {
        return takenResult;
        //vibiraem dlja menegera vse knigi
        //vidanije polzovateljam
    }

}