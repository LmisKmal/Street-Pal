package com.sharekeg.streetpal.userinfoforeditingprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HP on 03/03/17.
 */

public class UsersInfoForEditingProfile implements Serializable {

    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("pass")
    @Expose
    private String pass;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("email")
    @Expose
    private String email;
    private final static long serialVersionUID = 6935504488740568606L;

    public UsersInfoForEditingProfile(String email, String userName, String phone, String age, String work, String password) {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
