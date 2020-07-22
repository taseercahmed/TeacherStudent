package com.teacherstudent;//package com.studentlms;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.smarteist.autoimageslider.SliderViewAdapter;
//
//public class SliderAdapterExample extends SliderViewAdapter{
//
//    String myEmail;
//    FirebaseAuth mAuth;
//    FirebaseUser fu;
//
//    private Context context;
//
//    public SliderAdapterExample(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_teacher_slider, null);
//        return new SliderAdapterVH(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(final SliderAdapterVH viewHolder, int position) {
//        viewHolder.slider_notification_title.setText("This is slider item " + position);
//
//        mAuth = FirebaseAuth.getInstance();
//        fu = mAuth.getCurrentUser();
//        myEmail = fu.getEmail();
//
//        switch (position) {
//            case 0:
//
//                final int[] i = {1};
//                viewHolder.slider_notification_title.setText("Diary:");
//                final DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference()
//                        .child("Teacher")
//                        .child(myEmail.replace(".","_dot_"))
//                        .child("Recents")
//                        .child("Diary");
//                dbr1.limitToLast(2).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if (dataSnapshot.exists()) {
//                            i[0]++;
//                            if(i[0] == 2){
//                                viewHolder.slider_message.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date.setText(dataSnapshot.child("date").getValue().toString());
//                            }if(i[0] == 3){
//                                viewHolder.slider_message2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date2.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message2.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time2.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date2.setText(dataSnapshot.child("date").getValue().toString());
//                            }
//                        }else {
//                            viewHolder.slider_message.setText("Empty");
//                            viewHolder.slider_time.setText("Empty");
//                            viewHolder.slider_date.setText("Empty");
//                        }
//                    }
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });
//
//                viewHolder.teacher_slider_cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent1 = new Intent(context,DiarySubjects.class);
//                        intent1.putExtra("nextActivityName","Diary");
//                        context.startActivity(intent1);
//                    }
//                });
//                break;
//
//
//
//            case 1:
//
//                final int[] j = {1};
//                viewHolder.slider_notification_title.setText("Notes:");
//                final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
//                        .child("Teacher")
//                        .child(myEmail.replace(".","_dot_"))
//                        .child("Recents")
//                        .child("Notes");
//                dbr2.limitToLast(2).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if (dataSnapshot.exists()) {
//                            j[0]++;
//                            if(j[0] == 2){
//                                viewHolder.slider_message.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date.setText(dataSnapshot.child("date").getValue().toString());
//                            }if(j[0] == 3){
//                                viewHolder.slider_message2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date2.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message2.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time2.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date2.setText(dataSnapshot.child("date").getValue().toString());
//                            }
//                        }else {
//                            viewHolder.slider_message.setVisibility(View.GONE);
//                            viewHolder.slider_time.setVisibility(View.GONE);
//                            viewHolder.slider_date.setVisibility(View.GONE);
//                            /*viewHolder.slider_message.setText("Empty");
//                            viewHolder.slider_time.setText("Empty");
//                            viewHolder.slider_date.setText("Empty");*/
//                        }
//                    }
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });
//                viewHolder.teacher_slider_cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent3 = new Intent(context,DiarySubjects.class);
//                        intent3.putExtra("nextActivityName","Notes");
//                        context.startActivity(intent3);
//                    }
//                });
//                break;
//
//
//
//            case 2:
//                final int[] k = {1};
//                viewHolder.slider_notification_title.setText("ReceivedNotifications:");
//                final DatabaseReference dbr3 = FirebaseDatabase.getInstance().getReference()
//                        .child("Teacher")
//                        .child(myEmail.replace(".","_dot_"))
//                        .child("Notification")
//                        .child("Admin");
//                dbr3.limitToLast(2).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if (dataSnapshot.exists()) {
//                            k[0]++;
//                            if(k[0] == 2){
//                                viewHolder.slider_message.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date.setText(dataSnapshot.child("date").getValue().toString());
//                            }if(k[0] == 3){
//                                viewHolder.slider_message2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date2.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message2.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time2.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date2.setText(dataSnapshot.child("date").getValue().toString());
//                            }
//                        }else {
//                            viewHolder.slider_message.setText("Empty");
//                            viewHolder.slider_time.setText("Empty");
//                            viewHolder.slider_date.setText("Empty");
//                        }
//                    }
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });
//                viewHolder.teacher_slider_cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        context.startActivity(new Intent(context,ReceivedNotifications.class));
//                    }
//                });
//                break;
//
//
//
//            case 3:
//                final int[] l = {1};
//                viewHolder.slider_notification_title.setText("SentNotification:");
//                final DatabaseReference dbr4 = FirebaseDatabase.getInstance().getReference()
//                        .child("Teacher")
//                        .child(myEmail.replace(".","_dot_"))
//                        .child("Recents")
//                        .child("SentNotification");
//                dbr4.limitToLast(2).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        if (dataSnapshot.exists()) {
//                            l[0]++;
//                            if(l[0] == 2){
//                                viewHolder.slider_message.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date.setText(dataSnapshot.child("date").getValue().toString());
//                            }if(l[0] == 3){
//                                viewHolder.slider_message2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_time2.setVisibility(View.VISIBLE);
//                                viewHolder.slider_date2.setVisibility(View.VISIBLE);
//
//                                viewHolder.slider_message2.setText(dataSnapshot.child("message").getValue().toString());
//                                viewHolder.slider_time2.setText(dataSnapshot.child("time").getValue().toString());
//                                viewHolder.slider_date2.setText(dataSnapshot.child("date").getValue().toString());
//                            }
//                        }else {
//                            viewHolder.slider_message.setText("Empty");
//                            viewHolder.slider_time.setText("Empty");
//                            viewHolder.slider_date.setText("Empty");
//                        }
//                    }
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {}
//                });
//                viewHolder.teacher_slider_cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent3 = new Intent(context,DiarySubjects.class);
//                        intent3.putExtra("nextActivityName","SentNotifications");
//                        context.startActivity(intent3);
//                    }
//                });
//                break;
//
//            default:
//                break;
//        }
//    }
//
//
//
//    @Override
//    public int getCount() {
//        return 4;
//    }
//    class SliderAdapterVH extends ViewHolder{
//        CardView teacher_slider_cardView;
//        View itemView;
//        TextView slider_notification_title,slider_message,slider_time,slider_date;
//        TextView slider_message2,slider_time2,slider_date2;
//        public SliderAdapterVH(View itemView) {
//            super(itemView);
//            slider_notification_title = itemView.findViewById(R.id.slider_notification_title);
//
//            slider_message = itemView.findViewById(R.id.slider_message);
//            slider_time = itemView.findViewById(R.id.slider_time);
//            slider_date = itemView.findViewById(R.id.slider_date);
//
//            slider_message2 = itemView.findViewById(R.id.slider_message2);
//            slider_time2 = itemView.findViewById(R.id.slider_time2);
//            slider_date2 = itemView.findViewById(R.id.slider_date2);
//
//            teacher_slider_cardView = itemView.findViewById(R.id.teacher_slider_cardView);
//            this.itemView = itemView;
//        }
//
//
//    }
//}