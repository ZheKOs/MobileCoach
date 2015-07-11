package com.evdokimov.eugene.mobilecoach.db.workout;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "workouts")
public class Workout implements Parcelable { //I don't even know if I should really use Parcel  X)

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(dataType = DataType.STRING)
    private String instruction;
    @DatabaseField(dataType = DataType.STRING)
    private String imgPath;

    public static final String NO_IMG = "NO_IMG_TO_THE_WORKOUT";

    //constructors
    public Workout()
    {
//        this.setId(-1);
//        this.setName("");
//        this.setInstruction("");
//        this.setImgPath(null);
    }


    public Workout( String name, String instruction, String imgPath){
        //this.setId(id);
        this.setName(name);
        this.setInstruction(instruction);
        this.setImgPath(imgPath);
    }

    //getter and setter methods

    public long getId() { return id; }
    public Workout setId(long id) { this.id = id; return this; }

    public String getName() { return name; }
    public Workout setName(String name) { this.name = name; return this; }

    public String getInstruction() { return instruction; }
    public Workout setInstruction(String instruction) { this.instruction = instruction; return this;}

    public String getImgPath() { return imgPath; }
    public Workout setImgPath(String imgPath) { this.imgPath = imgPath; return this;}

    //override for some reason... testing?
    @Override
    public String toString() {
        return "Workout{" +
                "WorkoutName=\'" + name + "\'" +
                "instruction=\'" + instruction + "\'";
    }

    //Parcelling part
    //as far as I understood Parcel is like box which we can easily transport threw Activities
    //but for current db realization it is useless x)

    public Workout (Parcel in){ //create class using such "box" :D

        String[] data = new String[3];

        this.setId(in.readLong()); //coz unique var and Parcel works on FIFO ¯＼_(ツ)_/¯

        in.readStringArray(data); //read parcel StringArray to our temporarily String array


        this.setName(data[0]);
        this.setInstruction(data[1]);
        this.setImgPath(data[2]);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    //pack my class to this "box"  	(◕‿◕) ALSO. This "box" works like FIFO...
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getId());
        parcel.writeStringArray(new String[]{getName(),getInstruction(),getImgPath()} );
    }

    //and finally Creator of this "box".
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

}