package com.zacck.androidguessthecelebrity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView mCelebImage;
    Button bt0,bt1,bt2,bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up UI connect it to code
        mCelebImage = (ImageView)findViewById(R.id.celebImage);
        bt0 = (Button)findViewById(R.id.bt0);
        bt1 = (Button)findViewById(R.id.bt1);
        bt2 = (Button)findViewById(R.id.bt2);
        bt3 = (Button)findViewById(R.id.bt3);

        String result = null;
        DownloadImageTask mDownloadImageTask = new DownloadImageTask();
        try {
            result = mDownloadImageTask.execute("http://www.posh24.com/celebrities").get();
            Log.i("Url contents are: " , result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void celebChosen(View view)
    {

    }

    public class DownloadImageTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL mURL;

            //lets create a connection
            HttpURLConnection mUrlConnection = null;

            try
            {
                //convert the string passed in into a url
                mURL = new URL(urls[0]);
                mUrlConnection = (HttpURLConnection)mURL.openConnection();
                InputStream ImageStream = mUrlConnection.getInputStream();
                //make a reader
                InputStreamReader mInputReader = new InputStreamReader(ImageStream);

                int data = mInputReader.read();
                while(data != -1)
                {
                    char currentCharacter = (char)data;
                    result += currentCharacter;
                    data = mInputReader.read();
                    return result;
                }

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
