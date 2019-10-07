package com.example.facebook_login_check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ProfileData extends AppCompatActivity {
    private ImageView pic;
    private Button signout;
    private TextView txt_name, email1, location,txt_dg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);

        txt_name = (TextView) findViewById(R.id.namefb);
        email1 = (TextView) findViewById(R.id.fbemail);
        pic = (ImageView) findViewById(R.id.fbprofilepic);
        location = (TextView) findViewById(R.id.location_fb);
        txt_dg=(TextView)findViewById(R.id.fbgender);
        signout = (Button) findViewById(R.id.fblogout);


        String name  =  getIntent().getExtras().getString("name");
        String firstname = getIntent().getExtras().getString("firstname");
        String lastname = getIntent().getExtras().getString("lastname");
        String email = getIntent().getExtras().getString("email");
        String profile = getIntent().getExtras().getString("profile");
        String user_birthday = getIntent().getExtras().getString("userbirthday");
        String user_location = getIntent().getExtras().getString("userlocation");
        String user_hometown = getIntent().getExtras().getString("userhometown");
        String user_gender = getIntent().getExtras().getString("usergender");

        if (firstname != null && lastname != null && email != null) {

            txt_name.setText(firstname + " " + lastname);
           //

            email1.setText(email+" "+user_birthday);
            Glide.with(this).load(String.valueOf(profile )).into(pic);
            txt_dg.setText(user_birthday+" "+user_gender);
            location.setText(user_location+" "+user_hometown);
        }
//        else if(user_birthday!= null && user_gender!= null && user_location!= null && user_hometown!=null){
//            txt_dg.setText(user_birthday+" "+user_gender);
//            location.setText(user_location+" "+user_hometown);
//
//        }
        else {
            Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
        }


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ProfileData.this, MainActivity.class);
                startActivity(login);
                finish();

            }
        });


    }
}
