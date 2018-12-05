package com.example.user.lul;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int max = 5;
    private Button[][] btn = new Button[5][5];
    private int[][] map = new int[5][5];
    private Button btnRE;
    private int x_pre = 0, x_now = 0, y_pre = 0, y_now = 0;
    private int step = 0;
    private boolean endflag = false;
    private boolean bgmflag = true,fxflag = true;
    private Button btnRule;
    private Context context;
    private PopupWindow popupWindow;
    private ConstraintLayout constraintLayout;
    private MediaPlayer bgm, movesound;
    private Button btnset;
    private ImageButton btnbgm;
    private ImageButton btnfx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                String idName = "button" + String.format("%d%d", i, j);
                int resID = getResources().getIdentifier(idName, "id", getPackageName());
                btn[i][j] = (Button) findViewById(resID);
                final int finalI = i;
                final int finalJ = j;
                btn[i][j].setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Knightmove(finalI, finalJ);
                    }
                });
            }
        }
        btnRE = (Button) findViewById(R.id.btnRE);
        btnRE.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                step = 0;
                x_pre = 0;
                x_now = 0;
                y_pre = 0;
                y_now = 0;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        btn[i][j].setBackgroundResource(R.drawable.ice1);
                        btn[i][j].setClickable(true);
                        map[i][j] = 0;
                    }
                }
            }
        });
        btnRule = (Button) findViewById(R.id.btnRule);
        btnRule.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ruletext = "如同西洋棋中騎士的走法\n" +
                        "先直走或橫走2格\n" +
                        "轉90度再走一格\n" +
                        "即黃色提示格子\n" +
                        "不能重複走\n" +
                        "走完格子即能通關\n" +
                        "Have fun!!";
                window(ruletext);
            }
        });
        btnset = (Button) findViewById(R.id.btnset);
        btnset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                setwindow();
            }
        });
        context = getApplicationContext();
        constraintLayout = (ConstraintLayout) findViewById(R.id.layout1);
        try {
            bgm = MediaPlayer.create(this, R.raw.snowdrop);
            bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);
            bgm.setLooping(true);
        }
        catch (IllegalStateException e){}
        try {
            movesound = MediaPlayer.create(this, R.raw.aduio_dump);
            movesound.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        catch (IllegalStateException e){}
    }

    private void Knightmove(int x, int y) {
        x_pre = x_now;
        y_pre = y_now;
        x_now = x;
        y_now = y;
        step++;
        map[x_now][y_now] = step;
        reprint();
        if (fxflag)
            movesound.start();
        //btn[x_now][y_now].setText(Integer.toString(step));
    }

    private void reprint() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                btn[i][j].setClickable(false);
                if (map[i][j] == 0)
                    btn[i][j].setBackgroundResource(R.drawable.ice);
            }
        }
        nextstep();
        btn[x_now][y_now].setBackgroundResource(R.drawable.dumponice);
        int idname = R.drawable.num01;
        if (step > 1)
            btn[x_pre][y_pre].setBackgroundResource(idname + step - 2);
    }

    private void nextstep() {
        endflag = true;
        int btnx, btny;
        btnx = x_now - 2;
        if (btnx > -1) {
            btny = y_now - 1;
            if (btny > -1)
                check(btnx, btny);
            btny = y_now + 1;
            if (btny < max)
                check(btnx, btny);
        }
        btnx = x_now - 1;
        if (btnx > -1) {
            btny = y_now - 2;
            if (btny > -1)
                check(btnx, btny);
            btny = y_now + 2;
            if (btny < max)
                check(btnx, btny);
        }
        btnx = x_now + 1;
        if (btnx < max) {
            btny = y_now - 2;
            if (btny > -1)
                check(btnx, btny);
            btny = y_now + 2;
            if (btny < max)
                check(btnx, btny);
        }
        btnx = x_now + 2;
        if (btnx < max) {
            btny = y_now - 1;
            if (btny > -1)
                check(btnx, btny);
            btny = y_now + 1;
            if (btny < max)
                check(btnx, btny);
        }
        if (endflag)
            endgame();
    }

    private void check(int x, int y) {
        if (map[x][y] != 0)
            return;
        btn[x][y].setBackgroundResource(R.drawable.ice1);
        btn[x][y].setClickable(true);
        endflag = false;
    }
