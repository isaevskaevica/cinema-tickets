package mk.ukim.finki.cinema.web;

import mk.ukim.finki.cinema.service.MovieService;
import mk.ukim.finki.cinema.service.ScreeningService;
import mk.ukim.finki.cinema.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final TicketService ticketService;

    public HomeController(MovieService movieService,
                          ScreeningService screeningService,
                          TicketService ticketService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.ticketService = ticketService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("movieCount", movieService.count());
        model.addAttribute("upcomingCount", screeningService.countUpcoming());
        model.addAttribute("ticketCount", ticketService.count());
        model.addAttribute("seatsSold", ticketService.totalSeatsSold());
        model.addAttribute("upcoming", screeningService.findUpcoming().stream().limit(10).toList());
        model.addAttribute("recentTickets", ticketService.findAll().stream().limit(5).toList());
        return "index";
    }
}
