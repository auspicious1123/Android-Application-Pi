package cmu.rrg.pi.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cmu.rrg.pi.R;
import cmu.rrg.pi.Server.RegisterRequest;
import cmu.rrg.pi.model.User;
import cmu.rrg.pi.model.UserADO;

public class RegisterActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etRegName);
        final EditText etUserName = (EditText) findViewById(R.id.etRegUserName);
        final EditText etPassword = (EditText) findViewById(R.id.etRegPassword);
        final EditText etPhone = (EditText) findViewById(R.id.etRegPhone);
        final Button bRegester = (Button) findViewById(R.id.bRegRegester);
        final Button bBack = (Button) findViewById(R.id.bRegBack);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(LoginActitity.class);
            }
        });

        bRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String username = etUserName.getText().toString();
                final String phone = etPhone.getText().toString();
                final String password = etPassword.getText().toString();

                //Log.i("out-username",username);
                if (name==null || username==null || phone==null || password==null
                        || name.equals("") ||name.equals("") || phone.equals("") || password.equals("")) {

                    showToast("please implement information");
                    return;
                } else {
                    user = new User (username,name,phone,password);
                }
                //int statusCode = UserADO.register(user);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int statusCode = UserADO.register(user);
                        Log.i("statusCode",Integer.toString(statusCode));
                        if(statusCode==201) {
                            Intent intent = new Intent(RegisterActivity.this,LoginActitity.class);
                            RegisterActivity.this.startActivity(intent);
                        } else if (statusCode==403){
                            showToast("user exist");

                        }
                    }
                });

                thread.start();
            }
        });

    }
    private void toActivity(Class<?> target){
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(RegisterActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
