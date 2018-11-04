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
    boolean isHonored;

    //constructor for the Check class
    public Check(int cusIdIn, int checkIdIn, double checkAmtIn, String payToIn, Date dateCheckIn, String memoIn, Byte isHonored){
        this.customerID = cusIdIn;
        this.checkID = checkIdIn;
        this.checkAmt = checkAmtIn;
        this.payTo = payToIn;
        this.dateCheck = dateCheckIn;
        this.memo = memoIn;
        this.isHonored = (isHonored == 1) ? true : false ;

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
    public boolean isHonored() {return isHonored;}
    public void setHonored(boolean honored) {isHonored = honored;}

    @Override
    public String toString() {
        return "Check{" +
                "customerID=" + customerID +
                ", checkID=" + checkID +
                ", checkAmt=" + checkAmt +
                ", payTo='" + payTo + '\'' +
                ", dateCheck=" + dateCheck +
                ", memo='" + memo + '\'' +
                '}';
    }

    //current method to grab data from the checks textfile in "memory"
    public static ArrayList<Check> importFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File checksFileIn = new File("memory/checks.txt");

        //creates a bufferedreader to read from a file
        BufferedReader checksBR = null;
        checksBR = new BufferedReader(new InputStreamReader(new FileInputStream(checksFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Check> importChecks = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = checksBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int cusID = Integer.parseInt(splitLine[0]);
                int checkID = Integer.parseInt(splitLine[1]);
                double checkAmt = Double.parseDouble(splitLine[2]);
                String payTo = splitLine[3];
                Date dateCheck = new SimpleDateFormat("MM/dd/yyyy").parse(splitLine[4]);
                String memo = splitLine[5];
                Byte isHonored = Byte.parseByte(splitLine[6]);

                //add the new data (in our case checking) to the ArrayList
                importChecks.add(new Check(cusID, checkID, checkAmt, payTo, dateCheck, memo, isHonored));

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + importChecks.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        checksBR.close();
        return importChecks;
    }//end of checks data import method

    //export the checks to checks.txt
    public static void exportFile(ArrayList<Check> checks) throws FileNotFoundException {
        //create a new PrintWriter to write to a file
        PrintWriter checkWriter = new PrintWriter(new FileOutputStream("memory/checks.txt",false));

        //printing the headers of the files
        checkWriter.println("CustomerID,CheckID,CheckAmt,PayTo,DateCheck,Memo,");

        //go through all the checks
        for (Check check: checks) {
            checkWriter.println(check.getCustomerID() + "," +
                    check.getCheckID() + "," +
                    check.getCheckAmt() + "," +
                    check.getPayTo() + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + "," +
                    new SimpleDateFormat("MM/dd/yyy").format(check.getDateCheck()) + "," +
                    (check.isHonored() ? 1 : 0) + ",");
            checkWriter.flush();
        }

        //close the PrintWriter object
        checkWriter.flush();
        checkWriter.close();

    }//end of exportFile()

    //find all checks by made to one person
    public static ArrayList<Check> searchChecksByCustomerID(int custID){

        //initialize searchResults to null
        ArrayList<Check> searchResults = null;

        //loop through all checks in global arraylist
        for(Check check: Main.checks){
            if(check.getCustomerID() == custID){
                searchResults.add(check);
            }
        }

        //return found checks OR null
        return searchResults;
    }

    //find one check with a given check ID
    public static int searchChecksByCheckID(int checkID){

        //initialize searchResults to null
        Check searchResult = null;

        //loop through all checks in global arraylist
        for(Check check: Main.checks){
            if(check.getCheckID() == checkID){

                //if the check hasn't been honored yet, stop it
                if(!check.isHonored()){
                    check.setCheckAmt(0.0);
                    return 0;
                }
                else{
                    return -6;
                }
            }
        }

        //-7 = check with given checkID not found
        //-6 = check already honored
        // 0 = successful stop check
        return -7;
    }


    public static void debugImport(){
        System.out.println("Debugging Check import process");

        int count = 1;
        for(Check check: Main.checks){
            System.out.println("Check " + count + ":\n" +
                    "customerID=" + check.getCustomerID() +  "\n" +
                    "checkID=" + check.getCheckID() + "\n" +
                    "checkAmt=" + check.getCheckAmt() + "\n" +
                    "payTo='" + check.getPayTo() + '\'' + "\n" +
                    "dateCheck=" + check.getDateCheck() + "\n" +
                    "memo='" + check.getMemo() + '\'' + "\n" +
                    "isHonored=" + check.isHonored());

            count++;
        }
    }



}//end of Check
