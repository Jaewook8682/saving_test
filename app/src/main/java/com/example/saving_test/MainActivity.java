package com.example.saving_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button readBtn,writeBtn;
    private EditText idText, contentText;


    private int COUNT = 0;
    private JSONObject obj = null;
    private TextView tv; //저장한 json값 불러오기기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeBtn = (Button) findViewById(R.id.writeBtn);
        readBtn = (Button) findViewById(R.id.readBtn);
        idText = (EditText)findViewById(R.id.idText);
        contentText = (EditText)findViewById(R.id.contentText);
        tv = (TextView) findViewById(R.id.tv);

        obj = new JSONObject();

        /** 외부 저장소에에 저장하기 위 권한 설정 **/
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
        checkExternalStorage();

        //파일에서 읽어오기기
        readBtn.setOnClickListener(view->{
            readFile();
        });

        //파일에 쓰기
        writeBtn.setOnClickListener(view-> {
            writeFile();
        });
    }

    public JSONArray insertData(String id, String content){
        COUNT++; //입력한 데이터가 몇개인지 카운트하는 변수, 없어도 무관

        try {

            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            JSONArray jArray = new JSONArray();

            //COUNT변수 정수형에서 문자형으로 변환 후 JSONObject에 입력
            sObject.put("num",Integer.toString(COUNT));
            sObject.put("id", id);
            sObject.put("content", content);

            //JSONObject to JSONArray
            return jArray.put(sObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile() {
        String fileTitle = "title123.txt";
        File file = new File(Environment.getExternalStorageDirectory(), fileTitle);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, false);
            String str = "12354045ABC";
            writer.write(str);
            writer.close();
        } catch (IOException e) {

        }
    }


    public void readFile() {

        String fileTitle = "test.json";
        File file = new File(Environment.getExternalStorageDirectory(), fileTitle);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String result = "";
            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            tv.setText(result);
            reader.close();

        } catch (FileNotFoundException e1) {
            Log.i("파일못찾음",e1.getMessage());
        } catch (IOException e2) {
            Log.i("읽기오류",e2.getMessage());
        }
    }


    /**
     * 외부메모리 상태 확인 메서드
     */
    boolean checkExternalStorage() {
        String state = Environment.getExternalStorageState();
        // 외부메모리 상태
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 읽기 쓰기 모두 가능
            Log.d("test", "외부메모리 읽기 쓰기 모두 가능");
            Toast.makeText(getApplicationContext(),"외부메모리 읽기 쓰기 모두 가능",Toast.LENGTH_SHORT).show();
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            //읽기전용
            Log.d("test", "외부메모리 읽기만 가능");
            Toast.makeText(getApplicationContext(),"외부메모리 읽기만 가능",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // 읽기쓰기 모두 안됨
            Log.d("test", "외부메모리 읽기쓰기 모두 안됨 : "+ state);
            Toast.makeText(getApplicationContext(),"외부메모리 읽기쓰기 모두 안됨 : "+ state,Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}