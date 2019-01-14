package com.example.androidprocess;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnStartThread, btnStopThread;
    private TextView tvCount;
    private boolean mStopLoop;
    private int count = 0;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Thread id: "+Thread.currentThread().getId());

        btnStartThread  = findViewById(R.id.btn_thread_start);
        btnStopThread   = findViewById(R.id.btn_thread_stop);
        tvCount   = findViewById(R.id.tv_count);

        btnStartThread.setOnClickListener(this);
        btnStopThread.setOnClickListener(this);

        handler = new Handler(getApplicationContext().getMainLooper());
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_thread_start:
                mStopLoop = true;


                // We cannot update a view from any separate thread , that's why we need to use Handler and Looper

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mStopLoop){
                            try {
                                Thread.sleep(1000);
                                count++;
                            } catch (InterruptedException e) {
                                Log.i(TAG, e.getMessage());
                            }
                            tvCount.setText(" "+count);
                            Log.i(TAG, "Thread id in while loop: "+Thread.currentThread().getId() +", Count : "+count);
                        }
                    }
                }).start(); */


                // solution
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mStopLoop){
                            try {
                                Thread.sleep(1000);
                                count++;
                            } catch (InterruptedException e) {
                                Log.i(TAG, e.getMessage());
                            }
                            Log.i(TAG, "Thread id in while loop: "+Thread.currentThread().getId() +", Count : "+count);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvCount.setText(" "+count);
                                    Log.i(TAG, "Thread id within handler: "+Thread.currentThread().getId() +", Count : "+count);
                                }
                            });

                            // Also we can done the above code in this way, for that we do't need any Handler instance
                           /* tvCount.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvCount.setText(" "+count);
                                    Log.i(TAG, "Thread id within handler: "+Thread.currentThread().getId() +", Count : "+count);
                                }
                            });*/

                        }
                    }
                }).start();



                break;
            case R.id.btn_thread_stop:
                  mStopLoop = false;
                break;
        }
    }

}
