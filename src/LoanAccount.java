import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/*   For CC's initialAmount is the credit card limit,
 *   accountBalance is the sum of purchases including interest,
 *   currentPaymentDue is the sum of purchases for current payment cycle.
 *   For ST and LT loans the accountBalance is the current principal left to pay,
 *   the initialAmount is the original loan amount, currentPaymentDue is the calculated monthly payment.
 *   */

public class LoanAccount extends Account{
    private final static DecimalFormat loanDecimalFormatter = new DecimalFormat("0.00");
    private final static SimpleDateFormat loanDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    private double initialAmount;
    private double interestRate;
    private Date datePaymentDue;
    private double currentPaymentDue;
    private double interestDue;
    private double principalDue;
    private Date lastPaymentDate;
    private double paymentsMade;
    private boolean missedPaymentFlag;
    static ArrayList<LoanAccount> loans = new ArrayList<>();

    //Constructor used for import method
    private LoanAccount(int cusID, double balance, double payments, double initial, double currentPayDue,
                        double monthlyInterestDue, double monthlyPrincipalDue, double currIntRate,
                        Date dateOpened, Date datePayDue, Date dateLastPayMade, boolean missedPayFlag, String loanType){
        super(cusID, Double.valueOf(loanDecimalFormatter.format(balance)), dateOpened, loanType);
        paymentsMade = payments;
        initialAmount = Double.valueOf(loanDecimalFormatter.format(initial));
        interestRate = currIntRate;
        mainAccountType = "Loan - " + accountType;
        currentPaymentDue = currentPayDue;
        interestDue = monthlyInterestDue;
        principalDue = monthlyPrincipalDue;
        datePaymentDue = datePayDue;
        lastPaymentDate = dateLastPayMade;
        missedPaymentFlag = missedPayFlag;
    }//end of Constructor for import method

    //Constructor used only for editing an existing account
    public LoanAccount(int cusID, double balance, double payments, double initial, double currentPayDue,
                       double currIntRate, Date dateOpened, Date datePayDue,
                       Date dateLastPayMade, boolean missedPayFlag, String loanType) {
        super(cusID, Double.valueOf(loanDecimalFormatter.format(balance)), dateOpened, loanType);
        initialAmount = initial;
        currentPaymentDue = currentPayDue;
        paymentsMade = payments;
        interestRate = changeInterestRate(currIntRate);
        datePaymentDue = datePayDue;
        lastPaymentDate = dateLastPayMade;
        missedPaymentFlag = missedPayFlag;
    }//end of Constructor for editing an account

    //smallest possible loan account constructor only for the initial creation of an account
    public LoanAccount(int cusID, double initialLoan, double rate, String type){
        super(cusID, Double.valueOf(loanDecimalFormatter.format(initialLoan)), new Date(), type);
        initialAmount = Double.valueOf(loanDecimalFormatter.format(initialLoan));
        interestRate = rate;
        currentPaymentDue = calcCurrentPayment();
        paymentsMade = 0.0;
        interestDue = calcInterest();
        principalDue = calcPrincipal();
        datePaymentDue = calcFirstPaymentDate(new Date());
        lastPaymentDate = new Date();
        missedPaymentFlag = false;
        if (type.equalsIgnoreCase("credit card")){
            super.setAccountBalance(calcBalance());
        }
    }//end of smallest possible loan account constructor

    //TODO: test with credit cards
    //changes the interest rate for a specific account and updates related fields
    private double changeInterestRate(double newRate){
        //if the account is a credit card
        if (getAccountType().equalsIgnoreCase("Credit Card")){
            //set the new interest rate
            setInterestRate(newRate);
            //calculate and set the new balance, principal, and interest
            setPrincipalDue(getCurrentPaymentDue());
            setInterestDue(getPrincipalDue() * getInterestRate());
            setAccountBalance(getPrincipalDue() + getInterestDue());
        }
        //else it is a short or long term loan
        else {
            //set interest rate to new interest rate
            setInterestRate(newRate);
            //calculate and set the new monthly payment due
            setCurrentPaymentDue(calcCurrentPayment());
            //calculate and set the new interestDue and principalDue
            setInterestDue(calcInterest());
            setPrincipalDue(calcPrincipal());
        }
        return newRate;
    }//end of changeInterestRate

    //TODO: Use this when to show how much is needed to pay off a short or long term loan
    //calculates the amount to pay off the loan right now
    double calcPayOff(){
        double calculation = calcLength() - getPaymentsMade() * getCurrentPaymentDue();
        return Double.parseDouble(loanDecimalFormatter.format(calculation));
    }//end of calcPayOff

    //calculates the monthly initialAmount for an account
    private double calcPrincipal(){
        return Double.parseDouble(loanDecimalFormatter.format(getCurrentPaymentDue() - getInterestDue()));
    }//end of calcPrincipal

