package com.maya.mounty.mountyinternship;

public class UserDetails {


    private String name;
    private String email;
    private String mobile;


    public UserDetails()
    {

    }

    public UserDetails(String name, String email, String mobile)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
