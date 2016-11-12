package stayfit.DataBase;

import java.io.Serializable;

public class ActivityType implements Serializable {

    /*Tools use to store */
    public int ID;
    public String Name;
    public double Coef;

    public ActivityType(int ID, String Name, double Coef)
    {
        this.ID=ID; this.Name=Name;this.Coef=Coef;
    }
}
