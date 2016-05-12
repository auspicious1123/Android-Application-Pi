package cmu.rrg.pi.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cmu.rrg.pi.R;
import cmu.rrg.pi.model.Meetup;
import cmu.rrg.pi.model.Session;
import cmu.rrg.pi.model.User;
import cmu.rrg.pi.util.Storage;

public class MeetupActivity extends AppCompatActivity {

    private static final String site = "http://o6m9khyns.bkt.clouddn.com/";

    private Bundle bundle;
    private Meetup meetup;
    private User user;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup);

        Storage.bitmap = null;

        bundle = this.getIntent().getExtras();
        meetup = (Meetup)bundle.getSerializable("meetup");

        user = Session.loginUser;

        Log.i("Meetup avatar:", meetup.getAvatar());
        Log.i("Latitude in Meetup", Double.toString(meetup.getLatitude()));
        Log.i("Longitude in Meetup", Double.toString(meetup.getLongitude()));

        TextView meetupName = (TextView) findViewById(R.id.meetupNameTextView);
        String meetupNameStr = meetup.getName();
        meetupName.setText(meetupNameStr + "\n");

        String meetupContentStr = meetup.getContent();
        TextView meetupContent = (TextView) findViewById(R.id.meetupContentTextView);
        meetupContent.setText("Content: " + meetupContentStr + "\n");

        //TextView meetupStatus = (TextView) findViewById(R.id.meetupStatusTextView);
        //String meetupStatusStr = "";
        //meetupStatus.setText("Meetup Status: " + meetupStatusStr + "\n");

        TextView meetupTime = (TextView) findViewById(R.id.meetupTimeTextView);
        String meetupTimeStr = meetup.getStartTime();
        meetupTime.setText("Start time: " + meetupTimeStr + "\n");

        TextView locationView = (TextView) findViewById(R.id.locationClick);
        String locationViewStr = meetup.getAddress();
        locationView.setText("Address: " + locationViewStr);
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetupActivity.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("meetup", meetup);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView meetupCategoryTextView = (TextView) findViewById(R.id.meetupCategoryTextView);
        String meetupTextViewStr = meetup.getCategory();
        meetupCategoryTextView.setText("Category: " + meetupTextViewStr);

        final TextView currentPersonNum = (TextView) findViewById(R.id.meetupPersonNum);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                meetup.getMembers();
                return null;
            }

            @Override
            protected void onPostExecute(Void reslst) {
                int personNum = meetup.getCount();
                currentPersonNum.setText("Current Signup: " + personNum);
            }
        }.execute();

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageUrl = null;
                try {
                    imageUrl = new URL(site + meetup.getAvatar());

                    Log.i("image url", site + meetup.getAvatar());

                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void reslst){
                final ImageView meetupImageView = (ImageView) findViewById(R.id.meetupImageView);

                meetupImageView.setImageBitmap(bitmap);
            }
        }.execute();



        final Button meetupJoin = (Button) findViewById(R.id.meetupJoinButton);

        meetupJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new AlertDialog.Builder(MeetupActivity.this).setTitle("Sign up Meetup").setMessage("Do you make sure to sign this meetup?").setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if(meetup.userPartice(user)){
                                    showToast("You have signuped this Meetup");
                                }else{
                                    new AsyncTask<Void, Void, Void>(){
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            meetup.addMember(user.getId());
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void reslst) {
                                            int personNum = meetup.getCount();
                                            currentPersonNum.setText("Current Signup: " + personNum);
                                        }
                                    }.execute();
                                }
                            }
                        }).create();
                dialog.show();

            }
        });

        Button postNewActivity = (Button) findViewById(R.id.rightTitleButton);
        postNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PostNewMeetupActivity.class);
            }
        });

        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText("Meetup");

        Button home = (Button) findViewById(R.id.leftTitleButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(MeetupListActivity.class);
            }
        });

    }
    private void toActivity(Class<?> target){
        Intent intent = new Intent(this, target);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MeetupActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
