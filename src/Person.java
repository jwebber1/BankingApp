public class Person {
    protected int id;   //social security number
    protected String streetAddress;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String fName;
    protected String lName;
    protected int userLevel;    // 1 = customer, 2 = teller, 3 = manager

    public Person(int id, String addr, String city, String state, String zip, String fName, String lName, int uLevel){
        this.id = id;
        this.streetAddress = addr;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        this.fName = fName;
        this.lName = lName;
        this.userLevel = uLevel;
    }

    //getters and setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getStreetAddress() {return streetAddress;}
    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress;}
    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}
    public String getState() {return state;}
    public void setState(String state) {this.state = state;}
    public String getZipCode() {return zipCode;}
    public void setZipCode(String zipCode) {this.zipCode = zipCode;}
    public String getfName() {return fName;}
    public void setfName(String fName) {this.fName = fName;}
    public String getlName() {return lName;}
    public void setlName(String lName) {this.lName = lName;}
    public int getUserLevel() {return userLevel;}
    public void setUserLevel(int userLevel) {this.userLevel = userLevel;}

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", userLevel=" + userLevel +
                '}';
    }

}//end of Person
