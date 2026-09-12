package mk.ukim.finki.cinema.web;

import jakarta.validation.Valid;
import mk.ukim.finki.cinema.model.Ticket;
import mk.ukim.finki.cinema.service.CinemaRuleViolationException;
import mk.ukim.finki.cinema.service.ScreeningService;
import mk.ukim.finki.cinema.service.TicketService;
import mk.ukim.finki.cinema.web.form.TicketForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ScreeningService screeningService;

    public TicketController(TicketService ticketService, ScreeningService screeningService) {
        this.ticketService = ticketService;
        this.screeningService = screeningService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "tickets/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long screeningId, Model model) {
        TicketForm form = new TicketForm();
        form.setScreeningId(screeningId);
        model.addAttribute("form", form);
        populateOptions(model);
        return "tickets/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") TicketForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            populateOptions(model);
            return "tickets/form";
        }
        Ticket ticket;
        try {
            ticket = ticketService.book(
                    form.getScreeningId(), form.getCustomerName(), form.getCustomerEmail(), form.getSeats());
        } catch (CinemaRuleViolationException e) {
            model.addAttribute("error", e.getMessage());
            populateOptions(model);
            return "tickets/form";
        }
        redirect.addFlashAttribute("success",
                ticket.getSeats() + (ticket.getSeats() == 1 ? " seat" : " seats") + " booked for "
                        + ticket.getScreening().getMovie().getTitle() + " — total " + ticket.getTotalPrice() + " MKD.");
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirect) {
        Ticket ticket = ticketService.cancel(id);
        redirect.addFlashAttribute("success",
                "Booking for " + ticket.getCustomerName() + " was cancelled and "
                        + ticket.getSeats() + (ticket.getSeats() == 1 ? " seat was" : " seats were") + " released.");
        return "redirect:/tickets";
    }

    private void populateOptions(Model model) {
        model.addAttribute("screenings", screeningService.findBookable());
    }
}
