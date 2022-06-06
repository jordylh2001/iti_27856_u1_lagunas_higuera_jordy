package com.upv.pm_2022.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView txv;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txv=(TextView)findViewById(R.id.textview);
        b1=(Button) findViewById(R.id.button);
    }

    int requestCode=1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==requestCode && resultCode== Activity.RESULT_OK)
        {
            if(data==null)
                return;
            if(null!=data.getClipData()){
                String tempstring="";
                for(int i=0;i<data.getClipData().getItemCount();i++){
                    Uri uri=data.getClipData().getItemAt(i).getUri();
                    tempstring+=uri.getPath()+"\n";
                }
                txv.setText(tempstring);
            }
            else{
                Uri uri=data.getData();
                txv.setText(uri.getPath());
            }
        }
    }

    public void openFilechooser(View view){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,requestCode);
    }
}