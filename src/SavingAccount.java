import java.util.Date;
import java.util.ArrayList;
import java.io.*;
import java.text.ParseException;

class SavingAccount extends Account {
    protected double currentInterestRate;
    protected Date dateCDDue;

    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "Saving");
        this.currentInterestRate = currIntRateIn;
        this.dateCDDue = null;
    }

    //overloading SavingAccount to CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, Date dateAccOpenedIn, Date dateCDDueIn) {
        super(cusIDIn, accBalIn, dateAccOpenedIn, "CD");
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateCDDue = dateCDDueIn;
    }


    //Getters and setters
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

    @Override
    public String toString() {
        return "SavingAccount{" +
                "currentInterestRate=" + currentInterestRate +
                ", dateAccountOpened=" + dateAccountOpened +
                ", dateCDDue=" + dateCDDue +
                ", customerID=" + customerID +
                ", accountBalance=" + accountBalance +
                '}';
    }

    //Grab the current Savings account in the file in memory
    public static ArrayList<SavingAccount> importFile() throws IOException, ParseException {

    }
}


}//end of SavingAccount