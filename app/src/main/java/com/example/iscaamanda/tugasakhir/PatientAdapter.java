package com.example.iscaamanda.tugasakhir;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

        List<Patient> patients;
        private Context mContext;


        public PatientAdapter(List<Patient> patients, Context context) {
        this.patients = patients;
        this.mContext = context;
        }

    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientAdapter.ViewHolder holder, final int position) {
        holder.firstName.setText(patients.get(position).getFirstName());
        holder.lastName.setText(patients.get(position).getLastName());
        holder.patientId.setText(patients.get(position).getPatientId());
        holder.addBirthday.setText(patients.get(position).getAddBirthday());
        holder.eyePosition.setText(patients.get(position).getEyePosition());
        holder.addDate.setText(patients.get(position).getAddDate());



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra("first_name", patients.get(position).getFirstName());
                intent.putExtra("last_name",patients.get(position).getLastName());
                intent.putExtra("patient_id",patients.get(position).getPatientId());
                intent.putExtra("add_birthday",patients.get(position).getAddBirthday());
                intent.putExtra("add_date",patients.get(position).getAddDate());
                intent.putExtra("eye_position",patients.get(position).getEyePosition());
                intent.putExtra("image_loc",patients.get(position).getImageLoc());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView firstName;
        public TextView lastName;
        public TextView patientId;
        public LinearLayout parentLayout;
        public TextView addBirthday;
        public TextView addDate;
        public TextView eyePosition;


        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            patientId = itemView.findViewById(R.id.patient_id);
            addBirthday = itemView.findViewById(R.id.add_birthday);
            addDate = itemView.findViewById(R.id.add_date);
            eyePosition = itemView.findViewById(R.id.eye_position);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
