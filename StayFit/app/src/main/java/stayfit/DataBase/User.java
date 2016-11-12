package stayfit.DataBase;


import java.io.Serializable;

public class User implements Serializable{

    /*Tools use to store */
    public int ID;
    public String Pseudo;
    public String Email;
    public String MDP;
    public int Weight;
    public int Height;
    public String Birthdate;
    public String Gender;

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
