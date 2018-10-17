class SavingAccount extends Account{
    protected double currentInterestRate;
    protected String dateAccountOpened;
    protected String dateCDDue;

    //constructor for the SavingAccount
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, String dateAccOpenedIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateFormat.format(dateAccOpenedIn);
    }

    //overloading constructor if it is a CD
    public SavingAccount(int cusIDIn, double accBalIn, double currIntRateIn, String dateAccOpenedIn, String dateCDDueIn){
        this.customerID = cusIDIn;
        this.accountBalance = accBalIn;
        this.currentInterestRate = currIntRateIn;
        this.dateAccountOpened = dateFormat.format(dateAccOpenedIn);
        this.dateCDDue = dateFormat.format(dateCDDueIn);
    }

    //getters
    public double getCurrentInterestRate() {return currentInterestRate;}
    public String getDateAccountOpened() {return dateAccountOpened;}
    public String getDateCDDue() {return dateCDDue;}

    //setters
    public void setCurrentInterestRate(double currentInterestRate) {this.currentInterestRate = currentInterestRate;}
    public void setDateAccountOpened(String dateAccountOpened) {this.dateAccountOpened = dateAccountOpened;}
    public void setDateCDDue(String dateCDDue) {this.dateCDDue = dateCDDue;}

    //other methods to be decided





}//end of SavingAccount