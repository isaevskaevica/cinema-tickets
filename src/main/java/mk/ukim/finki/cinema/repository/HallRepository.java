package mk.ukim.finki.cinema.repository;

import mk.ukim.finki.cinema.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {

    List<Hall> findAllByOrderByNameAsc();
}
