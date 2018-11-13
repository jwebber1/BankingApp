import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/*   For CC's calculateBalance is the sum of purchases including interest,
 *   accountBalance is the credit card limit, currentPaymentDue is the sum
 *   of purchases for current payment cycle.
 *   For ST and LT loans the calculatedBalance is the total amount owed including interest,
 *   currentPaymentDue is the calculated monthly payment,
 *   the accountBalance is the initial loan amount.
 *   */

//TODO: have gui use new methods where appropriate
//TODO: change loan types in the gui from short hand to full title
//TODO: add the functionality to remove credit card purchases whenever they are payed off (not of high priority)

// Make sure this and any other Account classes and the Person class (and their getters/setters) all stay public.
public class LoanAccount extends Account{
    private final static DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    private double calculatedBalance;
    private double interestRate;
    private Date datePaymentDue;
    private double currentPaymentDue;
    private double interestDue;
    private double principalDue;
    private Date lastPaymentDate;
    private int paymentsLeft;
    private boolean missedPaymentFlag;
    static ArrayList<LoanAccount> loans = new ArrayList<>();

    //TODO: Deprecated, please use constructor with least fields for creating accounts
    public LoanAccount(int cusIDIn, double calculatedBal, double balance, double currIntRate, Date datePayDue, Date notified,
                       Date lastPayDateIn, boolean missPayFlagIn, int currentPaymentsLeft, String loanTypeIn){

        super(cusIDIn, balance, notified, loanTypeIn);

        mainAccountType = "Loan - " + accountType;

        paymentsLeft = calcPaymentsLeft();
        calculatedBalance = calculatedBal;
        interestRate = currIntRate;
        datePaymentDue = calcFirstPaymentDate(datePayDue);
        currentPaymentDue = calcCurrentPayment();
        lastPaymentDate = new Date();
        missedPaymentFlag = missPayFlagIn;
    }//end of LoanAccount Constructor

    //Constructor used for import method
    private LoanAccount(int cusID, double balance, double currentCalcBal, double currentPayDue, int payLeft,
                        double currIntRate, Date dateOpened, Date datePayDue, Date dateLastPayMade,
                        boolean missedPayFlag, String loanType){
        super(cusID, balance, dateOpened, loanType);
        mainAccountType = "Loan - " + accountType;
        paymentsLeft = payLeft;
        currentPaymentDue = currentPayDue;
        calculatedBalance = currentCalcBal;
        interestRate = currIntRate;
        datePaymentDue = datePayDue;
        lastPaymentDate = dateLastPayMade;
        missedPaymentFlag = missedPayFlag;
    }//end of Constructor for import method

    //smallest possible loan account constructor
    public LoanAccount(int cusID, double initialLoan, double rate, String type){
        super(cusID, initialLoan, new Date(), type);
        mainAccountType = "Loan - " + accountType;
        paymentsLeft = calcPaymentsLeft();
        currentPaymentDue = calcCurrentPayment();
        calculatedBalance = calcBalance();
        interestRate = rate;
        datePaymentDue = calcFirstPaymentDate(new Date());
        lastPaymentDate = new Date();
        missedPaymentFlag = false;
    }//end of smallest possible loan account constructor

    //changes the interest rate for a specific account and updates related fields
    private void changeInterestRate(double newRate){
        //if the account is a credit card
        if (getAccountType().equalsIgnoreCase("Credit Card")){
            //calculate and store the delta created from the old balance
            double delta = calcBalance();
            delta -= getCalculatedBalance();
            //calculate and set the new calculated balance
            setCalculatedBalance(calcBalance() - delta);
        }
        //else it is a short or long term loan
        else {
            //set interest rate to new interest rate
            setInterestRate(newRate);
            //calculate and set the new monthly interest payment due
            interestDue = calcInterest(principalDue * getPaymentsLeft(), getPaymentsLeft());
            //calculate and set the new monthly payment due
            setCurrentPaymentDue(interestDue + principalDue);
            //calculate and set the new calculated balance
            setCalculatedBalance(calcBalance());
        }
    }//end of changeInterestRate

    //calculates the monthly principal for an account
    private double calcPrincipal(double currentBalance, int months){
        return currentBalance / months;
    }//end of calcPrincipal

    //calculates the monthly interest for an account
    private double calcInterest(double currentBalance, int months){
        return ((currentBalance / 2) * (months / 12.0) * getInterestRate()) / months;
    }//end of calcInterest

