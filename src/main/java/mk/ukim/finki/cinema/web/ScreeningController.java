package mk.ukim.finki.cinema.web;

import jakarta.validation.Valid;
import mk.ukim.finki.cinema.model.Screening;
import mk.ukim.finki.cinema.service.CinemaRuleViolationException;
import mk.ukim.finki.cinema.service.HallService;
import mk.ukim.finki.cinema.service.MovieService;
import mk.ukim.finki.cinema.service.ScreeningService;
import mk.ukim.finki.cinema.web.form.ScreeningForm;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final HallService hallService;

    public ScreeningController(ScreeningService screeningService,
                               MovieService movieService,
                               HallService hallService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
        this.hallService = hallService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long movieId,
                       @RequestParam(required = false) Long hallId,
                       @RequestParam(defaultValue = "false") boolean includePast,
                       Model model) {
        List<Screening> screenings = screeningService.search(movieId, hallId, includePast);
        model.addAttribute("schedule", screeningService.groupByDay(screenings));
        model.addAttribute("screeningCount", screenings.size());
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("selectedHallId", hallId);
        model.addAttribute("includePast", includePast);
        model.addAttribute("today", LocalDate.now());
        populateOptions(model);
        return "screenings/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long movieId, Model model) {
        ScreeningForm form = new ScreeningForm();
        form.setMovieId(movieId);
        model.addAttribute("form", form);
        populateOptions(model);
        return "screenings/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ScreeningForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            populateOptions(model);
            return "screenings/form";
        }
        Screening screening = screeningService.create(
                form.getMovieId(), form.getHallId(), form.getStartsAt(), form.getPrice());
        redirect.addFlashAttribute("success",
                screening.getMovie().getTitle() + " was scheduled in " + screening.getHall().getName() + ".");
        return "redirect:/screenings";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", ScreeningForm.from(screeningService.findById(id)));
        model.addAttribute("screeningId", id);
        populateOptions(model);
        return "screenings/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ScreeningForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("screeningId", id);
            populateOptions(model);
            return "screenings/form";
        }
        try {
            screeningService.update(id, form.getMovieId(), form.getHallId(), form.getStartsAt(), form.getPrice());
        } catch (CinemaRuleViolationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("screeningId", id);
            populateOptions(model);
            return "screenings/form";
        }
        redirect.addFlashAttribute("success", "Screening updated.");
        return "redirect:/screenings";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        Screening screening = screeningService.findById(id);
        String label = screening.getMovie().getTitle();
        screeningService.delete(id);
        redirect.addFlashAttribute("success", "Screening of " + label + " and its bookings were removed.");
        return "redirect:/screenings";
    }

    private void populateOptions(Model model) {
        model.addAttribute("movies", movieService.search(null));
        model.addAttribute("halls", hallService.findAll());
    }
}
