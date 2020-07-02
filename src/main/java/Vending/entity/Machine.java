package Vending.entity;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Machine extends JFrame implements ActionListener {

    private int credit = 0;
    private int totalMoney = 0;
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<Product> prods = new ArrayList<>();
    private final long[] startingFloat = new long[6];
    private final String url = "http://localhost:8080/coins";
    private OkHttpClient client;
    private final MediaType mediaType = MediaType.parse("text/plain");
    private JTextField tf;
    private ArrayList<JButton> buttons = new ArrayList<>();
    private JFrame f;
    private final ButtonGroup cbCoins = new ButtonGroup();
    private final ButtonGroup cbProducts = new ButtonGroup();
    private ArrayList<JRadioButton> cbCoin, cbProduct;
    private ArrayList<JLabel> labelList = new ArrayList<>();
    private final String[] buttonLabel = {"Initialise","Deposit coin","Return change","Make purchase","Reset Machine"};
    private final String[] coinSelect = {"£2","£1","50p","20p","10p","5p"};
    private final String[] productSelect = {"Fanta", "Sprite", "CocaCola"};
    private final String creditString = "Your current credit: £";
    private ArrayList<Coin> change = new ArrayList<>();
    private JLabel coinLabel;

    /**
     * a constructor for Test-Harness called Machine
     */
    public Machine()  {
        client = new OkHttpClient();
        initialiseFrame();
    }

    /**
     * Method used to initialise the GUI of Test-Harness. Creates a JFrame and elements used for it.
     * Initialising elements with data contained in them is done in separate methods to make reading of code easier.
     */
    public void initialiseFrame(){
        f = new JFrame("Vending Machine GUI");
        f.setSize(800,400);
        cbCoin = new ArrayList<>();
        cbProduct = new ArrayList<>();
        initLabels();
        labelList.get(2).setText("Current coins by amount in machine:");
        labelList.get(4).setText("Change given:");
        displayCoins();
        initFloatRead();
        initButtons();
        initCoins();
        initProducts();
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Method used for initialising labels and denoting their bounds for information.
     * calls a method to update the label that shows user credit
     */
    public void initLabels(){
        for (int i = 0; i < 6; i++){
            labelList.add(new JLabel());
            labelList.get(i).setBounds(400, 100+i*25, 300, 50);
            labelList.get(i).setVisible(true);
            f.add(labelList.get(i));
        }
        updateCredit();
    }

    /**
     * Method is used to display the exact amount for each coin currently stored in the Machine.
     * Updates whenever a coin is inserted or change given
     */
    public void displayCoins(){
        labelList.get(3).setText(getAmountsString(coins));
    }

    /**
     * Initialises a Text Field interface object that allows the machine user input used to determine starting float
     * for the machine
     */
    public void initFloatRead(){
        tf = new JTextField("Input starting float, coin amounts separated by ','");
        tf.setBounds(50,50,300,20);
        tf.setVisible(true);
        tf.setEditable(true);
        f.add(tf);
        coinLabel = new JLabel("<html>Coin amounts entered bellow are determined largest to smallest denomination."+
                "<br> Should you wish to skip coin denomination, enter 0 in its location.</html>");
        coinLabel.setBounds(50, 0, 600,50);
        coinLabel.setVisible(true);
        f.add(coinLabel);
    }

    /**
     * Initialises the buttons for the GUI. Makes only "Initialise" button available.
     */
    public void initButtons(){
        for (int i = 0; i < 5; i++){
            buttons.add(new JButton(buttonLabel[i]));
            buttons.get(i).addActionListener(this);
            buttons.get(i).setBounds(50+130*i, 300, 130,25);
            buttons.get(i).setVisible(true);
            buttons.get(i).setEnabled(false);
            f.add(buttons.get(i));
        }
        buttons.get(0).setEnabled(true);
    }

    /**
     * Method used to initialise Product selection in GUI by providing product name and cost.
     * Creates a group of Radio Buttons allowing only single selection within the group.
     */
    public void initProducts(){
        prods.add(new Product(productSelect[0], (long)95,(long) 200));
        prods.add(new Product(productSelect[1], (long)135,(long) 200));
        prods.add(new Product(productSelect[2], (long)150,(long) 200));
        Product temp;
        for (int i = 0; i < prods.size(); i++){
            temp = prods.get(i);
            cbProduct.add(new JRadioButton(temp.getName() + " cost: £" + temp.getCost()/100 +"."+temp.getCost()%100,false));
            cbProduct.get(i).setBounds(125, 100+i*25, 250,25);
            cbProduct.get(i).setVisible(true);
            cbProducts.add(cbProduct.get(i));
            f.add(cbProduct.get(i));
        }
        cbProduct.get(0).setSelected(true);
    }

    /**
     * Method used to update the label where user credit in the Machine is displayed
     */
    public void updateCredit(){
        labelList.get(0).setText(creditString + credit/100 + "."+credit%100/10 + credit%10);
    }

    /**
     * Method used to determine amounts of coin for display for either current coin amounts in machine
     * or change given to the user. Ignores the coins which are not used
     * @param array ArrayList parameter used to create a string denoting values and amounts of coin provided.
     * @return a string in a readable format of coins and their amounts
     */
    public String getAmountsString(ArrayList<Coin> array){
        String changeString = "";
        for (int i = 0; i < array.size(); i++){
            if (array.get(i).getAmount() > 0){
                changeString += coinSelect[i] + ": " + array.get(i).getAmount();
            }
            if (i < array.size()-1){
                changeString += ", ";
            }
        }
        return changeString;
    }

    /**
     * Used to call other methods depending on the button pressed.
     * @param e Event that is created by the press of a button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttons.get(0)){
            try {
                getFloat();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == buttons.get(4)){
            try {
                reset();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == buttons.get(1)){
            try {
                depositCoin();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == buttons.get(2)){
            try {
                giveChange();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        } else if (e.getSource() == buttons.get(3)) {
            makePurchase();
        }
        displayCoins();
    }

    /**
     * Method used to initialise the float by reading the coin amounts from Text Field.
     * Checks how many "different" coins are placed with the initial float.
     * Should the amount of "different" coins attempted to be placed is greater than the unique coins accepted
     * the machine takes only the first 6 values. Coin amounts are entered largest to smallest denomination.
     * @throws IOException
     * @throws JSONException
     */
    public void getFloat() throws IOException, JSONException {
        String s= tf.getText();
        String[] div = s.split(",");
        int len;
        if (div.length > startingFloat.length){
            len = startingFloat.length;
        } else {
            len = div.length;
        }
        for (int i = 0; i < len; i++){
            startingFloat[i] = Integer.parseInt(div[i]);
        }
        initialiseFloat(startingFloat);
        getFromDB();
        for (int i = 0; i < buttons.size(); i++){
            buttons.get(i).setEnabled(!buttons.get(i).isEnabled());
        }
        tf.setEditable(false);
    }

    /**
     * Method used to call PUT request in the API to set initial starting money amount in the machine
     * @param money
     * @throws IOException
     */
    public void initialiseFloat(long[] money) throws IOException {
        for (int i = 0; i < money.length; i++){
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://localhost:8080/coins/" + (i+1) + "?amount=" + money[i])
                    .method("PUT", body)
                    .build();
            client.newCall(request).execute();
        }
    }

    /**
     * Method to reset the machine to pre-float placement state
     * @throws IOException
     * @throws JSONException
     */
    public void reset() throws IOException, JSONException {
        for (int i = 0; i < coins.size(); i++){
            coins.get(i).setAmount((long)0);
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://localhost:8080/coins/" + (i+1) + "?amount=" + coins.get(i).getAmount())
                    .method("PUT", body)
                    .build();
            client.newCall(request).execute();
        }
        for (int i = 0; i < buttons.size(); i++){
            buttons.get(i).setEnabled(!buttons.get(i).isEnabled());
        }
        tf.setEditable(true);
        credit = 0;
        updateCredit();
        getFromDB();
    }

    /**
     * Method used to deposit coin. Calls another method to send PUT request to API to update coin amounts in the table
     * @throws IOException
     */
    public void depositCoin() throws IOException {
        int index = 0;
        for (int i = 0; i < cbCoin.size(); i++){
            if (cbCoin.get(i).isSelected()){
                index = i;
                break;
            }
        }
        coins.get(index).setAmount(coins.get(index).getAmount()+1);
        credit += coins.get(index).getValue();
        updateCoin(index);
        updateCredit();
    }

    /**
     * Method to provide change to the user. Gives change in order of largest to smallest denomination available
     * based on the amount of coin available in the machine. Updates database table entries by calling PUT request on API
     * @throws IOException
     * @throws JSONException
     */
    public void giveChange() throws IOException, JSONException {
        int c = 0;
        while (credit > 0){
            Long req = credit/coins.get(c).getValue();
            if (req <= coins.get(c).getAmount()){
                credit -= coins.get(c).getValue()*req;
                change.add(new Coin(coins.get(c).getValue(), req));
                coins.get(c).setAmount(coins.get(c).getAmount()-req);
            } else {
                credit -= coins.get(c).getValue()*coins.get(c).getAmount();
                change.add(new Coin(coins.get(c).getValue(), coins.get(c).getAmount()));
                coins.get(c).setAmount((long)0);
            }
            updateCoin(c);
            c++;
        }
        labelList.get(5).setText(getAmountsString(change));
        change.clear();
        getFromDB();
        updateCredit();
    }

    /**
     * Method used to make a purchase in the machine, updating credit available if successful or providing an error
     * if user doesnt have enough credit available.
     */
    public void makePurchase(){
        int index = 0;
        String result;
        for (int i = 0; i < cbProduct.size(); i++){
            if (cbProduct.get(i).isSelected()){
                index = i;
                break;
            }
        }
        if (credit >= prods.get(index).getCost()){
            credit -= prods.get(index).getCost();
            prods.get(index).setAmount(prods.get(index).getAmount()-1);
            result = "You have purchased " + prods.get(index).getName() + " for £" + prods.get(index).getCostAsString();
            updateCredit();
        } else {
            result = "You lack the necessary credit to make this purchase";
        }
        labelList.get(1).setText(result);
    }

    /**
     * Method used to initialise Radio Button group for inserting coins
     */
    public void initCoins(){
        for (int i=0; i < 6; i++){
            cbCoin.add(new JRadioButton(coinSelect[i], false));
            cbCoin.get(i).setBounds(50, 100+i*25, 50,25);
            cbCoin.get(i).setVisible(true);
            cbCoins.add(cbCoin.get(i));
            f.add(cbCoin.get(i));
        }
        cbCoin.get(0).setSelected(true);
    }

    /**
     * Method used to retrieve information from the database by using GET request from API
     * @param url denotes the location of GET request
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONArray jb = new JSONArray(jsonText);
            return jb;
        } finally {
            is.close();
        }
    }

    /**
     * Method used to read the result of the method above into a JSON format
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read())!= -1){
            sb.append((char)cp);
        }
        return sb.toString();
    }

    /**
     * Method used to update the coin amount in database using PUT request in API
     * @param i coin identifier
     * @throws IOException
     */
    public void updateCoin(int i) throws IOException {
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://localhost:8080/coins/" + (i+1) + "?amount=" + coins.get(i).getAmount())
                .method("PUT", body)
                .build();
        client.newCall(request).execute();
    }

    /**
     * Method used to update coin amounts in the Machine by retrieving it using the API GET request
     * @throws IOException
     * @throws JSONException
     */
    public void getFromDB()throws IOException, JSONException{
        coins.clear();
        JSONArray js;
        Long joAmount;
        Long joVal;
        js = readJsonFromUrl(url);
        for (int i = 0; i < js.length(); i++){
            JSONObject jo = js.getJSONObject(i);
            joAmount = Long.parseLong(jo.get("amount").toString());
            joVal = Long.parseLong(jo.get("value").toString());
            coins.add(new Coin(joVal,joAmount));
        }
    }

}
