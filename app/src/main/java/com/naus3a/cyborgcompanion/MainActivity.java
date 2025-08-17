package com.naus3a.cyborgcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		Button btnHmac = findViewById(R.id.btn_hmacsha);
		Button btnNdef = findViewById(R.id.btn_ndef);
		
		addViewListener(btnHmac, HmacActivity.class);
		addViewListener(btnNdef, NdefActivity.class);
    }
	
	private void addViewListener(Button btn, Class viewClass){
		btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this, viewClass);
				startActivity(intent);
			}
		});
	}
}