package com.wumin.mykeytool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import sun.security.tools.KeyTool;

public class MainActivity extends AppCompatActivity {
    EditText input;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text3);
        input = findViewById(R.id.inputText);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> runExec());
        logInit();
        request();
    }

    private void request() {
        XXPermissions with = XXPermissions.with(this);
        with.permission(Permission.MANAGE_EXTERNAL_STORAGE);
        with.request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if (!all) {
                    toast("获取部分权限成功，但部分权限未正常授予");
                }
            }
            @Override
            public void onDenied(List<String> permissions, boolean never) {
                if (never) {
                    toast("被永久拒绝授权，请手动授予存储权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(getApplicationContext(), permissions);
                } else {
                    toast("获取存储权限失败");
                }
            }
        });// 申请单个权限
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void runExec() {
        String str = input.getText().toString();
        String[] arr = str.split(" ");
        textView.setText("");
        button.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                long startTime = 0;
                long endTime;
                try {
                    System.out.println("开始编译");
                    startTime = System.currentTimeMillis();
                    // D8.main(arr);
              KeyTool.main(arr);
                    endTime = System.currentTimeMillis();
                    System.out.println("编译完成");
                } catch (Throwable e) {
                    endTime = System.currentTimeMillis();
                    System.out.println("编译失败\n");
                    e.printStackTrace();
                }
                System.out.println("编译总耗时: " + (endTime - startTime));
                runOnUiThread(() -> button.setEnabled(true));
            }
        }.start();
    }

    private void logInit() {
        PrintStream print = new PrintStream(new OutputStr(textView));
        System.setOut(print);
        System.setErr(print);
    }


}