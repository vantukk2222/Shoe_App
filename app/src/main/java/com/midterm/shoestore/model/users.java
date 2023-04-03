package com.midterm.shoestore.model;

public class users {
    private String bod;
    private String name;
    private String phoneno;
    private Boolean sex;

    public users()
    {}
    public users(users u)
    {
        this.bod = u.bod;

        this.name = u.name;
        this.phoneno = u.phoneno;
        this.sex = u.sex;
    }
    public users(String bod, String name, String phoneno, Boolean sex) {
        this.bod = bod;
        this.name = name;
        this.phoneno = phoneno;
        this.sex = sex;
    }

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
