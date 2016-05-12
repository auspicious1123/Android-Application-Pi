package cmu.rrg.pi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cmu.rrg.pi.R;

public class CategoryActivity extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        bundle = this.getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText("Category");

        TextView sportsCategoryTextView = (TextView) findViewById(R.id.sporsTextView);
        sportsCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,MeetupListActivity.class);

                bundle.putString("category", "Sport");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView partyCategoryTextView = (TextView) findViewById(R.id.partyTextView);
        partyCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,MeetupListActivity.class);

                bundle.putString("category","Party");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView religionCategoryTextView = (TextView) findViewById(R.id.religionTextView);
        religionCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,MeetupListActivity.class);

                bundle.putString("category","Religion");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        TextView studyCategoryTextView = (TextView) findViewById(R.id.studyTextView);
        studyCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this,MeetupListActivity.class);

                bundle.putString("category","Study");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        // fragment title and buttom

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
                Intent intent = new Intent(CategoryActivity.this,MeetupListActivity.class);

                bundle.putString("category","All");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        ImageButton mainImageButton = (ImageButton) findViewById(R.id.myPostsListImageButton);
        mainImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("category","All");
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
