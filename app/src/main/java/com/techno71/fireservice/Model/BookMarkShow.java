package com.techno71.fireservice.Model;

public  class BookMarkShow {

   String company_id;

   String conpanyName;

   String company_Owner;

   String license_approved_date;

   String license_renew_date;

   String address;

   String division;

   String alert_tag;

   String companyArea;

   String company_Type;

   String image;

   String status;

   String latitude;

   String longtude;

   public BookMarkShow(String company_id, String conpanyName, String company_Owner, String license_approved_date, String license_renew_date, String address, String division, String alert_tag, String companyArea, String company_Type, String image, String status, String latitude, String longtude) {
      this.company_id = company_id;
      this.conpanyName = conpanyName;
      this.company_Owner = company_Owner;
      this.license_approved_date = license_approved_date;
      this.license_renew_date = license_renew_date;
      this.address = address;
      this.division = division;
      this.alert_tag = alert_tag;
      this.companyArea = companyArea;
      this.company_Type = company_Type;
      this.image = image;
      this.status = status;
      this.latitude = latitude;
      this.longtude = longtude;
   }

   public String getLatitude() {
      return latitude;
   }

   public void setLatitude(String latitude) {
      this.latitude = latitude;
   }

   public String getLongtude() {
      return longtude;
   }

   public void setLongtude(String longtude) {
      this.longtude = longtude;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getCompany_id() {
      return company_id;
   }

   public void setCompany_id(String company_id) {
      this.company_id = company_id;
   }

   public String getConpanyName() {
      return conpanyName;
   }

   public void setConpanyName(String conpanyName) {
      this.conpanyName = conpanyName;
   }

   public String getCompany_Owner() {
      return company_Owner;
   }

   public void setCompany_Owner(String company_Owner) {
      this.company_Owner = company_Owner;
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

   public String getAlert_tag() {
      return alert_tag;
   }

   public void setAlert_tag(String alert_tag) {
      this.alert_tag = alert_tag;
   }

   public String getCompanyArea() {
      return companyArea;
   }

   public void setCompanyArea(String companyArea) {
      this.companyArea = companyArea;
   }

   public String getCompany_Type() {
      return company_Type;
   }

   public void setCompany_Type(String company_Type) {
      this.company_Type = company_Type;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
