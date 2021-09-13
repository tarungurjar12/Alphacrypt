package com.example.alphacrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Decoder extends AppCompatActivity {

    EditText keytext;
    EditText normaltext2;
    EditText ciphertext2;
    Button copy_normal2;
    Button copy_cipher2;
    Button decrypt;
    Button delete_normal2;
    Button delete_cipher2;
    TextView char_count3;
    TextView char_count4;
    Context cd;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#2a9db7"));
        }

        cd = Decoder.this;

        normaltext2 = findViewById(R.id.normaltext2);
        keytext = findViewById(R.id.key2);
        ciphertext2 = findViewById(R.id.ciphertext2);
        copy_cipher2= findViewById(R.id.copy_cipher2);
        copy_normal2 = findViewById(R.id.copy_normal2);
        decrypt = findViewById(R.id.decrypt);
        delete_normal2 = findViewById(R.id.delete_normal2);
        delete_cipher2 = findViewById(R.id.delete_cipher2);
        char_count3 = findViewById(R.id.char_count3);
        char_count4 = findViewById(R.id.char_count4);

        decrypt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(ciphertext2.getText().toString().matches("")||keytext.getText().toString().matches(""))
                {
                    App.ToastMaker(cd,"Enter the encrypted text and key");
                }
                else if(keytext.getText().toString().length()!=8)
                {
                    App.ToastMaker(cd,"Enter key of 8 Characters");
                }
                else{
                    normaltext2.setText(decrypt(ciphertext2.getText().toString(),cd));
                }
            }
        });

        copy_normal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("cipher text",normaltext2.getText().toString());
                clipboard.setPrimaryClip(clip);
                App.ToastMaker(cd,"Input Text Copied");
            }
        });

        copy_cipher2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard= (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip= ClipData.newPlainText("cipher text",ciphertext2.getText().toString());
                clipboard.setPrimaryClip(clip);
                App.ToastMaker(cd,"Encrypted Text Copied");
            }
        });

        delete_normal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normaltext2.setText("");
            }
        });

        delete_cipher2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ciphertext2.setText("");
            }
        });

        normaltext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                char_count3.setText(normaltext2.getText().toString().length()+"");
            }
        });

        ciphertext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                char_count4.setText(ciphertext2.getText().toString().length()+"");
            }
        });

    }

    public String decrypt(String value,Context c){

        String coded;
        String result=null;
        if(value.startsWith("code==")){

            coded=value.substring(6,value.length()).trim();

        }
        else{
            coded=value.trim();
        }

        try{
            byte[] bytesDecoded = Base64.decode(coded.getBytes("UTF-8"),Base64.DEFAULT);
            SecretKeySpec key = new SecretKeySpec(keytext.getText().toString().getBytes(),"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] textDecrypted = cipher.doFinal(bytesDecoded);
            result =  new String(textDecrypted);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (NoSuchPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (IllegalBlockSizeException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (BadPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (Exception e){
            e.printStackTrace();
            App.DialogMaker(c,"Encrypt Error","Error"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        return result;
    }

}