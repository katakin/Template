package ru.katakin.template.model;

public class Sys {
    private int type;
    private long id;
    private float message;
    private String country;
    private long sunrise;
    private long sunset;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public float getMessage() {
        return message;
    }
    public void setMessage(float message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public long getSunrise() {
        return sunrise;
    }
    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }
    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}
