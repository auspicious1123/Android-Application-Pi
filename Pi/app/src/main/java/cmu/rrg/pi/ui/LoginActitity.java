package cmu.rrg.pi.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cmu.rrg.pi.R;
import cmu.rrg.pi.model.Session;
import cmu.rrg.pi.model.User;
import cmu.rrg.pi.model.UserADO;
import cmu.rrg.pi.service.SessionService;


public class LoginActitity extends AppCompatActivity {

    private TextView TextuserName;
    private TextView Textpassword;

    private Context context;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitity);

        TextuserName = (TextView) findViewById(R.id.TextUsername);
        Textpassword = (TextView) findViewById(R.id.Textpassword);

        context = this;

        final EditText uName = (EditText) findViewById(R.id.editUsername);
        final EditText pWord = (EditText) findViewById(R.id.editPasswd);

        View aboutText = (View) findViewById(R.id.aboutText);
        aboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(AboutActivity.class);
            }
        });

        View tvRegester = (View) findViewById(R.id.tvloginRegesiter);
        tvRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(RegisterActivity.class);
            }
        });



        // Longin -> TimeLineActivity
        Button loginButton = (Button) findViewById(R.id.loginButton);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = uName.getText().toString();
                final String password = pWord.getText().toString();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = UserADO.login(username, password);
                        if (user != null) {
                            showToast("welcome "+user.getUsername()+",have a good time");
                            Intent intent = new Intent(LoginActitity.this, MeetupListActivity.class);
                            Bundle bundle = new Bundle();

                            SessionService ss = new SessionService(context);
                            ss.addSession(user.getUsername(), user.getPassword());

                            Session.loginUser = user;

                            bundle.putSerializable("user", user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            showToast("error username or passward");
                        }

                    }

                });
                thread.start();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName.setText("");
                pWord.setText("");
            }
        });

        SessionService ss = new SessionService(context);
        Cursor cursor = ss.getSession();

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("username"));
            password = cursor.getString(cursor.getColumnIndex("password"));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = UserADO.login(username, password);
                    if (user != null) {
                        Intent intent = new Intent(LoginActitity.this, MeetupListActivity.class);
                        Bundle bundle = new Bundle();

                        Session.loginUser = user;

                        bundle.putSerializable("user", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                    }

                }

            });
            thread.start();
        }

    }

    private void toActivity(Class<?> target){
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(LoginActitity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
