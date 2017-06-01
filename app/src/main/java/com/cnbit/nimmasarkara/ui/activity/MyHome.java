package com.cnbit.nimmasarkara.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.PoliticiansAdapter;
import com.cnbit.nimmasarkara.model.response.events.PoliticiansModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harry on 3/17/2017.
 */

public class MyHome extends AppCompatActivity {

    String [] banner={"http://www.firstpost.com/wp-content/uploads/2014/11/Panneerselvam_PTI.jpg",
            "https://pbs.twimg.com/media/C5uwTWGU8AAvDmo.jpg",
            "http://www.assembly.tn.gov.in/members/profile/images/097.jpg",
            "http://www.assembly.tn.gov.in/15thassembly/members/102.jpg",
            "http://www.thehindu.com/migration_catalog/article12608173.ece/ALTERNATES/LANDSCAPE_615/Jayakumar",
            "http://www.assembly.tn.gov.in/members/profile/images/057.jpg",
            "http://timesofindia.indiatimes.com/thumb/msid-52223482,width-400,resizemode-4/52223482.jpg",
            "http://www.tamilnadumlas.com/admin/mlaimage/1464089547bhavani.JPEG",
            "http://www.truetamil.com/img/r-kamaraj.jpg",
            "http://www.truetamil.com/img/os-manian.jpg",
            "http://www.truetamil.com/img/udumalai-radhakrishnan.jpg",
            "http://www.tamilnadumlas.com/admin/mlaimage/1487588515179.jpg",
            "http://www.truetamil.com/img/duraikannu.jpg",
            "http://www.assembly.tn.gov.in/members/profile/images/218.jpg",
            "http://tamilnadumlas.com/admin/mlaimage/1464064642RBUdhayakumar2016.jpg",
            "http://cms.tn.gov.in/sites/default/files/assembly/pictures/205_0.jpg"
    };

    String [] names={"O.Panneerselvam",
            "K.Palaniswami",
            "P.Thangamani",
            "S.P.Velumani",
            "D.Jayakumar",
            "K.P.Anbalagan",
            "M.C.Sampath",
            "K.C.Karuppannan",
            "R.Kamaraj",
            "O.S.Manian",
            "Udumalai K Radhakrishnan",
            "Dr.C.Vijaya Baskar",
            "R.Doraikkannu",
            "Kadambur Raju",
            "R.B.Udhayakumar",
            "K.T.Rajenthra Bhalaji"};

    RecyclerView politics;
    PoliticiansAdapter adapter;
    ImageView backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        politics = (RecyclerView) findViewById(R.id.politics);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.backblack);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        backdrop=(ImageView)findViewById(R.id.backdrop);
        Picasso.with(getApplicationContext())
                .load("http://static.dnaindia.com/sites/default/files/2016/10/14/510140-jayalalithaa-2-pti-resized.jpg")
                .into(backdrop);
    /*  BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        //  add banner using image url
        for (int i = 0; i < banner.length; i++) {
            ToastUtils.showToast(getApplicationContext(), String.valueOf(i));
            bannerSlider.addBanner(new RemoteBanner(String.valueOf(banner[i])));
            //  add banner using resource drawable

        }*/
        ArrayList<PoliticiansModels> arrayList=arraydata();
        adapter=new PoliticiansAdapter(getApplicationContext(),arrayList,MyHome.this.getSupportFragmentManager());
        politics.setItemAnimator(new DefaultItemAnimator());
        politics.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        politics.setAdapter(adapter);

    }
        public ArrayList<PoliticiansModels> arraydata(){
            ArrayList<PoliticiansModels> arrayList=new ArrayList<>();

            for(int i=0;i<banner.length;i++){
                PoliticiansModels politiciansAdapter=new PoliticiansModels();
                politiciansAdapter.setImages(banner[i]);
                politiciansAdapter.setNames(names[i]);
                arrayList.add(politiciansAdapter);
            }

            return arrayList;
        }



}
