package com.evdokimov.eugene.mobilecoach.db.dish;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable { //same implementation is in Workout.class, so go and check out about it there

    public static final String NO_IMG = "NO_IMG_TO_THE_DISH";

    private long id;
    private String name;
    private String receipt;
    private double kcal;
    private String imgPath;

    public Dish()
    {
        this.setId(-1);
        this.setName("");
        this.setReceipt("");
        this.setKcal(0);
        this.setImgPath(null);//this.setImgPath(NO_IMG);
    }


    public Dish(long id, String name, String receipt, double kcal, String imgPath){
        this.setId(id);
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