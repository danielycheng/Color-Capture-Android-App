package cs4720.cs4720;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void blueButtonOnClick(View v) {
        TextView text = (TextView) findViewById(R.id.url);
        String url = text.getText().toString();
        sendJSONHttp(url, "{\"lights\": [{\"lightId\": 1, \"red\":0,\"green\":0,\"blue\":255, \"intensity\": 0.5}],\"propagate\": true}");
    }

    public void redButtonOnClick(View v) {
        TextView text = (TextView) findViewById(R.id.url);
        String url = text.getText().toString();
        sendJSONHttp(url, "{\"lights\": [{\"lightId\": 1, \"red\":255,\"green\":0,\"blue\":0, \"intensity\": 0.5}],\"propagate\": true}");
    }

    public void greenButtonOnClick(View v) {
        TextView text = (TextView) findViewById(R.id.url);
        String url = text.getText().toString();
        sendJSONHttp(url, "{\"lights\": [{\"lightId\": 1, \"red\":0,\"green\":255,\"blue\":0, \"intensity\": 0.5}],\"propagate\": true}");
    }

    public void colorCaptureOnClick(View v) {
        TextView text = (TextView) findViewById(R.id.url);
        String url = text.getText().toString();
        Intent capturePikachu = new Intent(this, CaptureActivity.class);
        startActivity(capturePikachu);
    }

    public void sendJSONHttp(final String url, final String json) {
        Thread thread = new Thread() {
            public void run() {
                Looper.prepare();
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse;

                try {
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new StringEntity(json.toString()));
                    httpResponse = httpClient.execute(httpPost);




                } catch (IOException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        };
        thread.start();
    }

}
