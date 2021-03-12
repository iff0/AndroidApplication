package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.project.fragment.DetectFragment;
import com.example.project.fragment.HistoryFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.utils.DataManageUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    public static final int CUT_CODE = 3;
    private static final int UPLOAD_CODE = 2;
    private static final int CAPTURE_CODE = 1;
    public static final int OPTION_REMOTE_SERVER_CODE = 101;
    public static final int OPTION_IMG_CROP_RATIO_CODE = 102;

    private static final int[] navigation_ids = {R.id.main_bottom_navigation_home, R.id.main_bottom_navigation_detect,
        R.id.main_bottom_navigation_history};



    private BottomNavigationView mBottomNavigationView;
    private HomeFragment mHomeFragment;
    private DetectFragment mDetectFragment;
    private HistoryFragment mHistoryFragment;
    private MaterialToolbar mainToolBar;
    SlidePagerAdapter pagerAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
        if (DataManageUtil.getCacheDirSize(this) > DataManageUtil.MB * 30) {
            DataManageUtil.cleanInternalCache(this);
        }
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int j = menuItem.getItemId();
            for (int i = 0; i < navigation_ids.length; i++)
                if (j == navigation_ids[i]) {
                    pager.setCurrentItem(i);
                    break;
                }
            return true;
        });

        mainToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.options:
                        return true;
                    case R.id.about_us:
                        Intent i = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.clear_cache:
                        DataManageUtil.cleanInternalCache(MainActivity.this);
                        Toast toast = Toast.makeText(MainActivity.this, "已清除缓存",Toast.LENGTH_LONG);
                        toast.show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void initUI() {
        mBottomNavigationView = findViewById(R.id.main_bottom_navigation);
        mHomeFragment = new HomeFragment();
        mDetectFragment = new DetectFragment();
        mHistoryFragment = new HistoryFragment();
        mainToolBar = findViewById(R.id.topAppBar);
        mBottomNavigationView.setSelectedItemId(R.id.main_bottom_navigation_detect);
        pager = findViewById(R.id.main_frag);
        pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                if (position < navigation_ids.length) {
                    mBottomNavigationView.setSelectedItemId(navigation_ids[position]);
                }
            }
        });
    }


    class SlidePagerAdapter extends FragmentPagerAdapter {
        SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            //进行选择1 2 3
            if (position == 0) {
                return mHomeFragment;
            }
            else if (position ==1) {
                return mDetectFragment;
            }
            else {
                return mHistoryFragment;
            }
        }

        @Override
        public int getCount() {
            return navigation_ids.length;
        }
    }


    public void uploadPhoto() {
        mDetectFragment.uploadPhoto(null);
        //Log.d("a", "........");
    }


    public void capturePhoto() {
        mDetectFragment.capturePhoto(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("oh", String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case UPLOAD_CODE:
                //方式一（不建议使用）
                //下面的一句代码，也可以把图片显示在ImageView中
                //但图片过大的时候，将无法显示，所以
                //img.setImageURI(data.getData());

                //方式二
                Uri uri = data.getData();
                DetectFragment.startCrop(this, uri);
                //ContentResolver cr = getActivity().getContentResolver();
               /* try {
                    //根据Uri获取流文件
                    InputStream is = cr.openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
                    onPhotoUploaded(bitmap);
                    img1.setImageBitmap(bitmap);
                    Toast.makeText(getActivity().getApplicationContext(), "okay", Toast.LENGTH_LONG).show();
                }
                catch(Exception e)
                {
                    Log.i("lyf", e.toString());
                }*/
                break;
            case CAPTURE_CODE:

                Uri uri_ = mDetectFragment.getTmpCapture();
                DetectFragment.startCrop(this, uri_);
                break;
            case CUT_CODE:

                Log.d("cameraDemo", "3333333333333333333333333333");
                final Uri croppedUri = UCrop.getOutput(data);
                try {
                    if (croppedUri != null) {
                        Bitmap bit = BitmapFactory.decodeStream(this.
                                getContentResolver().openInputStream(croppedUri));
                        mDetectFragment.onPhotoUploaded(bit);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case OPTION_REMOTE_SERVER_CODE:
                mHomeFragment.refreshOption("server");
                break;
            case OPTION_IMG_CROP_RATIO_CODE:
                mHomeFragment.refreshOption("crop_ratio");
                break;
            default:
                Log.d("cameraDemo", "Unknown RequestCode");

        }
    }

    public void notifyHistoryAdded(File resultDir) {
        mHistoryFragment.notifyHistoryAdded(resultDir);
    }

    public void notifyHistoryDeleted(File imgDir) {
        mDetectFragment.notifyHistoryDeleted(imgDir);
    }
}