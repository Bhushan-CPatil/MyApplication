package com.image.process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    //private LinearLayout linearLayout;
    private Button saveBtn,clickImage;
    ViewDialog progress;
    public List<JsonResItem> imagelist = new ArrayList<>();
    public List<ClientItem> clientListArray = new ArrayList<>();
    //ImageView picture;
    //TextView name,desig;
    ArrayList<String> imageUrl =new ArrayList<String>();
    Handler handler=null;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    TextView mTextViewCountDown,date;
    RecyclerView imagelistad;
    DatePickerDialog datePickerDialog;
    AppCompatSpinner clientList;
    String selDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //linearLayout = findViewById(R.id.lyt);
        saveBtn = findViewById(R.id.button1);
        //clickImage = findViewById(R.id.clickImage);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        clientList = findViewById(R.id.clientList);
        date = findViewById(R.id.date);
        String cdate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        selDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        date.setText(cdate);
        //picture = findViewById(R.id.picture);
        imagelistad = findViewById(R.id.imagelistad);
        //name = findViewById(R.id.name);
        //desig = findViewById(R.id.desig);
        progress = new ViewDialog(this);
        handler = new Handler();
        setDocLstAdapter();
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
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                //date.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                                date.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                                selDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        getClientName();
    }

    public void showOrder(final String id) {


    }

        private void callApi() {
        progress.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().getImagesList(selDate);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progress.dismiss();
                if(!res.isError()){
                    imagelist = res.getJsonRes();
                    imagelistad.getAdapter().notifyDataSetChanged();
                    /*for (int i = 0 ; i < imagelist.size() ; i++){
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



                        *//*for(int x=0; x<=200000;x++){
                            Log.d("forloop",""+x);
                        }*//*

                        //showOrder(model.getId());
                        //mTimeLeftInMillis = 3 * 1000;
                        //startTimer();
                        *//*try {
                            //sleep(10000);
                            TimeUnit.SECONDS.sleep(5);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*//*
                    }*/

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

    public File process(String id, LinearLayout linearLayout) {
            File file = saveBitMap(MainActivity.this, linearLayout, id, linearLayout);    //which view you want to pass that view as parameter
            if (file != null) {
                Log.i("TAG", "Drawing saved to the gallery!");
                return file;
            } else {
                Log.i("TAG", "Oops! Image could not be saved.");
            }
            return null;
    }

    private File saveBitMap(Context context, View drawView, String id, LinearLayout linearLayout){
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

    /*private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;

    }*/

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    public void setDocLstAdapter() {
        imagelistad.setNestedScrollingEnabled(false);
        imagelistad.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        imagelistad.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.adapter_images, viewGroup, false);
                                       Holder holder = new Holder(view);
                                       return holder;
                                   }

                                   @Override
                                   public long getItemId(int position) {
                                       return position;
                                   }

                                   @Override
                                   public int getItemViewType(int position) {
                                       return position;
                                   }

                                   @Override
                                   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                                       final Holder myHolder = (Holder) viewHolder;
                                       final JsonResItem model = imagelist.get(i);

                                       //new programming
                                       final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                                       int linearleft =  (int)(Integer.parseInt(model.getLinearMS()) * scale + 0.5f);
                                       int lineartop =  (int)(Integer.parseInt(model.getLinearMT()) * scale + 0.5f);
                                       int nameleft =  (int)(Integer.parseInt(model.getNameMS()) * scale + 0.5f);
                                       int nametop =  (int)(Integer.parseInt(model.getNameMT()) * scale + 0.5f);
                                       int desigleft =  (int)(Integer.parseInt(model.getDesigMS()) * scale + 0.5f);
                                       int desigtop =  (int)(Integer.parseInt(model.getDesigMT()) * scale + 0.5f);
                                       int pichight =  (int)(Integer.parseInt(model.getImageH()) * scale + 0.5f);
                                       int picwidth =  (int)(Integer.parseInt(model.getImageW()) * scale + 0.5f);

                                       ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myHolder.lpic.getLayoutParams();
                                       layoutParams.setMargins(linearleft,lineartop, 0, 0);
                                       myHolder.lpic.setLayoutParams(layoutParams);

                                       ViewGroup.MarginLayoutParams layoutParams2 = (ViewGroup.MarginLayoutParams) myHolder.name.getLayoutParams();
                                       layoutParams2.setMargins(nameleft,nametop, 0, 0);
                                       myHolder.name.setLayoutParams(layoutParams2);
                                       myHolder.name.setTextColor(Color.parseColor(model.getNameFontC()));
                                       myHolder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,Integer.parseInt(model.getNameFontSize()));
                                       if(model.getNameFontFam().equalsIgnoreCase("khandbold")){
                                           Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.khandbold);
                                           myHolder.name.setTypeface(typeface);
                                       }

                                       ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) myHolder.desig.getLayoutParams();
                                       layoutParams3.setMargins(desigleft,desigtop, 0, 0);
                                       myHolder.desig.setLayoutParams(layoutParams3);
                                       myHolder.desig.setTextColor(Color.parseColor(model.getDesigFontC()));
                                       myHolder.desig.setTextSize(TypedValue.COMPLEX_UNIT_SP,Integer.parseInt(model.getDesigFontSize()));
                                       if(model.getDesigFontFam().equalsIgnoreCase("khandbold")){
                                           Typeface typeface = ResourcesCompat.getFont(MainActivity.this, R.font.khandbold);
                                           myHolder.desig.setTypeface(typeface);
                                       }

                                       ViewGroup.LayoutParams params = myHolder.picture.getLayoutParams();
                                       params.height = pichight;
                                       params.width = picwidth;
                                       myHolder.picture.setLayoutParams(params);

                                       myHolder.name.setText(model.getName());
                                       myHolder.desig.setText(model.getDesignation());
                                       Glide.with(MainActivity.this)
                                               .load(model.getPersonImageUrl())
                                               .into(myHolder.picture);

                                       Glide.with(MainActivity.this)
                                               .load(model.getPostImageUrl())
                                               .into(myHolder.background);

                                       //ends here

                                       /*final String[] spltext = model.getImageUrl().split("~");

                                       if(!spltext[1].substring(0,3).equalsIgnoreCase("!@!")){
                                           myHolder.anilbondename.setText(spltext[1]);
                                       }else{
                                           myHolder.anilbondename.setText("");
                                       }

                                       if(!spltext[2].substring(0,3).equalsIgnoreCase("!@!")){
                                           myHolder.anilbondedesig.setText(spltext[2]);
                                       }else{
                                           myHolder.anilbondedesig.setText("");
                                       }

                                       if(!spltext[3].substring(0,3).equalsIgnoreCase("!@!")){
                                           myHolder.uid.setText("phone number - "+spltext[3]);
                                       }else{
                                           myHolder.uid.setText("phone number - Not Present");
                                       }

                                       //myHolder.name.setText(Html.fromHtml("<font color='#FFFFFF' font-family='"+R.font.khandbold+"'>"+model.getName()+"</font>"));
                                       Glide.with(MainActivity.this)
                                               .load(model.getImageUrl())
                                               .into(myHolder.anilbondepicture);

                                       Glide.with(MainActivity.this)
                                               .load(model.getBackground_image_url())
                                               .into(myHolder.anilbondebackground);

                                       myHolder.clickimage.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                                process(model.getId(), myHolder.linearLayout);
                                           }
                                       });

                                       myHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               File file = process(model.getId(), myHolder.linearLayout);
                                               if(file != null){
                                                   if(!spltext[3].substring(0,3).equalsIgnoreCase("!@!")){
                                                       popup(MainActivity.this,spltext[3],file);
                                                   }else{
                                                       sentToWhatsapp(file);
                                                   }
                                               }
                                           }
                                       });*/
                                   }

                                   @Override
                                   public int getItemCount() {
                                       return imagelist.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView uid;
                                       ImageButton clickimage,whatsapp;
                                       //anil bonde
                                       TextView name,desig;
                                       ImageView picture,background;
                                       private LinearLayout linearLayout,lpic;
                                       //vijay nahata
                                       TextView vijaynahataname;
                                       ImageView  vijaynahatapicture, vijaynahatabackground;
                                       LinearLayout vijaynahatalinlay;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           uid = itemView.findViewById(R.id.uid);
                                           clickimage = itemView.findViewById(R.id.clickimage);
                                           whatsapp = itemView.findViewById(R.id.whatsapp);
                                           //anil bonde
                                           desig = itemView.findViewById(R.id.desig);
                                           name = itemView.findViewById(R.id.name);
                                           picture = itemView.findViewById(R.id.picture);
                                           background = itemView.findViewById(R.id.background);
                                           linearLayout = itemView.findViewById(R.id.anilbonde);
                                           lpic = itemView.findViewById(R.id.lpic);
                                           //vijay nahata
                                           /*vijaynahataname = itemView.findViewById(R.id.vijaynahataname);
                                           vijaynahatapicture = itemView.findViewById(R.id.vijaynahatapicture);
                                           vijaynahatabackground = itemView.findViewById(R.id.vijaynahatabackground);
                                           vijaynahatalinlay = itemView.findViewById(R.id.vijaynahata);*/
                                       }
                                   }
                               }
        );
    }

    public void sentToWhatsapp(File imageurl){
        Uri imgUri = Uri.parse(imageurl.getAbsolutePath());
        //Uri imgUri = Uri.parse(pictureFile.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please post it on your Facebook Account and WhatsApp status.");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sentToWhatsappIndividual(String number, File imageurl){
        Uri imgUri = Uri.parse(imageurl.getAbsolutePath());
        //Uri imgUri = Uri.parse(pictureFile.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra("jid", "91" +number+ "@s.whatsapp.net");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Please post it on your Facebook Account and WhatsApp status.");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        whatsappIntent.setType("image/jpeg");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }
    public void popup(final Context context, final String number, final File imageurl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Alert");
        builder.setMessage("Want to send image to whatsapp group or an individual");
        builder.setPositiveButton("Group",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sentToWhatsapp(imageurl);
                    }
                });
        builder.setNeutralButton("Individual",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sentToWhatsappIndividual(number, imageurl);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getClientName() {
        progress.show();
        Call<ClientResponse> call = RetrofitClient
                .getInstance().getApi().getClientNames();
        call.enqueue(new Callback<ClientResponse>() {

            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                ClientResponse res = response.body();

                progress.dismiss();
                if(!res.isError()){
                    List<String> arrayList = new ArrayList<>();
                    clientListArray = res.getClient();
                    for (int j=0;j<clientListArray.size();j++) {
                        arrayList.add(clientListArray.get(j).getClientName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, "Failed to fetch client list !", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
