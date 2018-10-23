import java.util.Date;

/**
 * TODO:
 */

class LoanAccount extends Account{
    protected double currentInterestRate;
    protected String description;
    protected Date datePaymentDue;
    protected double currentPaymentDue;
    protected Date datePaymentNotified;
    protected Date lastPaymentDate;
    protected Byte missedPaymentFlag;   //1=true   0=false

    public LoanAccount(int cusIDIn, String desc, double accBalIn, double currIntRate, Date datePayDue, double currPayDue, Date datePayNotifIn, Date lastPayDateIn, Byte missPayFlagIn, String loanTypeIn){
        super(cusIDIn, accBalIn, null, loanTypeIn);
        this.description = desc;
        this.currentInterestRate = currIntRate;
        this.datePaymentDue = datePayDue;
        this.currentPaymentDue = currPayDue;
        this.datePaymentNotified = datePayNotifIn;
        this.lastPaymentDate = lastPayDateIn;
        this.missedPaymentFlag = missPayFlagIn;
    }

    public double getCurrentInterestRate() {return currentInterestRate;}
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
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
                ", customerID=" + customerID + 
                ", accountBalance=" + accountBalance + 
                '}';
    }

    //other methods to be decided

}//end of LoanAccount
