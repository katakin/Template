package ru.katakin.sample.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ru.katakin.sample.R;

public class ServerRequestProcessingLayout{
    private View process_layout;
    private WindowManager wm;
    private WindowManager.LayoutParams params;
    private boolean visible = false;

    public ServerRequestProcessingLayout(String msg, Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        process_layout = inflater.inflate(R.layout.server_request_processing, null);
        if (!TextUtils.isEmpty(msg)) {
            TextView text = (TextView) process_layout.findViewById(R.id.request_processing_text);
            text.setText(msg);
        }

        wm = ((BaseActivity) context).getWindowManager();
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.TRANSLUCENT;
    }

    public void showProcessLayout(){
        wm.addView(process_layout, params);
        visible = true;
    }

    public void hideProcessLayout(){
        if (process_layout != null && visible){
            wm.removeView(process_layout);
            visible = false;
        }
    }

    public boolean isVisible(){
        return visible;
    }
}