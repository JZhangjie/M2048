package com.test.zj.m2048;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.zj.m2048.utils.ArrayHelper;
import com.test.zj.m2048.utils.Constants;
import com.test.zj.m2048.view.GamePanelView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mGamePanel;
    private GamePanelView mGamePanelView;
    private TextView mGameScore;
    private TextView mGameLevel;

    private int[][] currentItems;
    private int currentScore;
    private int[][] levelItems;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
        mGamePanelView.restart(currentItems);
    }

    private void initData(){

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String s =sp.getString(Constants.GAMEITEMS,"");
        currentScore = sp.getInt(Constants.GAMESCORE,0);
        currentItems = ArrayHelper.StringToArray(s);
        currentLevel =sp.getInt(Constants.GAMELEVEL,0);

        setText();
    }

    private void saveData(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int[][] temp = mGamePanelView.getCurrentItems();
        String items = ArrayHelper.ArrayToString(temp);

        editor.putString(Constants.GAMEITEMS,items);
        editor.putInt(Constants.GAMESCORE,currentScore);
        editor.putInt(Constants.GAMELEVEL,currentLevel);
        editor.commit();
    }

    private void init(){

        mGamePanel = (LinearLayout)findViewById(R.id.game_panel);
        mGameScore = (TextView)findViewById(R.id.game_score);
        mGameLevel = (TextView)findViewById(R.id.game_level);

        mGamePanelView = new GamePanelView(this);

        mGamePanel.addView(mGamePanelView);

        mGamePanelView.setCallback(new GamePanelView.Callback() {
            @Override
            public void updateScore(int score) {
                currentScore = score;
                setText();
            }

            @Override
            public void gameFinished() {
                Toast.makeText(MainActivity.this, "游戏结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateLevel(int level) {
                currentLevel = level;
                levelItems = mGamePanelView.getCurrentItems();
                setText();
            }
        });
    }

    private void restart(){
        currentScore = 0;
        currentLevel = 0;
        setText();
        mGamePanelView.restart(null);
    }

    private void retry(){
        currentScore = currentLevel;
        setText();
        mGamePanelView.restart(levelItems);
    }

    /**
     * 更新界面数值
     */
    private void setText(){
        mGameLevel.setText(currentLevel +"");
        mGameScore.setText("分数："+currentScore);
    }

    public void click(View v){
        switch (v.getId()){
            case R.id.btn_restart:
                restart();
                break;
            case R.id.btn_retry:
                retry();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
}
