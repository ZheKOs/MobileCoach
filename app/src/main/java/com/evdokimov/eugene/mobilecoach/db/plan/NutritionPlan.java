package com.evdokimov.eugene.mobilecoach.db.plan;

public class NutritionPlan {

    private long idPlan;
    private long idDish;

    public NutritionPlan()
    {
        this.setIdPlan(-1);
        this.setIdDish(-1);
    }
    public NutritionPlan(long idPlan, long idDish)
    {
        this.setIdPlan(idPlan);
        this.setIdDish(idDish);
    }


    public long getIdPlan() {return idPlan;}
    public void setIdPlan(long idPlan) {this.idPlan = idPlan;}

    public long getIdDish() {return idDish;}
    public void setIdDish(long idDish) {this.idDish = idDish;}
}
