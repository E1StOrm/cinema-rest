package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CinemaHallController {
    private final List<Ticket> ticketList = Collections.synchronizedList(new ArrayList<>());
    private Hall hall = new Hall();

    @GetMapping("/seats")
    public Hall getHall() {
        return hall;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody Seat requestSeat) {
        Seat seat = hall.getSeat(requestSeat.getRow(), requestSeat.getColumn());
        if (seat == null) {
            return new ResponseEntity(
                    Map.of("error", "The number of a row or a column is out of bounds!"),
                    HttpStatusCode.valueOf(400));
        } else if (seat.getState() == PurchaseState.Purchased) {
            return new ResponseEntity(
                    Map.of("error", "The ticket has been already purchased!"),
                    HttpStatusCode.valueOf(400));
        } else {
            seat.setState(PurchaseState.Purchased);
            Ticket ticket = new Ticket(UUID.randomUUID().toString(), seat);
            ticketList.add(ticket);
            return new ResponseEntity(ticket, HttpStatus.OK);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnTicket(@RequestBody Map<String, String> token) {
        Ticket ticket = ticketList.stream()
                .filter(t -> t.getToken().equals(token.get("token")))
                .findFirst().orElse(null);

        if (ticket == null) {
            return new ResponseEntity(Map.of("error", "Wrong token!"), HttpStatus.BAD_REQUEST);
        }

        ticketList.remove(ticket);
        ticket.getTicket().setState(PurchaseState.Free);
        return new ResponseEntity(Map.of("ticket", ticket.getTicket()), HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<String> stats(@RequestParam String password) {
        if (!"super_secret".equals(password)) {
            return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }

        Stats stats = new Stats(
                ticketList.stream().collect(Collectors.summingInt(t -> t.getTicket().getPrice())),
                hall.getSeats().size() - ticketList.size(),
                ticketList.size()
        );
        return new ResponseEntity(stats, HttpStatus.OK);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handlerMissingParameter(
            MissingServletRequestParameterException e, WebRequest request) {
        return new ResponseEntity(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }
}
