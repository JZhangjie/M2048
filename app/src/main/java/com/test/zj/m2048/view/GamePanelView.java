package com.test.zj.m2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.test.zj.m2048.data.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2018/4/8.
 */

public class GamePanelView extends GridLayout {

    Callback callback;

    public final static int LEFT =0;
    public final static int UP =1;
    public final static int RIGHT =2;
    public final static int DOWN =3;
    public final static int ANIMATIONTIME=100;

    private int itemCount = 4;
    private GameItemView[][] gameItems;
    private int[][] gameTargetItems;
    private Point[][] gameAnimationItems;
    private List<Point> emptyItems;

    private int gamePanelSize = 425;
    private int itemSize=100;
    private int itemSpace=10;
    private int mLevel = 0;
    private int animationNum = 0;

    private AtomicInteger animationFinishedNum = new AtomicInteger(0);

    private int orientation = LEFT;
    private float startX,startY,offsetX,offsetY;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public GamePanelView(@NonNull Context context) {
        super(context);
        init();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint =new Paint();
        paint.setColor(0xffbbbbbb);
        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){
                float x = i*(itemSize+itemSpace);
                float y = j*(itemSize+itemSpace);
                canvas.drawRect(x,y,x+itemSize,y+itemSize,paint);
            }
        }
        super.onDraw(canvas);
    }

    public void init() {

        emptyItems = new ArrayList<>();
        gamePanelSize = getContext().getResources().getDisplayMetrics().widthPixels;

        itemSize = (gamePanelSize - itemSpace * (itemCount -1)) / itemCount;

        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(gamePanelSize, gamePanelSize);
        l.weight = 1;
        setLayoutParams(l);
        setRowCount(itemCount);
        setColumnCount(itemCount);

        setBackgroundColor(Color.GRAY);

        gameItems = new GameItemView[itemCount][itemCount];

        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){
                GameItemView v  = new GameItemView(getContext());
                v.setValue(0);

                GridLayout.LayoutParams lp = new LayoutParams();
                lp.rightMargin = itemSpace;
                lp.leftMargin = 0;
                lp.topMargin = 0;
                lp.bottomMargin = itemSpace;
                lp.width = itemSize;
                lp.height = itemSize;
                v.setLayoutParams(lp);

                addView(v);
                gameItems[i][j] = v;

            }
        }
        initEvent();
    }

    /***
     * 重新开始游戏，传入二维数组，用于恢复到某一次的成绩
     * @param items null时置零，
     */
    public void restart(GameStatus status){

        int[][] items = status.getItems();
        mLevel = status.getLevel();
        if(items == null){
            for(int i=0;i<itemCount;i++){
                for(int j=0;j<itemCount;j++){

                    gameItems[i][j].setValue(0);
                    gameItems[i][j].setTargetvalue(0);
                    gameItems[i][j].setTargetPosition(-1,-1);
                    gameItems[i][j].setMerge(false);
                }
            }

            getOneItem();
            getOneItem();
        }
        else{
            for(int i=0;i<itemCount;i++){
                for(int j=0;j<itemCount;j++){

                    gameItems[i][j].setValue(items[i][j]);
                    gameItems[i][j].setTargetvalue(items[i][j]);
                    gameItems[i][j].setTargetPosition(-1,-1);
                    gameItems[i][j].setMerge(false);
                }
            }
        }

    }
    /***
     * 统计得分
     */
    public int getGameScore(){
        int gameScore = 0;
        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){
                gameScore = gameScore+gameItems[i][j].getTargetvalue();
            }
        }

        return gameScore;
    }

    /**
     * 判断游戏是否结束
     * @return
     */
    public boolean isFinished(){
        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount-1;j++){

                if(gameItems[i][j].getTargetvalue() == 0 || gameItems[i][j+1].getTargetvalue() == 0 || gameItems[i][j].getTargetvalue() == gameItems[i][j+1].getTargetvalue()){
                    return false;
                }

            }
        }

        for(int j=0;j<itemCount;j++){
            for(int i=0;i<itemCount-1;i++){
                if(gameItems[i][j].getTargetvalue() == 0 || gameItems[i+1][j].getTargetvalue() == 0 || gameItems[i][j].getTargetvalue() == gameItems[i+1][j].getTargetvalue()){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 获取当前状态
     * @return
     */
    public int[][] getCurrentItems(){
        if(gameItems == null)
            return null;

        int[][] items = new int[itemCount][itemCount];


        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){

                items[i][j]=gameItems[i][j].getValue();
            }
        }

        return items;

    }

    /***
     * 获取最大的一个数字作为Level值
     * @return
     */
    public int getLevel(){
        int level = 0;
        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){
                if(level < gameItems[i][j].getTargetvalue())
                    level = gameItems[i][j].getTargetvalue();
            }
        }

        return level;
    }
    /***
     * 初始化面板滑动事件
     */
    private void initEvent(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX)>Math.abs(offsetY)){
                            if(offsetX>5){
                                orientation = RIGHT;
                                swipeRight();
                            }
                            if(offsetX<-5){
                                orientation = LEFT;
                                swipeLeft();
                            }
                        }
                        else
                        {
                            if(offsetY>5){
                                orientation = DOWN;
                                swipeDown();
                            }
                            if(offsetY<-5){

                                orientation = UP;
                                swipeUp();
                            }
                        }

                        startAnimation(ANIMATIONTIME);
                        break;

                }
                return true;
            }
        });
    }

    /**
     * 新增一个item
     * */
    private void getOneItem(){

        emptyItems.clear();

        for(int i=0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){

                if(gameItems[i][j].getValue() ==0){
                    emptyItems.add(new Point(i,j));
                }
            }
        }
        if(emptyItems.size() <=0){
            return;
        }
        Point p = emptyItems.remove((int)(Math.random()*emptyItems.size()));
        //随机产生2或4，生成2的概率大于4
        int value = Math.random()>0.1?2:4;
        gameItems[p.x][p.y].setValue(value);
        gameItems[p.x][p.y].setTargetvalue(value);


    }

    /**
     * 使用targetValue更新GameItemView的value
     * 动画结束后执行
     * */
    private void updateGameItemsView(){

        for(int i = 0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){
                int item = gameItems[i][j].getTargetvalue();
                int value = gameItems[i][j].getValue();
                if(item != value)
                    gameItems[i][j].setValue(item);
                gameItems[i][j].setMerge(false);
                gameItems[i][j].setTargetPosition(-1,-1);
            }
        }

    }

    /***
     * 动画执行完之后执行
     */
    private synchronized void animationEnd(){

        int temp =animationFinishedNum.incrementAndGet();

        if(animationNum == temp){
            updateGameItemsView();
            getOneItem();

            if(this.callback != null){
                this.callback.updateScore(getGameScore());

                boolean finish = isFinished();

                if(finish){
                    this.callback.gameFinished();
                }

                int level = getLevel();
                if(mLevel != level){
                    mLevel = level;
                    this.callback.updateLevel(mLevel);
                }

                this.callback.playTrigger();

            }
        }
    }

    /**
     * 执行动画
     * @param longtime
     */
    private void startAnimation(long longtime){

        animationNum = 0;
        animationFinishedNum.set(0);

        for(int i = 0;i<itemCount;i++){
            for(int j=0;j<itemCount;j++){

                Point p = gameItems[i][j].getTargetPosition();
                if(p != null && p.x !=-1 && p.y!=-1 && (p.x!=i || p.y!=j)){

                    animationNum++;

                    View v = gameItems[i][j];

                    int txnum = p.y - j;
                    int tynum = p.x - i;

                    float tx = txnum*(itemSize+itemSpace);
                    float ty = tynum*(itemSize+itemSpace);

                    TranslateAnimation translateAnimation = new TranslateAnimation(0,tx,0,ty);
                    translateAnimation.setDuration(longtime);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animationEnd();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    gameItems[i][j].setAnimation(translateAnimation);
                    gameItems[i][j].startAnimation(translateAnimation);

                }
            }
        }

    }

    /***
     * 向左滑动
     */
    private void swipeLeft(){

        //从上至下的第一行开始
        for (int i = 0; i < itemCount; i++) {
            //从左至右
            for(int j = 0; j < itemCount-1; j++){

                for(int y = j+1; y < itemCount; y++){
                    if(gameItems[i][y].getTargetvalue()>0){
                        if(gameItems[i][j].getTargetvalue()==0){

                            gameItems[i][j].setTargetvalue(gameItems[i][y].getTargetvalue());

                            gameItems[i][y].setTargetvalue(0);

                            gameItems[i][y].setTargetPosition(i,j);

                            continue;

                        }else if(gameItems[i][j].getTargetvalue() == gameItems[i][y].getTargetvalue() && !gameItems[i][j].isMerge()){

                            gameItems[i][j].setTargetvalue(gameItems[i][y].getTargetvalue()*2);

                            gameItems[i][j].setMerge(true);

                            gameItems[i][y].setTargetvalue(0);

                            gameItems[i][y].setTargetPosition(i,j);

                        }
                        break;
                    }
                }
            }
        }

    }

    private void swipeRight(){

        //从上至下的第一行开始
        for (int i = 0; i < itemCount; i++) {
            //从左至右
            for(int j = itemCount-1; j >0; j--){

                for(int y = j-1; y >=0; y--){

                    if(gameItems[i][y].getTargetvalue()>0){
                        if(gameItems[i][j].getTargetvalue()==0){

                            gameItems[i][j].setTargetvalue(gameItems[i][y].getTargetvalue());

                            gameItems[i][y].setTargetvalue(0);

                            gameItems[i][y].setTargetPosition(i,j);

                            continue;

                        }else if(gameItems[i][j].getTargetvalue() == gameItems[i][y].getTargetvalue() && !gameItems[i][j].isMerge()){

                            gameItems[i][j].setTargetvalue(gameItems[i][y].getTargetvalue()*2);

                            gameItems[i][j].setMerge(true);

                            gameItems[i][y].setTargetvalue(0);

                            gameItems[i][y].setTargetPosition(i,j);

                        }
                        break;
                    }
                }
            }
        }

    }

    private void swipeUp(){

        //左到右
        for(int j = 0; j < itemCount; j++){
            for (int i = 0; i < itemCount-1; i++) {
                //从上到下
                for(int x = i+1; x < itemCount; x++){

                    if(gameItems[x][j].getTargetvalue()>0){
                        if(gameItems[i][j].getTargetvalue()==0){

                            gameItems[i][j].setTargetvalue(gameItems[x][j].getTargetvalue());

                            gameItems[x][j].setTargetvalue(0);

                            gameItems[x][j].setTargetPosition(i,j);

                            continue;

                        }else if(gameItems[i][j].getTargetvalue() == gameItems[x][j].getTargetvalue() && !gameItems[i][j].isMerge()){

                            gameItems[i][j].setTargetvalue(gameItems[x][j].getTargetvalue()*2);

                            gameItems[i][j].setMerge(true);

                            gameItems[x][j].setTargetvalue(0);

                            gameItems[x][j].setTargetPosition(i,j);

                        }
                        break;
                    }

                }
            }
        }

    }

    private void swipeDown(){

        //左到右
        for(int j = 0; j < itemCount; j++){
            //从下到上
            for (int i = itemCount-1; i >0; i--) {

                for(int x = i-1; x >= 0; x--){
                    if(gameItems[x][j].getTargetvalue()>0){
                        if(gameItems[i][j].getTargetvalue()==0){

                            gameItems[i][j].setTargetvalue(gameItems[x][j].getTargetvalue());

                            gameItems[x][j].setTargetvalue(0);

                            gameItems[x][j].setTargetPosition(i,j);

                            continue;

                        }else if(gameItems[i][j].getTargetvalue() == gameItems[x][j].getTargetvalue() && !gameItems[i][j].isMerge()){

                            gameItems[i][j].setTargetvalue(gameItems[x][j].getTargetvalue()*2);

                            gameItems[i][j].setMerge(true);

                            gameItems[x][j].setTargetvalue(0);

                            gameItems[x][j].setTargetPosition(i,j);

                        }
                        break;
                    }
                }
            }
        }

    }


    public static interface Callback{

        void updateScore(int score);

        void gameFinished();

        void updateLevel(int level);

        void playTrigger();

        void playScore();

    }
}
