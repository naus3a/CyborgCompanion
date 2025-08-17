package com.naus3a.cyborgcompanion;

import android.os.Bundle;
import android.util.Log;
import android.util.Base64;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.text.InputType;
import androidx.appcompat.app.AppCompatActivity;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HmacActivity extends AppCompatActivity {
	
	public static final int CHALLENGE_RESPONSE_REQUEST_CODE = 41534;
	private EditText txtAccount;
	private EditText txtPw;
	private NumberPicker numLen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hmac);
		
		txtAccount = findViewById(R.id.txt_account);
		
		txtPw = findViewById(R.id.txt_pw);
		txtPw.setInputType(InputType.TYPE_NULL);
		
		numLen = findViewById(R.id.num_len);
		numLen.setMinValue(8);
		numLen.setMaxValue(28);
		
		Button btnBack = findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		Button btnDoit = findViewById(R.id.btn_doit);
		btnDoit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				final Intent ykdIntent = new Intent("net.pp3345.ykdroid.intent.action.CHALLENGE_RESPONSE");
				
				final byte[] challenge = txtAccount.getText().toString().getBytes(StandardCharsets.UTF_8);
				
				ykdIntent.putExtra("challenge", challenge);
				ykdIntent.putExtra("purpose", "in vivo password management");
				
				try{
					startActivityForResult(ykdIntent, CHALLENGE_RESPONSE_REQUEST_CODE);
				}catch (android.content.ActivityNotFoundException e){
					Toast.makeText(HmacActivity.this, "ykDroid not found", Toast.LENGTH_LONG ).show();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode!=CHALLENGE_RESPONSE_REQUEST_CODE)return;
		switch(resultCode){
			case RESULT_OK:
				final byte[] response = data.getByteArrayExtra("response");
				if(response!=null){
					String sRes = dataToPassword(response);
					txtPw.setText(sRes);
				}
				break;
			case RESULT_CANCELED:
				Toast.makeText(HmacActivity.this, "canceled", Toast.LENGTH_LONG ).show();
				break;
			default:
				break;
		}
	}
	
	private String dataToPassword(byte[] data){
		int wantedLen = numLen.getValue();
		String s = Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_PADDING);
		if(s.length()>wantedLen){
			s = s.substring(0, wantedLen);
		}
		
		PasswordBuilder pb = new PasswordBuilder(s);
		
		return pb.getPassword();
	}
	
	
	
	private boolean hasFeature(String src, String feat){
		Pattern p = Pattern.compile(feat);
		Matcher m = p.matcher(src);
		return  m.find();
	}
	
	private boolean hasLowerCase(String src){
		return hasFeature(src, ".*[a-z].*");
	}
	
	private boolean hasUpperCase(String src){
		return hasFeature(src, ".*[A-Z]+.*");
	}
	
	private boolean hasNumber(String src){
		return hasFeature(src, ".*[0-9]+.*");
	}
	
	private boolean hasSymbol(String src){
		return hasFeature(src, ".*[-_]+.*");
	}
}