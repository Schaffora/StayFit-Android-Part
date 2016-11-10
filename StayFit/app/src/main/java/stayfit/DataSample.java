package stayfit;

/**
 * Created by raphael.schaffo on 10.11.2016.
 */

public class DataSample {

    /*Tools use to store */
    int ID;
    int USER_ID;
    int Duration;
    String Date;
    int ACTIVITY_ID;
    int Distance;
    int Steps;
    int Calories;

    public DataSample(int ID, int USER_ID, int Duration, String Date, int ACTIVITY_ID, int Distance, int Steps, int Calories)
    {
        this.ID=ID; this.USER_ID=USER_ID; this.Duration=Duration; this.Date=Date; this.ACTIVITY_ID=ACTIVITY_ID; this.Distance=Distance; this.Steps=Steps; this.Calories=Calories;
    }
}
