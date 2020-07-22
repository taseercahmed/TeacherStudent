package com.teacherstudent.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseIdService extends FirebaseInstanceIdService {
String tokenserver="";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
       //
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        tokenserver= FirebaseInstanceId.getInstance().getToken();
               updateTokenServer(tokenserver);
       // updateTokenServer(refreshedToken);  // for uploading token to realtime database
    }

    public void updateTokenServer(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference tokens = db.getReference("Tokens");

        Map port = new HashMap();
        port.put("token",token);

        //Token token = new Token(refreshedToken);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)        // if already login. then it must update Token
        {
            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_dot_")).updateChildren(port);
        }
    }

}