    //sets initial payments left based on account type
    private int calcPaymentsLeft(){
        int paymentsLeft;
        if (getAccountType().equalsIgnoreCase("credit card")){
            paymentsLeft = 0;
        } else if (getAccountType().equalsIgnoreCase("long term")){
            paymentsLeft = 180;
        } else {
            paymentsLeft = 60;
        }
        return paymentsLeft;
    }//end of calcPaymentsLeft

    //calculates the balance of an account including interest
    private double calcBalance(){
        double amount = 0.0;
        //if it is a credit card
        if (getAccountType().equalsIgnoreCase("Credit Card")
                || getAccountType().equalsIgnoreCase("cc")){
            double sum = 0.0;
            int count = 0;
            for(CreditCardPurchase purchase: CreditCardPurchase.purchases){
                if (getCustomerID() == purchase.getSSN()){
                    sum += purchase.getPrice();
                    count++;
                }
            }
            if (count != 0) {
                amount = sum + ((sum / count) * getInterestRate());
            }
        }
        //else it is a short or long term loan
        else {
            amount = getCurrentPaymentDue() * getPaymentsLeft();
        }
        return amount;
    }//end of calcBalance

    //creates a CreditCardPurchase tied to a specific account
    private void makeCCPurchase(double cost, String desc, Date date){
        //if the purchase would not put account over the limit
        if (!ccPurchaseIsTooMuch(cost)){
            //update current payment due
            setCurrentPaymentDue(getCurrentPaymentDue() + cost);
            //create and add the new purchase to the purchases arrayList
            CreditCardPurchase purchase = new CreditCardPurchase(getCustomerID(), desc, cost, date);
            CreditCardPurchase.purchases.add(purchase);
            //update calculated balance for new purchase
            setCalculatedBalance(calcBalance());
        }
    }//end of makeCCPurchase

    //returns true if new CC purchase would put account over limit and false if it would not
    private boolean ccPurchaseIsTooMuch(double newAmount){
        return (getCurrentPaymentDue() + newAmount) < getAccountBalance();
    }//end of isCCPurchaseTooMuch

    //checks to see if payment is late
    private boolean paymentIsLate(Date date){
        return date.after(getDatePaymentDue());
    }//end of checkIfLate

    //TODO: Deprecated, please use method "makePayment" for all loan account payments
    void makeLoanPayment(){
        Date date = new Date();
//        for (LoanAccount loan : loans) {
        //if the account matches
//            if (loan.getCustomerID() == getCustomerID() && loan.getAccountType().equalsIgnoreCase(getAccountType())) {
        if (paymentIsLate(date)) { //find out if the payment is late
            setMissedPaymentFlag(true);
            setLastPaymentDate(date);
            // TODO: I just put this in because nothing was paid otherwise if it was late - Hunter
            double tempBalance = getAccountBalance();
            double tempPaymentDue = getCurrentPaymentDue();
            setAccountBalance(tempBalance - tempPaymentDue);
        }
        else {
            //else the payment is not late
            setMissedPaymentFlag(false);
            double tempBalance = getAccountBalance();
            double tempPaymentDue = getCurrentPaymentDue();
            setAccountBalance(tempBalance - tempPaymentDue);
            setLastPaymentDate(date);
        }
        paymentsLeft--;
//            }
//        }
    }//end of makeCCPayment

