package mk.ukim.finki.cinema.repository;

import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOrderByBookedAtDesc();

    @Query("select coalesce(sum(t.seats), 0) from Ticket t")
    long sumSeats();

    void deleteByScreening(Screening screening);
}
