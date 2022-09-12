package com.wumin.mykeytool;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.io.OutputStream;

public class OutputStr extends OutputStream implements Runnable {
    Handler handler;

    public OutputStr(TextView text) {
        handler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                text.append((CharSequence) msg.obj);
            }
        };
    }

    @Override
    public void write(int i) {
        handler.sendEmptyMessage(i);
        handler.post(this);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        String str = new String(b, off, len);
        Message message = Message.obtain();
        message.obj = str;
        handler.sendMessage(message);
        handler.post(this);
    }

    @Override
    public void run() {
    }

}
