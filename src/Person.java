import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

public class Person {
    protected int id;   //social security number
    protected String streetAddress;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String firstName;
    protected String lastName;
    protected int userLevel;    // 1 = customer, 2 = teller, 3 = manager
    static ArrayList<Person> people = new ArrayList<>();

    public Person(int id, String addr, String city, String state, String zip, String firstName, String lastName, int uLevel){
        this.id = id;
        this.streetAddress = addr;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
        this.firstName = firstName;
        this.lastName = lastName;
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
    public String getFirstName() {return firstName;}
    public void setfirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setlastName(String lastName) {this.lastName = lastName;}
    public int getUserLevel() {return userLevel;}
    public void setUserLevel(int userLevel) {this.userLevel = userLevel;}


    //current method to grab data from the persons textfile in "memory"
    public static void importFile() throws IOException, ParseException {

        //creates a file referencing the text file in the memory folder
        File personsFileIn = new File("memory/people.txt");

        //creates a bufferedreader to read from a file
        BufferedReader personsBR = null;
        personsBR = new BufferedReader(new InputStreamReader(new FileInputStream(personsFileIn)));

        //buffer string to temporarily hold the line retrieved
        String line;

        //creates the ArrayList of data
        ArrayList<Person> importPerson = new ArrayList<>();
        ArrayList<ArrayList> accounts = new ArrayList<>();

        //generic counter to know the line currently on
        int lineNum = 0;

        //while loop to go through the file
        while ((line = personsBR.readLine()) != null) {

            //if the file has a header, this if statement is to avoid that
            //remove "if" if final file has no header
            if(lineNum > 0) {

                //split the line into an array of strings
                String[] splitLine = line.split(",");

                //create temp variable to hold info from the split lines
                int socialSecurityNumber = Integer.parseInt(splitLine[0]);//customerID in account classes
                String streetAddress = splitLine[1];
                String city = splitLine[2];
                String state = splitLine[3];
                String zipCode = splitLine[4];
                String firstName = splitLine[5];
                String lastName = splitLine[6];
                int userLevel = 1;  //default to customer
                //hard code userLevel to ssn
                if (socialSecurityNumber == 000000002 || socialSecurityNumber == 000000001){userLevel = 2;}
                else if (socialSecurityNumber == 000000000){userLevel = 3;}

                //add the new data (in our case checking) to the ArrayList
                people.add(new Person(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName, userLevel));

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + people.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        personsBR.close();
    }//end of person data import method

    //export people to people.txt
    public static void exportFile() throws FileNotFoundException {

        //create a new PrintWriter to write to a file
        PrintWriter personWriter = new PrintWriter(new FileOutputStream("memory/people.txt",false));

        //printing the headers of the files
        personWriter.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");


        for(Person person: people) {
            personWriter.println(person.getId() + "," + person.getStreetAddress() + "," +
                    person.getCity() + "," + person.getState() + "," + person.getZipCode() + "," +
                    person.getFirstName() + "," + person.getLastName() + ",");

            personWriter.flush();
        }
        //close the PrintWriter object
        personWriter.flush();
        personWriter.close();

    }//end of exportFile()

    //find one person given a customerID
    public static Person searchPeopleByCustomerID(int custId){

        //initialize searchResults to null
        Person searchResults = null;

        //loop through people in global arraylist
        for (Person person: people) {
            if(person.getId() == custId){
                searchResults = person;
            }
        }

        //return found person OR null
        return searchResults;
    }
}//end of Person