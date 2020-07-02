package Vending.repository;

import Vending.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for creating a repository of Coin entity
 */
public interface CoinRepo extends JpaRepository<Coin, Long> {
}
