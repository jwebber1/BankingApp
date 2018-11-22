import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Check{
    int customerID;
    int checkID;
    double checkAmt;
    String payTo;
    Date dateCheck;
    String memo;
    Date dateHonored;
    static ArrayList<Check> checks = new ArrayList<>();

    //constructor for the Check class (without date honored)
    public Check(int cusIdIn, int checkIdIn, double checkAmtIn, String payToIn, Date dateCheckIn, String memoIn){
        this.customerID = cusIdIn;
        this.checkID = checkIdIn;
        this.checkAmt = checkAmtIn;
        this.payTo = payToIn;
        this.dateCheck = dateCheckIn;
        this.memo = memoIn;
        this.dateHonored = null;

    }

    //constructor for the Check class (with date honored)
    public Check(int cusIdIn, int checkIdIn, double checkAmtIn, String payToIn, Date dateCheckIn, String memoIn, Date dateHonored){
        this.customerID = cusIdIn;
        this.checkID = checkIdIn;
        this.checkAmt = checkAmtIn;
        this.payTo = payToIn;
        this.dateCheck = dateCheckIn;
        this.memo = memoIn;
        this.dateHonored = dateHonored;

    }

    //getters and setters
    public int getCustomerID() {return customerID;}
    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public int getCheckID() {return checkID;}
    public void setCheckID(int checkID) {this.checkID = checkID;}
    public double getCheckAmt() {return checkAmt;}
    public void setCheckAmt(double checkAmt) {this.checkAmt = checkAmt;}
    public String getPayTo() {return payTo;}
    public void setPayTo(String payTo) {this.payTo = payTo;}
    public Date getDateCheck() {return dateCheck;}
    public void setDateCheck(Date dateCheck) {this.dateCheck = dateCheck;}
    public String getMemo() {return memo;}
    public void setMemo(String memo) {this.memo = memo;}
    public Date getDateHonored() {return dateHonored;}
    public void setDateHonored(Date dateHonored) {this.dateHonored = dateHonored;}

    //current method to grab data from the checks textfile in "memory"
    public static void importFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checksFileIn = new File("memory/checks.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checksBR = null;
        checksBR = new BufferedReader(new InputStreamReader(new FileInputStream(checksFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = checksBR.readLine()) != null) {
            System.out.println(line);
            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",",-1);

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                int checkID = Integer.parseInt(splitLine[1]);
                double checkAmt = Double.parseDouble(splitLine[2]);
                String payTo = splitLine[3];
                Date dateCheck = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                String memo = splitLine[5];

                //create a new check depending on the 6th position of the line
                if(splitLine[6].equals("")) {
                    checks.add(new Check(cusID, checkID, checkAmt, payTo, dateCheck, memo));
                }
                else {
                    Date dateHonored = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[6]);
                    checks.add(new Check(cusID, checkID, checkAmt, payTo, dateCheck, memo, dateHonored));
                }

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + checks.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        checksBR.close();
    }//end of checks data import method

    //export the checks to checks.txt
    public static void exportFile() throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter checkWriter = new PrintWriter(new FileOutputStream("memory/checks.txt",false));

        //printing the headers of the files
        checkWriter.println("CustomerID,CheckID,CheckAmt,PayTo,DateCheck,Memo,dateHonored");

        //go through all the checks
        for (Check check: checks) {
            checkWriter.println(check.getCustomerID() + "," +
                    check.getCheckID() + "," +
                    check.getCheckAmt() + "," +
                    check.getPayTo() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + "," +
                    check.getMemo() + "," +
                    (check.getDateHonored() == null ? "": new SimpleDateFormat("MM/dd/yyy").format(check.getDateHonored()))
                     + ",");
            checkWriter.flush();
        }

        //close the PrintWriter object
        checkWriter.flush();
        checkWriter.close();

    }//end of exportFile()

    //find all checks by made to one person
    public static ArrayList<Check> searchChecksByCustomerID(int custID){

        //initialize searchResults to null
        ArrayList<Check> searchResults = new ArrayList<>();

        //loop through all checks in global arraylist
        for(Check check: Check.checks){
            if(check.getCustomerID() == custID){
                searchResults.add(check);
            }
        }

        //return found checks
        return searchResults;
    }

    //find one check with a given check ID
    public static Check searchChecksByCheckID(int checkID){

        //initialize searchResults to null
        Check searchResult = null;

        //loop through all checks in global arraylist
        for(Check check: Check.checks){
            if(check.getCheckID() == checkID){
                searchResult = check;
            }
        }

        //this could return null
        return searchResult;
    }

    //method to remove the check from the arraylist
    public static void stopCheck(int checkID){

        //get the check. If it doesn't exist, it will return null
        Check foundCheck = searchChecksByCheckID(checkID);

        //if the check was found AND it has not been honored yet...
        if(foundCheck != null && foundCheck.getDateHonored() == null){
            int count = 0;

            //go through each check...
            for(Check check: Check.checks){

                //if it finds the check, break out
                if(foundCheck.equals(check)){
                    break;
                }
                count++;
            }

            //remove the check at the found location
            checks.remove(count);
        }
    }//end of stopCheck

    //method to honor a check
    public void honorCheck(){

        //get the checking account of this customer
        CheckingAccount customerChecking = CheckingAccount.search(customerID);

        if(customerChecking != null) {

            //withdraw the money from the checking account
            customerChecking.withdraw(checkAmt);

            //set the honoredDate
            setDateHonored(new Date());
        }
    }//end of honorCheck

}//end of Check
