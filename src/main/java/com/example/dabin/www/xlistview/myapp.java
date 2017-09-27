package com.example.dabin.www.xlistview;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Dabin on 2017/9/26.
 */

public class myapp  extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration aDefault = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(aDefault);
    }
}
