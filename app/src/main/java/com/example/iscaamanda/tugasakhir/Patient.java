package com.example.iscaamanda.tugasakhir;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Patient {


    public Patient(String firstName, String lastName, String patientId,
                   String addBirthday, String addDate, String eyePosition,
                   String imageLoc, String imageLabel, String imageConfidence) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patientId = patientId;
        this.addBirthday = addBirthday;
        this.addDate = addDate;
        this.eyePosition = eyePosition;
        this.imageLoc = imageLoc;
        this.imageLabel = imageLabel;
        this.imageConfidence = imageConfidence;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "patientId")
    private String patientId;

    @ColumnInfo(name = "addBirthday")
    private  String addBirthday;

    @ColumnInfo(name = "addDate")
    private  String addDate;

    @ColumnInfo(name = "eyePosition")
    private  String eyePosition;

    @ColumnInfo(name = "imageLoc")
    private  String imageLoc;

    @ColumnInfo(name = "imageLabel")
    private String imageLabel;

    @ColumnInfo(name = "imageConfidence")
    private String imageConfidence;


    //GETTER AND SETTER


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAddBirthday() { return addBirthday;  }

    public void setAddBirthday(String addBirthday) { this.addBirthday = addBirthday; }

    public String getAddDate() {  return addDate; }

    public void setAddDate(String addDate) {   this.addDate = addDate; }

    public String getEyePosition() {  return eyePosition; }

    public void getEyePosition(String eyePosition) {   this.eyePosition = eyePosition; }

    public String getImageLoc() {
        return imageLoc;
    }

    public void setImageLoc(String imageLoc) {
        this.imageLoc = imageLoc;
    }

    public String getImageLabel() {  return imageLabel; }

    public void setImageLabel(String imageLabel) {  this.imageLabel = imageLabel; }

    public String getImageConfidence() {
        return imageConfidence;
    }

    public void setImageConfidence(String imageConfidence) {  this.imageConfidence = imageConfidence; }

}

