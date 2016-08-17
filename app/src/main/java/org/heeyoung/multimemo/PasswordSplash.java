package org.heeyoung.multimemo;


        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import org.heeyoung.multimemo.R;

/**
 * Created by user on 16. 5. 25..
 */
public class PasswordSplash extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText searchQuery;
    Button search;
    TextView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_splash);
        setDefault();
    }

    private void setDefault() {//선언부 한 액티비티 넘어갈 때마다 선언
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        search = (Button) findViewById(R.id.searchButton);
        searchQuery = (EditText) findViewById(R.id.searchQuery);
        searchResult = (TextView) findViewById(R.id.result);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = searchQuery.getText().toString().trim();
                if (s.equals("")) {
                    searchQuery.setError("공백 없이 입력해주세요");
                } else {
                    if (sharedPreferences.getAll().containsKey(s)) {
                        searchResult.setText("비밀번호가 일치합니다");
                        Intent intent = new Intent(getApplicationContext(), MultiMemoActivity.class);
                        startActivity(intent);
                    } else{
                        searchResult.setText("비밀번호가 일치하지 않습니다");
                    }
                }

            }
        });
    }
}
