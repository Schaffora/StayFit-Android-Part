package stayfit.DataBase;

import java.io.Serializable;
import java.util.List;

public class DataSample implements Serializable{

    /*Tools use to store */
    public int ID;
    public int USER_ID;
    public int Duration;
    public String Date;
    public int ACTIVITY_ID;
    public int Distance;
    public int Steps;
    public int Calories;
    public List<String> lats;
    public List<String>longs;

    public DataSample(int ID, int USER_ID, int Duration, String Date, int ACTIVITY_ID, int Distance, int Steps, int Calories,List <String> lats, List <String> longs)
    {
        this.ID=ID; this.USER_ID=USER_ID; this.Duration=Duration; this.Date=Date; this.ACTIVITY_ID=ACTIVITY_ID; this.Distance=Distance; this.Steps=Steps; this.Calories=Calories; this.lats=lats; this.longs=longs;
    }
}
