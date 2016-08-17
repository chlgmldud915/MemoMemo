package org.heeyoung.multimemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by user on 16. 8. 7..
 */
public class SplashActivity extends Activity {

//    SharedPreferences isOn;
    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        setting=getSharedPreferences("setting",0);
//        setting.getString("onoff","");
        String isON = setting.getString("onoff", "");
        Toast.makeText(this,isON,Toast.LENGTH_LONG).show();
        if(isON.equals("on"))
        {
            startActivity(new Intent(this, PasswordSplash.class));
            finish();
        } else{
            startActivity(new Intent(this, MultiMemoActivity.class));
            finish();
        }

    }
}
// if (setting.getBoolean("onoff",false))
//    if (setting!=null)