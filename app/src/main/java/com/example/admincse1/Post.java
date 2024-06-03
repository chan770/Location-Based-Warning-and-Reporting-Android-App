package com.example.admincse1;

public class Post {
    String place;
    String description;
    String damage;

    public Post() {
    }

    @Override
    public String toString() {
        return "spot{" +
                "place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", damage='" + damage + '\'' +
                '}';
    }

    public String getPlace() {
        return place;
    }

    public void setplace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDamage() {
        return damage;
    }

    public void setdamage(String damage) {
        this.damage = damage;
    }
}
