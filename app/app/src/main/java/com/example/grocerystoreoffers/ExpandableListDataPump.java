package com.example.grocerystoreoffers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(String ques1, String a1,String ques2,String a2,String ques3, String a3, String ques4, String a4) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> q1 = new ArrayList<String>();
        q1.add(a1);


        List<String> q2 = new ArrayList<String>();
        q2.add(a2);


        List<String> q3 = new ArrayList<String>();
        q3.add(a3);

        List<String> q4 = new ArrayList<String>();
        q4.add(a4);

        expandableListDetail.put(ques1, q1);
        expandableListDetail.put(ques2, q2);
        expandableListDetail.put(ques3, q3);
        expandableListDetail.put(ques4, q4);
        return expandableListDetail;
    }
}