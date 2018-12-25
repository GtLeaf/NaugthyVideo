package com.example.pc_0775.naugthyvideo.bean;

/**
 * Created by PC-0775 on 2018/12/25.
 */

public class UserBean {

    /**
     * _id : 5c0242149a5369af2ce8dcae
     * phone_number : 15662360528
     * nick_name : Candy
     * password : cen3799
     * sex : 1
     * deadline : 2019-12-05 24:00:00
     * isVIP : true
     */

    private String _id;
    private String phone_number;
    private String nick_name;
    private String password;
    private String sex;
    private String deadline;
    private boolean isVIP;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isIsVIP() {
        return isVIP;
    }

    public void setIsVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }
}
