import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    //returns all credit card purchases that match the given ssn
    public static ArrayList<CreditCardPurchase> search(int ssn){
        ArrayList<CreditCardPurchase> ccPurchases = new ArrayList<>();
        for (int i = 0; i < purchases.size(); i++){
            //if it is a match
            if (purchases.get(i).getSSN() == ssn){
                //add the match to ccPurchases
                ccPurchases.add(purchases.get(i));
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
                Date dateOfPurchase = (new SimpleDateFormat("mm/dd/yyyy")).parse(field[3]);

                //create new CreditCardPurchase object
                CreditCardPurchase purchase = new CreditCardPurchase(ssn, desc, price, dateOfPurchase);

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
        ccWriter.println("SSN,description,price,dateOfPurchase,");

        //go through all the checking accounts
        for (CreditCardPurchase purchase : purchases) {
            ccWriter.println(
                    purchase.getSSN() + "," +
                            purchase.getDescription() + "," +
                            purchase.getPrice() + "," +
                            purchase.getDateOfPurchase() + ","
            );
            ccWriter.flush();
        }
        //close the PrintWriter objects
        ccWriter.flush();
        ccWriter.close();
    }//end of exportFile

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
