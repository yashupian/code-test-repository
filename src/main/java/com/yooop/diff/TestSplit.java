package com.yooop.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSplit {
    public static void main(String[] args) {

        String data = "<p>aa<p>bb</p><p>bb999</p></p>"

                + "<img alt=\"\" src=\"/a/201901.jpg\" id=\"pic1\" class=\"pic\"/>"
                + "<p>bb<img alt=\"\" src=\"/a/201902.jpg\" id=\"pic1\" class=\"pic\"/></p>"
                + "<img alt=\"\" src=\"/a/201902.jpg\" id=\"pic1\" class=\"pic\"/>"
                + "<p>cc</p>";
        List<String> data1 = cutStringByImgTag(data);
        for (String s : data1) {
            System.out.println(s);
        }
    }

    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>|</p>|</h[1-6]>|</div>");
        Matcher matcher = pattern.matcher(targetStr);

        Pattern pattern2 = Pattern.compile("<p>|<h[1-6]>|<div>");
        Matcher matcher2 = pattern2.matcher(targetStr);
        int[] depthBefore = new int[targetStr.length()];
        List<Integer> list = new ArrayList<>();
        while (matcher2.find()) {
            list.add(matcher2.end());
        }
        for (int i = 0; i < depthBefore.length; i++) {
            depthBefore[i] = getDepthByIndex(i, list);
        }
        for (int i : depthBefore) {
            System.out.print(i + " ");
        }
        System.out.println("");

        int lastIndex = 0;
        int depthAfter = 0;
        boolean saveElement;
        while (matcher.find()) {
            saveElement = false;
            if(Arrays.asList("</p>", "</div>").contains(targetStr.substring(matcher.start(), matcher.end()))){
                depthAfter++;
            }
            if(depthAfter == depthBefore[matcher.end()-1]){
                if (!Arrays.asList("</p>", "</div>").contains(targetStr.substring(matcher.start(), matcher.end()))) {
                    splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
                    saveElement = true;
                    System.out.println("B");
                }else{
                    splitTextList.add(targetStr.substring(lastIndex, matcher.end()));
                    saveElement = true;
                    System.out.println("A");
                }
            }
            if(saveElement){
                lastIndex = matcher.end();
            }
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex));
            System.out.println("C");
        }
        return splitTextList;
    }

    public static int getDepthByIndex(int index, List<Integer> list) {
        int depth = 0;
        for (Integer i : list) {
            if (index >= i) {
                depth++;
            }
        }
        return depth;
    }

}
