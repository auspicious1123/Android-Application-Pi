package cmu.rrg.pi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cmu.rrg.pi.R;
import cmu.rrg.pi.adapter.EventAdapter;
import cmu.rrg.pi.model.EventLite;
import cmu.rrg.pi.model.Meetup;
import cmu.rrg.pi.model.MeetupADO;
import cmu.rrg.pi.model.MeetupFactory;
import cmu.rrg.pi.util.Storage;

public class MeetupListActivity extends AppCompatActivity {

    private PullToRefreshListView lv;
    private List<EventLite> eventList = new ArrayList<EventLite>();
    int eventNum=0;

    private Bundle bundle = null;
    private String category;
    private MeetupFactory meetupFactory;
    private EventAdapter eventAdapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_list);

        context = this;

        Storage.bitmap = null;

        if(this.getIntent().getExtras()!=null) {
            bundle = this.getIntent().getExtras();
            category = bundle.getString("category");
            if (category==null) {
                category = "All";
            }
            TextView title = (TextView) findViewById(R.id.titleText);
            title.setText(category);

        } else {
            category = "All";
        }
        meetupFactory = new MeetupFactory(category);

        //meetup = bundle.getClass();

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
                bundle.putString("category","All");
                toActivity(MeetupListActivity.class);
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

        getMeetups();

        /*
        EventAdapter adapter = new EventAdapter(MeetupListActivity.this, R.layout.list_item, meetupFactory.getMeetups());
        lv.setAdapter(adapter);
        */

        lv = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_meetup_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meetup mu = meetupFactory.getMeetups().get(position-1);
                //Toast.makeText(MeetupListActivity.this, el.getEventTitle(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MeetupListActivity.this, MeetupActivity.class);
                bundle.putSerializable("meetup",mu);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (refreshView.isShownHeader()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(3000);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void reslst) {
                            updateEvent();
                            // 更行内容，通知 PullToRefresh 刷新结束
                            lv.onRefreshComplete();
                        }
                    }.execute();
                }
                if (refreshView.isShownFooter()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            // 处理刷新任务
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void reslst) {
                            eventNum = eventList.size();
                            eventNum++;
                            Random random = new Random();
                            int max = 8;
                            int min = 1;
                            int imgNum = random.nextInt(max) % (max - min + 1) + min;
                            String imgURL = "img"+Integer.toString(imgNum);
                            int resID = getResources().getIdentifier(imgURL , "drawable", getPackageName());
                            String eventName = "event" + Integer.toString(eventNum);
                            EventLite newEvent = new EventLite(eventName, resID);
                            eventList.add(newEvent);
                            // 更行内容，通知 PullToRefresh 刷新结束
                            lv.onRefreshComplete();
                        }
                    }.execute();
                }
            }
        });

    }

    private void toActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getMeetups() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                meetupFactory = MeetupADO.getMeetups(context, meetupFactory.getCategory());
                Log.i("getMeetups", "start");
                return null;
            }

            @Override
            protected void onPostExecute(Void reslst) {
                eventAdapter = new EventAdapter(MeetupListActivity.this, R.layout.list_item, meetupFactory.getMeetups());
                lv = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_meetup_list);
                lv.setAdapter(eventAdapter);

                Log.i("getMeetups", "post");
            }
        }.execute();
    }

    private void updateEvent() {
        eventList.clear();
    }

}