/*
    private void rule() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Rule");
        builder.setMessage("如同西洋棋中騎士的走法\n" +
                "先直走或橫走2格，轉90度再走一格\n" +
                "即黃色提示格子\n" +
                "不能重複走，走完格子即能通關\n" +
                "Have fun!!");
        builder.setPositiveButton("close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        builder.show();
    }*/

    private void window(String text) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                btn[i][j].setClickable(false);
        }
        btnRE.setClickable(false);
        btnRule.setClickable(false);
        btnset.setClickable(false);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View winview = inflater.inflate(R.layout.window,null);
        popupWindow = new PopupWindow(winview, LayoutParams.MATCH_PARENT, 800);
        if(Build.VERSION.SDK_INT>=21)
            popupWindow.setElevation(5.0f);
        RelativeLayout relativeLayout = (RelativeLayout) winview.findViewById(R.id.r1);
        relativeLayout.setBackgroundResource(R.drawable.rule);
        TextView wintext = (TextView) winview.findViewById(R.id.text);
        ImageButton closeButton = (ImageButton) winview.findViewById(R.id.ib_close);
        wintext.setTextColor(Color.BLACK);
        wintext.setTextSize(18);
        wintext.setText(text);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++)
                        if (map[i][j] == 0)
                            btn[i][j].setClickable(true);
                }
                btnRE.setClickable(true);
                btnRule.setClickable(true);
                btnset.setClickable(true);
            }
        });
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER,0,-300);
    }

    private void endwindow(String text) {
        btnRE.setClickable(false);
        btnRule.setClickable(false);
        btnset.setClickable(false);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View winview = inflater.inflate(R.layout.window,null);
        popupWindow = new PopupWindow(winview, LayoutParams.MATCH_PARENT, 800);
        if(Build.VERSION.SDK_INT>=21)
            popupWindow.setElevation(5.0f);
        RelativeLayout relativeLayout = (RelativeLayout) winview.findViewById(R.id.r1);
        relativeLayout.setBackgroundResource(R.drawable.about);
        TextView wintext = (TextView) winview.findViewById(R.id.text);
        ImageButton closeButton = (ImageButton) winview.findViewById(R.id.ib_close);
        wintext.setTextSize(48);
        wintext.setTextColor(Color.BLACK);
        wintext.setText(text);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                btnRE.setClickable(true);
                btnRule.setClickable(true);
                btnset.setClickable(true);
            }
        });
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER,0,-300);
    }

    private void setwindow() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                btn[i][j].setClickable(false);
        }
        btnRE.setClickable(false);
        btnRule.setClickable(false);
        btnset.setClickable(false);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View winview = inflater.inflate(R.layout.setting,null);
        popupWindow = new PopupWindow(winview, LayoutParams.MATCH_PARENT, 900);
        if(Build.VERSION.SDK_INT>=21)
            popupWindow.setElevation(5.0f);
        ImageButton closeButton = (ImageButton) winview.findViewById(R.id.ib_close);
        btnbgm = (ImageButton) winview.findViewById(R.id.btnbgm);
        btnfx = (ImageButton) winview.findViewById(R.id.btnfx);
        btnrepiant();
        btnbgm.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgmflag) {
                    bgmflag = false;
                    bgm.pause();
                }
                else {
                    bgmflag = true;
                    bgm.start();
                }
                btnrepiant();
            }
        });
        btnfx.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fxflag)
                    fxflag = false;
                else
                    fxflag = true;
                btnrepiant();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++)
                        if (map[i][j] == 0)
                            btn[i][j].setClickable(true);
                }
                btnRE.setClickable(true);
                btnRule.setClickable(true);
                btnset.setClickable(true);
            }
        });
        popupWindow.showAtLocation(constraintLayout, Gravity.CENTER,0,-300);
    }

    private  void btnrepiant()
    {
        if (bgmflag)
            btnbgm.setImageResource(R.drawable.musicon);
        else
            btnbgm.setImageResource(R.drawable.musicoff);
        if (fxflag)
            btnfx.setImageResource(R.drawable.soundeffecton);
        else
            btnfx.setImageResource(R.drawable.soundeffectoff);
    }

    private void endgame() {
        if (step == 25)
            endwindow("You Win!");
        else
            endwindow("You Lose!");
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if (bgmflag)
            bgm.start();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        bgm.pause();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        bgm.release();
    }
}

