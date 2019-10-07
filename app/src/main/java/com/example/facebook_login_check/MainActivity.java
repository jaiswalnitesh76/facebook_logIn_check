package com.example.facebook_login_check;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginButton fb_loginButton;
    URL profilepic;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final int RC_FACEBOOK_SIGN_IN = 64206;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        fb_loginButton = (LoginButton) findViewById(R.id.login_button);


        fb_loginButton.setPermissions(Arrays.asList(
                 "public_profile", "email"));

        fb_loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.i("LoginActivity", response.toString());//fb27
                        Bundle bFacebookData = getFacebookData(object);
                        String name = bFacebookData.getString("name");
                        String firstname = bFacebookData.getString("first_name");
                        String lastname = bFacebookData.getString("last_name");
                        String email = bFacebookData.getString("email");
                        String profilepic = bFacebookData.getString("profile_pic");
                        String user_birthday = bFacebookData.getString("user_birthday");
                        String user_location = bFacebookData.getString("user_location");
                        String user_hometown = bFacebookData.getString("user_hometown");
                        String user_gender = bFacebookData.getString("user_gender");

                        Intent in = new Intent(MainActivity.this, ProfileData.class);
                        in.putExtra("name", name);
                        in.putExtra("firstname", firstname);
                        in.putExtra("lastname", lastname);
                        in.putExtra("email", email);
                        in.putExtra("profile", profilepic);
                        in.putExtra("userbirthday", user_birthday);
                        in.putExtra("userlocation", user_location);
                        in.putExtra("usergender", user_gender);
                        in.putExtra("userhometown", user_hometown);

                        startActivity(in);
                        finish();
                        if (response.getError() != null) {
                            System.out.println("ERROR " + response.getError().toString());
                        } else {
                            try {

                                String firstName = object.getString("first_name");
                                String lastName = object.getString("last_name");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name, last_name, email,location,birthday,gender,hometown");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

//Genrate for Hashkey
       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.facebook_login_check", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {
            String id = object.getString("id");
            profilepic = new URL("https:///graph.facebook.com/" + id + "/picture?width=100&height=100");
            Log.i("profilepic", profilepic.toString());
            bundle.putString("profile_pic", profilepic.toString());
            bundle.putString("idFacebook", id);
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        if (object.has("first_name")) {
            try {
                bundle.putString("first_name", object.getString("first_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (object.has("last_name")) {
            try {
                bundle.putString("last_name", object.getString("last_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (object.has("email")) {
            try {
                bundle.putString("email", object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (object.has("birthday")) {
            try {
                Log.d(MainActivity.class.getSimpleName(), "getFacebookData: " +object.getString("birthday"));
                bundle.putString("user_birthday", object.getString("birthday"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (object.has("location")) {
                try {
                    JSONObject locationoj=object.getJSONObject("location");
                    Log.d(MainActivity.class.getSimpleName(), "getFacebookData: " +locationoj.getString("name"));
                    bundle.putString("user_location", locationoj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            if(object.has("gender")){
                try{
                    Log.d(MainActivity.class.getSimpleName(),"getFacebookData: "+object.getString("gender"));
                    bundle.putString("user_gender",object.getString("gender"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            if(object.has("hometown")) {
                try {
                    JSONObject homelocation = object.getJSONObject("hometown");
                    Log.d(MainActivity.class.getSimpleName(), "getFacebookData :" + homelocation.getString("name"));
                    bundle.putString("user_hometown", homelocation.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (object.has("name")) {
                try {
                    bundle.putString("name", object.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_FACEBOOK_SIGN_IN) {
            fb_loginButton.setVisibility(View.GONE);

        }


    }
}


