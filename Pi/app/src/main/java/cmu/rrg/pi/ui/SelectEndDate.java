package cmu.rrg.pi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import cmu.rrg.pi.R;

public class SelectEndDate extends AppCompatActivity {

    private DatePicker pickEndDate;
    private Button b_end;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_end_date);

        System.out.println(this.getIntent().getExtras().getString("meetupName"));

        bundle = this.getIntent().getExtras();
        pickEndDate=(DatePicker)findViewById(R.id.date_pick_end);
        //initial time
        pickEndDate.updateDate(2016, 5, 1);
        b_end=(Button)findViewById(R.id.b_get_end_date);
        OnClicLisers  cl=new OnClicLisers();
        b_end.setOnClickListener(cl);
    }

    class OnClicLisers implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int y=pickEndDate.getYear();
            int m=pickEndDate.getMonth()+1;
            int d=pickEndDate.getDayOfMonth();
            bundle.putString("endDate", m + "-" + d + "-" + y);
            Intent intent =new Intent(SelectEndDate.this,PostNewMeetupActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            //System.out.println("y:"+y+" m:"+m+" d:"+d);
        }
    }

}
