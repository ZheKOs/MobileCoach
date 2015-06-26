package com.evdokimov.eugene.mobilecoach.db.dish;

public class Dish {

    //public static final String NO_IMG = "NO_IMG";

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
}