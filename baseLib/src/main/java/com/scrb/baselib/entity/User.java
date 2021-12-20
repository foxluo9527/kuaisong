package com.scrb.baselib.entity;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * album :
     * fansCount : 0
     * followCount : 5
     * head : http://image.yysc.online/files/2020/5/6/3fac2426bd3924257b88669cd5dae6c0.png
     * id : 4178
     * nickName : 酷酷的松
     * password : 654321
     * phone : 18781793053
     * project : null
     * projectKey : futures
     * signature : 快乐快乐快乐快乐
     * talkCount : 0
     * type : 1
     * uuid : 23b98f1213a941c3868284055c18103f
     */

    private String album;
    private int fansCount;
    private int followCount;
    private String head;
    private int id;
    private String nickName;
    private String password;
    private String phone;
    private Object project;
    private String projectKey;
    private String signature;
    private int talkCount;
    private int type;
    private String uuid;
    private boolean isCheck;
    private boolean isFollow;

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getProject() {
        return project;
    }

    public void setProject(Object project) {
        this.project = project;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getTalkCount() {
        return talkCount;
    }

    public void setTalkCount(int talkCount) {
        this.talkCount = talkCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}