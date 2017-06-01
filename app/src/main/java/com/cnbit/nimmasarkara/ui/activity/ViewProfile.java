package com.cnbit.nimmasarkara.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.model.response.CountUser;
import com.cnbit.nimmasarkara.model.response.OtpResponse;
import com.cnbit.nimmasarkara.network.ShriSaiApi;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.AppUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Harry on 2/9/2017.
 */

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView bacbutton,rateus,share;
    TextView name,address;
    TextView imagesposted,videosposted,audiosposted;
    List<CountUser> countUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        bacbutton=(ImageView)findViewById(R.id.back);
        rateus=(ImageView)findViewById(R.id.rate);
        share=(ImageView)findViewById(R.id.share);
        rateus.setOnClickListener(this);
        share.setOnClickListener(this);
        name=(TextView)findViewById(R.id.name);
        address=(TextView)findViewById(R.id.address);
        imagesposted=(TextView)findViewById(R.id.imagesposted);
        videosposted=(TextView)findViewById(R.id.videosposted);
        audiosposted=(TextView)findViewById(R.id.audiosposted);
        bacbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //ToastUtils.showToast(getApplicationContext(),getApplicationContext().getPackageName());
        AppUtils appUtils = new AppUtils(this);
        OtpResponse response = appUtils.getObject("OTP_RESPONSE", OtpResponse.class);
        if (response != null) {
            name.setText(response.getName());
            address.setText(response.getMobile());
        }
        init();
    }

    private void init(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(AppConstantsUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShriSaiApi api=retrofit.create(ShriSaiApi.class);
        AppUtils appUtils=new AppUtils(getApplicationContext());
        Call<List<CountUser>> call=api.getCount(appUtils.getUserId());
        call.enqueue(new Callback<List<CountUser>>() {
            @Override
            public void onResponse(Call<List<CountUser>> call, Response<List<CountUser>> response) {
                if(response.code()==200) {
                    countUsers = response.body();
                    imagesposted.setText(countUsers.get(0).getImage());
                    videosposted.setText(countUsers.get(0).getVideo());
                    audiosposted.setText(countUsers.get(0).getAudio());

                }
                else{
                    ToastUtils.showToast(getApplicationContext(),"Please Check Internet Connection");
                }

            }

            @Override
            public void onFailure(Call<List<CountUser>> call, Throwable t) {
                ToastUtils.showToast(getApplicationContext(),t.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==rateus){
            Uri uri = Uri.parse("market://details?id="+getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
            }
        }

        if(v==share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out Apna Sarkar App at: https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        }

}
