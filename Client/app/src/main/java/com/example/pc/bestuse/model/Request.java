package com.example.pc.bestuse.model;

public class Request {
    String _id;
    Integer selling;

    public Request() {
    }
    public Request(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getSelling() {
        return selling;
    }

    public void setSelling(Integer selling) {
        this.selling = selling;
    }
}
