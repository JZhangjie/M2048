package com.test.zj.m2048.utils;

/**
 * 使用，和；将二维数组和字符串进行转换
 */

public class ArrayHelper {

    public static String ArrayToString(int[][] items){
        if(items == null)
            return "";

        StringBuilder builder = new StringBuilder();
        for(int i=0;i<items.length;i++){
            int[] temp = items[i];

            for(int j = 0;j<temp.length;j++){
                builder.append(items[i][j]);
                builder.append(",");
            }
            builder.deleteCharAt(builder.lastIndexOf(","));
            builder.append(';');
        }
        builder.deleteCharAt(builder.lastIndexOf(";"));

        return builder.toString();
    }


    public static int[][] StringToArray(String s){
        if(s==null || s == ""){
            return null;
        }

        String[] list1 = s.split(";");

        int n = list1.length;

        int[][] array = new int[n][n];

        for(int i=0;i<n;i++){

            String s2 = list1[i];

            String[] list2 = s2.split(",");

            for(int j = 0;j<n;j++){
                String s3 = list2[j];

                int t = Integer.parseInt(s3);

                array[i][j] = t;
            }
        }

        return  array;
    }
}
