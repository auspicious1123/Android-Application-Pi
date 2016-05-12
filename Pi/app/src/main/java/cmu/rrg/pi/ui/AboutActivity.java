package cmu.rrg.pi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cmu.rrg.pi.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutBackLogin = (View) findViewById(R.id.aboutBackLogin);
        aboutBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoginActitity.class);
            }
        });
    }

    private void toActivity(Class<?> target){
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }
}
