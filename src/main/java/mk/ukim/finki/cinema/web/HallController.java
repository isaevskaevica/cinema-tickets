package mk.ukim.finki.cinema.web;

import mk.ukim.finki.cinema.model.Hall;
import mk.ukim.finki.cinema.service.HallService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/halls")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public String list(Model model) {
        List<Hall> halls = hallService.findAll();
        Map<Long, Long> upcomingCounts = new LinkedHashMap<>();
        for (Hall hall : halls) {
            upcomingCounts.put(hall.getId(), hallService.countUpcomingScreenings(hall));
        }
        model.addAttribute("halls", halls);
        model.addAttribute("upcomingCounts", upcomingCounts);
        return "halls/list";
    }
}
