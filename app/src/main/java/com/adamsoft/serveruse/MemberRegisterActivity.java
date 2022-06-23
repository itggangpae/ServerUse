package com.adamsoft.serveruse;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MemberRegisterActivity extends AppCompatActivity {
    private Button registerbtn, mainbtn, localbtn, gallerybtn, camerabtn;
    private EditText emailinput, pwinput, pwconfirminput, nameinput;
    private ImageView image;

    //갤러리 화면에서 이미지를 선택한 후 동작하는 Launcher 인스턴스
    ActivityResultLauncher<Intent> imageLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == RESULT_OK){
                                Intent intent = result.getData();
                                Uri uri = intent.getData();
                                image.setImageURI(uri);
                            }
                        }
                    });

    //카메라 화면에서 이미지를 선택한 후 동작하는 Launcher 인스턴스
    ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if(result.getResultCode() == RESULT_OK){
                                Intent intent = result.getData();
                                Bundle extras = intent.getExtras();
                                Bitmap bitmap = (Bitmap)extras.get("data");
                                image.setImageBitmap(bitmap);
                            }
                        }
                    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        registerbtn = (Button)findViewById(R.id.registerbtn);
        
        mainbtn = (Button)findViewById(R.id.mainbtn);
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 Activity 종료
                finish();
            }
        });
        
        localbtn = (Button)findViewById(R.id.localbtn);
        localbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //raw 디렉토리에 있는 이미지를 읽어서 ImageView에 출력
                Bitmap bitmap = BitmapFactory.decodeResource(
                        getResources(),R.raw.choi);
                image.setImageBitmap(bitmap);
            }
        });

        gallerybtn = (Button)findViewById(R.id.gallerybtn);
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                imageLauncher.launch(intent);
            }
        });

        camerabtn = (Button)findViewById(R.id.camerabtn);

        emailinput = (EditText)findViewById(R.id.emailinput);
        pwinput = (EditText)findViewById(R.id.pwinput);
        pwconfirminput = (EditText)findViewById(R.id.pwconfirminput);
        nameinput = (EditText)findViewById(R.id.nameinput);

        image = (ImageView)findViewById(R.id.image);


    }


}