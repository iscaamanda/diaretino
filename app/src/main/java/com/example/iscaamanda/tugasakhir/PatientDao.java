package com.example.iscaamanda.tugasakhir;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PatientDao {
    @Query("SELECT * FROM patient")
    List<Patient> getAllPatients();

    @Insert
    void insertAll(Patient... patients);
}
