import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CD extends Account {

    int cdNumber;
    private double currentInterestRate;
    private Date dateCDDue;
    private boolean beforeDueDate;
    static ArrayList<CD> cds;
    private String mainAccounttype;

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
    private double getCurrentInterestRate() {
        return currentInterestRate;
    }
    public void setCurrentInterestRate(double currentInterestRate) {
        this.currentInterestRate = currentInterestRate;
    }
    private Date getDateCDDue() {
        return dateCDDue;
    }
    public void setDateCDDue(Date dateCDDue) {
        this.dateCDDue = dateCDDue;
    }
    public boolean isBeforeDueDate() {
        return beforeDueDate;
    }

    private int getCdNumber() {
        return cdNumber;
    }
    public void setCdNumber(int cdNumber) {
        this.cdNumber = cdNumber;
    }

     static ArrayList<CD> importFile() throws IOException, ParseException {
        //creates a file referencing the text file in the memory folder
        File cdFileIn = new File("memory/cds.txt");

        //creates a bufferedreader to read from a file
        BufferedReader cdBR = new BufferedReader(new InputStreamReader(new FileInputStream(cdFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<CD> importCD = new ArrayList<>();

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
                importCD.add(new CD(cusID, balance, currentInterest, dateAccountOpened, dateCdDue, cdNum));

                //Debugging
                System.out.println("count: " + (lineNum) + "\t" + importCD.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        cdBR.close();
        return importCD;

    }//end of importFile

    // TODO: The account type was used in this but not the import
     void exportFile() throws FileNotFoundException {
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

    public static ArrayList<CD> search(int custID, int cdID) {
        //search by cusID, shows all CD
        //initialize searchResults to null
        ArrayList<CD> searchResults = null;
        for (CD cd : cds) {
            if (cd.getCustomerID() == custID && cd.getCdNumber() == cdID) {
                searchResults.add(cd);
            }
        }

        //return found checking accounts OR null
        return searchResults;
    }

    void withdraw() {
        double amtToReturn = 0.0;//Charge if withdrawn before due date

        if (dateCDDue.compareTo(dateNow) >= 0) {
            //This gives the the original deposit in addition to the interest because it is after the Due Date
            amtToReturn = getAccountBalance() * (getCurrentInterestRate() * getAccountBalance());
            setAccountBalance(0);
        } else
            //This only gives the original balance as because it is before the due date
            amtToReturn = getAccountBalance();
            setAccountBalance(0);


    }//end of CD withdraw

}//end of CD
