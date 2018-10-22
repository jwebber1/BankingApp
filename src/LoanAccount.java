import java.util.Date;

class LoanAccount extends Account{
    protected double currentInterestRate;
<<<<<<< HEAD
=======
    protected String description;
>>>>>>> JonTextInBranch
    protected Date datePaymentDue;
    protected double currentPaymentDue;
    protected Date datePaymentNotified;
    protected Date lastPaymentDate;
    protected Byte missedPaymentFlag;   // 1=true   0=false
    protected String loanType;      //long term = "LT"  short term = "ST"   credit card = "CC"

<<<<<<< HEAD
    public LoanAccount(int cusIDIn, double accBalIn, String loanTypeIn, double currIntRate, Date datePayDue, double currPayDue, Date datePayNotifIn, Date lastPayDateIn, Byte missPayFlagIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
=======
    public LoanAccount(int cusIDIn, String desc, double accBalIn, double currIntRate, Date datePayDue, double currPayDue, Date datePayNotifIn, Date lastPayDateIn, Byte missPayFlagIn, String loanTypeIn){
        super(cusIDIn, accBalIn);
        this.description = desc;
>>>>>>> JonTextInBranch
        this.loanType = loanTypeIn;
        this.currentInterestRate = currIntRate;
        this.datePaymentDue = datePayDue;
        this.currentPaymentDue = currPayDue;
        this.datePaymentNotified = datePayNotifIn;
        this.lastPaymentDate = lastPayDateIn;
        this.missedPaymentFlag = missPayFlagIn;
    }

<<<<<<< HEAD
    //getters
    public String getLoanType() {return loanType;}
    public double getCurrentInterestRate() {return currentInterestRate;}
    public Date getDatePaymentDue() {return datePaymentDue;}
    public double getCurrentPaymentDue() {return currentPaymentDue;}
    public Date getDatePaymentNotified() {return datePaymentNotified;}
    public Date getLastPaymentDate() {return lastPaymentDate;}
    public Byte   getMissedPaymentFlag() {return missedPaymentFlag;}

    //setters
    public void setLoanType(String loanType) {this.loanType = loanType;}
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public void setDatePaymentDue(Date datePaymentDue) {this.datePaymentDue = datePaymentDue;}
    public void setCurrentPaymentDue(double currentPaymentDue) {this.currentPaymentDue = currentPaymentDue;}
    public void setDatePaymentNotified(Date datePaymentNotified) {this.datePaymentNotified = datePaymentNotified;}
    public void setLastPaymentDate(Date lastPaymentDate) {this.lastPaymentDate = lastPaymentDate;}
    public void setMissedPaymentFlag(Byte missedPaymentFlag) {this.missedPaymentFlag = missedPaymentFlag;}

=======
>>>>>>> JonTextInBranch
    //other methods to be decided

    @Override
    public String toString() {
        return "LoanAccount{" + 
                "currentInterestRate=" + currentInterestRate + 
                ", description='" + description + '\'' + 
                ", datePaymentDue=" + datePaymentDue + 
                ", currentPaymentDue=" + currentPaymentDue + 
                ", datePaymentNotified=" + datePaymentNotified + 
                ", lastPaymentDate=" + lastPaymentDate + 
                ", missedPaymentFlag=" + missedPaymentFlag + 
                ", loanType='" + loanType + '\'' + 
                ", customerID=" + customerID + 
                ", accountBalance=" + accountBalance + 
                '}';
    }
}//end of LoanAccount