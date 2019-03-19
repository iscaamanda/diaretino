package com.example.iscaamanda.tugasakhir;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Patient.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PatientDao patientDao();

}
