package Vending.controller;

import Vending.entity.Coin;
import Vending.repository.CoinRepo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CoinController {

    private final CoinRepo repo;

    /**
     * Constructor for the API controller
     * @param repo requires the interface for creation of API, as the API accesses the table created on the back end
     */
    public CoinController(CoinRepo repo) {
        this.repo = repo;
    }

    /**
     * a GET request to the API to receive all entries in "coins" table
     * @return returns all entries in the table
     */
    @GetMapping("/coins")
    List<Coin> all(){
        return repo.findAll();
    }

    /**
     * a GET request to the API to receive an entry based on ID used as a parameter
     * @param id a parameter used to determine which entry should be used as a return
     * @return either entry found by ID or exception of entry not being found
     */
    @GetMapping("/coins/{id}")
    Coin c(@PathVariable Long id){
        return repo.findById(id).orElseThrow(() -> new CoinNotFoundException(id));
    }

    /**
     * a POST request to API to add a new "coin" entry in the table
     * @param newCoin "coin" entry to be added accepted as a parameter
     * @return either success or failure of adding a new entry
     */
    @PostMapping("/coins")
    Coin newCoin(Coin newCoin){
        return repo.save(newCoin);
    }

    /**
     * a DELETE request to the API to remove a specific entry in the table
     * @param id used as a parameter to determine which entry is to be removed from the table
     */
    @DeleteMapping("/coins/{id}")
    void deleteCoin(@PathVariable Long id){
        repo.deleteById(id);
    }

    /**
     * a PUT request to API to determine which entry is to be updated
     * @param c "coin" object from the front end that is used to update the values in the table
     * @param id ID of the "coin" entry in the table that is being updated
     * @return either success of update or error of entry not being found
     */
    @PutMapping("/coins/{id}")
    Coin updateCoin(Coin c, @PathVariable Long id){
        return repo.findById(id).map(coin -> {
            coin.setAmount(c.getAmount());
            return repo.save(coin);
        }).orElseThrow(() -> new CoinNotFoundException(id));
    }


}
