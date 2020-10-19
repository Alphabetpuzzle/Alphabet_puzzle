package com.example.mypuzzle;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
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


        int flag1=0;//1表示已经开始游戏
        int now9=0;
        int[] pbegin=new int[]{7,4,6,3,5,2,1,8,0};
        //int[] gbegin=new int[]{6,3,5,2,4,1,0,7,8};
        int[] anslist=new int[1000];
        int runflag=0;//是否跑过eightpuggle


        ImageButton ib00,ib01,ib02,ib10,ib11,ib12,ib20,ib21,ib22;
        ImageButton restartBtn;
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
        private int blankImgid = R.id.ib_02x02;

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


                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_gameai);
                initView();

//        打乱碎片的函数
                disruptRandom();
        }

        //  随机打乱数组当中元素，以不规则的形式进行图片显示
        private void disruptRandom() {
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

        }
        //  交换数组指定角标上的数据
        private void swap(int rand1, int rand2) {
                int temp = imageIndex[rand1];
                imageIndex[rand1] = imageIndex[rand2];
                imageIndex[rand2] = temp;
        }

        /* 初始化控件*/
        private void initView() {
                ib00 = findViewById(R.id.ib_00x00);
                ib01 = findViewById(R.id.ib_00x01);
                ib02 = findViewById(R.id.ib_00x02);
                ib10 = findViewById(R.id.ib_01x00);
                ib11 = findViewById(R.id.ib_01x01);
                ib12 = findViewById(R.id.ib_01x02);
                ib20 = findViewById(R.id.ib_02x00);
                ib21 = findViewById(R.id.ib_02x01);
                ib22 = findViewById(R.id.ib_02x02);
                timeTv = findViewById(R.id.tv_time);
                restartBtn = findViewById(R.id.btn_restart);
                stepTv=findViewById(R.id.tv_step);
        }

        public void gameshow(int id) {
//        九个按钮执行的点击事件的逻辑应该是相同的，如果有空格在周围，可以改变图片显示的位置，否则点击事件不响应
                switch (id) {
                        case 0:
                                move(R.id.ib_00x00,0);
                                break;
                        case 1:
                                move(R.id.ib_00x01,1);
                                break;
                        case 2:
                                move(R.id.ib_00x02,2);
                                break;
                        case 3:
                                move(R.id.ib_01x00,3);
                                break;
                        case 4:
                                move(R.id.ib_01x01,4);
                                break;
                        case 5:
                                move(R.id.ib_01x02,5);
                                break;
                        case 6:
                                move(R.id.ib_02x00,6);
                                break;
                        case 7:
                                move(R.id.ib_02x01,7);
                                break;
                        case 8:
                                move(R.id.ib_02x02,8);
                                break;
                }
        }
        /*表示移动指定位置的按钮的函数，将图片和空白区域进行交换*/
        private void move(int imagebuttonId, int site) {
//            判断选中的图片在第几行
                int sitex = site / imageX;
                int sitey = site % imageY; //第几列
//        获取空白区域的坐标
                int blankx = blankSwap / imageX;
                int blanky = blankSwap % imageY;
//        可以移动的条件有两个
//        1.在同一行，列数相减，绝对值为1，可移动   2.在同一列，行数相减，绝对值为1，可以移动
                int x = Math.abs(sitex-blankx);
                int y = Math.abs(sitey-blanky);
                if ((x==0&&y==1)||(y==0&&x==1)){
//            通过id，查找到这个可以移动的按钮
                        ImageButton clickButton = findViewById(imagebuttonId);
                        clickButton.setVisibility(View.INVISIBLE);
//            查找到空白区域的按钮
                        ImageButton blankButton = findViewById(blankImgid);
//            将空白区域的按钮设置图片
                        blankButton.setImageResource(image[imageIndex[site]]);
//            移动之前是不可见的，移动之后，将控件设置为可见
                        blankButton.setVisibility(VISIBLE);
//            将改变角标的过程记录到存储图片位置数组当中
                        swap(site,blankSwap);
//            新的空白区域位置更新等于传入的点击按钮的位置
                        blankSwap = site;
                        blankImgid = imagebuttonId;
                        scount++;
                        ib00.setImageResource(image[imageIndex[0]]);
                        ib01.setImageResource(image[imageIndex[1]]);
                        ib02.setImageResource(image[imageIndex[2]]);
                        ib10.setImageResource(image[imageIndex[3]]);
                        ib11.setImageResource(image[imageIndex[4]]);
                        ib12.setImageResource(image[imageIndex[5]]);
                        ib20.setImageResource(image[imageIndex[6]]);
                        ib21.setImageResource(image[imageIndex[7]]);
                        ib22.setImageResource(image[imageIndex[8]]);
                        judgeGameOver();
                }
//              判断本次移动完成后，是否完成了拼图游戏
        }

        /* 判断拼图是否成功*/
        private void judgeGameOver() {
                boolean loop = true;   //定义标志位
                for (int i = 0; i < imageIndex.length; i++) {
                        if (imageIndex[i]!=i) {
                                loop = false;
                                break;
                        }
                }
                if (loop) {
//            拼图成功了
//            停止计时
                        handler.removeMessages(1);
//            拼图成功后，禁止玩家继续移动按钮
                        ib22.setImageResource(image[8]);
                        ib22.setVisibility(VISIBLE);
//            弹出提示用户成功的对话框
                        scount--;
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("AI拼图成功！用的时间为:"+df.format((double)time/10)+"秒"+"步数为:"+scount+"步")
                                .setPositiveButton("确认",null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                }
        }
        int isrun=0;
        /* 重新开始按钮的点击事件*/
        public void restart(View view) {
                if(isrun==0)
                {
                       // 将状态还原
                        restore();
//       将拼图重新打乱
                        //disruptRandom();
                        handler.removeMessages(1);
//        将时间重新归0，并且重新开始计时
                        time = 0;
                        scount=0;
                        flag1=0;
                        timeTv.setText("时间:"+time+" 秒");
                        stepTv.setText("步数:"+scount+"步");
                        start();
                        eightpuzzless();
                        final int[] count = {1};
                        final Handler handler = new Handler( );
                        Runnable runnable;
                        runnable = new Runnable( ) {
                                public void run ( ) {
                                        gameshow(anslist[count[0]]);
                                        count[0]++;
                                        handler.postDelayed(this,500);
                                        //postDelayed(this,2000)方法安排一个Runnable对象到主线程队列中
                                }
                        };
                        handler.postDelayed(runnable,1000);
                        if(count[0] >=anslist.length)
                        {
                                handler.removeCallbacks(runnable);
                        }
                        isrun=1;
                        restartBtn.setImageResource(R.mipmap.back);
                }
              else
                {
                        Intent intent = new Intent();
                        intent.setClass(Gameai.this,Game.class);
                        startActivity(intent);
                }

        }

        private void restore() {
//        还原被点击的图片按钮变成初始化的模样
                ImageButton clickBtn = findViewById(blankImgid);
                clickBtn.setVisibility(VISIBLE);
//        默认隐藏第九章图片
                ImageButton blankBtn = findViewById(R.id.ib_02x02);
                blankBtn.setVisibility(View.INVISIBLE);
                blankImgid = R.id.ib_02x02;   //初始化空白区域的按钮id
                blankSwap = imgCount - 1;
        }

        public void start() {
                blankSwap = 8;
                handler.sendEmptyMessageDelayed(1, 100);
        }


        @SuppressWarnings("unchecked")
        public  void eightpuzzless() {
                //定义open表
                ArrayList<EightPuzzle> open = new ArrayList<EightPuzzle>();
                ArrayList<EightPuzzle> close = new ArrayList<EightPuzzle>();
                EightPuzzle start = new EightPuzzle();
                EightPuzzle target = new EightPuzzle();
                int stnum[] = {7, 4, 6, 3, 5, 2, 1, 8, 0};
                int tanum[] = {1, 2, 3, 4, 5, 6, 7, 8, 0};
                for (int k = 0; k < 9; k++) {
                        stnum[k] = (imageIndex[k]+1)%9;
                }

                start.setNum(stnum);
                target.setNum(tanum);
                if (start.isSolvable(target)) {
                        //初始化初始状态
                        start.init(target);
                        open.add(start);
                        while (open.isEmpty() == false) {
                                Collections.sort(open);            //按照evaluation的值排序
                                EightPuzzle best = open.get(0);    //从open表中取出最小估值的状态并移出open表
                                open.remove(0);
                                close.add(best);

                                if (best.isTarget(target)) {
                                        //输出
                                        anslist = best.printRoute();
                                        return ;
                                }

                                int move;
                                //由best状态进行扩展并加入到open表中
                                //0的位置上移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if (best.isMoveUp()) {//可以上移的话
                                        move = 0;//上移标记
                                        EightPuzzle up = best.moveUp(move);//best的一个子状态
                                        up.operation(open, close, best, target);
                                }
                                //0的位置下移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if (best.isMoveDown()) {
                                        move = 1;
                                        EightPuzzle down = best.moveUp(move);
                                        down.operation(open, close, best, target);
                                }
                                //0的位置左移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if (best.isMoveLeft()) {
                                        move = 2;
                                        EightPuzzle left = best.moveUp(move);
                                        left.operation(open, close, best, target);
                                }
                                //0的位置右移之后状态不在close和open中设定best为其父状态，并初始化f(n)估值函数
                                if (best.isMoveRight()) {
                                        move = 3;
                                        EightPuzzle right = best.moveUp(move);
                                        right.operation(open, close, best, target);
                                }

                        }
                } else {
                        //"目标状态不可达";
                }

                return ;
        }
}