import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class CD extends Account{

    protected double currentInterestRate;
    protected Date dateCDDue;

   public  CD(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "CD");
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateCDDue = dateCDDueIn;
    }

    public double getCurrentInterestRate() {
        return currentInterestRate;
    }

    public void setCurrentInterestRate(double currentInterestRate) {
        this.currentInterestRate = currentInterestRate;
    }

    public Date getDateCDDue() {
        return dateCDDue;
    }

    public void setDateCDDue(Date dateCDDue) {
        this.dateCDDue = dateCDDue;
    }


    public static ArrayList<CD> importFile() throws IOException, ParseException {
        //creates a file referencing the text file in the memory folder
        File cdFileIn = new File("memory/cds.txt");

        //creates a bufferedreader to read from a file
        BufferedReader cdBR = null;
        cdBR = new BufferedReader(new InputStreamReader(new FileInputStream(cdFileIn)));

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
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                double balance = Double.parseDouble(splitLine[1]);
                double currentInterest = Double.parseDouble(splitLine[2]);
                Date dateAccountOpened = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[3]);
                Date dateCdDue = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);

                //add the new data to the ArrayList
                importCD.add(new CD(cusID, balance, currentInterest,dateAccountOpened,dateCdDue));

                //Debugging
                //System.out.println("count: " + (lineNum) + "\t" + importCD.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        cdBR.close();
        return importCD;

    }//end of importFile

}//end of CD
