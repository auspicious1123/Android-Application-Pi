package cmu.rrg.pi.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cmu.rrg.pi.R;
import cmu.rrg.pi.model.Meetup;
import cmu.rrg.pi.model.MeetupADO;
import cmu.rrg.pi.model.User;
import cmu.rrg.pi.service.LocationService;
import cmu.rrg.pi.util.Config;
import cmu.rrg.pi.util.Storage;

public class PostNewMeetupActivity extends AppCompatActivity {

    private TextView locationView;


    private double finalLatitude;
    private double finalLongitude;

    private String address;

    private Button b_start;
    private Button b_end;
    private Button b_photo;
    private EditText et_meetupName;
    private EditText et_meetupContent;
    private EditText et_address;

    private TextView tv_startDate;
    private TextView tv_endDate;

    private TextView et_location;

    //photo
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Uri imageUri; //url of an image
    private String filename; //name of an image
    private ImageView showImage;
    private File outputImage;

    private Meetup meetup;

    private Bundle mainbundle;

    private String meetupCategory;

    //spinner
    private static final String[] categoryStringArray = {"Study", "Sport", "Party", "Religion"};
    private ArrayAdapter<String> adapter;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_meetup);


        b_start = (Button) findViewById(R.id.beginTimeButton);
        b_end = (Button) findViewById(R.id.endTimeButton);
        b_photo = (Button) findViewById(R.id.takeBroadcastPhoto);

        et_meetupName = (EditText) findViewById(R.id.meetUpNameInput);

        et_meetupContent = (EditText) findViewById(R.id.meetUpContentTextView);
        et_address = (EditText) findViewById(R.id.locationInput);

        et_location = (TextView) findViewById(R.id.locationView);

        showImage = (ImageView) findViewById(R.id.broadcastPhoto);

        b_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toActivity(SelectStartDate.class);
            }
        });

        b_end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toActivity(SelectEndDate.class);
            }
        });

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        locationView = (TextView) findViewById(R.id.locationView);

        tv_startDate = (TextView) findViewById(R.id.meetUpBeginTimeView);
        tv_endDate = (TextView) findViewById(R.id.meetupEndTimeView);

        //mainbundle = new Bundle();
        mainbundle = this.getIntent().getExtras();

        final Button b_location = (Button) findViewById(R.id.locationButton);
        b_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation(PostNewMeetupActivity.this);
            }
        });

        // finish post meetup event, go back to meetup list.
        Button postMeetUpButton = (Button) findViewById(R.id.finishPostMeetUpButton);
        postMeetUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_meetupName.getText().toString() == null || et_meetupName.getText().toString().equals("")) {
                    showToast("write a name");
                    return;
                }
                if (et_meetupContent.getText().toString() == null || et_meetupContent.getText().toString().equals("")) {
                    showToast("write contents");
                    return;
                }
                if (et_address.getText().toString() == null || et_address.getText().toString().equals("")) {
                    showToast("write a address");
                    return;
                }

                if(filename == null){
                    showToast("Please take a picture");
                }
                //if (location==null) {
                //    showToast("choose location");
                //    return;
                //}

                if (tv_startDate.getText().toString() == null || tv_startDate.getText().toString().equals("")) {
                    showToast("choose start date");
                    return;
                }
                if (tv_endDate.getText().toString() == null || tv_endDate.getText().toString().equals("")) {
                    showToast("choose end date");
                    return;
                }
                meetup = new Meetup(et_meetupName.getText().toString(), et_meetupContent.getText().toString(),
                        meetupCategory, et_address.getText().toString(), finalLatitude, finalLongitude,
                        0, tv_startDate.getText().toString(), tv_endDate.getText().toString());
                meetup.setAvatar(filename);

                Log.i("meet up Avatar", meetup.getAvatar());

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Meetup remoteMeetup = MeetupADO.add(meetup);
                        if (remoteMeetup != null) {
                            showToast("Post new meetup successfully");
                            Intent intent = new Intent(PostNewMeetupActivity.this, MeetupActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("meetup", remoteMeetup);
                            User user = (User) mainbundle.getSerializable("user");
                            bundle.putSerializable("user", user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            showToast("Failed to create new Meetup because of network error.");
                        }
                    }
                });

                thread.start();

                /*
                meetup.setBitmap(bitmap);
                Intent intent = new Intent(PostNewMeetupActivity.this,MeetupActivity.class);
                Bundle bundle = new Bundle();
                User user = (User) mainbundle.getSerializable("user");
                bundle.putSerializable("user",user);
                bundle.putSerializable("meetup", meetup);
                intent.putExtras(bundle);
                startActivity(intent);
                */
            }
        });

        //点击"Photo Button"按钮照相
        b_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片名称 时间命名
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date(System.currentTimeMillis());
                filename = format.format(date);
                //创建File对象用于存储拍照的图片 SD卡根目录
                //File outputImage = new File(Environment.getExternalStorageDirectory(),"test.jpg");
                //存储至DCIM文件夹
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                outputImage = new File(path, filename + ".jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //将File对象转换为Uri并启动照相程序
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
                startActivityForResult(intent, TAKE_PHOTO); //启动照相
                //拍完照startActivityForResult() 结果返回onActivityResult()函数

            }
        });

        Button postNewActivity = (Button) findViewById(R.id.rightTitleButton);
        postNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PostNewMeetupActivity.class);
            }
        });
        postNewActivity.setVisibility(View.INVISIBLE);

        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText("New Meetup");

        Button home = (Button) findViewById(R.id.leftTitleButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostNewMeetupActivity.this, MeetupListActivity.class);
                Bundle bundle = new Bundle();
                User user = (User) mainbundle.getSerializable("user");
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // set spinner
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        //set a ArrayAdapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryStringArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        categorySpinner.setVisibility(View.VISIBLE);

        if (this.getIntent().getExtras() != null) {
            Log.i("test bundle", "test 0");
            mainbundle = this.getIntent().getExtras();
            et_meetupName.setText(mainbundle.getString("meetupName"));
            //et_meetupCategory.setText(mainbundle.getString("meetupCategory"));
            categorySpinner.setSelection(mainbundle.getInt("categoryId"));
            Log.i("category selection input", Integer.toString(mainbundle.getInt("categoryId")));
            et_meetupContent.setText(mainbundle.getString("meetupContent"));
            et_address.setText(mainbundle.getString("address"));
            tv_startDate.setText(mainbundle.getString("startDate"));
            tv_endDate.setText(mainbundle.getString("endDate"));
            locationView.setText(mainbundle.getString("location"));

            finalLatitude = mainbundle.getDouble("latitude");
            finalLongitude = mainbundle.getDouble("longitude");

            if(Storage.bitmap != null) {
                showImage.setImageBitmap(Storage.bitmap);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Storage.bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //showImage.setMaxHeight(1);
                        //showImage.setMaxWidth(1);
                        UploadManager uploadManager = new UploadManager();
                        final String token = Config.getToken();
                        final String key = filename + ".jpg";

                        uploadManager.put(outputImage, key, token, new UpCompletionHandler() {
                            @Override
                            public void complete(String s, ResponseInfo info, JSONObject res) {
                                Log.i("token", token);
                                Log.i("key", key);
                                Log.i("qiniu", key);
                                Log.i("info", info.toString());
                                Log.i("res", res.toString());
                                try {
                                    filename = res.getString("key");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                showImage.setImageBitmap(Storage.bitmap);
                            }
                        }, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void toActivity(Class<?> target) {
        Intent intent = new Intent(this, target);
        mainbundle.putString("meetupName", et_meetupName.getText().toString());
        mainbundle.putString("meetupCategory", meetupCategory);
        mainbundle.putInt("categoryId", categorySpinner.getSelectedItemPosition());
        mainbundle.putString("meetupContent", et_meetupContent.getText().toString());
        mainbundle.putString("startDate", tv_startDate.getText().toString());
        mainbundle.putString("endDate", tv_endDate.getText().toString());
        mainbundle.putString("meetupcontent", et_meetupContent.getText().toString());
        mainbundle.putString("address", et_address.getText().toString());
        mainbundle.putString("location", locationView.getText().toString());

        mainbundle.putDouble("latitude", finalLatitude);
        mainbundle.putDouble("longitude", finalLongitude);

        intent.putExtras(mainbundle);
        startActivity(intent);
    }


    //get location
    private void initLocation(Context context) {
        Location loc = LocationService.getLocation(context);
        final double latitude;
        final double longitude;
        if (loc == null) {
            latitude = 40.4421965;
            longitude = -79.9462458;
        } else {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
        }

        finalLatitude = latitude;
        finalLongitude = longitude;

        Log.i("latitude", Double.toString(latitude));
        Log.i("longitude", Double.toString(longitude));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                address = LocationService.getAddressByGoodle(latitude, longitude);
                return null;
            }

            @Override
            protected void onPostExecute(Void reslst) {
                et_location.setText(address);
                et_address.setText(address);
            }
        }.execute();

    }

    // show toast
    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(PostNewMeetupActivity.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.i("change", Integer.toString(parent.getSelectedItemPosition()));
            meetupCategory = parent.getItemAtPosition(position).toString();
            Log.i("category", meetupCategory);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
