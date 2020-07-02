package Vending.db;

import Vending.entity.Coin;
import Vending.repository.CoinRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class LoadDB {

    /**
     * Creating and populating Coin database. All coins are denoted in pence value for ease of calculations.
     * Creates the entries for values of coin accepted by the machine, easily modifiable to accept lower denomination
     * @param repo uses CoinRepo interface for the schema
     * @return a created table containing values we're storing bellow
     */
    @Bean
    CommandLineRunner initDatabase(CoinRepo repo){
        return args -> {
            repo.save(new Coin((long) 200, (long) 0));
            repo.save(new Coin((long) 100, (long) 0));
            repo.save(new Coin((long) 50, (long) 0));
            repo.save(new Coin((long) 20, (long) 0));
            repo.save(new Coin((long) 10, (long) 0));
            repo.save(new Coin((long) 5, (long) 0));
        };
    }
}
