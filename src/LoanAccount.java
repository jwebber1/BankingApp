import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/*   For CC's currentPaymentDue is the actual amount due including interest
 *   for current payment cycle, current balance is the sum of purchases for
 *   current payment cycle, and initialAmount is the credit card limit.
 *   For ST and LT loans the currentPaymentDue is the calculated monthly payment,
 *   the accountBalance is the payOff amount, and the initialAmount is the
 *   original amount the loan is based on.
 *   To close a loan account the current balance must be 0.0, and to close a cc
 *   account the currentPaymentDue must be 0.0.
 *   */

//TODO:
// * possibly add late fee to make loan payment
// * change structure of constructor to account for calculated fields

class LoanAccount extends Account{
    private final static DecimalFormat decimalFormat = new DecimalFormat("#,###,###,##0.00");
    private double initialAmount;
    private double currentInterestRate;
    private Date datePaymentDue;
    private double currentPaymentDue;
    private Date datePaymentNotified;
    private Date lastPaymentDate;
    private int paymentsLeft;
    private boolean missedPaymentFlag;
    static ArrayList<LoanAccount> loans = new ArrayList<>();

    public LoanAccount(int cusIDIn, double initial, double balance, double currIntRate, Date datePayDue,
                       double currPayDue, Date datePayNotifIn, Date lastPayDateIn,
                       boolean missPayFlagIn, int currentPaymentsLeft, String loanTypeIn){

        super(cusIDIn, balance, null, loanTypeIn);

        initialAmount = initial;
        currentInterestRate = currIntRate;
        datePaymentDue = datePayDue;
        currentPaymentDue = currPayDue;
        datePaymentNotified = datePayNotifIn;
        lastPaymentDate = lastPayDateIn;
        missedPaymentFlag = missPayFlagIn;
        paymentsLeft = currentPaymentsLeft;
    }//end of LoanAccount Constructor

    //closes a loan account as long as the account balance is 0.0
    public static void close(LoanAccount account){
        for (int i = 0; i < loans.size(); i++) {
            //if the account doesn't have a balance
            if (loans.get(i).getAccountBalance() == 0.0) {
                //if account at i matches
                if (loans.get(i).getAccountType().equalsIgnoreCase(account.getAccountType())
                        && loans.get(i).getCustomerID() == account.getCustomerID()) {
                    loans.remove(i);
                }//end of if account at i matches
            }
        }
    }//end of closeLoanAccount

    //returns the current payment due for a short or long term loan
    public static double calcCurrentLoanPayment(String type, double balance, double rate){
        int years = 0;
        int months = 0;
        double amount;
        //if the loan is a long term loan
        if (type.equalsIgnoreCase("lt")){
            years = 15;
            months = 180;
        }
        //if the loan is a short term loan
        if (type.equalsIgnoreCase("st")){
            years = 5;
            months = 60;
        }
        amount = (balance / months) + (((balance / 2) * years * rate) / months);
        double amount2dec = Double.valueOf(decimalFormat.format(amount));
        return amount2dec;
    }//end of calcCurrentLoanPayment

    //creates a CreditCardPurchase tied to a specific account
    //returns boolean representing whether the action was successful
    public static void makeCCPurchase(LoanAccount account, double cost, String desc, Date date){
        //if the purchase would not put account over the limit
        if (!ccPurchaseTooMuch(account, cost)){
            double tempDouble = account.getCurrentPaymentDue();
            account.setCurrentPaymentDue(tempDouble + cost);
            CreditCardPurchase purchase = new CreditCardPurchase(account.getCustomerID(), desc, cost, date);
            CreditCardPurchase.purchases.add(purchase);
        }
    }//end of makeCCPurchase

    //returns true if new CC purchase would put account over limit and false if it would not
    public static boolean ccPurchaseTooMuch(LoanAccount loanAccount, double newAmount){
        return (loanAccount.getCurrentPaymentDue() + newAmount) < loanAccount.getAccountBalance();
    }//end of isCCPurchaseTooMuch

    //checks to see if payment is late
    public static boolean paymentIsLate(Date date, LoanAccount account){
        return date.after(account.getDatePaymentDue());
    }//end of checkIfLate

