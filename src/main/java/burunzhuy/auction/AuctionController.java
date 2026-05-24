package burunzhuy.auction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {
    private AuctionService service;

    public AuctionController (AuctionService service) {
        this.service = service;
    }

    @GetMapping()
    public String getData()
    {
        return this.service.getId();
    }
}
