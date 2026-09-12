package mk.ukim.finki.cinema.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TicketForm {

    @NotNull(message = "Pick a screening")
    private Long screeningId;

    @NotBlank(message = "Name is required")
    @Size(max = 80)
    private String customerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 120)
    private String customerEmail;

    @NotNull(message = "Number of seats is required")
    @Min(value = 1, message = "Book at least 1 seat")
    @Max(value = 20, message = "At most 20 seats per booking")
    private Integer seats = 1;

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }
}
