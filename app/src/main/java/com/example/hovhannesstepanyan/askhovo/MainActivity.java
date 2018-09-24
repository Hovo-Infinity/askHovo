package com.example.hovhannesstepanyan.askhovo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Random;

import Core.Constants;
import Core.DataManager;
import Core.Database.QuestonDatabase;
import Core.GlideApp;
import Core.MGPrefsCacheManager;
import Core.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private QuestionItem mItem;
    private ImageView mBackgroundImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBackgroundImageView = findViewById(R.id.iv_background);
        mRecyclerView = findViewById(R.id.rv_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mItem = new QuestionItem(this, QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().getAllQuestions());
        mRecyclerView.setAdapter(mItem);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CreateQuestionActivity.class),  null));
        String url = MGPrefsCacheManager.getInstance().getStringFromCashe(Constants.BACKGROUND_IMAGE_URI, DataManager.getInstance().getSelecredImage());
        if (url != null) {
            GlideApp.with(this)
                    .load("https://www.wallpaperwolf.com/wallpapers/iphone-wallpapers/hd/download/night-stars-0467.png")
                    .into(mBackgroundImageView);
        }

        mRecyclerView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            public void onSwipeRight() {
                Log.e(TAG, "onSwipeRight");
            }

            public void onSwipeLeft() {
                Log.e(TAG, "onSwipeLeft");
            }
        });
    }

    @Override
    protected void onResume() {
        mItem.setData(QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().getAllQuestions());
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String descr = bundle.getString(Constants.DESCRIPTION);
                    String question = bundle.getString(Constants.QUESTION);
                    if (mRecyclerView.getAdapter() != null) {
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        }

        if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            GlideApp.with(this)
                    .load(selectedImage)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            MGPrefsCacheManager.getInstance().putInCashe(Constants.BACKGROUND_IMAGE_URI, selectedImage.toString());
                            return false;
                        }
                    })
                    .into(mBackgroundImageView);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_bg_image) {
            Intent i = new
                    Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
            return true;
        }
        if (id == R.id.action_reset_bg) {
            DataManager dataManager = DataManager.getInstance();
            int count = dataManager.getImageUrls().size();
            dataManager.setSelectedIndex(new Random().nextInt(count));
            String selectedImage = dataManager.getSelecredImage();
            MGPrefsCacheManager.getInstance().putInCashe(Constants.BACKGROUND_IMAGE_URI, selectedImage);
            GlideApp.with(this)
                    .load("https://wallpapershome.com/images/pages/pic_v/547.jpg")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mBackgroundImageView);
        }

        return super.onOptionsItemSelected(item);
    }
}
