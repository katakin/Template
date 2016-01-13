package ru.katakin.template.model;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("3h")
    private float _3h;

    public float get3h() {
        return _3h;
    }
    public void set3d(float _3h) {
        this._3h = _3h;
    }
}
