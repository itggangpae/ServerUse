package com.adamsoft.serveruse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    EditText emailinput, pwinput;
    Button btnlogin, btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailinput = (EditText)findViewById(R.id.emailinput);
        pwinput = (EditText)findViewById(R.id.pwinput);

        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //email 과 password 읽어오기
                String email = emailinput.getText().toString().trim().toLowerCase();
                String pw = pwinput.getText().toString().trim();

                //유효성 검사

                Handler handler = new Handler(Looper.getMainLooper()){
                    public void handleMessage(Message message){
                        String error = (String)message.obj;
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                };

                //웹 서버에게 데이터를 전송하는 스레드 생성
                new Thread(){
                    public void run(){
                        try {
                            //URL 생성
                            URL url = new URL("http://192.168.10.8:9000/member/login");

                            //연결 객체 생성
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();

                            //옵션 설정
                            con.setRequestMethod("POST");
                            con.setUseCaches(false);

                            //파일업로드가 없는 POST 방식에서의 파라미터 생성(email, password)
                            String data = URLEncoder.encode("email", "UTF-8") + "="
                                    + URLEncoder.encode(email, "UTF-8")
                                    + "&" + URLEncoder.encode("password", "UTF-8") + "="
                                    + URLEncoder.encode(pw, "UTF-8");
                            //파라미터를 전송
                            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                            wr.write(data);
                            wr.flush();

                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            while(true){
                                String line = br.readLine();
                                if(line == null){
                                    break;
                                }
                                sb.append(line + "\n");
                            }
                            Log.e("받아온 문자열", sb.toString());
                            br.close();
                            con.disconnect();

                            //받아온 데이터 파싱
                            JSONObject object = new JSONObject(sb.toString());
                            String error = object.getString("error");
                            //Log.e("error", error);
                            if(error.equals("null")){
                                Log.e("결과", "로그인 성공");
                                //로그인 성공했을 때 넘어온 데이터를 읽기
                                String name = object.getString("name");
                                String imageurl = object.getString("imageurl");
                                //개인 정보를 파일에 저장 - 이 정보가 있으면 로그인을 한 것이고 없으면 로그인을 안한 것
                                FileOutputStream fos = openFileOutput(
                                        "login.txt", Context.MODE_PRIVATE);
                                String str = email + ":" + name + ":" + imageurl;
                                fos.write(str.getBytes());
                                fos.close();

                                //메인 화면으로 이동
                                //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //startActivity(intent);

                                finish();
                            }else{
                                Message message = new Message();
                                message.obj = error;
                                handler.sendMessage(message);
                            }



                        }catch(Exception e){
                            Log.e("로그인 시도 실패", e.getLocalizedMessage());
                        }
                    }
                }.start();
            }
        });



        btnregister = (Button)findViewById(R.id.btnregister);
        btnregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //이동할 Activity를 Intent로 생성
                Intent intent = new Intent(LoginActivity.this, MemberRegisterActivity.class);
                //전송할 데이터가 있으면 데이터 설정
                //intent.putExtra("이름", 데이터);
                startActivity(intent);
            }
        });
    }
}