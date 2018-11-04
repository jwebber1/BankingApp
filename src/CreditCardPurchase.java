import java.util.ArrayList;
import java.util.Date;

public class CreditCardPurchase {
    private int SSN;
    private String description;
    private double price;
    private Date dateOfPurchase;
    static ArrayList<CreditCardPurchase> purchases = new ArrayList<>();

    public CreditCardPurchase(int ssn, String desc, double cost, Date date){
        SSN = ssn;
        description = desc;
        price = cost;
        dateOfPurchase = date;
    }//end of CreditCardPurchase Constructor

    public int getSSN() {
        return SSN;
    }
    public void setSSN(int SSN) {
        this.SSN = SSN;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }
    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }
}//end of CreditCardPurchase Class
