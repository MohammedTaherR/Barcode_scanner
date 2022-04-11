package com.example.zeroq;

import java.util.ArrayList;

public class PointValue {

    long xValue;
    long yValue;
public  PointValue(String formattedDate, ArrayList<String> price){

}
    public PointValue(long xValue, long yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public long getxValue() {
        return xValue;
    }

    public long getyValue() {
        return yValue;
    }
}