    //Makes a payment on a selected loan account
    void makePayment(double amount){
        Date now = new Date();
        //if the account is a credit card
        if (getAccountType().equalsIgnoreCase("credit card")){
            //if the payment is equal to the current amount due
            if (amount == getAccountBalance()){
                setAccountBalance(0.0);
                setCurrentPaymentDue(0.0);
                setMissedPaymentFlag(paymentIsLate(now));
            }
            //if the payment is less than the current amount due
            if (amount < getAccountBalance()){
                setAccountBalance(getAccountBalance() - amount);
                setMissedPaymentFlag(paymentIsLate(now));
            }
        }
        //else the account is a short or long term loan
        else {
            setMissedPaymentFlag(paymentIsLate(now));
            double monthlyInterest = interestDue;
            double monthlyPrincipal = principalDue;
            //if the amount payed is equal to the current amount due
            if (amount == getCurrentPaymentDue()){
                //set the missed payment flag based on date of payment
                setMissedPaymentFlag(paymentIsLate(now));
                //reduce calculated balance appropriately
                double tempBalance = getCalculatedBalance();
                setCalculatedBalance(tempBalance - getCurrentPaymentDue());
                //reset currentPayment due
                setCurrentPaymentDue(monthlyInterest + monthlyPrincipal);
                //decrement payments left
                paymentsLeft--;
            }
            //else if the amount payed is less than the current amount due
            else if (amount < getCurrentPaymentDue()){
                //reduce calculated balance and current payment due to correct amount
                setCalculatedBalance(getCalculatedBalance() - amount);
                setCurrentPaymentDue(getCurrentPaymentDue() - amount);
            }
            //else the amount payed is greater than the current amount due
            else {
                //if amount is equal to the calculated balance
                if (amount == getCalculatedBalance()){
                    //set the missed payment flag based on date of payment
                    setMissedPaymentFlag(paymentIsLate(now));
                    //set calculated balance, current payment due, and payments left to 0
                    setCalculatedBalance(0.0);
                    setCurrentPaymentDue(0.0);
                    setPaymentsLeft(0);
                }
                //else it is less than calculated balance and greater than current payment due
                else {
                    //set missed payment flag based on date of payment
                    setMissedPaymentFlag(paymentIsLate(now));
                    //calculate the number of whole payments the amount payed represents
                    int payments = (int)(amount / getCurrentPaymentDue());
                    //set date payment is due based on calculated payments made
                    setDatePaymentDue(incrementDate(getDatePaymentDue(), payments));
                    //reduce calculated balance by amount payed
                    setCalculatedBalance(getCalculatedBalance() - amount);
                    //reduce the current payment due by the amount that is left over from the amount payed
                    double excess = amount - (getCurrentPaymentDue() * payments);
                    setCurrentPaymentDue(getCurrentPaymentDue() - excess);
                    //decrement payments left by payments made
                    setPaymentsLeft(getPaymentsLeft() - payments);
                }//end of else amount is less than calculated balance and greater than current payment due
            }//end of else the amount payed is greater than the current amount due
        }//end of else the account is a short or long term loan
    }//end of makePayment

    //increments date by given number of months
    private Date incrementDate(Date date, int months){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        date = c.getTime();
        return date;
    }//end of incrementDate