    //calculates the monthly interest for an account
    private double calcInterest(){
        return Double.parseDouble(loanDecimalFormatter.format(getAccountBalance() * (getInterestRate() / 12)));
    }//end of calcInterest

    //calculates the balance of an account including interest
    private double calcBalance(){
        double amount;
        //if it is a credit card
        if (getAccountType().equalsIgnoreCase("Credit Card")){
            setPrincipalDue(getCurrentPaymentDue());
            setInterestDue(Double.valueOf(loanDecimalFormatter.format(getPrincipalDue() * getInterestRate())));
            amount = getPrincipalDue() + getInterestDue();
        }
        //else it is a short or long term loan
        else {
            amount = getInitialAmount();
        }
        return Double.parseDouble(loanDecimalFormatter.format(amount));
    }//end of calcBalance

    //returns the length of the loan type
    private int calcLength(){
        int payments = 0;
        switch (getAccountType().toLowerCase()){
            case "short term":
                payments = 60;
                break;
            case "long term":
                payments = 180;
                break;
        }//end of switch
        return payments;
    }//end of calcLength

    //TODO: implement into the GUI and test
    //creates a CreditCardPurchase tied to a specific account's ssn
    void makeCCPurchase(double cost, String desc, Date date){
        //if the purchase would not put account over the limit
        if (!ccPurchaseIsTooMuch(cost)){
            //create and add the new purchase to the purchases arrayList
            CreditCardPurchase purchase = new CreditCardPurchase(getCustomerID(), desc, cost, date);
            CreditCardPurchase.purchases.add(purchase);
            //calculate and set current payment, principal, and interest due
            setCurrentPaymentDue(getCurrentPaymentDue() + cost);
            setPrincipalDue(getCurrentPaymentDue());
            setInterestDue(Double.valueOf(loanDecimalFormatter.format(getPrincipalDue() * getInterestRate())));
            //calculate and set new account balance
            setAccountBalance(getPrincipalDue() + getInterestDue());
        }
    }//end of makeCCPurchase

    //returns true if new CC purchase would put account over limit and false if it would not
    boolean ccPurchaseIsTooMuch(double newAmount){
        return (getCurrentPaymentDue() + newAmount) > getInitialAmount();
    }//end of isCCPurchaseTooMuch

    //checks to see if payment is late
    private boolean paymentIsLate(Date date){
        return date.after(getDatePaymentDue());
    }//end of checkIfLate

