import java.util.Date;

class LoanAccount extends Account{
    protected double currentInterestRate;
    protected String description;
    protected Date datePaymentDue;
    protected double currentPaymentDue;
    protected Date datePaymentNotified;
    protected Date lastPaymentDate;
    protected Byte missedPaymentFlag;   // 1=true   0=false
    protected String loanType;      //long term = "LT"  short term = "ST"   credit card = "CC"

    public LoanAccount(int cusIDIn, String desc, double accBalIn, double currIntRate, Date datePayDue, double currPayDue, Date datePayNotifIn, Date lastPayDateIn, Byte missPayFlagIn, String loanTypeIn){
        super(cusIDIn, accBalIn);
        this.description = desc;
        this.loanType = loanTypeIn;
        this.currentInterestRate = currIntRate;
        this.datePaymentDue = datePayDue;
        this.currentPaymentDue = currPayDue;
        this.datePaymentNotified = datePayNotifIn;
        this.lastPaymentDate = lastPayDateIn;
        this.missedPaymentFlag = missPayFlagIn;
    }

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