package com.kevinhuang.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayTeamImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView mImgView;

    public DisplayTeamImageTask(ImageView imgView) {
        mImgView = imgView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        InputStream is = null;
        Bitmap bmp = null;

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
        }
        catch (Exception e) {
            Log.e("DisplayImageTask", e.toString());
        }
        finally {
            try {
                if (is != null)
                    is.close();
            }
            catch (Exception e) {
                Log.e("DisplayImageTask", "Finally: " + e.toString());
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                return bmp;
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);

        if (bmp != null)
            mImgView.setImageBitmap(bmp);
    }
}