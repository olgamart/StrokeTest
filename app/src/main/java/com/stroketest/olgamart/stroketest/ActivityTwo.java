package com.stroketest.olgamart.stroketest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ActivityTwo extends AppCompatActivity implements View.OnClickListener {

    TextView textframe;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn_traning;
    Button btn_test;
    Button btn_res;
    int set_fr;
    Patient p = new Patient(Parcel.obtain());

    final int REQUEST_CODE_ACTIVITYRESULTS = 1;
    final int REQUEST_CODE_ACTIVITYTHIRD = 2;
    boolean clear_data_training;
    boolean clear_data_testing;

    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        set_fr = 1;
        clear_data_testing = false;
        clear_data_training = false;

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            p = b.getParcelable("Patient");

        }

        clear_data_training = getIntent().getBooleanExtra("clear_data_training", false);
        clear_data_testing = getIntent().getBooleanExtra("clear_data_testing", false);


        textframe = (TextView) findViewById(R.id.textframe);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn_traning = (Button) findViewById(R.id.btn_traning);
        btn_test = (Button) findViewById(R.id.btn_test);
        btn_res = (Button) findViewById(R.id.btn_res);


        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn_traning.setOnClickListener(this);
        btn_test.setOnClickListener(this);
        btn_res.setOnClickListener(this);

        if (p.time_test != 0 || p.time_training != 0) btn_res.setVisibility(View.VISIBLE);
        else btn_res.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();


        if (p.time_test != 0 || p.time_training != 0) btn_res.setVisibility(View.VISIBLE);
        else btn_res.setVisibility(View.INVISIBLE);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn1:
                textframe.setText(R.string.set_frame1);
                set_fr = 1;
                break;
            case R.id.btn2:
                textframe.setText(R.string.set_frame2);
                set_fr = 2;
                break;
            case R.id.btn3:
                textframe.setText(R.string.set_frame3);
                set_fr = 3;
                break;
            case R.id.btn4:
                textframe.setText(R.string.set_frame4);
                set_fr = 4;
                break;
            case R.id.btn_traning:

                Intent intent = new Intent(ActivityTwo.this, ActivityThird.class);
                p.testing = 0;
                p.set_fr_training = set_fr;

                intent.putExtra("clear_data_testing", clear_data_testing);
                intent.putExtra("clear_data_training", clear_data_training);

                Bundle b = new Bundle();
                b.putParcelable("Patient", p);
                intent.putExtras(b);

                startActivityForResult(intent, REQUEST_CODE_ACTIVITYTHIRD);

                break;
            case R.id.btn_test:

                Intent intent1 = new Intent(ActivityTwo.this, ActivityThird.class);
                p.testing = 1;
                p.set_fr_test = set_fr;

                intent1.putExtra("clear_data_testing", clear_data_testing);
                intent1.putExtra("clear_data_training", clear_data_training);

                Bundle b1 = new Bundle();
                b1.putParcelable("Patient", p);
                intent1.putExtras(b1);

                startActivityForResult(intent1, REQUEST_CODE_ACTIVITYTHIRD);
                break;
            case R.id.btn_res:

                Intent intent2 = new Intent(ActivityTwo.this, ActivityResults.class);

                intent2.putExtra("clear_data_testing", clear_data_testing);
                intent2.putExtra("clear_data_training", clear_data_training);

                Bundle b2 = new Bundle();
                b2.putParcelable("Patient", p);
                intent2.putExtras(b2);

                startActivityForResult(intent2, REQUEST_CODE_ACTIVITYRESULTS);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_ACTIVITYTHIRD:

                clear_data_training = data.getBooleanExtra("clear_data_training", false);
                clear_data_testing = data.getBooleanExtra("clear_data_testing", false);
                Bundle b = data.getExtras();

                if (b != null) {
                    p = b.getParcelable("Patient");
                }
                break;

            case REQUEST_CODE_ACTIVITYRESULTS:
                clear_data_training = data.getBooleanExtra("clear_data_training", false);
                clear_data_testing = data.getBooleanExtra("clear_data_testing", false);
                break;
            default:
                break;
        }
    }
}
