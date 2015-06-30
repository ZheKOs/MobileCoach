package com.evdokimov.eugene.mobilecoach.db.workout;

import android.os.Parcel;
import android.os.Parcelable;

public class Workout implements Parcelable { //I don't even know if I should really use Parcel  X)

    private long id;
    private String name;
    private String instruction;
    private String imgPath;

    //constructors

    public Workout()
    {
        this.setId(-1);
        this.setName("");
        this.setInstruction("");
        this.setImgPath(null);
    }


    public Workout(long id, String name, String instruction, String imgPath){
        this.setId(id);
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

    //override for some reason... testing
    @Override
    public String toString() {
        return "Dish{" +
                "fullName=\'" + name + "\'" +
                "instruction=\'" + instruction + "\'";
    }

    //Parcelling part
    //as far as I understood Parcel is like box which we can easily transport threw Activities

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

    //pack my class to this "box"  	(◕‿◕) ALSO. This "box" workts like FIFO...
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