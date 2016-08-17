package org.heeyoung.multimemo;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import org.heeyoung.multimemo.R;

        import static android.content.SharedPreferences.*;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{
    Button save;
    EditText key;
    SharedPreferences sharedPreferences;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_setting_activity);
        setDefault();
        key.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE); //0
        editor = sharedPreferences.edit();
    }


    private void setDefault() {
        save = (Button) findViewById(R.id.Save);
        save.setOnClickListener(this);
        key=(EditText)findViewById(R.id.editText);
        //        value=(EditText)findViewById(R.id.editText2);

    }

    private void saveToDB() {
        String keyString = key.getText().toString().trim();//왼쪽 오른쪽 공백 자르기
//        String isOn="on";
        sharedPreferences=getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("onoff","on");
        if (keyString.equals("")) {
            Toast.makeText(this, "공백 없이 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            editor = this.editor.putString("setting",keyString);
            editor.commit();
            Toast.makeText(this, keyString + " 저장되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Save:
                saveToDB();
                break;

        }

    }

}

