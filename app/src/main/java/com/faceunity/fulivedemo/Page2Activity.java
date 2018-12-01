package com.faceunity.fulivedemo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Page2Activity extends AppCompatActivity {


    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
        initView();

        initListener();
    }


    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener(){
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext,int position) {
                Log.e(TAG,"释放位置:"+position +" 下一页:"+isNext);
                int index = 0;
                if (isNext){
                    index = 0;
                }else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position,boolean isBottom) {
                Log.e(TAG,"选中位置:"+position+"  是否是滑动到底部:"+isBottom);
                playVideo(0);
            }


            public void onLayoutComplete() {
                playVideo(0);
            }

        });
    }


    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                Log.e(TAG,"onInfo");
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG,"onPrepared");

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()){
                    Log.e(TAG,"isPlaying:"+videoView.isPlaying());
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                }else {
                    Log.e(TAG,"isPlaying:"+videoView.isPlaying());
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void releaseVideo(int index){
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private int[] imgs = {R.mipmap.video11,R.mipmap.video12,R.mipmap.video13,R.mipmap.video14,R.mipmap.img_video_2};
        private int[] videos = {R.raw.video11,R.raw.video12,R.raw.video13,R.raw.video14,R.raw.video_2};



        public MyAdapter(){
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            //holder.img_thumb.setImageResource(imgs[position%5]);

            for (int i=0 ;i<videos.length;i++){
            Log.d("video1111:",String.valueOf(videos[i])) ;

            }
            Log.d("video1111:",Environment.getExternalStorageState());

            String suf;

            File f=new File("/storage/emulated/0/DCIM/Camera");
            List<String> itemList=new ArrayList<>();
            File files [] =f.listFiles();
            for (int i=0;i<files.length;i++){
                int j=files[i].getName().lastIndexOf(".");
                suf= files[i].getName().substring((j+1));

                if (suf.equalsIgnoreCase("mp4") )
                { Log.d("video1111",files[i].getName());
                    itemList.add(files[i].getName());
                }

            }

//            holder.videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ videos[position%5]));
            holder.videoView.setVideoURI(Uri.parse("/storage/emulated/0/DCIM/Camera"+"/"+itemList.get(position%itemList.size())));
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
            }
        }
    }
}