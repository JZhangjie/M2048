package com.test.zj.m2048;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.zj.m2048.data.GameStatus;
import com.test.zj.m2048.utils.ArrayHelper;
import com.test.zj.m2048.utils.Constants;
import com.test.zj.m2048.utils.RingPlayer;
import com.test.zj.m2048.view.GameItemView;
import com.test.zj.m2048.view.GamePanelView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mGamePanel;
    private GamePanelView mGamePanelView;
    private TextView mGameScore;
    private GameItemView mGameLevel;

    private GameStatus lastRecordStatus;
    private GameStatus currentStatus;

    private RingPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
        mGamePanelView.restart(currentStatus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    private void initData(){

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String current =sp.getString(Constants.CURRENTSTATUS,"");
        String last =sp.getString(Constants.LASTRECORD,"");

        currentStatus.loadFromString(current);
        lastRecordStatus.loadFromString(last);

        setText();
    }

    private void saveData(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int[][] temp = mGamePanelView.getCurrentItems();
        currentStatus.setItems(temp);

        editor.putString(Constants.LASTRECORD,lastRecordStatus.toString());
        editor.putString(Constants.CURRENTSTATUS,currentStatus.toString());

        editor.commit();
    }

    private void init(){
        lastRecordStatus = new GameStatus();
        currentStatus = new GameStatus();

        mPlayer = new RingPlayer(R.raw.trigger,this);

        mGamePanel = (LinearLayout)findViewById(R.id.game_panel);
        mGameScore = (TextView)findViewById(R.id.game_score);
        mGameLevel = (GameItemView)findViewById(R.id.game_level);

        mGamePanelView = new GamePanelView(this);

        mGamePanel.addView(mGamePanelView);

        mGamePanelView.setCallback(new GamePanelView.Callback() {
            @Override
            public void updateScore(int score) {
                currentStatus.setScore(score);
                currentStatus.setItems(mGamePanelView.getCurrentItems());
                setText();
            }

            @Override
            public void gameFinished() {
                Toast.makeText(MainActivity.this, "游戏结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateLevel(int level) {
                currentStatus.setLevel(level);
                setText();
            }

            @Override
            public void playTrigger() {
                mPlayer.play();
            }

            @Override
            public void playScore() {

            }
        });
    }

    private void restart(){
        currentStatus.reset();
        lastRecordStatus.reset();
        setText();
        mGamePanelView.restart(currentStatus);
    }

    private void retry(){
        currentStatus = lastRecordStatus.copy();
        setText();
        mGamePanelView.restart(currentStatus);
    }

    /**
     * 更新界面数值
     */
    private void setText(){
        mGameLevel.setValue(currentStatus.getLevel());
        mGameScore.setText("分数："+currentStatus.getScore());
    }

    /**
     * 保存当前状态为最新状态，后面retry会回到这个状态
     */
    private void saveStatus(){
        lastRecordStatus = currentStatus.copy();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_cache:
                saveStatus();
                Toast.makeText(this, "以保存当前状态", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.destroy();
        mPlayer = null;
        saveData();
    }
}
