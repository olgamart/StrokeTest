package com.stroketest.olgamart.stroketest;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by olgamart on 13/12/2017.
 */

public class ActivityResults extends AppCompatActivity implements View.OnClickListener {
    TextView resTest;
    TextView timeTest;
    TextView resTraining;
    TextView timeTraining;
    TextView setFrameTest;
    TextView setFrameTraining;
    TextView SaveResults;
    Button btn_save;
    Patient p;
    boolean clear_data_testing;
    boolean clear_data_training;

    final String LOG_TAG = "myLogs";

    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileST.txt";


    int set_frame_test = 0;
    int set_frame_training = 0;
    int res_test = 0;
    int count_training = 0;
    int tm_test = 0;
    int tm_training = 0;
    final int tm_limited = 18; // Time limit for test in seconds


    String str_tm_training; //training time hours, minutes, seconds
    ArrayList<String> arr_result = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        p = new Patient(Parcel.obtain());

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            p = b.getParcelable("Patient");


        }

        clear_data_testing = getIntent().getBooleanExtra("clear_data_testing", false);
        clear_data_training = getIntent().getBooleanExtra("clear_data_training", false);

        res_test = p.result_test;
        tm_test = p.time_test / 1000;
        set_frame_test = p.set_fr_test;
        count_training = p.count;
        tm_training = p.time_training / 1000;
        set_frame_training = p.set_fr_training;


        if (clear_data_testing) {
            set_frame_test = 0;
            res_test = 0;
            tm_test = 0;
        }

        if (clear_data_training) {
            set_frame_training = 0;
            count_training = 0;
            tm_training = 0;
        }
        clear_data_testing = false;
        clear_data_training = false;

        setArray();

        resTest = (TextView) findViewById(R.id.resTest);
        timeTest = (TextView) findViewById(R.id.timeTest);
        resTraining = (TextView) findViewById(R.id.resTraining);
        timeTraining = (TextView) findViewById(R.id.timeTraining);
        setFrameTest = (TextView) findViewById(R.id.setFrameTest);
        setFrameTraining = (TextView) findViewById(R.id.setFrameTraining);
        SaveResults = (TextView) findViewById(R.id.SaveResults);

        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
        SaveResults.setText("");


        if (tm_test != 0) {
            if (res_test == 1) {
                resTest.setText(getResources().getString(R.string.result) + getResources().getString(R.string.test_result));
                arr_result.set(10, getResources().getString(R.string.result) + getResources().getString(R.string.test_result)
                        + getResources().getString(R.string.line_break));
            } else {
                resTest.setText(getResources().getString(R.string.result) + getResources().getString(R.string.test_not_result));
                arr_result.set(10, getResources().getString(R.string.result) + getResources().getString(R.string.test_not_result)
                        + getResources().getString(R.string.line_break));
            }

            if (tm_test == tm_limited && p.result_test == 0) {
                timeTest.setText(getResources().getString(R.string.time_over));
                arr_result.set(11, getResources().getString(R.string.time_over) + getResources().getString(R.string.line_break));
            } else {
                timeTest.setText(getResources().getString(R.string.time) + tm_test + getResources().getString(R.string.seconds));
                arr_result.set(11, getResources().getString(R.string.time) + tm_test + getResources().getString(R.string.seconds) +
                        getResources().getString(R.string.line_break));
            }
            setFrameTest.setText(frameSet(set_frame_test));
            arr_result.set(12, frameSet(set_frame_test) + getResources().getString(R.string.line_break));
        }

        if (tm_training != 0) {
            setFrameTraining.setText(frameSet(set_frame_training));


            if (tm_training < 60) str_tm_training = tm_training + getResources().getString(R.string.seconds);
            else {
                int tm_min = tm_training / 60;
                if (tm_min < 60) str_tm_training = tm_min + getResources().getString(R.string.minutes) + tm_training % 60
                        + getResources().getString(R.string.seconds);
                else
                    str_tm_training = tm_min / 60 + getResources().getString(R.string.hours) + tm_min % 60 +  getResources().getString(R.string.minutes)
                            + tm_training % 60 + getResources().getString(R.string.seconds);
            }
            resTraining.setText(getResources().getString(R.string.count) + count_training);
            timeTraining.setText(getResources().getString(R.string.time) + str_tm_training);

            arr_result.set(15, getResources().getString(R.string.count) + count_training + getResources().getString(R.string.line_break));
            arr_result.set(16, getResources().getString(R.string.time) + str_tm_training + getResources().getString(R.string.line_break));
            arr_result.set(17, frameSet(set_frame_training) + getResources().getString(R.string.line_break));
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_save:

                String filePath = writeFileSD(arr_result);
                sendResults(this, filePath);
                SaveResults.setText(getResources().getString(R.string.save_results));
                break;

            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("clear_data_training", clear_data_training);
        intent.putExtra("clear_data_testing", clear_data_testing);

        setResult(RESULT_OK, intent);
        finish();

    }

    public String frameSet(int set) {
        String str = "";
        switch (set) {
            case 1:
                str = getResources().getString(R.string.set_frame) + getResources().getString(R.string.set_frame1);
                break;
            case 2:
                str = getResources().getString(R.string.set_frame) + getResources().getString(R.string.set_frame2);
                break;
            case 3:
                str = getResources().getString(R.string.set_frame) + getResources().getString(R.string.set_frame3);
                break;
            case 4:
                str = getResources().getString(R.string.set_frame) + getResources().getString(R.string.set_frame4);
                break;
            default:
                break;
        }
        return str;
    }

    public void setArray() {

        Calendar c = Calendar.getInstance();
        //   SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");

        SimpleDateFormat dateformat = new SimpleDateFormat(getResources().getString(R.string.date));
        String datetime = dateformat.format(c.getTime());

        arr_result.add(getResources().getString(R.string.header_results));
        arr_result.add(datetime + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.patient_data));
        arr_result.add(getResources().getString(R.string.second_name) + p.second_name + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.first_name) + p.first_name + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.middle_name) + p.middle_name + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.age) + p.age + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.gender) + p.gender + getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.mode_test));
        arr_result.add(getResources().getString(R.string.not_result));
        arr_result.add(getResources().getString(R.string.time_test_str));
        arr_result.add(getResources().getString(R.string.not_set_frame));
        arr_result.add(getResources().getString(R.string.line_break));
        arr_result.add(getResources().getString(R.string.mode_training));
        arr_result.add(getResources().getString(R.string.not_result));
        arr_result.add(getResources().getString(R.string.time_training_str));
        arr_result.add(getResources().getString(R.string.not_set_frame));

    }

    String writeFileSD(ArrayList<String> array) {
        //  SD availability
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, getResources().getString(R.string.sd_card) + Environment.getExternalStorageState());
            return getResources().getString(R.string.sd_error);
        }

        File sdFile = new File(getExternalFilesDir(DIR_SD), FILENAME_SD);


/*
        if (!sdFile.mkdirs()) {
            Log.d(LOG_TAG, "Directory not created");
        }
        */

        try {
            //    Log.d(LOG_TAG, "Путь к файлу: " + sdFile.getAbsolutePath());

            FileWriter bw = new FileWriter(sdFile);
            for (int i = 0; i < array.size(); i++) bw.write(array.get(i));
            //   bw.write("Новая запись в файл\n");

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sdFile.getAbsolutePath();
    }

    void sendResults(Context context, String filePath) {
        String contact = "stroketest01@gmail.com";
        String subject = "STest";
        String message = "";

        for (int i = 0; i < arr_result.size(); i++) {

            message += arr_result.get(i);
        }

        Intent resultsIntent = new Intent(android.content.Intent.ACTION_SEND);
        resultsIntent.setType("text/plain");
        resultsIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contact});
        resultsIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        resultsIntent.putExtra(Intent.EXTRA_TEXT, message);
        resultsIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

        startActivity(Intent.createChooser(resultsIntent, "Send results."));
        context.startActivity(resultsIntent);


    }


}
