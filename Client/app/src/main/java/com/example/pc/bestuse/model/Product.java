package com.example.pc.bestuse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@SuppressWarnings("serial")
public class Product implements Serializable {
    Object _id;
    private String name;
    private String description;
    private String image;
    private String category;
    private String address;
    private int old_price;
    private int new_price;
    private int selling;
    private int expiration_date;
    private Date up_date;
    private int amount;
    private Object _user;

    public Product(String name, String description, String image,String category, String address, int old_price, int new_price, int expiration_date, Date up_date, int amount) {
        this.name = name;
        this.image = image;
        this.category=category;
        this.address=address;
        this.description = description;
        this.old_price = old_price;
        this.new_price = new_price;
        this.expiration_date = expiration_date;
        this.up_date = up_date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOld_price() {
        return old_price;
    }

    public void setOld_price(int old_price) {
        this.old_price = old_price;
    }

    public int getNew_price() {
        return new_price;
    }

    public void setNew_price(int new_price) {
        this.new_price = new_price;
    }

    public int getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(int expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Date getUp_date() {
        return up_date;
    }

    public void setUp_date(Date up_date) {
        this.up_date = up_date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Object get_id() {
        return _id;
    }

    public void set_id(Object _id) {
        this._id = _id;
    }

    public Object get_user() {
        return _user;
    }

    public void set_user(Object _user) {
        this._user = _user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", old_price=" + old_price +
                ", new_price=" + new_price +
                ", expiration_date=" + expiration_date +
                ", up_date=" + up_date +
                ", amount=" + amount +
                '}';
    }
}
