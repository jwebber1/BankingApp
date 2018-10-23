abstract class Person {
    protected int id;
    protected String fName;
    protected String lName;
    protected String password;

    public Person(int id, String fName, String lName, String pw){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.password = pw;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getfName() {return fName;}
    public void setfName(String fName) {this.fName = fName;}
    public String getlName() {return lName;}
    public void setlName(String lName) {this.lName = lName;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}//end of Person
