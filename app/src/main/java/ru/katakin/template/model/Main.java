package ru.katakin.template.model;

public class Main {
    private float temp;
    private float pressure;
    private float humidity;
    private float tempMin;
    private float tempMax;
    private float sea_level;
    private float grnd_level;

    public float getTemp() {
        return temp;
    }
    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getTempMin() {
        return tempMin;
    }
    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }
    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getSea_level() {
        return sea_level;
    }
    public void setSea_level(float sea_level) {
        this.sea_level = sea_level;
    }

    public float getGrnd_level() {
        return grnd_level;
    }
    public void setGrnd_level(float grnd_level) {
        this.grnd_level = grnd_level;
    }
}
