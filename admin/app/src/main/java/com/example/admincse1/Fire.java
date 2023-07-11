package com.example.admincse1;

public class Fire {
    String Description;
    String Place;
    String Damage;


    public void  Fire(){

    }

    public Fire(String description, String place, String damage) {
        Description = description;
        Place = place;
        Damage = damage;
    }

    public String getDescription() {
        return Description;
    }

    public String getPlace() {
        return Place;
    }

    public String getDamage() {
        return Damage;
    }
}
