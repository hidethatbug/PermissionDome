package com.bawei.permissiondome;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView picture;
    private int PIC_CODE = 1;
    private int PERMISSIONCODE = 1;
    private ImageView img_pic;
    private String[] permissionlist = new String[]{
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picture = findViewById(R.id.text_picture);
        img_pic = findViewById(R.id.img_pic);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查应用是否拥有该权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    startForCamame();
                } else {
                    //如果没有权限，就开始请求权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            permissionlist, PERMISSIONCODE);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONCODE) {
            //用户同意  获取到权限
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startForCamame();
                //这里是用户没有同意时返回的方法
            } else {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                // 如果用户在过去拒绝了权限请求，
                // 并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回false。
                // 如果设备规范禁止应用具有该权限，此方法也会返回 false。
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this
                        , Manifest.permission.CAMERA)) {
                    //这里面为空作为判断用户是否选中了Don't ask again 选项，
                    //如果没有选择那么下次的点击还是可以正常请求权限
                    Toast.makeText(this, "相机权限获取失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "手动授权", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 相机的跳转
     */
    public void startForCamame() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PIC_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == 1) {
                    Bundle url = data.getExtras();
                    Bitmap bitmap = (Bitmap) url.get("data");
                    if (bitmap != null) {
                        img_pic.setImageBitmap(bitmap);
                    }
                }
            }
        }


    }
}
