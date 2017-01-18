package stayfit.DataBase;


import android.content.Context;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DatabaseAcesser {

    /* Lists*/
    private List<User> users;
    private List<DataSample> dataSamples;
    private List<String> DATABASE;
    private  Context context;

    public DatabaseAcesser(Context c)
    {
        /* DataBase List initialisation */
        context=c;
        users = new ArrayList<User>();
        dataSamples = new ArrayList<DataSample>();
        DATABASE= new ArrayList<String>();
        DataBaseRefresh();
    }

    /* Db acesser */
    public void DataBaseRefresh()
    {
        try {

            InputStream inputStream = context.openFileInput("DATABASE.txt");
            users.clear();
            dataSamples.clear();
            DATABASE.clear();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DATABASE.add(line);

            while (line !=null)
            {
                try {
                    line =reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DATABASE.add(line);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(int i=0; i<DATABASE.size();i++)
            {
                if (DATABASE.get(i) != null) {
                    DataBaseInterpret(DATABASE.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /* Db interpreter */
    public void DataBaseInterpret(String line)
    {
        if(line!=null)
        {

            String datas=line.substring(1,line.length()-1);
            List <String> dataTypes = Arrays.asList(datas.split("\\s*=\\s*"));
            List<String> values =Arrays.asList(dataTypes.get(1).split("\\s*;\\s*"));

            if(dataTypes.get(0).equals("user"))
            {
                users.add(new User(Integer.parseInt(values.get(0)),values.get(1),values.get(2),values.get(3),Integer.parseInt(values.get(4)),Integer.parseInt(values.get(5)),values.get(6),values.get(7)));
            }

            if(dataTypes.get(0).equals("datasample"))
            {
                List<String> lats = new ArrayList<String>();
                List<String> longs = new ArrayList<String>();

                for(int i=8; i<values.size(); i++)
                {
                    if(values.get(i) !=null)
                    {
                        String[]LatLong= values.get(i).split("/");
                        lats.add(LatLong[0]);
                        longs.add(LatLong[1]);
                    }

                }
                dataSamples.add(new DataSample(Integer.parseInt(values.get(0)),Integer.parseInt(values.get(1)),Integer.parseInt(values.get(2)),values.get(3),Integer.parseInt(values.get(4)),Integer.parseInt(values.get(5)),Integer.parseInt(values.get(6)),Integer.parseInt(values.get(7)),lats,longs));
            }
            else{}

        }
    }
    /* Getters */
    public List<User> getUsers()
    {
        return users;
    }
    public List<DataSample> getDataSamples()
    {
        return dataSamples;
    }


    /* Setters*/
    public void saveUser(int weight,int size, String dayvalue, String monthvalue,String yearvalue, String gender,String finalActualUser)
    {
        try {
            File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
            OutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);

            for (User user : users) {
                if(user.Pseudo.equals(finalActualUser))
                {
                    outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + Integer.toString(weight).toString()+ ";" + Integer.toString(size).toString() + ";" + dayvalue.toString()+"."+monthvalue.toString()+"."+yearvalue.toString() + ";" + gender.toString() + "]" + "\n");
                }
                else
                {
                    outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + user.Weight + ";" + user.Height + ";" + user.Birthdate + ";" + user.Gender + "]" + "\n");
                }
            }

            for(DataSample datasample :dataSamples)
            {
                String latsLongs="";
                for(int i =0; i < datasample.lats.size(); i++)
                {
                    latsLongs += ";" +datasample.lats.get(i).toString()+ "/"+datasample.longs.get(i).toString();
                }
                outputStreamWriter.write("[datasample="+datasample.ID +";"+datasample.USER_ID +";"+datasample.Duration +";"+datasample.Date+";"+datasample.ACTIVITY_ID+";"+datasample.Distance+";"+datasample.Steps+";"+datasample.Calories+latsLongs +"]"+"\n");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }
    public void saveDataSample(String finalActualUser,String date,long elapsedSeconds,String activityType,int COVERED_DISTANCE,int FOOT_STEPS,int CALORIES,String latsLongs)
    {
        try {
            File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
            OutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);

            String ActualUserID="0";

            for (User user : users) {
                if(user.Pseudo.equals(finalActualUser))
                {
                    ActualUserID=Integer.toString(user.ID);
                }
                outputStreamWriter.write("[user=" + user.ID + ";" + user.Pseudo + ";" + user.Email + ";" + user.MDP + ";" + user.Weight + ";" + user.Height + ";" + user.Birthdate + ";" + user.Gender + "]" + "\n");
            }
            for(DataSample datasample :dataSamples)
            {
                String latLongs="";
                for(int i =0; i < datasample.lats.size(); i++)
                {
                    latLongs += ";" +datasample.lats.get(i).toString()+ "/"+datasample.longs.get(i).toString();
                }
                outputStreamWriter.write("[datasample="+datasample.ID +";"+datasample.USER_ID +";"+datasample.Duration +";"+datasample.Date+";"+datasample.ACTIVITY_ID+";"+datasample.Distance+";"+datasample.Steps+";"+datasample.Calories+latLongs +"]"+"\n");
            }
            outputStreamWriter.write("[datasample="+dataSamples.size() +";"+ActualUserID +";"+String.valueOf(elapsedSeconds)+";"+date+";"+activityType+";"+COVERED_DISTANCE+";"+FOOT_STEPS+";"+CALORIES+ latsLongs +"]"+"\n");

            outputStreamWriter.close();
        }
        catch (IOException e) {

        }
    }
    public void createUser(String userName,String emailAdress,String password)
    {
        try {
            File outputFile = new File(context.getFilesDir(),"DATABASE.txt");
            OutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter outputStreamWriter= new OutputStreamWriter(outStream);

            for (User user : users) {
                outputStreamWriter.write("[user="+user.ID +";" +user.Pseudo+";"+user.Email +";"+user.MDP+";"+user.Weight+";"+user.Height +";"+user.Birthdate +";"+user.Gender +"]"+"\n");
            }
            int ID =users.size()+1;
            outputStreamWriter.write("[user="+ID+";"+userName+";"+emailAdress+";"+password+";" +"0;" + "0;" +"1.1.1970;"+"male]"+"\n");

            for(DataSample datasample :dataSamples)
            {
                String latsLongs="";
                for(int i =0; i < datasample.lats.size(); i++)
                {
                    latsLongs += ";" +datasample.lats.get(i).toString()+ "/"+datasample.longs.get(i).toString();
                }
                outputStreamWriter.write("[datasample="+datasample.ID +";"+datasample.USER_ID +";"+datasample.Duration +";"+datasample.Date+";"+datasample.ACTIVITY_ID+";"+datasample.Distance+";"+datasample.Steps+";"+datasample.Calories+latsLongs +"]"+"\n");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }
}
