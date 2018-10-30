import java.util.Date;

class CD extends Account{

    private double currentInterestRate;
    private Date dateCDDue;

    private CD(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn) {
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

}
