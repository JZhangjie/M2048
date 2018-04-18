package com.test.zj.m2048;

import android.util.Log;

import com.test.zj.m2048.utils.ArrayHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        int[][] ar = new int[4][4];

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                ar[i][j] = i+j;
            }
        }
        String s = ArrayHelper.ArrayToString(ar);
        System.out.println(s);


        int[][] b = ArrayHelper.StringToArray(s);

        for(int i=0;i<b.length;i++){
            for(int j=0;j<b[i].length;j++){
                System.out.println(b[i][j]);
            }
        }

    }
}