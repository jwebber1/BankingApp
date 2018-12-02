import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Make sure this and any other Account classes and the Person class (and their getters/setters) all stay public.
public class CD extends Account {

    int cdNumber;
    private double currentInterestRate;
    private Date dateCDDue;
    static ArrayList<CD> cds = new ArrayList<>();

    //Get the current date to check if the CD is due
    private Date dateNow = new Date();

    CD(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn, int cdNumber) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "CD");
        mainAccountType = "CD";
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateCDDue = dateCDDueIn;
        this.cdNumber = cdNumber;
    }

    //Create import, export, and search by cusID
    public double getCurrentInterestRate() { return currentInterestRate; }
    public void setCurrentInterestRate(double currentInterestRate) { this.currentInterestRate = currentInterestRate; }
    public Date getDateCDDue() { return dateCDDue; }
    public void setDateCDDue(Date dateCDDue) { this.dateCDDue = dateCDDue; }
    public int getCdNumber() { return cdNumber; }
    public void setCdNumber(int cdNumber) { this.cdNumber = cdNumber; }

    static void importFile() throws IOException, ParseException {
        //creates a file referencing the text file in the memory folder
        File cdFileIn = new File("memory/cds.txt");

        //creates a bufferedreader to read from a file
        BufferedReader cdBR = new BufferedReader(new InputStreamReader(new FileInputStream(cdFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = cdBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if (lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");
                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                double currentInterest = Double.parseDouble(splitLine[2]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[3]);
                Date dateCdDue = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                int cdNum = Integer.parseInt(splitLine[5]);
                //add the new data to the ArrayList
                cds.add(new CD(cusID, balance, currentInterest, dateAccountOpened, dateCdDue, cdNum));

                //Debugging
                //System.out.println("count: " + (lineNum) + "\t" + cds.get(lineNum - 1).toString());
            }
            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        cdBR.close();


    }//end of importFile

     static void exportFile() throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter cdWriter = new PrintWriter(new FileOutputStream("memory/cds.txt", false));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        //printing the headers of the files
        cdWriter.println("CustomerID,AccountBalance,CurrInterestRate,DateAccOpened,DateCDDue,CDNumber");
        for (CD cd : cds) {

            //format the line to put back into the file
            cdWriter.println(cd.getCustomerID() + "," +
                    cd.getAccountBalance() + "," +
                    cd.getCurrentInterestRate() + "," +
                    formatter.format(cd.getDateAccountOpened()) + "," +
                    formatter.format(cd.getDateCDDue()) + "," +
                    cd.getCdNumber() + ",");
            cdWriter.flush();
        }
        //close the PrintWriter objects
        cdWriter.flush();
        cdWriter.close();
    }//end of exportFile()

    static ArrayList<CD> search(int custID) {
        //search by cusID, shows all CD
        ArrayList<CD> searchResults = new ArrayList<>();
        for (CD cd : cds) {
            if (cd.getCustomerID() == custID) {
                searchResults.add(cd);
            }
        }
        //return found checking accounts OR null
        return searchResults;
    }

    //Overload the seach method to allow a seach that includes the cdID as well
    public static CD search(int custID, int cdID) {
        CD searchResult = null;
        for (CD cd : cds) {
            if (cd.getCustomerID() == custID && cd.getCdNumber() == cdID) {
                searchResult = cd;
                return searchResult;//Exit the search when it finds the account
            }
        }

        //return found checking accounts OR null
        return searchResult;
    }

    double withdraw() {
        double amtToReturn;//Charge if withdrawn before due date

        if (dateCDDue.compareTo(dateNow) < 0) {
            //This gives the the original deposit in addition to the interest because it is after the Due Date
            amtToReturn = getAccountBalance() + (getCurrentInterestRate() * getAccountBalance() / 10);
        } else {
            //This only gives the original balance as because it is before the due date
            amtToReturn = getAccountBalance();
        }
        return amtToReturn;
    }//end of CD withdraw
}//end of CD
