package com.techno71.fireservice.Model;

public    class CommentModel {

  private String id;
  private int UserImage;
  private String commentStatus;
  private String UploadDate;
  private String distance;

  public CommentModel(String id, int userImage, String commentStatus, String uploadDate, String distance) {
    this.id = id;
    UserImage = userImage;
    this.commentStatus = commentStatus;
    UploadDate = uploadDate;
    this.distance = distance;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getUserImage() {
    return UserImage;
  }

  public void setUserImage(int userImage) {
    UserImage = userImage;
  }

  public String getCommentStatus() {
    return commentStatus;
  }

  public void setCommentStatus(String commentStatus) {
    this.commentStatus = commentStatus;
  }

  public String getUploadDate() {
    return UploadDate;
  }

  public void setUploadDate(String uploadDate) {
    UploadDate = uploadDate;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }
}
