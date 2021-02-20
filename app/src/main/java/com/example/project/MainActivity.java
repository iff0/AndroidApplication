package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.example.project.fragment.DetectFragment;
import com.example.project.fragment.HistoryFragment;
import com.example.project.fragment.HomeFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    public static final int CUT_CODE = 3;
    private static final int UPLOAD_CODE = 2;
    private static final int CAPTURE_CODE = 1;

    private  static Context mContext;

    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private HomeFragment mHomeFragment;
    private DetectFragment mDetectFragment;
    private HistoryFragment mHistoryFragment;
    private MaterialToolbar mainToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListener();
        mContext=getContext();
    }

    public static Context getContext(){
        return mContext;
    }
    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Log.d("oh", String.valueOf(menuItem.getItemId()));
                switch (menuItem.getItemId()){
                    case R.id.main_bottom_navigation_home:
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.main_bottom_navigation_detect:
                        switchFragment(mDetectFragment);
                        break;
                    case R.id.main_bottom_navigation_history:
                        switchFragment(mHistoryFragment);
                        break;
                }
                return true;
            }
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
                    default:
                        return false;
                }
            }
        });
    }
    private Fragment lastFragment = null;
    private void switchFragment(Fragment f) {
        mTransaction = mFragmentManager.beginTransaction();
        if (!f.isAdded()) {
            mTransaction.add(R.id.main_frag,f);
        } else {
            mTransaction.show(f);
        }
        if (lastFragment != null && lastFragment != f){
            mTransaction.hide(lastFragment);
        }
        lastFragment = f;
        //mTransaction.replace(R.id.frag1,homeFragment);
        mTransaction.commit();
    }
    private void initUI() {
        mBottomNavigationView = findViewById(R.id.main_bottom_navigation);
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mDetectFragment = new DetectFragment();
        mHistoryFragment = new HistoryFragment();
        mainToolBar = findViewById(R.id.topAppBar);
        mBottomNavigationView.setSelectedItemId(R.id.main_bottom_navigation_detect);
        switchFragment(mDetectFragment);
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
            default:
                Log.d("cameraDemo", "Unknown RequestCode");

        }
    }
}