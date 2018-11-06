import java.util.ArrayList;
import java.util.Date;
/*  For CC's currentPaymentDue is the current total bill for this payment cycle,
 *   and current balance is the total amount that can be borrowed per payment cycle.
 *   When a 0 is returned that indicates the action was successful.
 *   When a negative is returned that indicates the action failed where
 *   specific negative numbers indicate specific errors.
 *   */
class LoanAccount extends Account{
    protected double currentInterestRate;
    protected Date datePaymentDue;
    protected double currentPaymentDue;
    protected Date datePaymentNotified;
    protected Date lastPaymentDate;
    protected Byte missedPaymentFlag;   //1=true   0=false
    static ArrayList<LoanAccount> loans = new ArrayList<>();


    public LoanAccount(int cusIDIn, double accBalIn, double currIntRate, Date datePayDue, double currPayDue, Date datePayNotifIn, Date lastPayDateIn, Byte missPayFlagIn, String loanTypeIn){
        super(cusIDIn, accBalIn, null, loanTypeIn);
        currentInterestRate = currIntRate;
        datePaymentDue = datePayDue;
        currentPaymentDue = currPayDue;
        datePaymentNotified = datePayNotifIn;
        lastPaymentDate = lastPayDateIn;
        missedPaymentFlag = missPayFlagIn;
    }//end of LoanAccount Constructor

    public static int createLoanAccount(int cusIDIn, double accBalIn, double currIntRate, Date datePayDue,
                                        double currPayDue, Date datePayNotifIn, Date lastPayDateIn,
                                        Byte missPayFlagIn, String loanTypeIn){
        int completionCode = -1;
        boolean duplicate = false;
        for (int i = 0; i < loans.size(); i++){
            if (cusIDIn == loans.get(i).getCustomerID() &&
                    loans.get(i).getAccountType().equalsIgnoreCase(loanTypeIn)){
                duplicate = true;
            }
        }
        if (!duplicate){
            LoanAccount account = new LoanAccount(cusIDIn, accBalIn, currIntRate, datePayDue,
                    currPayDue, datePayNotifIn, lastPayDateIn, missPayFlagIn, loanTypeIn);
            loans.add(account);
            completionCode = 0;
        }
        //-1 = account already exists
        //0 = account successfully created and added
        return completionCode;
    }//end of createLoanAccount

    //creates a CreditCardPurchase tied to a specific account
    //returns boolean representing whether the action was successful
    public static int makeCCPurchase(LoanAccount account, double cost, String desc, Date date){
        int completionCode = -1;
        //if the purchase would not put account over the limit
        if (!ccPurchaseTooMuch(account, cost)){
            double tempDouble = account.getCurrentPaymentDue();
            account.setCurrentPaymentDue(tempDouble + cost);
            CreditCardPurchase purchase = new CreditCardPurchase(account.getCustomerID(), desc, cost, date);
            CreditCardPurchase.purchases.add(purchase);
            completionCode = 0;
        }
        //0 is returned when the action is successful
        //-1 is returned when purchase is too much
        return completionCode;
    }//end of makeCCPurchase

    //returns true if new CC purchase would put account over limit and false if it would not
    public static boolean ccPurchaseTooMuch(LoanAccount loanAccount, double newAmount){
        return (loanAccount.getCurrentPaymentDue() + newAmount) < loanAccount.getAccountBalance();
    }//end of isCCPurchaseTooMuch

    //checks to see if payment is late
    public static boolean paymentIsLate(Date datePayed, LoanAccount account){
        return datePayed.after(account.getDatePaymentDue());
    }//end of checkIfLate

    //updates interest rates to interestRate where the loan type matches identifier
    //returns int representing whether the action was successful (0 = no, 1 = yes)
    public static int updateLoanInterest(double interestRate, String identifier){
        int completionCode = -1;
        for (int i = 0; i < loans.size(); i++){
            //if the account type at i matches the identifier
            if (loans.get(i).getAccountType().equalsIgnoreCase(identifier)){
                loans.get(i).setCurrentInterestRate(interestRate);
                completionCode = 0;
            }
        }
        //0 is returned when the action is successful
        //-1 is returned when the interest rate is not updated
        return completionCode;
    }//end of updateLoanInterest

    //makes a payment on a specified loan as long as a payment can be made
    //returns boolean representing whether the action was successful
    public static int makePayement(String identifier, int ssn, double payment, Date date){
        int completionCode = -1;
        for (int i = 0; i< loans.size(); i++){
            //if the given ssn and account type match the ssn and account type at i
            if(ssn== loans.get(i).getCustomerID() && identifier.equalsIgnoreCase(loans.get(i).getAccountType())){
                //A payment can't be made on an account with no purchases
                if (loans.get(i).getCurrentPaymentDue() != 0.0) {
                    double amountDue = loans.get(i).getCurrentPaymentDue();
                    if(paymentIsLate(date, loans.get(i))){
                        loans.get(i).setCurrentPaymentDue(amountDue - payment);
                        loans.get(i).setMissedPaymentFlag((byte) 1);
                    } else {
                        loans.get(i).setCurrentPaymentDue(amountDue - payment);
                        loans.get(i).setMissedPaymentFlag((byte) 0);
                    }
                    completionCode = 0;
                }
            }
        }
        //0 is returned when the action was successful
        //-1 is returned when a payment can't be made
        return completionCode;
    }//end of makePayment

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
        return average / count;
    }//end of averageMonthlyBalance

    @Override
    public String toString() {
        return "LoanAccount{" +
                "currentInterestRate=" + currentInterestRate +
                ", datePaymentDue=" + datePaymentDue +
                ", currentPaymentDue=" + currentPaymentDue +
                ", datePaymentNotified=" + datePaymentNotified +
                ", lastPaymentDate=" + lastPaymentDate +
                ", missedPaymentFlag=" + missedPaymentFlag +
                ", customerID=" + customerID +
                ", accountBalance=" + accountBalance +
                '}';
    }//end of toString

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
    public Byte getMissedPaymentFlag() {return missedPaymentFlag;}
    public void setMissedPaymentFlag(Byte missedPaymentFlag) {this.missedPaymentFlag = missedPaymentFlag;}
}//end of LoanAccount
