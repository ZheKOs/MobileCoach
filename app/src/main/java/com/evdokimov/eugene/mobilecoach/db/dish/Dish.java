package com.evdokimov.eugene.mobilecoach.db.dish;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dishes")
public class Dish implements Parcelable { //same implementation is in Workout.class, so go and check out about it there

    public static final String NO_IMG = "NO_IMG_TO_THE_DISH";

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(dataType = DataType.STRING)
    private String receipt;
    @DatabaseField(dataType = DataType.DOUBLE)
    private double kcal;
    @DatabaseField(dataType = DataType.STRING)
    private String imgPath;

    public Dish()
    {
//        this.setName("");
//        this.setReceipt("");
//        this.setKcal(0);
//        this.setImgPath(null);//this.setImgPath(NO_IMG);
    }


    public Dish( String name, String receipt, double kcal, String imgPath){
        this.setName(name);
        this.setReceipt(receipt);
        this.setKcal(kcal);
        this.setImgPath(imgPath);
    }

    public long getId() { return id; }
    public Dish setId(long id) { this.id = id; return this; }

    public String getName() { return name; }
    public Dish setName(String name) { this.name = name; return this; }

    public String getReceipt() { return receipt; }
    public Dish setReceipt(String receipt) { this.receipt = receipt; return this; }

    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }

    public String getImgPath() { return imgPath; }
    public Dish setImgPath(String imgPath) { this.imgPath = imgPath; return this;}

    @Override
    public String toString() {
        return "Dish{" +
                "fullName=\'" + name + "\'" +
                "receipt=\'" + receipt + "\'" +
                "kcal = \'" + getKcal() + "\'";
    }

    public Dish (Parcel in){
        this.setId(in.readLong());
        this.setName(in.readString());
        this.setReceipt(in.readString());
        this.setKcal(in.readDouble());
        this.setImgPath(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(getId());
        parcel.writeString(getName());
        parcel.writeString(getReceipt());
        parcel.writeDouble(getKcal());
        parcel.writeString(getImgPath());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

}