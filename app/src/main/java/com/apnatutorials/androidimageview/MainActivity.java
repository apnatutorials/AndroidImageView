package com.apnatutorials.androidimageview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static int PICK_IMAGE = 100 ;
    private static String IMAGE_URL = "http://www.apnatutorials.com/img/logo.png" ;
    ProgressDialog progressDialog ;
    ImageView ivMyImageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivMyImageView = (ImageView) findViewById(R.id.ivMyImageView);

        // Initializing progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Wait ! Downloading image");
        progressDialog.setIndeterminate(true);
    }

    /**
     * This method will be called when user click a button
     * @param view
     */
    public void showImageDemo(View view){
        switch (view.getId()){
            // Load image from drawable folder
            case R.id.btnFromDrawable:
                    ivMyImageView.setImageResource(R.drawable.java_logo);
                    ivMyImageView.setImageMatrix(new Matrix());
                    ivMyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                 break;
            // Load image from gallery
            case R.id.btnFromSdCard:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                ivMyImageView.setImageMatrix(new Matrix());
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
            // Load image from url
            case R.id.btnFromUrl:
                ImageDownloaderTask downloaderTask = new ImageDownloaderTask(ivMyImageView) ;
                downloaderTask.execute(IMAGE_URL);
                ivMyImageView.setImageMatrix(new Matrix());
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            case R.id.btnScaleType_FIT_XY:
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                break;

            case R.id.btnScaleType_FIT_START:
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_START);
                break;

            case R.id.btnScaleType_FIT_END:
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_END);
                break;

            case R.id.btnScaleType_FIT_CENTER:
                ivMyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;

            case R.id.btnScaleType_CENTER_CROP:
                ivMyImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;

            case R.id.btnScaleType_CENTER:
                ivMyImageView.setScaleType(ImageView.ScaleType.CENTER);
                break;
            case R.id.btnScaleType_CENTER_INSIDE:
                ivMyImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
            case R.id.btnScaleType_MATRIX:

                ivMyImageView.setScaleType(ImageView.ScaleType.MATRIX);

                BitmapDrawable bd =(BitmapDrawable)  ivMyImageView.getDrawable() ;
                Matrix m = ivMyImageView.getImageMatrix();
                RectF drawableRect = new RectF(0, 0, bd.getBitmap().getWidth(), bd.getBitmap().getHeight());

                RectF viewRect = new RectF(0, 0, ivMyImageView.getWidth(), ivMyImageView.getHeight());
                m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
                m.postRotate((float) 180f, ivMyImageView.getPivotX(), ivMyImageView.getPivotY());
                ivMyImageView.setImageMatrix(m);

                break;
            case R.id.btnAnimate:
                // Animation
                RotateAnimation rotate = new RotateAnimation(0 ,300);
                rotate.setDuration(500);
                ivMyImageView.startAnimation(rotate);
                break;
        }
    }

    @Override
    /**
     * This method is called once user select a image from gallery
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                ivMyImageView.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * private class to download image asynchronouslly
     */
    private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public ImageDownloaderTask(ImageView imageView) {
            this.imageView = imageView;
            progressDialog.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmapFromUrl = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmapFromUrl = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmapFromUrl;
        }
        
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            progressDialog.cancel();
        }
    }
}
