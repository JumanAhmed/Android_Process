package com.example.androidprocess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnStartThread, btnStopThread;
    private boolean mStopLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Thread id: "+Thread.currentThread().getId());

        btnStartThread  = findViewById(R.id.btn_thread_start);
        btnStopThread   = findViewById(R.id.btn_thread_stop);

        btnStartThread.setOnClickListener(this);
        btnStopThread.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_thread_start:
                mStopLoop = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mStopLoop){
                            Log.i(TAG, "Thread id in while loop: "+Thread.currentThread().getId());
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
