package edu.hcmus.playwithfens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuState extends Activity {

    private TextView txtMenu;
    private Button btnStart;
    private Button btnTutorial;
    private Button btnAbout;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menustate);

        initWork();
    }

    private void initWork() {
        txtMenu = findViewById(R.id.txtMenu);
        btnStart = findViewById(R.id.btnStart);
        btnTutorial = findViewById(R.id.btnTutorial);
        btnAbout = findViewById(R.id.btnAbout);
        btnExit = findViewById(R.id.btnExit);

        txtMenu.setText("MENU");
        btnStart.setText("BẮT ĐẦU");
        btnTutorial.setText("HƯỚNG DẪN");
        btnAbout.setText("THÔNG TIN");
        btnExit.setText("THOÁT");

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bắt đầu tìm người chơi. -> MainActivity.
            }
        });
        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình hướng dẫn.
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình thông tin ứng dụng và nhóm phát triển.
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thoát game.
            }
        });
    }
}
