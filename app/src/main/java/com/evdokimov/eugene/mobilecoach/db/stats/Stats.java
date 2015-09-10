package com.evdokimov.eugene.mobilecoach.db.stats;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;

@DatabaseTable(tableName = "stats")
public class Stats implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String date;
    @DatabaseField(canBeNull = false, dataType = DataType.SHORT)
    private short type;
    @DatabaseField(dataType = DataType.FLOAT)
    private float value;
//    @DatabaseField(dataType = DataType.INTEGER)
//    private int index;
    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean picked;

    public Stats(){}
    public Stats(String name, String date, short type, float value, boolean picked){
        setName(name);
        setDate(date);
        setType(type);
        setValue(value);
//        setIndex(index);
        setIsChartInList(picked);
    }

    //getters and setters

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}

    /**
     * @return (short) 0 workout; 1 dish; 2 workoutPlan; 3 nutrition
     */
    public short getType() {return type;}
    /**
     * @param type 0 workout; 1 dish; 2 workoutPlan; 3 nutrition
     */
    public void setType(short type) {this.type = type;}

    public float getValue() {return value;}
    public void setValue(float value) {this.value = value;}

//    public int getIndex() {return index;}
//    public void setIndex(int index) {this.index = index;}

    public boolean getIsChartInList(){return picked;}
    public void setIsChartInList(boolean isChartInList){this.picked = isChartInList;}

    //for debug was override toString method
    @Override
    public String toString() {
        return "Stats{" +
                "name=\'" + getName() + "\'" +
                "date=\'" + getDate() + "\'" +
                "type=\'" + getType() + "\'" +
                "value=\'" + getValue() + "\'" +
//                "index= \'" + getIndex() + "\'" +
                "picked= \'" + getIsChartInList() + "\'" +
                "}";
    }

    //parcel part

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeInt((int)this.type);
        dest.writeFloat(this.value);
//        dest.writeInt(this.index);
        dest.writeByte((byte)(picked ?0:1)); //if picked == true, byte == 1
    }

    protected Stats(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.date = in.readString();
        this.type  = (short)in.readInt();
        this.value = in.readFloat();
//        this.index = in.readInt();
        this.picked = in.readByte() != 0; //picked == true if byte != 0
    }

    public static final Creator<Stats> CREATOR = new Creator<Stats>() {
        public Stats createFromParcel(Parcel source) {
            return new Stats(source);
        }
        public Stats[] newArray(int size) {
            return new Stats[size];
        }
    };

}
