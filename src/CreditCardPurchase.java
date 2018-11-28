import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreditCardPurchase {
    private final static SimpleDateFormat ccDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    private final static DecimalFormat ccDecimalFormatter = new DecimalFormat("0.00");
    private int SSN;
    private String description;
    private double price;
    private Date dateOfPurchase;
    private boolean paid;
    static ArrayList<CreditCardPurchase> purchases = new ArrayList<>();

    //create a new CreditCardPurchase object instantiated with the given variables
    public CreditCardPurchase(int ssn, String desc, double cost, Date date, boolean isPaid){
        SSN = ssn;
        description = desc;
        price = Double.valueOf(ccDecimalFormatter.format(cost));
        dateOfPurchase = date;
        paid = isPaid;
    }//end of CreditCardPurchase Constructor

    //returns all credit card purchases that match the given ssn
    public static ArrayList<CreditCardPurchase> search(int ssn){
        ArrayList<CreditCardPurchase> ccPurchases = new ArrayList<>();
        for (CreditCardPurchase purchase : purchases){
            //if it is a match
            if (purchase.getSSN() == ssn){
                //add the match to ccPurchases
                ccPurchases.add(purchase);
            }
        }
        return ccPurchases;
    }//end of search

    //imports the ccpurchases.txt file to the arraylist purchases
    public static void importFile() throws IOException, ParseException {
        //open a buffered reader
        BufferedReader br = new BufferedReader(new FileReader("memory/ccpurchases.txt"));
        String line;
        int lineNumber = 0;
        while((line = br.readLine()) != null){
            //if it is not the first line
            if (lineNumber != 0) {
                //split the line into its parts separated by a comma
                String[] field = line.split(",");

                //parse those parts
                int ssn = Integer.parseInt(field[0]);
                String desc = field[1];
                double price = Double.parseDouble(field[2]);
                Date date = ccDateFormatter.parse(field[3]);
                boolean isPaid = Boolean.parseBoolean(field[4]);

                //create new CreditCardPurchase object
                CreditCardPurchase purchase = new CreditCardPurchase(ssn, desc, price, date, isPaid);

                //add purchase to the arraylist
                purchases.add(purchase);
            }//end of is it not the first line
            //increment line number
            lineNumber++;
        }//end of while loop
        //close the buffered reader
        br.close();
    }//end of importFile

    //exports the purchases arraylist to the file ccpurchases.txt
    public static void exportFile() throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter ccWriter = new PrintWriter(new FileOutputStream("memory/ccpurchases.txt"));

        //printing the headers of the files
        ccWriter.println("SSN,Description,Price,DateOfPurchase,IsPaid");

        //go through all the checking accounts
        for (CreditCardPurchase purchase : purchases) {
            ccWriter.println(
                    purchase.getSSN() + "," +
                            purchase.getDescription() + "," +
                            purchase.getPrice() + "," +
                            ccDateFormatter.format(purchase.getDateOfPurchase()) + "," +
                            purchase.paid + ","
            );
            ccWriter.flush();
        }
        //close the PrintWriter objects
        ccWriter.flush();
        ccWriter.close();
    }//end of exportFile

    //Getters and Setters for CreditCardPurchase Class
    public boolean isPaid() {
        return paid;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
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
