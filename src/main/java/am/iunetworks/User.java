package am.iunetworks;

import java.util.Date;

public class User {
    private String firstName;
    private String lastName;
    private int age;
    private Date birthdate;

    public User() {
    }

    public User(String firstName, String lastName, Date birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        if (birthdate != null) {
            age = new Date().getYear() - birthdate.getYear();
        }
    }

    public String whatIsYourName(){
        return firstName + " " + lastName;
    }
    public Integer getAge(){
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