    //returns the date of the next instance of the first of a month
    private Date calcFirstPaymentDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //if there is at least half a month in between the current day and the first of the next month
        if (Calendar.DAY_OF_MONTH < 14) {
            //set the day payment is due to the first of the the next month
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, 1);
            date = c.getTime();
        }
        //else there is not at least half a month
        else {
            //set the day payment is due to the first of the month after next
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, 2);
            date = c.getTime();
        }
        return date;
    }//end of calcFirstPaymentDate

    //completely pays off loan account balance
    void payOffLoan() {
        Date date = new Date();
        for (LoanAccount loan : loans) {
            //if the account matches
            if (loan.getCustomerID() == getCustomerID()
                    && loan.getAccountType().equalsIgnoreCase(getAccountType())) {
                //update account info
                loan.setLastPaymentDate(date);
                loan.setCurrentPaymentDue(0.0);
                loan.setAccountBalance(0.0);
                loan.setPaymentsLeft(0);
            }//end of if the account matches
        }//end of for loop
    }//end of payOffLoan

    //calculate the current payment due based on account type
    private double calcCurrentPayment(){
        double amount;
        double sum = 0.0;
        //if the account is a credit card
        if (this.getAccountType().equalsIgnoreCase("Credit Card")){
            //for all credit card purchases sum the purchases where the account matches ssn and the date of purchase
            for (CreditCardPurchase ccPurchase : CreditCardPurchase.purchases) {
                if (ccPurchase.getSSN() == this.getCustomerID()) {
                    sum += ccPurchase.getPrice();
                }
            }
            //set amount to the formatted sum of credit card purchases
            amount = sum;
        }
        //else the account is a short or long term loan
        else {
            //stop gap till this is fleshed out
            interestDue = calcInterest(getAccountBalance(), paymentsLeft);
            principalDue = calcPrincipal(getAccountBalance(), paymentsLeft);
            amount = interestDue + principalDue;
            if (amount == Double.NaN){
                amount = 0.0;
            }
        }
        return amount;
    }//end of calcCurrentPayment

    //returns all loan accounts that match the given ssn
    static ArrayList<LoanAccount> search(int ssn){
        ArrayList<LoanAccount> accounts = new ArrayList<>();
        for (LoanAccount loan : loans) {
            //if it is a match
            if (loan.getCustomerID() == ssn) {
                accounts.add(loan);
            }
        }
        return accounts;
    }//end of search

    //imports the loans.txt file to the arraylist loans
    static void importFile() throws IOException, ParseException {
        CreditCardPurchase.importFile();
        //open a buffered reader
        BufferedReader br = new BufferedReader(new FileReader("memory/loans.txt"));
        String line;
        int lineNumber = 0;
        while((line = br.readLine()) != null){
            //if it is not the first line
            if (lineNumber != 0) {
                //split the line into its parts separated by a comma
                String[] field = line.split(",");

                //parse those parts
                int ssn = Integer.parseInt(field[0]);
                double balance = Double.parseDouble(field[1]);
                double calculatedBalance = Double.parseDouble(field[2]);
                double currentPaymentDue = Double.parseDouble(field[3]);
                int paymentsLeft = Integer.parseInt(field[4]);
                double interest = Double.parseDouble(field[5]);
                Date dateOpened = dateFormatter.parse(field[6]);
                Date dateDue = dateFormatter.parse(field[7]);
                Date dateOfLastPayment = dateFormatter.parse(field[8]);
                boolean missedPaymentFlag = Boolean.parseBoolean(field[9]);
                String accountType = field[10];

                //create new loan account object
                LoanAccount account = new LoanAccount(ssn, balance, calculatedBalance, currentPaymentDue, paymentsLeft, interest, dateDue, dateOpened,
                        dateOfLastPayment, missedPaymentFlag, accountType);

                //add account to the arraylist
                loans.add(account);
            }//end of is it not the first line
            lineNumber++; //increment line number
        }//end of while loop
        br.close(); //close the buffered reader
    }//end of importFile

    //exports the loans arraylist to the file loans.txt
    public static void exportFile() throws FileNotFoundException{
        CreditCardPurchase.exportFile();
        //create a new PrintWriter to write to a file
        PrintWriter loanWriter = new PrintWriter(new FileOutputStream("memory/loans.txt"));

        //printing the headers of the files
        loanWriter.println("CustomerID,CurrentBalance,CalculatedBalance,CurrentPaymentDue,PaymentsLeft," +
                "CurrentInterestRate,DateAccountOpened,DatePaymentDue,DateLastPaymentMade," +
                "MissedPaymentFlag,AccountType,");

        //go through all the checking accounts
        for (LoanAccount account : loans) {
            loanWriter.println(
                    account.getCustomerID() + "," +
                            account.getAccountBalance() + "," +
                            account.getCalculatedBalance() + "," +
                            account.getCurrentPaymentDue() + "," +
                            account.getPaymentsLeft() + "," +
                            account.getInterestRate() + "," +
                            account.getDateAccountOpened() + "," +
                            account.getDatePaymentDue() + "," +
                            account.getLastPaymentDate() + "," +
                            account.getMissedPaymentFlag() + "," +
                            account.getAccountType() + ","
            );

            loanWriter.flush();
        }
        //close the PrintWriter objects
        loanWriter.flush();
        loanWriter.close();
    }//end of exportFile

    //getters and setters for LoanAccount Class
    public double getCalculatedBalance() {
        return calculatedBalance;
    }
    public void setCalculatedBalance(double initialAmount) {
        this.calculatedBalance = initialAmount;
    }
    public double getInterestRate() {return interestRate;}
    public void setInterestRate(double interestRate) {this.interestRate = interestRate;}
    public Date getDatePaymentDue() {return datePaymentDue;}
    public void setDatePaymentDue(Date datePaymentDue) {this.datePaymentDue = datePaymentDue;}
    public double getCurrentPaymentDue() {return currentPaymentDue;}
    public void setCurrentPaymentDue(double currentPaymentDue) {this.currentPaymentDue = currentPaymentDue;}
    public Date getLastPaymentDate() {return lastPaymentDate;}
    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }
    public boolean getMissedPaymentFlag() {return missedPaymentFlag;}
    public void setMissedPaymentFlag(boolean missedPaymentFlag) {this.missedPaymentFlag = missedPaymentFlag;}
    public int getPaymentsLeft() {
        return paymentsLeft;
    }
    public void setPaymentsLeft(int paymentsLeft) {
        this.paymentsLeft = paymentsLeft;
    }
}//end of LoanAccount