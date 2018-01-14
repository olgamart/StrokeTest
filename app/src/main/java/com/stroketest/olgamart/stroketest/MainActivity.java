package com.stroketest.olgamart.stroketest;

import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button next1;
    EditText secondName;
    EditText firstName;
    EditText middleName;
    EditText age;
    RadioButton mn;
    RadioButton fm;
    boolean clear_data_training;
    boolean clear_data_testing;


    Patient p = new Patient(Parcel.obtain());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p.gender = getResources().getString(R.string.male);

        secondName = (EditText) findViewById(R.id.secondName);
        firstName = (EditText) findViewById(R.id.firstName);
        middleName = (EditText) findViewById(R.id.middleName);
        age = (EditText) findViewById(R.id.age);
        mn = (RadioButton) findViewById(R.id.mn);
        fm = (RadioButton) findViewById(R.id.fm);

        mn.setOnClickListener(this);
        fm.setOnClickListener(this);
        next1 = (Button) findViewById(R.id.next1);
        next1.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mn:
                p.gender = getResources().getString(R.string.male);
                break;
            case R.id.fm:
                p.gender = getResources().getString(R.string.female);
                break;

            case R.id.next1:
                p.second_name = secondName.getText().toString();
                p.first_name = firstName.getText().toString();
                p.middle_name = middleName.getText().toString();
                p.age = age.getText().toString();
                clear_data_testing = true;
                clear_data_training = true;

                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putParcelable("Patient", p);
                intent.putExtras(b);

                intent.putExtra("clear_data_testing", clear_data_testing);
                intent.putExtra("clear_data_training", clear_data_training);
                intent.setClass(this, ActivityTwo.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
