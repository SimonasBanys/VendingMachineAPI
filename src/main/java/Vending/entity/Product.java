package Vending.entity;

public class Product {

    private String name;
    private Long cost;
    private Long amount;



    /**
     * Constructor for Product object. Only used by the test-harness.
     * @param name name of the product being created
     * @param cost cost of the product
     * @param amount amount available
     */
    public Product(String name, Long cost, Long amount) {
        this.name = name;
        this.cost = cost;
        this.amount = amount;
    }

    /**
     * Method for updating the amount of product available in the "Machine"
     * @param amount new product amount being updated to
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * Used for creation of labels in test harness to provide information to the user
     * @return name of the product being accessed
     */
    public String getName() {
        return name;
    }

    /**
     * Used for calculation of credit available to user and the success of an attempted purchase
     * @return cost of the product in pence value
     */
    public Long getCost() {
        return cost;
    }

    /**
     * Used to determine availability of the  product in the Machine
     * @return
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Used to get the cost of product in string value for the product allowing more sensible representation of the product cost
     * @return
     */
    public String getCostAsString(){
        return cost/100 + "."+ cost%100/10 + cost%10;
    }
}