    //updates interest rates to interestRate where the loan type matches identifier
    //returns int representing whether the action was successful (0 = no, 1 = yes)
    public static void updateLoanInterest(double interestRate, String identifier){
        for (int i = 0; i < loans.size(); i++){
            //if the account type at i matches the identifier
            if (loans.get(i).getAccountType().equalsIgnoreCase(identifier)){
                loans.get(i).setCurrentInterestRate(interestRate);
            }
        }
    }//end of updateLoanInterest

    //makes a payment on a specified credit card account
    public static void makeCCPayment(int ssn, double payment){
        Date date = new Date();
        for (int i = 0; i< loans.size(); i++){
            //if the given ssn and account type match the ssn and account type at i
            if(ssn== loans.get(i).getCustomerID()){
                //if a payment can be made
                if (loans.get(i).getCurrentPaymentDue() != 0.0) {
                    double amountDue = loans.get(i).getCurrentPaymentDue();
                    //if the payment is late
                    if(paymentIsLate(date, loans.get(i))){
                        loans.get(i).setCurrentPaymentDue(amountDue - payment);
                        loans.get(i).setMissedPaymentFlag(true);
                    }
                    //else the payment is not late
                    else {
                        loans.get(i).setCurrentPaymentDue(amountDue - payment);
                        loans.get(i).setMissedPaymentFlag(false);
                    }
                }//end of if a payment can be made
            }//end of if there is a match
        }//end of for loop
    }//end of makeLoanPayment

    //makes a payment on a specified loan account
    public static void makeLoanPayment(LoanAccount account){
        Date date = new Date();
        for (int i = 0; i < loans.size(); i++){
            //if the account matches
            if (loans.get(i).getCustomerID() == account.getCustomerID()
                    && loans.get(i).getAccountType().equalsIgnoreCase(account.getAccountType())){
                //find out if the payment is late
                if (paymentIsLate(date, loans.get(i))){
                    loans.get(i).setMissedPaymentFlag(true);
                    loans.get(i).setLastPaymentDate(date);
                }
                //else the payment is not late
                else {
                    loans.get(i).setMissedPaymentFlag(false);
                    double tempBalance = loans.get(i).getAccountBalance();
                    double tempPaymentDue = loans.get(i).getCurrentPaymentDue();
                    loans.get(i).setAccountBalance(tempBalance - tempPaymentDue);
                    loans.get(i).setLastPaymentDate(date);
                }
                account.paymentsLeft--;
            }
        }
    }//end of makeCCPayment

    //completely pays off loan account balance
    public static void payOffLoan(LoanAccount account){
        Date date = new Date();
        for (int i = 0; i < loans.size(); i++){
            //if the account matches
            if (loans.get(i).getCustomerID() == account.getCustomerID()
                    && loans.get(i).getAccountType().equalsIgnoreCase(account.getAccountType())){
                //update account info
                loans.get(i).setLastPaymentDate(date);
                loans.get(i).setCurrentPaymentDue(0.0);
                loans.get(i).setAccountBalance(0.0);
                loans.get(i).setPaymentsLeft(0);
            }//end of if the account matches
        }//ens of for loop
    }//end of payOffLoan

    //calculates average monthly balance for a credit card account
    //returns 0.0 if there are no purchases
    public static double averageMonthlyBalance(LoanAccount account){
        double average = 0.0;
        int count = 0;
        ArrayList<CreditCardPurchase> ccPurchases = CreditCardPurchase.purchases;
        //for all credit card purchases sum the purchases where the account matches ssn and the date of purchase
        for (int i = 0; i < ccPurchases.size(); i++){
            if (ccPurchases.get(i).getSSN() == account.getCustomerID()){
                Date dateOfPurchase = ccPurchases.get(i).getDateOfPurchase();
                //if the date of purchase is in the current pay cycle
                if (dateOfPurchase.before(account.getDatePaymentDue())
                        && dateOfPurchase.after(account.getLastPaymentDate()))
                    average += ccPurchases.get(i).getPrice();
                count++;
            }
        }
        double amount = Double.valueOf(decimalFormat.format(average / count));
        return amount * account.getCurrentInterestRate();
    }//end of averageMonthlyBalance

