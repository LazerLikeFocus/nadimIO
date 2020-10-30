package com.lazerlikefoucs.nadimioo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewPP, imageViewProfilePhoto, imageView_RemoveAcc, imageView_signOut;
    private Button back, yes, no, developer;
    private TextView username, toastText;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        imageViewPP = (ImageView) findViewById(R.id.imageView_PP);
        back = (Button) findViewById(R.id.back_return);
        imageViewProfilePhoto = (ImageView) findViewById(R.id.imageView_profilePhoto);
        username = (TextView) findViewById(R.id.textView_username);
        imageView_signOut = (ImageView) findViewById(R.id.imageView_signOut);
        imageView_RemoveAcc = (ImageView) findViewById(R.id.imageView_RemoveAcc);
        yes = (Button) findViewById(R.id.button_yes);
        no = (Button) findViewById(R.id.button_no);
        toastText = (TextView) findViewById(R.id.textView2);
        developer = (Button) findViewById(R.id.button_developer);


        imageViewPP.setOnClickListener(this);
        back.setOnClickListener(this);

        googleUserData();
    }

    @Override
    public void onClick(View view) {
        if (view == imageViewPP){  openUrl(); }
        if (view == back){ Intent intent = new Intent(OptionsActivity.this, AppActivity.class);  startActivity(intent); finish(); }
        if (view == imageView_signOut){ signOut(); }
        if (view == imageView_RemoveAcc){ showAccRemDialog(); }
        if (view == developer){ showDeveloperDialog(); }
    }



    //Animation
    private void showDeveloperDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_developer, null);

        yes = (Button) view.findViewById(R.id.button_yes);
        no = (Button) view.findViewById(R.id.button_no);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view).create();

        alertDialog.show();
    }

    private void showAccRemDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog, null);

        yes = (Button) view.findViewById(R.id.button_yes);
        no = (Button) view.findViewById(R.id.button_no);

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view).create();

        alertDialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void makeToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout  = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.layout_toast));


        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM ,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    private void makeToastRem() {

        LayoutInflater inflater = getLayoutInflater();
        View layout  = inflater.inflate(R.layout.toast_layout_romoveacc, (ViewGroup) findViewById(R.id.layout_toast_rem));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM ,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Privacy policy
    private void openUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://applecrabbypp.blogspot.com/p/privacy-policy-lazerlikefocus-built.html"));
        startActivity(intent);
    }

    //google sign in
    private  void  googleUserData(){
        //Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            imageView_signOut.setVisibility(View.VISIBLE);
            imageView_RemoveAcc.setVisibility(View.VISIBLE);
            developer.setEnabled(false);
            imageView_signOut.setOnClickListener(this);
            imageView_RemoveAcc.setOnClickListener(this);

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            username.setText(personName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageViewProfilePhoto);
        }
        else{
            imageView_signOut.setVisibility(View.INVISIBLE);
            imageView_RemoveAcc.setVisibility(View.INVISIBLE);
            developer.setEnabled(true);
            developer.setOnClickListener(this);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        makeToast();
                        finish();
                        //WE used finish to close the currnent activity and move to previous activity
                        //this solves the looping problem
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        makeToastRem();
                        finish();
                    }
                });
    }

}