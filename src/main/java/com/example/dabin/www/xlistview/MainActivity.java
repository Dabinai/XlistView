package com.example.dabin.www.xlistview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {
    private XListView xListView;
    String loadUrl = "https://api.tianapi.com/wxnew/?key=8d6e3228d25298f13af4fc40ce6c9679&num=10&page=";
    int page = 1;
    List<Jiexi.NewslistBean> newslist;
    ImageLoader instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        instance = ImageLoader.getInstance();
        new MyAsync().execute(loadUrl, page);
    }

    //初始化控件
    private void init() {
        xListView = (XListView) findViewById(R.id.xList_main);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        new MyAsync().execute(loadUrl, 1);
    }

    //上拉加载
    @Override
    public void onLoadMore() {
        new MyAsync().execute(loadUrl, ++page);
    }

    public void OnLoad() {
        xListView.stopLoadMore();
        xListView.stopRefresh();
        xListView.setRefreshTime("刚刚");
    }
    //异步加载
    class MyAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String all = "";
            String temp1 = (String) params[0];
            int temp2 = (int) params[1];
            try {
                URL url = new URL(temp1 + temp2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5 * 1000);
                conn.setConnectTimeout(5 * 1000);
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream inputStream = conn.getInputStream();
                    int length = 0;
                    byte[] b = new byte[1024];
                    while ((length = inputStream.read(b)) != -1) {
                        String string = new String(b, 0, length);
                        all += string;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return all;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String getJson = (String) o;
            Gson gson = new Gson();
            newslist = gson.fromJson(getJson, Jiexi.class).getNewslist();

            Myclass myclass = new Myclass();
            xListView.setAdapter(myclass);

            OnLoad();
        }
    }

    class Myclass extends BaseAdapter {

        @Override
        public int getCount() {
            return newslist.size();
        }

        @Override
        public Object getItem(int position) {
            return newslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item, null);
            }
            ImageView iamge = (ImageView) convertView.findViewById(R.id.image_item);
            TextView text = (TextView) convertView.findViewById(R.id.textview_item);
            instance.displayImage(newslist.get(position).getPicUrl(), iamge);
            text.setText(newslist.get(position).getTitle());
            return convertView;
        }
    }

}
