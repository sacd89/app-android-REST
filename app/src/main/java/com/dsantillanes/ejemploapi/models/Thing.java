package com.dsantillanes.ejemploapi.models;

/**
 * Created by dsantillanes on 23/03/17.
 */

public class Thing {
    private String _id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
