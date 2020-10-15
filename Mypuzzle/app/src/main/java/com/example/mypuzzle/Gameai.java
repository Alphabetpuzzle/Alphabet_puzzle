package com.example.mypuzzle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;


import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.example.mypuzzle.History;

import static android.view.View.VISIBLE;

public class Gameai extends AppCompatActivity {


        private History.MyDbHElper myDbHElper;
        private SQLiteDatabase db;
        private ContentValues values;
        private  static final String mTableName = "contacts";
        int flag1=0;//1表示已经开始游戏
        int[] puzzle=new int[]{1,2,3,4,5,6,7,8,9};

        ImageButton ib00,ib01,ib02,ib10,ib11,ib12,ib20,ib21,ib22;
        Button restartBtn,startBtn;
        TextView timeTv;
        TextView stepTv;
        TextView tv_show;
        DecimalFormat df = new DecimalFormat("#.0");

        //    每行的图片个数
        private int imageX = 3;
        private int imageY = 3;  //每列的图片的个数

        //    图片的总数目
        private int imgCount = imageX*imageY;
        //    空白区域的位置
        private int blankSwap = imgCount-1;
        //    初始化空白区域的按钮id
        private int blankImgid = R.id.pt_ib_02x02;

        //    定义计数时间的变量
        int time = 0;
        int scount=0;
        //    存放碎片的数组，便于进行统一的管理
        private int[]image = {R.mipmap.img0,R.mipmap.img1,R.mipmap.img2,
                R.mipmap.img3,R.mipmap.img4,R.mipmap.img5,
                R.mipmap.img6,R.mipmap.img7,R.mipmap.img8};
        //  声明一个图片数组的下标数组，随机排列这个数组
        private int[]imageIndex = new int[image.length];
        Handler handler = new Handler(){
                @SuppressLint("HandlerLeak")
                @Override
                public void handleMessage(Message msg) {
                        if (msg.what==1) {
                                time++;
                                timeTv.setText("时间: "+df.format((double)time/10)+" 秒");
                                stepTv.setText("步数: "+scount+"步");
                                handler.sendEmptyMessageDelayed(1,100);
                        }
                }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {

                myDbHElper =new History.MyDbHElper(this);

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_gameai);
                initView();
//        打乱碎片的函数
                new Thread() {
                        @Override
                        public void run() {
                                super.run();
                                try {
                                        Thread.sleep(5000);//休眠5秒让用户看原图
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                }.start();
                disruptRandom();


        }

        //  随机打乱数组当中元素，以不规则的形式进行图片显示
        private void disruptRandom() {
                for(int i = 0;i <9;i++) {
                        puzzle[i] = i+1;
                }
                for (int i = 0; i < imageIndex.length; i++) {
                        imageIndex[i] = i;
                }
//        规定20次，随机选择两个角标对应的值进行交换
                int rand1,rand2;
                for (int j = 0; j < 20; j++) {
//            随机生成第一个角标   生成0-8之间的随机数
                        rand1 = (int)(Math.random()*(imageIndex.length-1));
//            第二次随机生成的角标，不能和第一次随机生成的角标相同，如果相同，就不方便交换了
                        do {
                                rand2 = (int)(Math.random()*(imageIndex.length-1));
                                if (rand1!=rand2) {
                                        break;
                                }
                        }while (true);
//            交换两个角标上对应的值
                        int tempint=puzzle[rand1];
                        puzzle[rand1]=puzzle[rand2];
                        puzzle[rand2]=tempint;
                        swap(rand1,rand2);
                }
//        随机排列到指定的控件上
                ib00.setImageResource(image[imageIndex[0]]);
                ib01.setImageResource(image[imageIndex[1]]);
                ib02.setImageResource(image[imageIndex[2]]);
                ib10.setImageResource(image[imageIndex[3]]);
                ib11.setImageResource(image[imageIndex[4]]);
                ib12.setImageResource(image[imageIndex[5]]);
                ib20.setImageResource(image[imageIndex[6]]);
                ib21.setImageResource(image[imageIndex[7]]);
                ib22.setImageResource(image[imageIndex[8]]);
                String show="";
                for(int i=0;i<9;i++)
                {
                        show+=String.valueOf(puzzle[i]);
                }
                tv_show.setText(show);
        }
        //  交换数组指定角标上的数据
        private void swap(int rand1, int rand2) {
                int temp = imageIndex[rand1];
                imageIndex[rand1] = imageIndex[rand2];
                imageIndex[rand2] = temp;
        }

        /* 初始化控件*/
        private void initView() {
                ib00 = findViewById(R.id.pt_ib_00x00);
                ib01 = findViewById(R.id.pt_ib_00x01);
                ib02 = findViewById(R.id.pt_ib_00x02);
                ib10 = findViewById(R.id.pt_ib_01x00);
                ib11 = findViewById(R.id.pt_ib_01x01);
                ib12 = findViewById(R.id.pt_ib_01x02);
                ib20 = findViewById(R.id.pt_ib_02x00);
                ib21 = findViewById(R.id.pt_ib_02x01);
                ib22 = findViewById(R.id.pt_ib_02x02);
                timeTv = findViewById(R.id.pt_tv_time);
                restartBtn = findViewById(R.id.pt_btn_restart);
                stepTv=findViewById(R.id.pt_tv_step);
                tv_show = findViewById(R.id.tv_show);

        }





        /* 重新开始按钮的点击事件*/
        public void restart(View view) {
//        将状态还原
                restore();
//       将拼图重新打乱
                disruptRandom();
                handler.removeMessages(1);
//        将时间重新归0，并且重新开始计时
                time = 0;
                scount=0;
                flag1=0;
                timeTv.setText("时间:"+time+"秒");
                stepTv.setText("步数:"+scount+"步");
        }

        private void restore() {
                //      拼图游戏重新开始，允许完成移动碎片按钮
//        还原被点击的图片按钮变成初始化的模样
                ImageButton clickBtn = findViewById(blankImgid);
                clickBtn.setVisibility(VISIBLE);
//        默认隐藏第九章图片
                ImageButton blankBtn = findViewById(R.id.pt_ib_02x02);
                blankBtn.setVisibility(View.INVISIBLE);
                blankImgid = R.id.pt_ib_02x02;   //初始化空白区域的按钮id
                blankSwap = imgCount - 1;
        }



        @SuppressWarnings("unchecked")
        public static void main(String args[]){
                //定义open表
                ArrayList<EightPuzzle> open = new ArrayList<EightPuzzle>();
                ArrayList<EightPuzzle> close = new ArrayList<EightPuzzle>();
                EightPuzzle start = new EightPuzzle();
                EightPuzzle target = new EightPuzzle();

                int stnum[] = {7,4,6,3,5,2,1,8,0};
                int tanum[] = {1,2,3,4,5,6,7,8,0};

                start.setNum(stnum);
                target.setNum(tanum);
                if(start.isSolvable(target)){
                        //初始化初始状态
                        start.init(target);
                        open.add(start);
                        while(open.isEmpty() == false){
                                Collections.sort(open);            //按照evaluation的值排序
                                EightPuzzle best = open.get(0);    //从open表中取出最小估值的状态并移出open表
                                open.remove(0);
                                close.add(best);

                                if(best.isTarget(target)){
                                        //输出
                                        ArrayList list=new ArrayList();
                                       list= best.printRoute();

                                        for (int i = 0; i < list.size(); i++) {
                                                System.out.println(list.get(i));
                                        }

                                }

                                int move;
                                //由best状态进行扩展并加入到open表中
                                //0的位置上移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if(best.isMoveUp()){//可以上移的话
                                        move = 0;//上移标记
                                        EightPuzzle up = best.moveUp(move);//best的一个子状态
                                        up.operation(open, close, best, target);
                                }
                                //0的位置下移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if(best.isMoveDown()){
                                        move = 1;
                                        EightPuzzle down = best.moveUp(move);
                                        down.operation(open, close, best, target);
                                }
                                //0的位置左移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if(best.isMoveLeft()){
                                        move = 2;
                                        EightPuzzle left = best.moveUp(move);
                                        left.operation(open, close, best, target);
                                }
                                //0的位置右移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if(best.isMoveRight()){
                                        move = 3;
                                        EightPuzzle right = best.moveUp(move);
                                        right.operation(open, close, best, target);
                                }

                        }
                }else
                {
                        //"目标状态不可达";
                }

        }
}