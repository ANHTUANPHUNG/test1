package com.example.test.Model;

import com.example.test.Service.dto.Enums.EGender;

import java.sql.Date;

public class Teacher {
    private Long id;
    private String name;
    private Date dob;
    private String hobie;
    private EGender gender;
    private Category category;

    public Teacher(Long id, String name, Date dob, String hobie, EGender gender, Category category) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.hobie = hobie;
        this.gender = gender;
        this.category = category;
    }

    public Teacher(Long id) {
        this.id = id;
    }

    public Teacher() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        if(dob == null) return "";
        return dob.toString();
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getHobie() {
        return hobie;
    }

    public void setHobie(String hobie) {
        this.hobie = hobie;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
