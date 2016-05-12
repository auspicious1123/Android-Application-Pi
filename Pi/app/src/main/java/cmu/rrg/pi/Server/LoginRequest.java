package cmu.rrg.pi.Server;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyihe on 4/10/16.
 */
public class LoginRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL = "http://128.237.207.55:8080/Pi-Server/login";
    private Map<String,String> params;
    private int mStatusCode;

    public LoginRequest(String username,String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL,listener, null);
        params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        Log.i("sendRequest to", LOGIN_REQUEST_URL);
    }

    @Override
    public Map<String,String> getParams() {
        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

}
