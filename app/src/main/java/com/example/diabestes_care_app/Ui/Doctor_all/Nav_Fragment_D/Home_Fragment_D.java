package com.example.diabestes_care_app.Ui.Doctor_all.Nav_Fragment_D;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.diabestes_care_app.Adapters.PatientList_Adapter;
import com.example.diabestes_care_app.Models.DoctorList_Model;
import com.example.diabestes_care_app.Notification_Controller.Notification_Number;
import com.example.diabestes_care_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Home_Fragment_D extends Fragment {

    // Firebase
    DatabaseReference myRef, followRef, followList, Ref;
    // Widget
    RecyclerView recyclerView;
    // Variables
    ArrayList<DoctorList_Model> list;
    // Adapter
    PatientList_Adapter patientList_adapter;
    // Search Variables
    EditText searchInput;
    CharSequence search = "";
    TextView username;
    ImageView imageProfile;
    // ShardPreference
    public static final String MyPREFERENCES_D = "D_Username";
    Context context;
    // Patient Username TextView
    String DoctorUsername;
    // Notification Counter
    Notification_Number notification_number;
    // Follow Checker
    Boolean followChecker = false;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home___d, container, false);

        //============================Defines=======================================================
        searchInput = view.findViewById(R.id.HP_search_input_d);
        username = view.findViewById(R.id.HP_patient_name_d);
        recyclerView = view.findViewById(R.id.HP_recyclerView_d);
        imageProfile = view.findViewById(R.id.HP_profile_img_d);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("إنتظر قليلاً يتم تحميل المحتوى..");
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dilog_background));
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        progressDialog.show();

        //============================Firebase======================================================
        myRef = FirebaseDatabase.getInstance().getReference();

        //============================Get Doctor Username===========================================
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MyPREFERENCES_D, MODE_PRIVATE);
        DoctorUsername = prefs.getString("TAG_NAME", null);

        //============================Configure Recyclerview========================================
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //============================ArrayList=====================================================
        list = new ArrayList<>();
        // Clear ArrayList
        ClearAll();
        // Get Patient Data Method
        GetDataFromFirebase();
        //============================Search And Filter Function====================================
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                patientList_adapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //============================Notification_Number Counter===================================
        notification_number = new Notification_Number(view.findViewById(R.id.bell));

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_number.incrementNumber();
            }
        });
        return view;
    }

    //========================Get Patient list Data From Firebase Function===========================
    private void GetDataFromFirebase() {
        Query query = myRef.child("patient");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DoctorList_Model doctorListModel = new DoctorList_Model();
                        doctorListModel.setName(snapshot.child("personal_info").child("name").getValue().toString());
                        doctorListModel.setUsername(snapshot.child("username").getValue().toString());
                        doctorListModel.setImageUrl(snapshot.child("User_Profile_Image").child("Image").child("mImageUrI").getValue().toString());
                        list.add(doctorListModel);
                        progressDialog.dismiss();

                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "إنتظر قليلاً ", Toast.LENGTH_SHORT).show();

                }
                patientList_adapter = new PatientList_Adapter(getContext(), list);
                recyclerView.setAdapter(patientList_adapter);
                patientList_adapter.updateUsersList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });
    }

    //============================Clear Recycle Review Data Function================================
    private void ClearAll() {
        if (list != null) {
            list.clear();
        }
        if (patientList_adapter != null) {
            patientList_adapter.notifyDataSetChanged();
        }
        list = new ArrayList<>();
    }

    //============================Show The doctor name + image======================================
    @Override
    public void onStart() {
        super.onStart();

        myRef = FirebaseDatabase.getInstance().getReference("doctor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String image = snapshot.child(DoctorUsername).child("User_Profile_Image").child("Image").child("mImageUrI").getValue(String.class);
                String name = snapshot.child(DoctorUsername).child("personal_info").child("name_ar").getValue(String.class);
                Glide.with(getActivity()).load(image).into(imageProfile);
                Log.d("TAG", name + "/" + image);
                username.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });
    }
}