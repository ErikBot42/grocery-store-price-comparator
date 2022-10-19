package com.example.grocerystoreoffers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> q1 = new ArrayList<String>();
        q1.add("Erbjudandena i appen uppdateras efter butikens nya erbjudanden vilket ofta sker veckovis");


        List<String> q2 = new ArrayList<String>();
        q2.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis commodo ex, et fermentum ligula vestibulum scelerisque. Vestibulum nec leo vel ligula dapibus volutpat. Sed ut facilisis orci. Quisque porta, enim ac semper dictum, odio mi tincidunt nibh, eget ullamcorper purus libero suscipit magna. Nulla sit amet est lacus. Etiam blandit, purus vitae rutrum pulvinar, turpis erat accumsan libero, a blandit tellus dolor a ante. Sed volutpat in enim id hendrerit. Ut elementum lectus tristique, interdum velit eget, cursus lacus. Maecenas suscipit pretium aliquam. Integer tincidunt erat nisi, et lacinia enim fermentum volutpat. Donec sit amet metus ante. Duis accumsan lorem augue, non tristique diam vehicula ut. Etiam sodales neque a lorem convallis fringilla. Praesent convallis, lacus at aliquet congue, libero arcu tincidunt risus, at feugiat sem elit sit amet sem. Morbi tincidunt, erat vitae dapibus tincidunt, ligula orci consectetur nibh, rhoncus finibus lectus enim eu metus. Donec suscipit maximus scelerisque.");


        List<String> q3 = new ArrayList<String>();
        q3.add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis lobortis commodo ex, et fermentum ligula vestibulum scelerisque. Vestibulum nec leo vel ligula dapibus volutpat. Sed ut facilisis orci. Quisque porta, enim ac semper dictum, odio mi tincidunt nibh, eget ullamcorper purus libero suscipit magna. Nulla sit amet est lacus. Etiam blandit, purus vitae rutrum pulvinar, turpis erat accumsan libero, a blandit tellus dolor a ante. Sed volutpat in enim id hendrerit. Ut elementum lectus tristique, interdum velit eget, cursus lacus. Maecenas suscipit pretium aliquam. Integer tincidunt erat nisi, et lacinia enim fermentum volutpat. Donec sit amet metus ante. Duis accumsan lorem augue, non tristique diam vehicula ut. Etiam sodales neque a lorem convallis fringilla. Praesent convallis, lacus at aliquet congue, libero arcu tincidunt risus, at feugiat sem elit sit amet sem. Morbi tincidunt, erat vitae dapibus tincidunt, ligula orci consectetur nibh, rhoncus finibus lectus enim eu metus. Donec suscipit maximus scelerisque.");


        expandableListDetail.put("Matvaran jag sökt efter finns inte som erbjudande, vad gör jag?", q1);
        expandableListDetail.put("Question 2", q2);
        expandableListDetail.put("Question 3", q3);
        return expandableListDetail;
    }
}