package com.example.home.diplom.view.AboutActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.home.diplom.R;

public class AboutActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        String versionName = "";
        int versionCode = -1;
        TextView textView = (TextView) findViewById(R.id.app_info);

        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
            textView.setText(
                    String.format("Created By Tiko Karapetyan \nVersion name = %s \nVersion code = %d",
                            versionName,
                            versionCode)
            );
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
