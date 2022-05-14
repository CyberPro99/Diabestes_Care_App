package com.example.diabestes_care_app.Ui.Sing_up_pages.Doctor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.diabestes_care_app.Base_Activity.Basic_Activity;
import com.example.diabestes_care_app.R;
import com.example.diabestes_care_app.Ui.Sing_up_pages.Patient.Sing_Up_1_P;
import com.example.diabestes_care_app.Ui.Sing_up_pages.Patient.Sing_Up_2_P;
import com.example.diabestes_care_app.Ui.Sing_up_pages.Patient.Sing_Up_4_P;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Sing_Up_1_D extends Basic_Activity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://diabeticsproject-default-rtdb.firebaseio.com/");
    final Calendar myCalendar = Calendar.getInstance();
    RadioGroup mGender;
    RadioButton mGenderOption;
    String strGender;
    Button btn_next_S;
    EditText name_ar, name_en, username, mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        setContentView(R.layout.activity_sign_up_1_d);

        //====================================Define variables===============================
        name_ar = findViewById(R.id.Sp1_name_D);
        name_en = findViewById(R.id.Sp1_en_name_D);
        mDate = findViewById(R.id.Sp1_date_D);
        username = findViewById(R.id.Sp1_username_D);
        mGender = findViewById(R.id.Sp1_Gender_D);
        btn_next_S = findViewById(R.id.Sp1_bt_next_D);

        //====================================Next Button===============================
        btn_next_S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get data form edit text into string variables
                String doctorName_ar = name_ar.getText().toString();
                String doctorName_en = name_en.getText().toString();
                String doctorUsername = username.getText().toString();
                String doctorDate = mDate.getText().toString();

                //====================================Validation===============================
                // cheek if user fill all data fields before sending data to firebase
                if (validIsEmpty(doctorName_ar, doctorName_en, doctorUsername, doctorDate, doctorDate, doctorDate)) {
                    Toast.makeText(Sing_Up_1_D.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!doctorUsername.startsWith("D")) {
                    Toast.makeText(Sing_Up_1_D.this, "User Name Must Start With D", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("doctor").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if username is not registered before
                            if (snapshot.hasChild(doctorUsername)) {
                                Toast.makeText(Sing_Up_1_D.this, "ID is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // sending data to firebase real time
                                // we are using a phone number as unique identity of every user
                                databaseReference.child("doctor").child(doctorUsername).child("personal_info").child("name_ar").setValue(doctorName_ar);
                                databaseReference.child("doctor").child(doctorUsername).child("personal_info").child("name_en").setValue(doctorName_en);
                                databaseReference.child("doctor").child(doctorUsername).child("personal_info").child("Gender").setValue(strGender);
                                databaseReference.child("doctor").child(doctorUsername).child("personal_info").child("date").setValue(doctorDate);
                                databaseReference.child("doctor").child(doctorUsername).child("username").setValue(doctorUsername);
                                databaseReference.child("doctor").child(doctorUsername).child("personal_info").child("username").setValue(doctorUsername);
                                Toast.makeText(Sing_Up_1_D.this, "User have registered successfully ", Toast.LENGTH_SHORT).show();
                                finish();

                                Intent intent2 = new Intent(Sing_Up_1_D.this, Sing_Up_2_D.class);
                                intent2.putExtra("username", doctorUsername);
                                startActivity(intent2);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("My_Error", error.getMessage());
                        }
                    });
                }
            }
        });

        //====================================Gender Radio Group to get Data And set into Firebase===============================
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mGenderOption = mGender.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.Sp1_male_P:
                        strGender = mGenderOption.getText().toString();
                        break;
                    case R.id.Sp1_female_P:
                        strGender = mGenderOption.getText().toString();
                        break;
                    default:
                }
            }
        });

        //====================================DataPicker===============================
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(mDate, myCalendar, "dd/MM/yyyy");
            }
        };
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Sing_Up_1_D.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
