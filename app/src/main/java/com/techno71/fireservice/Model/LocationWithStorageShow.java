package com.techno71.fireservice.Model;

public   class LocationWithStorageShow {

    private String id;
    private String floor;
    private String company_name;
    private String company_owner;
    private String license_approved_date;
    private String license_renew_date;
    private String address;
    private String division;
    private String distric;
    private String thana;
    private String company_type;
    private String company_detils;
    private String storage_img;
    private String alert_tag;
    private String status;

    public LocationWithStorageShow(String id, String floor, String company_name, String company_owner, String license_approved_date, String license_renew_date, String address, String division, String distric, String thana, String company_type, String company_detils, String storage_img, String alert_tag, String status) {
        this.id = id;
        this.floor = floor;
        this.company_name = company_name;
        this.company_owner = company_owner;
        this.license_approved_date = license_approved_date;
        this.license_renew_date = license_renew_date;
        this.address = address;
        this.division = division;
        this.distric = distric;
        this.thana = thana;
        this.company_type = company_type;
        this.company_detils = company_detils;
        this.storage_img = storage_img;
        this.alert_tag = alert_tag;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_owner() {
        return company_owner;
    }

    public void setCompany_owner(String company_owner) {
        this.company_owner = company_owner;
    }

    public String getLicense_approved_date() {
        return license_approved_date;
    }

    public void setLicense_approved_date(String license_approved_date) {
        this.license_approved_date = license_approved_date;
    }

    public String getLicense_renew_date() {
        return license_renew_date;
    }

    public void setLicense_renew_date(String license_renew_date) {
        this.license_renew_date = license_renew_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistric() {
        return distric;
    }

    public void setDistric(String distric) {
        this.distric = distric;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getCompany_detils() {
        return company_detils;
    }

    public void setCompany_detils(String company_detils) {
        this.company_detils = company_detils;
    }

    public String getStorage_img() {
        return storage_img;
    }

    public void setStorage_img(String storage_img) {
        this.storage_img = storage_img;
    }

    public String getAlert_tag() {
        return alert_tag;
    }

    public void setAlert_tag(String alert_tag) {
        this.alert_tag = alert_tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
