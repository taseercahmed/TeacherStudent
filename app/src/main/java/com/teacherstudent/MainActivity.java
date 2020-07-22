package com.teacherstudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.agik.AGIKSwipeButton.Controller.OnSwipeCompleteListener;
import com.agik.AGIKSwipeButton.View.Swipe_Button_View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.teacherstudent.Service.MyFirebaseIdService;
import com.teacherstudent.Student.StudentMainActivity;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    private Button mSigninBtn;
    TextView forgetPassword;
    private TextInputEditText mEmail,mPassword;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;

    public View resetView;
    public TextInputEditText resetEmail;
    public Button resetPassBtn,cancelBtn;
    public TextView signUpBtn;
    public ConstraintLayout loginPage;

    public String type="";
    public AlertDialog alertDialog;

    private Swipe_Button_View start_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        mSigninBtn=findViewById(R.id.loginBtn);
        forgetPassword=findViewById(R.id.forgetPasswordBtn);
        mEmail=findViewById(R.id.loginEmail);
        mPassword=findViewById(R.id.loginPassword);
        resetView = findViewById(R.id.forgetPassPage);
        resetEmail = findViewById(R.id.resetEmail);
        resetPassBtn = findViewById(R.id.resetPassBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        //signUpBtn = findViewById(R.id.forgetPasswordBtn);
        loginPage = findViewById(R.id.loginPage);

        start_stop = findViewById(R.id.start_stop);
        FirebaseApp.initializeApp(getApplicationContext());

        //initialize FirebaseAuth
        mAuth= FirebaseAuth.getInstance();

        //chk if user is already login or not if login then proceed to MainActivity
        listener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =mAuth.getCurrentUser();
                if(user!=null){
                    if (user.getDisplayName().equals("Teacher")) {
                        Intent intent = new Intent(MainActivity.this, ActivityTeacher.class);
                        startActivity(intent);
                        finish();
                    }
                    if (user.getDisplayName().equals("Student")) {
                        Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
       // FirebaseAuth.getInstance().addAuthStateListener(listener);
        //FirebaseAuth.getInstance().removeAuthStateListener(listener);


        //for loading alert dialog
        alertDialog = new SpotsDialog.Builder()
                .setContext(MainActivity.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();

        //check if fields are filled then signin and chk verification
        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                //mAuth.removeAuthStateListener(listener);
                final String email=mEmail.getText().toString();
                final String password=mPassword.getText().toString();

                if(check()){
                    if (mSigninBtn.getText().toString().toLowerCase().contains("login")) {
                      if (mAuth!=null){
                          mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                              @Override
                              public void onSuccess(AuthResult authResult) {
                                  checkIfEmailVerified();

                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                  alertDialog.dismiss();
                              }
                          });
                      }else{
                          alertDialog.dismiss();
                        //  Intent intent=new Intent(MainActivity.this, StudentMainActivity.class);
                          //startActivity(intent);
                          //finish();
                          //Toast.makeText(MainActivity.this, "null vafjsk", Toast.LENGTH_SHORT).show();
                      }
                    }else if(mSigninBtn.getText().toString().toLowerCase().contains("sign")){
                        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Toast.makeText(MainActivity.this,"Account created Successfully",Toast.LENGTH_SHORT).show();
                                sendVerificationEmail();
                                alertDialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.dismiss();
                    }
                }else {
                    alertDialog.dismiss();
                }
            }
        });



        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email=resetEmail.getText().toString().trim();

                if(!TextUtils.isEmpty(Email)){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            // alert for changin password
                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                            builder2.setTitle("Done");
                            builder2.setMessage("Check Your E-mail.");
                            builder2.setCancelable(false);
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelBtn.setPressed(true);
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog alert12 = builder2.create();
                            alert12.show();

                            //Toast.makeText(MainActivity.this,"Successful, Check your Email",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                }else {
                    resetEmail.setError("Empty");
                }

            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resetView.getVisibility()==View.GONE){              // changes
                    resetView.setVisibility(View.VISIBLE);
                    Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down_animation);
                    loginPage.startAnimation(animZoomIn);

                    mSigninBtn.setVisibility(View.GONE);
                    start_stop.setVisibility(View.GONE);
                }
                //resetPassword();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resetView.getVisibility()==View.VISIBLE){
                    Animation animZoomout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up_animation);
                    loginPage.startAnimation(animZoomout);
                    resetView.setVisibility(View.GONE);
                    mSigninBtn.setVisibility(View.VISIBLE);
                    start_stop.setVisibility(View.VISIBLE);
                }
            }
        });



        start_stop.setOnSwipeCompleteListener_forward_reverse(new OnSwipeCompleteListener() {
            @Override
            public void onSwipe_Forward(Swipe_Button_View swipeView) {
                start_stop.setText("Swipe for Login");
                mSigninBtn.setText("Sign Up");
                start_stop.setThumbBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                start_stop.setSwipeBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            }

            @Override
            public void onSwipe_Reverse(Swipe_Button_View swipeView) {
                start_stop.setText("Swipe for Sign Up");
                mSigninBtn.setText("Login");
                start_stop.setThumbBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));
                start_stop.setSwipeBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            }
        });

    }

    private boolean check(){
        boolean result=true;
        final String Email=mEmail.getText().toString().trim();
        final String password=mPassword.getText().toString();

        if(TextUtils.isEmpty(Email)){

            mEmail.setError("Email is empty");
            result=false;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is empty");
            result=false;
        }

        return result;
    }

    //send verification email
    public void sendVerificationEmail(){
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            Toast.makeText(MainActivity.this,"Please verify your email and Log In",Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();

                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    //Check if email verified or not (if verified then open MainActivity else open dialogue to send verification email
    private void checkIfEmailVerified(){
        final String Email=mEmail.getText().toString().trim();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final MyFirebaseIdService myFirebaseIdService=new MyFirebaseIdService();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.

            DatabaseReference emailType = FirebaseDatabase.getInstance().getReference()             // save to receiver mail
                    .child("User")
                    .child(Email.replace(".","_dot_"));
            emailType.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        type = dataSnapshot.child("type").getValue().toString();
                        Log.e("112233","hmza"+type);


                        switch (type){
                            case "Student":

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("Student").build();
                                user.updateProfile(profileUpdates);

                                //registerToken();

                                Toast.makeText(MainActivity.this, "Type: "+ type, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                myFirebaseIdService.updateTokenServer( FirebaseInstanceId.getInstance().getToken());
                                Intent intent=new Intent(MainActivity.this, StudentMainActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case "Teacher":
                                UserProfileChangeRequest profileUpdates2 = new UserProfileChangeRequest.Builder().setDisplayName("Teacher").build();
                                user.updateProfile(profileUpdates2);
                                myFirebaseIdService.updateTokenServer( FirebaseInstanceId.getInstance().getToken());

                                //registerToken();

                                Toast.makeText(MainActivity.this, "Type: "+ type, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Intent intent2=new Intent(MainActivity.this,ActivityTeacher.class);
                                startActivity(intent2);
                                finish();
                                break;
                            case "Parent":
                                alertDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Type: "+ type, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "User don't exits. Contact admin.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            Toast.makeText(MainActivity.this, "Email not Verified", Toast.LENGTH_SHORT).show();

            //Dialog to send verification email or not
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            sendVerificationEmail();
                            // NOTE: don't forget to log out the user.
                            FirebaseAuth.getInstance().signOut();
                            //startActivity(new Intent(LoginActivity.this, LoginActivity.class));


                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            // NOTE: don't forget to log out the user.
                            FirebaseAuth.getInstance().signOut();
                            //restart this activity
                            //startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Send Verification Email?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();



            alertDialog.dismiss();
        }
    }

    public void swipeclicked(View view) {
        if (mSigninBtn.getText().toString().toLowerCase().contains("login")){
            start_stop.setreverse_180();
            mSigninBtn.setText("Sign Up");
            start_stop.setText("Swipe for Login");
        }
        else if (mSigninBtn.getText().toString().toLowerCase().contains("sign")){
            start_stop.setreverse_0();
            mSigninBtn.setText("Login");
            start_stop.setText("Swipe for Sign Up");
        }
    }


    /*private void registerToken() {
        Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();

        DateFormat df1 = new SimpleDateFormat("h:mm a dd-MM-yyyy");
        String currentDateTime = df1.format(Calendar.getInstance().getTime());

        OkHttpClient client = new OkHttpClient();
        MyFirebaseInstanceService firebaseInstanceService=new MyFirebaseInstanceService();
        RequestBody body = new FormBody.Builder()
                .add("token",firebaseInstanceService.returnTokenf())
                .add("email",FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .add("type",mAuth.getCurrentUser().getDisplayName())
                .add("date",currentDateTime)
                .build();


        Log.d("url","https://bvoir.website/serverFolder/register.php");
        Request request = new Request.Builder()
                .url("https://bvoir.website/serverFolder/register.php")
                .post(body)
                .build();

        try {
            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
//your codes here
                client.newCall(request).execute();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*public class MyFirebaseInstanceService extends FirebaseInstanceIdService {

        private static final String TAG="MyFirebaseInstanceServi";
        String refreshedToken="";
        public String returnTokenf(){
             refreshedToken= FirebaseInstanceId.getInstance().getToken();
            return refreshedToken;
        }

        @Override
        public void onTokenRefresh() {
// Get updated InstanceID token.

            FirebaseMessaging.getInstance().subscribeToTopic("all");
            Log.d(TAG, "Refreshed token: " + refreshedToken);

             If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.
//saveTokentoServer(refreshedToken);
            sendRegistrationToServer(refreshedToken);
        }

        private void sendRegistrationToServer(String refreshedToken) {
            Log.d("TOKEN ", refreshedToken.toString());
        }
    }*/

}

