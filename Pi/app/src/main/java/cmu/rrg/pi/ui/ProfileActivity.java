package cmu.rrg.pi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cmu.rrg.pi.R;
import cmu.rrg.pi.model.Session;
import cmu.rrg.pi.model.User;
import cmu.rrg.pi.service.SessionService;
import cmu.rrg.pi.util.Storage;

public class ProfileActivity extends AppCompatActivity {

    private Bundle bundle;
    private User user;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Storage.bitmap = null;

        context = this;
        bundle = this.getIntent().getExtras();
        user = (User) bundle.getSerializable("user");

        ImageView profileImageView = (ImageView) findViewById(R.id.profileImage);
        profileImageView.setImageResource(R.drawable.img2);

        TextView userName = (TextView) findViewById(R.id.userNameTextView);
        userName.setText("Username: " + user.getUsername());

        TextView nickName = (TextView) findViewById(R.id.nickNameTextView);
        nickName.setText(user.getName());

        TextView phoneNum = (TextView) findViewById(R.id.phoneNumberTextView);
        phoneNum.setText("Phone Number: " + user.getPhoneNumber());


        Button postNewActivity = (Button) findViewById(R.id.rightTitleButton);
        postNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PostNewMeetupActivity.class);
            }
        });

        Button home = (Button) findViewById(R.id.leftTitleButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(MeetupListActivity.class);
            }
        });

        Button b_mypost = (Button) findViewById(R.id.myMeetups);
        b_mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("category","All");
                toActivity(MeetupListActivity.class);
            }
        });

        Button logOut = (Button) findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionService ss = new SessionService(context);
                ss.deleteSession();
                Log.i("Logout", "logout invokes");
                Session.loginUser = null;
                toActivity(LoginActitity.class);
            }
        });

        ImageButton mainImageButton = (ImageButton) findViewById(R.id.myPostsListImageButton);
        mainImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("category", "All");
                toActivity(MeetupListActivity.class);
            }
        });

        ImageButton nearbyImageButton = (ImageButton) findViewById(R.id.nearbyImageButton);
        nearbyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("category", "Nearby");
                toActivity(MeetupListActivity.class);
            }
        });

        ImageButton categoryImageButton = (ImageButton) findViewById(R.id.categoryImageButton);
        categoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(CategoryActivity.class);
            }
        });

        ImageButton profleImageButton = (ImageButton) findViewById(R.id.profleImageButton);
        profleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(ProfileActivity.class);
            }
        });


    }

    private void toActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
