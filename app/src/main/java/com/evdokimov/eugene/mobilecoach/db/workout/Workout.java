package com.evdokimov.eugene.mobilecoach.db.workout;

public class Workout {

    private long id;
    private String name;
    private String instruction;
    private String imgPath;

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

    public long getId() { return id; }
    public Workout setId(long id) { this.id = id; return this; }

    public String getName() { return name; }
    public Workout setName(String name) { this.name = name; return this; }

    public String getInstruction() { return instruction; }
    public Workout setInstruction(String instruction) { this.instruction = instruction; return this;}

    public String getImgPath() { return imgPath; }
    public Workout setImgPath(String imgPath) { this.imgPath = imgPath; return this;}

    @Override
    public String toString() {
        return "Dish{" +
                "fullName=\'" + name + "\'" +
                "instruction=\'" + instruction + "\'";
    }
}