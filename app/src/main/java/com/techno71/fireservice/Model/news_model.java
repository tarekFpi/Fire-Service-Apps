package com.techno71.fireservice.Model;

 public class news_model {

 private String id;
 private String  title;
 private String longDesription;
 private String image;
 private String UpdateDate;

     public news_model(String id, String title, String longDesription, String image, String updateDate) {
         this.id = id;
         this.title = title;
         this.longDesription = longDesription;
         this.image = image;
         UpdateDate = updateDate;
     }

     public String getUpdateDate() {
         return UpdateDate;
     }

     public void setUpdateDate(String updateDate) {
         UpdateDate = updateDate;
     }

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getTitle() {
         return title;
     }

     public void setTitle(String title) {
         this.title = title;
     }

     public String getLongDesription() {
         return longDesription;
     }

     public void setLongDesription(String longDesription) {
         this.longDesription = longDesription;
     }

     public String getImage() {
         return image;
     }

     public void setImage(String image) {
         this.image = image;
     }
 }
