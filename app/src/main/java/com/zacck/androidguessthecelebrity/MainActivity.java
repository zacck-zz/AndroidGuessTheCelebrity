package com.zacck.androidguessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView mCelebImage;
    Button bt0,bt1,bt2,bt3;
    ArrayList<String> ImageLinks = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int chosenCeleb = 0;

    //gamify data
    int locationofCorrecAnswer =0;
    String[] answers = new String[4];

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

       //init a result string for use
        String result = null;
        DownloadImageTask mDownloadImageTask = new DownloadImageTask();
        try {
            result = mDownloadImageTask.execute("http://www.posh24.com/celebrities").get();

            String[] splitResult = result.split("<div class=\"sidebarContainer\">");


            Pattern webp = Pattern.compile("<img src=\"(.*?)\"");
            //matcher
            Matcher webm = webp.matcher(splitResult[0]);

            while(webm.find())
            {

                //String[] msplitString = webm.group(1).split("\" alt=\"");

                //System.out.println(Arrays.toString(msplitString));
                ImageLinks.add(webm.group(1));
            }


            Pattern namep = Pattern.compile("alt=\"(.*?)\"");
            //matcher
            Matcher namem = namep.matcher(splitResult[0]);

            while(namem.find())
            {

                //String[] msplitString = webm.group(1).split("\" alt=\"");

                //System.out.println(Arrays.toString(msplitString));
                celebNames.add(namem.group(1));
            }

            generateQuiz();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void generateQuiz()
    {
        try
        {
            //create a random
            Random mRandom = new Random();
            //pick a random celebrity to show pick and name
            chosenCeleb = mRandom.nextInt(celebNames.size());

            ImageDownloader mImageDownloader = new ImageDownloader();
            Bitmap celebImage;
            celebImage = mImageDownloader.execute(ImageLinks.get(chosenCeleb)).get();

            mCelebImage.setImageBitmap(celebImage);

            locationofCorrecAnswer = mRandom.nextInt(4);
            int incorrectanswerlocation;
            //set up the values
            for(int i=0; i<4; i++)
            {
                if(i == locationofCorrecAnswer)
                {
                    answers[i] = celebNames.get(chosenCeleb);
                }
                else
                {
                    incorrectanswerlocation = mRandom.nextInt(celebNames.size());
                    while (incorrectanswerlocation == chosenCeleb)
                    {
                        incorrectanswerlocation = mRandom.nextInt(celebNames.size());
                    }
                    answers[i] = celebNames.get(incorrectanswerlocation);
                }
            }

            bt0.setText(answers[0]);
            bt1.setText(answers[1]);
            bt2.setText(answers[2]);
            bt3.setText(answers[3]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL mImageUrl = new URL(urls[0]);
                HttpURLConnection mImageConnection = (HttpURLConnection)mImageUrl.openConnection();
                mImageConnection.connect();
                InputStream ImageStream = mImageConnection.getInputStream();
                Bitmap mImageBitmap = BitmapFactory.decodeStream(ImageStream);
                return  mImageBitmap;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    //handle button presses
    public void celebChosen(View view)
    {
        if(view.getTag().toString().equals(Integer.toString(locationofCorrecAnswer)))
        {
            alert("Correct");
        }
        else
        {
            alert("Wrong! It was "+celebNames.get(chosenCeleb));

        }
        generateQuiz();
    }

    public void alert(String Message)
    {
        Toast.makeText(getApplicationContext(),Message, Toast.LENGTH_SHORT).show();
    }

    public class DownloadImageTask extends AsyncTask<String, Void, String>
    {
        String result = "";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // Log.i("Url contents are: ", result);
        }

        @Override
        protected String doInBackground(String... urls) {

            URL mURL;

            //lets create a connection
            HttpURLConnection mUrlConnection = null;

            try {
                //convert the string passed in into a url
                mURL = new URL(urls[0]);
                mUrlConnection = (HttpURLConnection) mURL.openConnection();
                InputStream ImageStream = mUrlConnection.getInputStream();
                //make a reader
                InputStreamReader mInputReader = new InputStreamReader(ImageStream);

                int data = mInputReader.read();
                while(data != -1)
                {
                    char currentCharacter = (char)data;
                    result += currentCharacter;
                    data = mInputReader.read();

                }
                /*
                BufferedReader buffer = new BufferedReader(mInputReader);

                String s = "";

                while ((s = buffer.readLine()) != null) {

                    result += s;

                }
                */
                return result;

            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
