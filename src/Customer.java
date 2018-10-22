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
}
