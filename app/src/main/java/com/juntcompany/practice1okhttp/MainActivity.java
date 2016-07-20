package com.juntcompany.practice1okhttp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    public static class GetExample {
        OkHttpClient client = new OkHttpClient();

        @TargetApi(Build.VERSION_CODES.KITKAT)
        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }
//    response = example.run("https://raw.github.com/square/okhttp/master/README.md");



    public static class WorkerThread extends Thread{

      Handler handler;
        WorkerThread(Handler handler){
            this.handler = handler;
        }
        String response;
        @Override
        public void run() {
            super.run();
            GetExample example = new GetExample();

            try {
                 response = example.run("https://raw.github.com/square/okhttp/master/README.md");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.what =1;
            msg.obj = response;
            handler.sendMessage(msg);
        }
    }
//    Handler mHandler = new Handler(Looper.getMainLooper());

     TextView textTest;
    WorkerThread thread;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         textTest = (TextView)findViewById(R.id.text_test);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    textTest.setText(msg.obj.toString());
                }
            }
        };
        thread = new WorkerThread(handler);
        thread.start();

    }
}
