package com.stroketest.olgamart.stroketest;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;

/**
 * Created by olgamart on 31/10/2017.
 */

public class Patient implements Parcelable{

    int set_fr_test = 1;
    int set_fr_training = 1;
    int testing = 0;
    int time_test = 0;
    int time_training = 0;
    int count = 0;
    int result_test = 0;
    String second_name = "";
    String first_name = "";
    String middle_name = "";
    String age = "";
    String gender = "";

public Patient(Parcel in){
  this.set_fr_test = in.readInt();
  this.set_fr_training = in.readInt();
  this.testing = in.readInt();
  this.time_test = in.readInt();
  this.time_training = in.readInt();
  this.count = in.readInt();
  this.result_test = in.readInt();
  this.second_name = in.readString();
  this.first_name = in.readString();
  this.middle_name = in.readString();
  this.age = in.readString();
  this.gender = in.readString();

}

  public static final Creator<Patient> CREATOR = new Creator<Patient>() {
    @Override
    public Patient createFromParcel(Parcel in) {
      return new Patient(in);
    }

    @Override
    public Patient[] newArray(int size) {
      return new Patient[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeInt(set_fr_test);
    dest.writeInt(set_fr_training);
    dest.writeInt(testing);
    dest.writeInt(time_test);
    dest.writeInt(time_training);
    dest.writeInt(count);
    dest.writeInt(result_test);
    dest.writeString(second_name);
    dest.writeString(first_name);
    dest.writeString(middle_name);
    dest.writeString(age);
    dest.writeString(gender);
  }
}
