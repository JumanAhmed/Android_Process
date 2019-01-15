package com.example.androidprocess;

import android.os.AsyncTask;
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

    private MyAsyncTask myAsyncTask;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Thread id: " + Thread.currentThread().getId());

        btnStartThread = findViewById(R.id.btn_thread_start);
        btnStopThread = findViewById(R.id.btn_thread_stop);
        tvCount = findViewById(R.id.tv_count);

        btnStartThread.setOnClickListener(this);
        btnStopThread.setOnClickListener(this);

        handler = new Handler(getApplicationContext().getMainLooper());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_thread_start:
                mStopLoop = true;

                // now we use AsyncTask for below task
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mStopLoop) {
                            try {
                                Thread.sleep(1000);
                                count++;
                            } catch (InterruptedException e) {
                                Log.i(TAG, e.getMessage());
                            }
                            Log.i(TAG, "Thread id in while loop: " + Thread.currentThread().getId() + ", Count : " + count);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvCount.setText(" " + count);
                                    Log.i(TAG, "Thread id within handler: " + Thread.currentThread().getId() + ", Count : " + count);
                                }
                            });

                        }
                    }
                }).start();*/

                 myAsyncTask = new MyAsyncTask();
                 myAsyncTask.execute(count);

                break;
            case R.id.btn_thread_stop:
                //mStopLoop = false;
                myAsyncTask.cancel(true);
                break;
        }
    }

    private  class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> { // Generic 1, 2, 3

        private int customCounter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customCounter = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) { // Generic 1 , Params(Integer)
            customCounter = integers[0];
            while (mStopLoop) {
                try {
                    Thread.sleep(1000);
                    customCounter++;
                    publishProgress(customCounter);    // Generic 2, Progress(Integer)
                } catch (InterruptedException e) {
                    Log.i(TAG, e.getMessage());
                }
                Log.i(TAG, "Thread id doInBackground(): " + Thread.currentThread().getId() + ", Count : " + customCounter);
                if (isCancelled()){
                    break;
                }
            }
            return customCounter;         // Generic 3, Result(Integer)
        }

        @Override
        protected void onProgressUpdate(Integer... values) {  // get value from Generic 2
            super.onProgressUpdate(values);
            tvCount.setText(" "+values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {    // get value from doInBackground() return
            super.onPostExecute(integer);
            tvCount.setText(" "+integer);
            count = integer;
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
        }
    }


}