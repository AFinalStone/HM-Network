package com.hm.iou.network.demo.bean;

import java.io.Serializable;

public class CheckVersionResBean implements Serializable {
    private String titile;
    private String content;
    private String subContent;
    private int type;
    private int osType;
    private String fileMD5;
    private String fileSize;
    private String downloadUrl;

    public CheckVersionResBean() {
    }

    public String getTitile() {
        return this.titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubContent() {
        return this.subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOsType() {
        return this.osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
    }

    public String getFileMD5() {
        return this.fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "CheckVersionResBean{" +
                "titile='" + titile + '\'' +
                ", content='" + content + '\'' +
                ", subContent='" + subContent + '\'' +
                ", type=" + type +
                ", osType=" + osType +
                ", fileMD5='" + fileMD5 + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}