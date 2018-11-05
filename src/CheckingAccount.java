import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

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

    //current method to grab data from the persons textfile in "memory"
    public static ArrayList<Person> importFile() throws IOException, ParseException {

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
                importPerson.add(new Person(socialSecurityNumber, streetAddress, city, state, zipCode, firstName, lastName, userLevel));

                //debugging importPersons
                //System.out.println("count: " + (lineNum) + "\t" + importPerson.get(lineNum-1).toString());
            }

            //increment the line number
            lineNum++;
        }

        //close the bufferfile and return the ArrayList
        personsBR.close();
        return importPerson;
    }//end of person data import method

    //export people to people.txt
    public static void exportFile(ArrayList<Person> people) throws FileNotFoundException {

        //create a new PrintWriter to write to a file
        PrintWriter personWriter = new PrintWriter(new FileOutputStream("memory/people.txt",false));

        //printing the headers of the files
        personWriter.println("SocialSecurityNumber,Address,City,State,ZIP,FirstName,LastName,");


        for(Person person: people) {
            personWriter.println(person.getId() + "," + person.getStreetAddress() + "," +
                    person.getCity() + "," + person.getState() + "," + person.getZipCode() + "," +
                    person.getfName() + "," + person.getlName() + ",");

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
        for (Person person: Main.people) {
            if(person.getId() == custId){
                searchResults = person;
            }
        }

        //return found person OR null
        return searchResults;
    }

    public static void debugImport(){
        System.out.println("Debugging Person import process");

        int count = 1;
        for(Person person: Main.people){
            System.out.println("Person " + count + ":\n" +
                    "id=" + person.getId() + "\n" +
                    "streetAddress='" + person.getStreetAddress() + '\'' + "\n" +
                    "city='" + person.getCity() + '\'' + "\n" +
                    "state='" + person.getState() + '\'' + "\n" +
                    "zipCode='" + person.getZipCode() + '\'' + "\n" +
                    "fName='" + person.getfName() + '\'' + "\n" +
                    "lName='" + person.getlName() + '\'' + "\n" +
                    "userLevel=" + person.getUserLevel());

            count++;
        }

    }//end of debugImport()



}//end of Person
