package Vending.controller;

/**
 * An exception class created for the API in case a coin is not found during a GET request that API receives
 */
public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(Long id) {
        super("Couldnt find coin by id= "+ id);
    }
}
