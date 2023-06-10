package com.techno71.fireservice.Model;

public   class commentShowModel {

   private String title;
   private String floor;
   private String comment;
   private String alert_tag;
   private String created_at;

   public commentShowModel(String title, String floor, String comment, String alert_tag, String created_at) {
      this.title = title;
      this.floor = floor;
      this.comment = comment;
      this.alert_tag = alert_tag;
      this.created_at = created_at;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getFloor() {
      return floor;
   }

   public void setFloor(String floor) {
      this.floor = floor;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getAlert_tag() {
      return alert_tag;
   }

   public void setAlert_tag(String alert_tag) {
      this.alert_tag = alert_tag;
   }

   public String getCreated_at() {
      return created_at;
   }

   public void setCreated_at(String created_at) {
      this.created_at = created_at;
   }
}
