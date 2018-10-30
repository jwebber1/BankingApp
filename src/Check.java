import java.util.Date;

public class Check{
    int customerID;
    int checkID;
    double checkAmt;
    String payTo;
    Date dateCheck;
    String memo;

    //constructor for the Check class
    public Check(int cusIdIn, int checkIdIn, double checkAmtIn, String payToIn, Date dateCheckIn, String memoIn){
        this.customerID = cusIdIn;
        this.checkID = checkIdIn;
        this.checkAmt = checkAmtIn;
        this.payTo = payToIn;
        this.dateCheck = dateCheckIn;
        this.memo = memoIn;

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
}
