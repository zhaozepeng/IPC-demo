package com.android.binderpool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-15
 */
public class Weather implements Parcelable{
    public String cityName;
    public double temperature;
    public double humidity;
    public AllWeather weather;

    protected Weather(Parcel in) {
        temperature = in.readDouble();
        humidity = in.readDouble();
        //使用该方式来写入枚举
        weather = AllWeather.values()[in.readInt()];
        cityName = in.readString();
    }

    public Weather() {

    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(temperature);
        dest.writeDouble(humidity);
        dest.writeInt(weather.ordinal());
        dest.writeString(cityName);
    }

    public enum AllWeather{
        sunny,cloudy,rain,snowy
    }
}