    //TODO: test credit card payments
    //Makes a payment on a selected loan account
    void makePayment(double amount){
        Date now = new Date();
        double fee = 0.0;
        //if the payment is late set the fee
        if (paymentIsLate(now)){
            fee = 75.00;
        }
        //if the account is a credit card
        if (getAccountType().equalsIgnoreCase("credit card")){
            //set missed payment flag
            setMissedPaymentFlag(paymentIsLate(now));
            //if the payment is equal to the current amount due
            if (amount == getAccountBalance()){
                //clear the balance and payment due
                setCurrentPaymentDue(fee);
            }
            //if the payment is less than the current amount due
            if (amount < getAccountBalance()){
                //charge fee for late payment/too little payed
                fee = 75.00;
                //calculate new account balance
                setCurrentPaymentDue(getAccountBalance() - amount + fee);
            }
            //update account balance, principal, and interest due
            setPrincipalDue(getCurrentPaymentDue());
            setInterestDue(Double.valueOf(loanDecimalFormatter.format(getPrincipalDue() * getInterestRate())));
            setAccountBalance(getPrincipalDue() + getInterestDue());
            //increment date payment due by one month
            setDatePaymentDue(incrementDate(getDatePaymentDue(), 1));
        }
        //else the account is a short or long term loan
        else {
            //set the missed payment flag based on whether the payment is late
            setMissedPaymentFlag(paymentIsLate(now));
            //calculate and set the payments made by the ratio of the amount payed to the current monthly payment
            double paymentsMade = amount / getCurrentPaymentDue();
            setPaymentsMade(Double.parseDouble(loanDecimalFormatter.format(getPaymentsMade() + paymentsMade)));
            //if the the payment is less than the interest due
            if (amount < getInterestDue()){
                setMissedPaymentFlag(true);
            }
            //if the payments made is less than 1 then force the advancement of the date payment is due
            if (paymentsMade < 1){
                paymentsMade = 1;
            }
            //calculate and set the new date payment is required
            setDatePaymentDue(incrementDate(getDatePaymentDue(), (int)Math.round(paymentsMade)));
            //calculate the delta for amount payed and interest due
            double delta = amount - getInterestDue();
            //calculate and set the new balance including the delta and any fees
            setAccountBalance(Double.parseDouble(loanDecimalFormatter.format(getAccountBalance() - delta + fee)));
            //calculate and set the new interestDue and principalDue
            setInterestDue(calcInterest());
            setPrincipalDue(calcPrincipal());
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

    //TODO: deprecate this method, and use a combination of calcPayOff and a message
    //completely pays off loan account balance
    void payOffLoan() {
        return;
    }//end of payOffLoan

    //calculate the current payment due based on account type
    private double calcCurrentPayment(){
        double amount = 0.0;
        //if the account is a credit card
        if (this.getAccountType().equalsIgnoreCase("credit card")){
            //for all credit card purchases sum the purchases where the account matches ssn and the date of purchase
            for (CreditCardPurchase ccPurchase : CreditCardPurchase.purchases) {
                if (ccPurchase.getSSN() == this.getCustomerID()) {
                    amount += ccPurchase.getPrice();
                }
            }
        }
        //else the account is a short or long term loan
        else {
            //calculate monthly interest rate
            double monthlyRate = getInterestRate() / 12;
            //calculate the two parts of the equation
            double part1 = monthlyRate * Math.pow((1 + monthlyRate), calcLength());
            double part2 = Math.pow((1 + monthlyRate), calcLength()) - 1;
            //combine the two parts and set amount to calculated monthly payment
            amount = getInitialAmount() * (part1 / part2);
        }
        return Double.parseDouble(loanDecimalFormatter.format(amount));
    }//end of calcCurrentPayment

    //returns all loan accounts that match the given ssn and account type
    static LoanAccount search(int ssn, String type){
        for (LoanAccount loan : loans) {
            //if it is a match
            if (loan.getCustomerID() == ssn && loan.getAccountType().equalsIgnoreCase(type)) {
                return loan;
            }
        }
        return null;
    }//end of search

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
                double payments = Double.parseDouble(field[2]);
                double initialAmount = Double.parseDouble(field[3]);
                double currentPaymentDue = Double.parseDouble(field[4]);
                double interestDue = Double.parseDouble(field[5]);
                double principalDue = Double.parseDouble(field[6]);
                double interest = Double.parseDouble(field[7]);
                Date dateOpened = loanDateFormatter.parse(field[8]);
                Date dateDue = loanDateFormatter.parse(field[9]);
                Date dateOfLastPayment = loanDateFormatter.parse(field[10]);
                boolean missedPaymentFlag = Boolean.parseBoolean(field[11]);
                String accountType = field[12];

                //create new loan account object
                LoanAccount account = new LoanAccount(ssn, balance, payments, initialAmount, currentPaymentDue, interestDue,
                        principalDue, interest, dateOpened, dateDue, dateOfLastPayment, missedPaymentFlag,
                        accountType);

                //add account to the arraylist
                loans.add(account);
            }//end of is it not the first line
            lineNumber++; //increment line number
        }//end of while loop
        br.close(); //close the buffered reader
    }//end of importFile

    //exports the loans arraylist to the file loans.txt
    public static void exportFile() throws FileNotFoundException{
        //create a new PrintWriter to write to a file
        PrintWriter loanWriter = new PrintWriter(new FileOutputStream("memory/loans.txt"));

        //printing the headers of the files
        loanWriter.println("CustomerID,CurrentBalance,PaymentsMade,Limit/InitialAmount,CurrentPaymentDue,InterestDue,PrincipalDue," +
                "CurrentInterestRate,DateAccountOpened,DatePaymentDue,DateLastPaymentMade," +
                "MissedPaymentFlag,AccountType,");

        //go through all the checking accounts
        for (LoanAccount account : loans) {
            loanWriter.println(
                    account.getCustomerID() + "," +
                            account.getAccountBalance() + "," +
                            account.getPaymentsMade() + "," +
                            account.getInitialAmount() + "," +
                            account.getCurrentPaymentDue() + "," +
                            account.getInterestDue() + "," +
                            account.getPrincipalDue() + "," +
                            account.getInterestRate() + "," +
                            loanDateFormatter.format(account.getDateAccountOpened()) + "," +
                            loanDateFormatter.format(account.getDatePaymentDue()) + "," +
                            loanDateFormatter.format(account.getLastPaymentDate()) + "," +
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
    public double getPaymentsMade() {
        return paymentsMade;
    }
    public void setPaymentsMade(double paymentsMade) {
        this.paymentsMade = paymentsMade;
    }
    public double getInterestDue() {
        return interestDue;
    }
    public void setInterestDue(double interestDue) {
        this.interestDue = interestDue;
    }
    public double getPrincipalDue() {
        return principalDue;
    }
    public void setPrincipalDue(double principalDue) {
        this.principalDue = principalDue;
    }
    public double getInitialAmount() {
        return initialAmount;
    }
    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
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
}//end of LoanAccount