    //returns all loan accounts that match the given ssn
    public static ArrayList<LoanAccount> search(int ssn){
        ArrayList<LoanAccount> accounts = new ArrayList<>();
        for (int i = 0; i < loans.size(); i++){
            //if it is a match
            if (loans.get(i).getCustomerID() == ssn){
                accounts.add(loans.get(i));
            }
        }
        return accounts;
    }//end of search

    //imports the loans.txt file to the arraylist loans
    public static void importFile() throws IOException, ParseException {
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
                double initial = Double.parseDouble(field[1]);
                double balance = Double.parseDouble(field[2]);
                double interest = Double.parseDouble(field[3]);
                Date dateDue = (new SimpleDateFormat("mm/dd/yyyy")).parse(field[4]);
                Date dateNotified = (new SimpleDateFormat("mm/dd/yyyy")).parse(field[5]);
                double currentPayment = Double.parseDouble(field[6]);
                Date dateOfLastPayment = (new SimpleDateFormat("mm/dd/yyyy")).parse(field[7]);
                boolean missedPaymentFlag = Boolean.parseBoolean(field[8]);
                int numberPaymentsLeft = Integer.parseInt(field[9]);
                String accountType = field[10];

                //create new loan account object
                LoanAccount account = new LoanAccount(ssn, initial, balance, interest, dateDue, currentPayment, dateNotified,
                        dateOfLastPayment, missedPaymentFlag, numberPaymentsLeft, accountType);

                //add account to the arraylist
                loans.add(account);
            }//end of is it not the first line
            //increment line number
            lineNumber++;
        }//end of while loop
        //close the buffered reader
        br.close();
    }//end of importFile

    //exports the loans arraylist to the file loans.txt
    public static void exportFile() throws FileNotFoundException{
        //create a new PrintWriter to write to a file
        PrintWriter loanWriter = new PrintWriter(new FileOutputStream("memory/loans.txt"));

        //printing the headers of the files
        loanWriter.println("CustomerID,InitialLoan,CurrentBalance,CurrentInterestRate,DatePaymentDue,DateNotifiedOfPayment,CurrentPaymentDue,DateSinceLastPaymentMade,MissedPaymentFlag,PaymentsLeft,AccountType,");

        //go through all the checking accounts
        for (LoanAccount account : loans) {
            loanWriter.println(
                    account.getCustomerID() + "," +
                            account.getInitialAmount() + "," +
                            account.getAccountBalance() + "," +
                            account.getCurrentInterestRate() + "," +
                            account.getDatePaymentDue() + "," +
                            account.getDatePaymentNotified() + "," +
                            account.getCurrentPaymentDue() + "," +
                            account.getLastPaymentDate() + "," +
                            account.getMissedPaymentFlag() + "," +
                            account.getPaymentsLeft() + "," +
                            account.getAccountType() + ","
            );
            loanWriter.flush();
        }
        //close the PrintWriter objects
        loanWriter.flush();
        loanWriter.close();
    }//end of exportFile

    //getters and setters for LoanAccount Class
    public double getInitialAmount() {
        return initialAmount;
    }
    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
    }
    public double getCurrentInterestRate() {return currentInterestRate;}
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public Date getDatePaymentDue() {return datePaymentDue;}
    public void setDatePaymentDue(Date datePaymentDue) {this.datePaymentDue = datePaymentDue;}
    public double getCurrentPaymentDue() {return currentPaymentDue;}
    public void setCurrentPaymentDue(double currentPaymentDue) {this.currentPaymentDue = currentPaymentDue;}
    public Date getDatePaymentNotified() { return datePaymentNotified;}
    public void setDatePaymentNotified(Date datePaymentNotified) {this.datePaymentNotified = datePaymentNotified;}
    public Date getLastPaymentDate() {return lastPaymentDate;}
    public void setLastPaymentDate(Date lastPaymentDate) {this.lastPaymentDate = lastPaymentDate;}
    public boolean getMissedPaymentFlag() {return missedPaymentFlag;}
    public void setMissedPaymentFlag(boolean missedPaymentFlag) {this.missedPaymentFlag = missedPaymentFlag;}
    public int getPaymentsLeft() {
        return paymentsLeft;
    }
    public void setPaymentsLeft(int paymentsLeft) {
        this.paymentsLeft = paymentsLeft;
    }
}//end of LoanAccount