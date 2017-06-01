package com.cnbit.nimmasarkara.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cnbit.nimmasarkara.R;
import com.cnbit.nimmasarkara.adapter.AudioAdapter;
import com.cnbit.nimmasarkara.ui.activity.AudioDetails;
import com.cnbit.nimmasarkara.utils.AppConstantsUtils;
import com.cnbit.nimmasarkara.utils.ToastUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DC on 10/10/2016.
 */

public class AudioListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
  private AudioAdapter mAdapter;
  private ListView mListView;
  private static final String TAG_ID = "id";
  private static final String TAG_TITLE = "title";
  private static final String TAG_DESC = "description";
  private static final String TAG_LIKES = "likes";
  private static final String TAG_POSTEDBY = "postedby";
  private static final String TAG_DATETIME= "datetime";
  private static final String TAG_MEDIA = "media";
  ArrayList<HashMap<String, String>> resultList;
  ProgressBar progressBar;
  int n=1;
  SwipeRefreshLayout mSwipeRefresh;
  RelativeLayout relativeLayout;
  String nomoreaudios="";

  private int firstVisibleItem, visibleItemCount,totalItemCount;





  public static AudioListFragment getInstance() {
    AudioListFragment fragment = new AudioListFragment();
    Bundle bundle = new Bundle();
    bundle.putString(AppConstantsUtils.TYPE, AppConstantsUtils.AUDIO);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.exampleaudiolistfragment, container, false);
    mListView=(ListView) rootView.findViewById(R.id.listaudio);

    progressBar =(ProgressBar)rootView.findViewById(R.id.progress);
    mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
    mSwipeRefresh.setOnRefreshListener(this);
    relativeLayout=(RelativeLayout) rootView.findViewById(R.id.progresslayout);
    relativeLayout.setVisibility(View.INVISIBLE);
    resultList = new ArrayList<HashMap<String, String>>();
    mListView.setOnScrollListener(this);
    mListView.setOnItemClickListener(this);
    nomoreaudios="";
    GetResultAudio gra=new GetResultAudio();
    gra.execute();
    return rootView;
  }

  @Override
  public void onRefresh() {
    nomoreaudios="";
    resultList.clear();
    n=1;
    mListView.setAdapter(null);
    GetResultAudio gra=new GetResultAudio();
    gra.execute();
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    final int lastItem = firstVisibleItem + visibleItemCount;
    if (lastItem == totalItemCount && scrollState == SCROLL_STATE_IDLE) {
      if (!nomoreaudios.equals("No More")) {
        n++;
        GetResultAudio ga = new GetResultAudio();
        ga.execute();
      }
    }


  }

  @Override
  public void onScroll(AbsListView view,
                       int firstVisibleItemm, int visibleItemCountt,
                       int totalItemCountt) {
   /* firstVisibleItem = firstVisibleItemm;
    visibleItemCount = visibleItemCountt;
    totalItemCount = totalItemCountt;
    mSwipeRefresh.setEnabled((firstVisibleItem == 0));*/
    boolean enable = true;
    if(mListView != null && mListView.getChildCount() > 0){
      // check if the first item of the list is visible
      boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
      // check if the top of the first item is visible
      boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
      // enabling or disabling the refresh layout
      enable = firstItemVisible && topOfFirstItemVisible;
    }
    mSwipeRefresh.setEnabled(enable);

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    HashMap<String, String> res_hashmap = new HashMap<String, String>();
    res_hashmap = resultList.get(position);

    //converting to strings
    String iid = res_hashmap.get(TAG_ID);
    String title = res_hashmap.get(TAG_TITLE);
    String description = res_hashmap.get(TAG_DESC);
    String likes = res_hashmap.get(TAG_LIKES);
    String postedby = res_hashmap.get(TAG_POSTEDBY);
    String datetime = res_hashmap.get(TAG_DATETIME);
    String media = res_hashmap.get(TAG_MEDIA);

    Intent intent=new Intent(getActivity(),AudioDetails.class);
    intent.putExtra(TAG_ID,iid);
    intent.putExtra(TAG_TITLE,title);
    intent.putExtra(TAG_DESC,description);
    intent.putExtra(TAG_LIKES,likes);
    intent.putExtra(TAG_POSTEDBY,postedby);
    intent.putExtra(TAG_DATETIME,datetime);
    intent.putExtra(TAG_MEDIA,media);
    startActivity(intent);
  }


  class GetResultAudio extends AsyncTask<String,String,String>  {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {

      InputStream is = null;
      BufferedReader bufferedReader;
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
      nameValuePairs.add(new BasicNameValuePair("type", "Audio"));
      nameValuePairs.add(new BasicNameValuePair("pageno", String.valueOf(n)));
      nameValuePairs.add(new BasicNameValuePair("album_id", "0"));
      try {
        HttpClient client = new DefaultHttpClient();
        // HttpPost post=new HttpPost("http://10.0.3.2/srisaikoti/getMedia");
        HttpPost post = new HttpPost(AppConstantsUtils.BASE_URL + "getMedia");
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = client.execute(post);
        int code = response.getStatusLine().getStatusCode();
        String recode = String.valueOf(code);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();
        bufferedReader = new BufferedReader(new InputStreamReader(is));
        String json_result = bufferedReader.readLine();
        try {
          if (code == 200) {
            JSONArray jsonArray = new JSONArray(json_result);
            if (jsonArray != null && jsonArray.length() > 0) {
              for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonChildArray = jsonArray.getJSONArray(i);
                if (jsonChildArray != null && jsonChildArray.length() > 0) {
                  for (int j = 0; j < jsonChildArray.length(); j++) {
                    JSONObject c = jsonChildArray.getJSONObject(j);
                    String id = c.getString(TAG_ID);
                    String title = c.getString(TAG_TITLE);
                    String description = c.getString(TAG_DESC);
                    String likes = c.getString(TAG_LIKES);
                    String postedby = c.getString(TAG_POSTEDBY);
                    String datetime = c.getString(TAG_DATETIME);
                    String media = c.getString(TAG_MEDIA);

                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put(TAG_ID, id);
                    result.put(TAG_TITLE, title);
                    result.put(TAG_DESC, description);
                    result.put(TAG_LIKES, likes);
                    result.put(TAG_POSTEDBY, postedby);
                    result.put(TAG_DATETIME, datetime);
                    result.put(TAG_MEDIA, media);

                    resultList.add(result);
                  }
                }
              }
            }
            return recode;
          } else {
            if (json_result != null) {
              JSONObject jsonObject = new JSONObject(json_result);
              String status = jsonObject.getString("status");
              String msg = jsonObject.getString("msg");
              return msg;
            } else {
              return "No Internet";
            }
          }

        } catch (JSONException e) {
          return e.toString();
        }
      } catch (UnsupportedEncodingException e) {
        return e.toString();
      } catch (ClientProtocolException e) {
        return e.toString();
      } catch (IOException e) {
        return e.toString();
      }


    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      relativeLayout.setVisibility(View.INVISIBLE);
      if (mSwipeRefresh.isRefreshing()) {
        mSwipeRefresh.setRefreshing(false);
      }
      if (s != null) {
        if (s.equals("200")) {
          mAdapter = new AudioAdapter(getActivity(), getActivity(), resultList);
          mListView.setAdapter(mAdapter);
          mListView.setSelectionFromTop(firstVisibleItem, 0);
          nomoreaudios = "";

        } else {
          ToastUtils.showToast(getActivity(), s);
          nomoreaudios = "No More";
        }
      } else {
        //    ToastUtils.showToast(getActivity(), "No Audio Found");

      }
    }
  }


}
