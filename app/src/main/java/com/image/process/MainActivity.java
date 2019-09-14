package com.image.process;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button saveBtn,clickImage;
    ViewDialog progress;
    public List<JsonResItem> imagelist = new ArrayList<>();
    ImageView picture;
    TextView name,desig;
    ArrayList<String> imageUrl =new ArrayList<String>();
    Handler handler=null;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    TextView mTextViewCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.lyt);
        saveBtn = findViewById(R.id.button1);
        //clickImage = findViewById(R.id.clickImage);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        picture = findViewById(R.id.picture);
        name = findViewById(R.id.name);
        desig = findViewById(R.id.desig);
        progress = new ViewDialog(this);
        handler = new Handler();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });
        /*clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                process("000000002");
            }
        });*/
    }

    public void showOrder(final String id) {


    }

        private void callApi() {
        progress.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().getImagesList();
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                if(!res.isError()){
                    imagelist = res.getJsonRes();
                    for (int i = 0 ; i < imagelist.size() ; i++){
                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final JsonResItem model = imagelist.get(finalI);
                                name.setText(model.getName());
                                desig.setText(model.getDesignation());
                                Glide.with(MainActivity.this).
                                        load(model.getImageUrl())
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                //ProgressAppGlideModule.forget(url);
                                                //onFinished();
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                //ProgressAppGlideModule.forget(url);
                                                onFinished();
                                                return false;
                                            }

                                            private void onFinished() {
                                                for(int x=0; x<=20000;x++){
                                                    Log.d("forloop",""+x);
                                                }
                                                process(model.getId());
                                            }
                                        })
                                        .into(picture);
                            }
                        }, 2000);



                        /*for(int x=0; x<=200000;x++){
                            Log.d("forloop",""+x);
                        }*/

                        //showOrder(model.getId());
                        //mTimeLeftInMillis = 3 * 1000;
                        //startTimer();
                        /*try {
                            //sleep(10000);
                            TimeUnit.SECONDS.sleep(5);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                    progress.dismiss();
                }else{
                    progress.dismiss();
                    Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progress.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "Internet Issue ! Failed to process your request !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Data Conversion Issue ! Contact to admin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void process(String id) {
            File file = saveBitMap(MainActivity.this, linearLayout, id);    //which view you want to pass that view as parameter
            if (file != null) {
                Log.i("TAG", "Drawing saved to the gallery!");
            } else {
                Log.i("TAG", "Oops! Image could not be saved.");
            }
    }

    private File saveBitMap(Context context, View drawView, String id){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() +File.separator+ System.currentTimeMillis()+"_"+id+".jpg";
        imageUrl.add(filename);
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            linearLayout.setDrawingCacheEnabled(true);
            Bitmap b = linearLayout.getDrawingCache();
                b.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                process("00001");
            }
        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}
