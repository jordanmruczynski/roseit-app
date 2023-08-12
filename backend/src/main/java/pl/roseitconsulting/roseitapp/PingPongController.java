package pl.roseitconsulting.roseitapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {
    record PingPong(String result){}

    int pongs = 0;

    @GetMapping("/ping")
    public PingPong ping() {
        return new PingPong("Pong!" + pongs++);
    }
}
