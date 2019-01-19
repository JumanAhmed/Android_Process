package com.example.androidprocess;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnStartThread, btnStopThread;
    TextView textViewthreadCount;
    private boolean mStopLoop;

    int count = 0;
    LooperThread looperThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Thread id: "+Thread.currentThread().getId());

        btnStartThread  = findViewById(R.id.btn_thread_start);
        btnStopThread   = findViewById(R.id.btn_thread_stop);
        textViewthreadCount = findViewById(R.id.tvshow);

        btnStartThread.setOnClickListener(this);
        btnStopThread.setOnClickListener(this);

        looperThread=new LooperThread();
        looperThread.start();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_thread_start:
                mStopLoop = true;

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mStopLoop){
                            try{
                                Thread.sleep(1000);
                                count++;
                            }catch (InterruptedException e){
                                Log.i(TAG,e.getMessage());
                            }
                            Log.i(TAG,"Thread id in while loop: "+Thread.currentThread().getId()+", Count : "+count);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textViewthreadCount.setText(" "+count);
                                }
                            });
                        }
                    }
                }).start();*/

                executeOnCustomLooper();

                break;
            case R.id.btn_thread_stop:
                  mStopLoop = false;
                break;
        }
    }



    public void executeOnCustoLooperWithCustomHandler(){

        looperThread.handler.post(new Runnable() {
            @Override
            public void run() {
                while (mStopLoop){
                    try{
                        Thread.sleep(1000);
                        count++;
                        //looperThread.handler.sendMessage(getMessageWithCount(""+count));
                        Log.i(TAG,"Thread id of Runnable posted: "+Thread.currentThread().getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG,"Thread id of runOnUIThread: "+Thread.currentThread().getId()+", Count : "+count);
                                textViewthreadCount.setText(" "+count);
                            }
                        });
                    }catch (InterruptedException exception){
                        Log.i(TAG,"Thread for interrupted");
                    }

                }
            }
        });
    }

    public void executeOnCustomLooper(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (mStopLoop){
                    try{
                        Log.i(TAG,"Thread id of thread that sends message: "+Thread.currentThread().getId());
                        Thread.sleep(1000);
                        count++;
                        Message message=new Message();
                        message.obj=""+count;
                        looperThread.handler.sendMessage(message);
                    }catch (InterruptedException exception){
                        Log.i(TAG,"Thread for interrupted");
                    }

                }
            }
        }).start();

    }


    private Message getMessageWithCount(String count){
        Message message=new Message();
        message.obj=""+count;
        return message;
    }

}
