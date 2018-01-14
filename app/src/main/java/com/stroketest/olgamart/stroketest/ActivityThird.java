package com.stroketest.olgamart.stroketest;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class ActivityThird extends AppCompatActivity {
    public Patient pt = new Patient(Parcel.obtain());
    public MovementView movementView;
    boolean clear_data_testing;
    boolean clear_data_training;

    final String LOG_TAG = "myLogs";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            pt = b.getParcelable("Patient");
            movementView = new MovementView(this, pt);
        }

        clear_data_testing = getIntent().getBooleanExtra("clear_data_testing", false);
        clear_data_training = getIntent().getBooleanExtra("clear_data_training", false);

        if (pt.testing == 1) clear_data_testing = false;
        else clear_data_training = false;

        setContentView(movementView);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putParcelable("Patient", pt);
        intent.putExtras(b);

        intent.putExtra("clear_data_testing", clear_data_testing);
        intent.putExtra("clear_data_training", clear_data_training);
        setResult(RESULT_OK, intent);
        finish();

    }

}
