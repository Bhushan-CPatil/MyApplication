package com.image.process;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.multidex.MultiDexApplication;


public class ViewDialog extends MultiDexApplication {

    Context activity;
    Dialog dialog;

    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Context activity) {
        this.activity = activity;
    }

    public void show() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.custom_loading_layout);
        // final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void dismiss() {
        dialog.dismiss();
    }
}