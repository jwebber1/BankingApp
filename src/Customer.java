public class Customer {
    private int socialSecurityNumber;    //customerID in account classes
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String firstName;
    private String lastName;

    //constructor for the Customer class
    public Customer (int ssn, String addr, String city, String state, String zip, String fName, String lName){
        this.socialSecurityNumber = ssn;
        this.streetAddress = addr;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        this.firstName = fName;
        this.lastName = lName;
    }

    public int getSocialSecurityNumber() {return socialSecurityNumber;}
    public void setSocialSecurityNumber(int socialSecurityNumber) {this.socialSecurityNumber = socialSecurityNumber;}
    public String getStreetAddress() {return streetAddress;}
    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress;}
    public String getCity() {return city;}
    public void setCity(String city) {this.city = city;}
    public String getState() {return state;}
    public void setState(String state) {this.state = state;}
    public String getZipCode() {return zipCode;}
    public void setZipCode(String zipCode) {this.zipCode = zipCode;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    @Override
    public String toString() {
        return "Customer{" +
                "socialSecurityNumber=" + socialSecurityNumber +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    //other methods to be decided


}
