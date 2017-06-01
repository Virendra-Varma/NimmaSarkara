package com.cnbit.nimmasarkara.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbit.nimmasarkara.model.response.events.PoliticiansModels;
import com.cnbit.nimmasarkara.ui.activity.SlideshowDialogFragment;
import com.squareup.picasso.Picasso;

import com.cnbit.nimmasarkara.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on 3/18/2017.
 */

public class PoliticiansAdapter extends RecyclerView.Adapter<PoliticiansAdapter.ViewHolder> {


    ArrayList<PoliticiansModels> resultList;
    Context context;
    FragmentManager fragmentcontext;

    public PoliticiansAdapter(Context context, ArrayList<PoliticiansModels> resultList, FragmentManager fragmentcontext){
        this.context=context;
        this.resultList=resultList;
        this.fragmentcontext=fragmentcontext;
    }


    @Override
    public PoliticiansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_politicians,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PoliticiansAdapter.ViewHolder holder, final int position) {

        holder.polinames.setText(resultList.get(position).getNames());

        Picasso.with(context)
                .load(resultList.get(position).getImages())
                .resize(450, 450)
                .centerCrop()
                .into(holder.poliimages);

        holder.poliimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> images=new ArrayList<String>();
                for(int i=0;i<getItemCount();i++){
                    String imageslinks=resultList.get(i).getImages();
                    images.add(imageslinks);
                }

                Log.d("Your Arraylist       ", images.toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) images);
                bundle.putInt("position", position);
                bundle.putString("adapter", "adapter");
                FragmentTransaction ft = fragmentcontext.beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");


            }
        });

    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poliimages;
        TextView polinames;

        public ViewHolder(View itemView) {
            super(itemView);
            poliimages=(ImageView)itemView.findViewById(R.id.poliimage);
            polinames=(TextView)itemView.findViewById(R.id.poliname);

        }
    }
}
