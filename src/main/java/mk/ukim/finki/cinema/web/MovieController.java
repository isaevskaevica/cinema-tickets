package mk.ukim.finki.cinema.web;

import jakarta.validation.Valid;
import mk.ukim.finki.cinema.model.Movie;
import mk.ukim.finki.cinema.model.enums.AgeRating;
import mk.ukim.finki.cinema.model.enums.Genre;
import mk.ukim.finki.cinema.service.MovieService;
import mk.ukim.finki.cinema.web.form.MovieForm;
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
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Genre genre, Model model) {
        model.addAttribute("movies", movieService.search(genre));
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("genres", Genre.values());
        return "movies/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new MovieForm());
        populateOptions(model);
        return "movies/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") MovieForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            populateOptions(model);
            return "movies/form";
        }
        Movie movie = new Movie();
        form.applyTo(movie);
        movieService.save(movie);
        redirect.addFlashAttribute("success", movie.getTitle() + " was added.");
        return "redirect:/movies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", MovieForm.from(movieService.findById(id)));
        model.addAttribute("movieId", id);
        populateOptions(model);
        return "movies/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") MovieForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("movieId", id);
            populateOptions(model);
            return "movies/form";
        }
        Movie movie = movieService.findById(id);
        form.applyTo(movie);
        movieService.save(movie);
        redirect.addFlashAttribute("success", movie.getTitle() + " was updated.");
        return "redirect:/movies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        String title = movieService.findById(id).getTitle();
        movieService.delete(id);
        redirect.addFlashAttribute("success", title + " and its screenings were removed.");
        return "redirect:/movies";
    }

    private void populateOptions(Model model) {
        model.addAttribute("genres", Genre.values());
        model.addAttribute("ratings", AgeRating.values());
    }
}
