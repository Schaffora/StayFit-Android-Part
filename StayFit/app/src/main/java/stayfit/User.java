package stayfit;

/**
 * Created by raphael.schaffo on 10.11.2016.
 */

public class User {

    /*Tools use to store */
    int ID;
    String Pseudo;
    String Email;
    String MDP;
    int Weight;
    int Height;
    String Birthdate;
    String Gender;

    public User(int ID, String Pseudo, String Email, String MDP, int Weight, int Height, String Birthdate, String Gender) {
        this.ID = ID;
        this.Pseudo = Pseudo;
        this.Email = Email;
        this.MDP = MDP;
        this.Weight = Weight;
        this.Height = Height;
        this.Birthdate = Birthdate;
        this.Gender = Gender;
    }
}
