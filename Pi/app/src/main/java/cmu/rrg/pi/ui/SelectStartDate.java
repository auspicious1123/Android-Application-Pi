package cmu.rrg.pi.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import cmu.rrg.pi.R;

public class SelectStartDate extends AppCompatActivity {

    private DatePicker pickStartDate;
    private Button b_start;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_start_date);

        //System.out.println(this.getIntent().getExtras().getString("meetupName"));

        bundle = this.getIntent().getExtras();
        pickStartDate=(DatePicker)findViewById(R.id.date_pick_start);
        //initial time
        pickStartDate.updateDate(2016, 5, 1);
        b_start=(Button)findViewById(R.id.b_get_start_date);
        OnClicLisers  cl=new OnClicLisers();
        b_start.setOnClickListener(cl);
    }

    class OnClicLisers implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int y=pickStartDate.getYear();
            int m=pickStartDate.getMonth()+1;
            int d=pickStartDate.getDayOfMonth();
            bundle.putString("startDate", m + "-" + d + "-" + y);
            Intent intent =new Intent(SelectStartDate.this,PostNewMeetupActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //System.out.println("y:"+y+" m:"+m+" d:"+d);
        }
    }
    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu, menu);
        return true;
    }*/

}
