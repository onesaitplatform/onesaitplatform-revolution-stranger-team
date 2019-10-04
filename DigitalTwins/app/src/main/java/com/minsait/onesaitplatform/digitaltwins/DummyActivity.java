package com.minsait.onesaitplatform.digitaltwins;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        new DownloadImageTask().execute();
    }

    class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private Exception exception;

        protected Bitmap doInBackground(Void ...voids) {
            try {
                URL url = null;
                url = new URL("https://lab.onesaitplatform.com/controlpanel/dashboards/generateDashboardImage/66075504-93d1-4d42-a08b-947a1b6ff04a?waittime=20000&height=794&width=1123&fullpage=false&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwcmluY2lwYWwiOiJzdHJhbmdlclRlYW0iLCJjbGllbnRJZCI6Im9uZXNhaXRwbGF0Zm9ybSIsInVzZXJfbmFtZSI6InN0cmFuZ2VyVGVhbSIsInNjb3BlIjpbIm9wZW5pZCJdLCJuYW1lIjoic3RyYW5nZXJUZWFtIiwiZXhwIjoxNTcwMTMyMTMwLCJncmFudFR5cGUiOiJwYXNzd29yZCIsInBhcmFtZXRlcnMiOnsiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcm5hbWUiOiJzdHJhbmdlclRlYW0ifSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERVZFTE9QRVIiXSwianRpIjoiNTFkMzg5NTctYjA0Ny00NzIwLTkzYjAtYWMxZjI0ZTllYzI2IiwiY2xpZW50X2lkIjoib25lc2FpdHBsYXRmb3JtIn0.2n_HGwExLF0oHzkm33wPIkJq6FW9biV_Qm3ZWH-kKTQ");
                URLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("access_token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwcmluY2lwYWwiOiJzdHJhbmdlclRlYW0iLCJjbGllbnRJZCI6Im9uZXNhaXRwbGF0Zm9ybSIsInVzZXJfbmFtZSI6InN0cmFuZ2VyVGVhbSIsInNjb3BlIjpbIm9wZW5pZCJdLCJuYW1lIjoic3RyYW5nZXJUZWFtIiwiZXhwIjoxNTcwMTMyMTMwLCJncmFudFR5cGUiOiJwYXNzd29yZCIsInBhcmFtZXRlcnMiOnsiZ3JhbnRfdHlwZSI6InBhc3N3b3JkIiwidXNlcm5hbWUiOiJzdHJhbmdlclRlYW0ifSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERVZFTE9QRVIiXSwianRpIjoiNTFkMzg5NTctYjA0Ny00NzIwLTkzYjAtYWMxZjI0ZTllYzI2IiwiY2xpZW50X2lkIjoib25lc2FpdHBsYXRmb3JtIn0.2n_HGwExLF0oHzkm33wPIkJq6FW9biV_Qm3ZWH-kKTQ");
                conn.setRequestProperty("token_type", "Bearer");
                conn.connect();
                InputStream input = conn.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView)findViewById(R.id.dummyImageView);
            imageView.setImageBitmap(bitmap);
        }
    }
}
