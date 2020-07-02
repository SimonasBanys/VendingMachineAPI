package Vending.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Coin {


    /**
     * An Entity class to hold all the required information requiring coins
     * @param id is a automatically generated value for the database when a new "Coin" is inserted
     * @param value a variable containing coin denomination in pence value
     * @param amount a variable containing amount of specific value coin in the machine
     */
    private @Id @GeneratedValue Long id;
    private Long value;
    private Long amount;


    /**
     * default constructor required for JPA repository, not used currently but required by default
     */
    public Coin(){}


    /**
     * Constructor used for "Coin" entity.
     * @param value a variable containing coin denomination in pence value
     * @param amount a variable containing amount of specific value coin in the machine
     */
    public Coin(Long value, Long amount){
        this.value = value;
        this.amount = amount;
    }

    /**
     * Method for returning the amount of coins being used of certain value
     * @return amount
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Method for returning the value of coins being used
     * @return
     */
    public Long getValue() {
        return value;
    }

    /**
     * Method for updating the amount of coins of specific value
     * @param amount new amount for the "Coin" variable being accessed
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
