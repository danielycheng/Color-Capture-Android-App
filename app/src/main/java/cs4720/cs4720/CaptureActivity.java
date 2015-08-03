package cs4720.cs4720;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by Jonny on 11/13/14.
 */
public class CaptureActivity extends Activity {

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;

    private static final String access_token = "2885310448-2v4oxjqUgk08ILl1i0NX3kYhSw8amgn1cdofKYA";
    private static final String access_token_secret = "87z0lqr2tzu1zKB79UvGesTs6MOMuc2X7dcS5ElI4Nihs";
    private static final String consumer_key = "4EP92u9bMmASYndMzylVGoT7R";
    private static final String consumer_secret = "tpoAUHehUJLBsIA0FiIL6t5CZhSMl730Vhb7mOKImBXvovRORm";

    Uri fileUri = null;
    ImageView photoImage = null;

    private Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false)
                .setOAuthConsumerKey(consumer_key)
                .setOAuthConsumerSecret(consumer_secret)
                .setOAuthAccessToken(access_token)
                .setOAuthAccessTokenSecret(access_token_secret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();


        photoImage = (ImageView) findViewById(R.id.photo_image);

        Button callCameraButton = (Button) findViewById(R.id.button_callcamera);
        callCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getOutputPhotoFile();
                fileUri = Uri.fromFile(getOutputPhotoFile());
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
            }
        });

    }

    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    // A known bug here! The image should have saved in fileUri
                    Toast.makeText(this, "Image captured!",
                            Toast.LENGTH_SHORT).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Image saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
                showPhoto(photoUri);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showPhoto(Uri photoUri) {
        File imageFile = new File(photoUri.getPath());
        if (imageFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoImage.setImageDrawable(drawable);
        }
    }

    public void rgbcaptureOnClick(View v) {

        ImageView imageView = ((ImageView)v);

        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v1, MotionEvent event) {
//                System.out.println(event.getX() + ", " + event.getY());
                ImageView imageView = ((ImageView)v1);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                int pixel = bitmap.getPixel((int)event.getX(),(int)event.getY());
                System.out.println(event.getX() + ", " + event.getY());
                rgbSetter(pixel);
                return true;
            }
        });
    }

    public String jsonThing = "{\"lights\": [{\"lightId\": 1, \"red\":255,\"green\":255,\"blue\":255, \"intensity\": 0.5}],\"propagate\": true}";
    public int r = 255;
    public int g = 255;
    public int b = 255;

    public void rgbSetter(int pix) {
        int pixel = pix;
        r = Color.red(pixel);
        g = Color.green(pixel);
        b = Color.blue(pixel);

        Toast.makeText(this, "R: " + r + " G: " + g + " B: " + b, Toast.LENGTH_SHORT).show();


//        System.out.println(r + "," + g + "," + b);
    }

    public void rgbsendOnClick(View v) {
        TextView text = (TextView) findViewById(R.id.url);
        String url = text.getText().toString();

        String pi = "{\"lights\": [{\"lightId\": 1, \"red\": ";
        String ka = ",\"green\": ";
        String ch = ",\"blue\": ";
        String u = ", \"intensity\": 0.5}],\"propagate\": true}";
        jsonThing = pi + r + ka + g + ch + b + u;

        String value = jsonThing;
//        System.out.println(jsonThing);
        sendJSONHttp(url, value);
    }

    public void sendJSONHttp(final String url, final String json) {
        Thread thread = new Thread() {
            public void run() {
                Looper.prepare();
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse;

                try {
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new StringEntity(json));
                    httpResponse = httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        };
        thread.start();
    }

    public void tweetColor(View v) throws TwitterException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String tweetThis = "Yay!";

        if (r > g && r > b) {
            tweetThis = "What a hot color! It's got a lot of red in it.";
        }
        if (g > r && g > b) {
            tweetThis = "Feels natural! There's a lot of green.";
        }
        if (b > r && b > g) {
            tweetThis = "Nice cool color! Blue is most prominent.";
        }

        if (r == 255 && b == 255 && g == 255) {
            Toast.makeText(this, "You didn't capture a color yet!", Toast.LENGTH_LONG).show();
        }


        else {
            tweetThis += " [R: " + r + " G: " + g + " B: " + b + "]";
            Toast.makeText(this, "Tweeting: " + tweetThis, Toast.LENGTH_LONG).show();
            twitter.updateStatus(tweetThis);
        }





    }

}

