package stop.one.onestopblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Blog_page extends AppCompatActivity {

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    TextView blog, cover_text_blogname, cover_text_authorname;
    EditText comment_place;
    ImageView comment_iv;
    LinearLayout cover_image;

    //Set here user id or roll no
    public String userid="190123063";

    public String url, docid, blogName, authorName,uniqedoc;

    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_page);

        Intent i = getIntent();
        docid = i.getStringExtra("docID");
        blogName = i.getStringExtra("blogName");
        authorName = i.getStringExtra("authorName");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("blogs").document(docid).collection("blog_detail")
                .document("blog").collection("comment");
        recyclerView.hasFixedSize();

        blog = findViewById(R.id.blog);
        cover_image = findViewById(R.id.cover_image);
        comment_place = findViewById(R.id.comment_place);
        comment_iv = findViewById(R.id.comment_iv);
        cover_text_blogname = findViewById(R.id.cover_text_blogname);
        cover_text_authorname = findViewById(R.id.cover_text_authorname);

        // Attaching the layout to the toolbar.xml object
        toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorToolbar));
        // Setting toolbar.xml as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        FirebaseFirestore coverphoto = FirebaseFirestore.getInstance();

        coverphoto.collection("blogs").document(docid)
                .collection("blog_detail").document("blog")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        url = document.get("cover_photo").toString();
                        blog.setText(document.get("blog").toString());
                        cover_text_blogname.setText(blogName);
                        cover_text_authorname.setText(authorName);


                        Picasso.with(Blog_page.this).load(url).noFade().into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                cover_image.setBackground(new BitmapDrawable(Blog_page.this.getResources(), bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                // Implement Logic
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                // Implement Logic
                            }
                        });

                    }
                }
            }
        });

        final FirestoreRecyclerOptions<HolderItem> options = new FirestoreRecyclerOptions.Builder<HolderItem>()
                .setQuery(query, HolderItem.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<HolderItem, ViewHolder>(options) {


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Blog_page.this).inflate(R.layout.comment_layout, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull HolderItem holderItem) {

                viewHolder.user_name.setText(holderItem.getUsername());
                viewHolder.comment_body.setText(holderItem.getCommentbody());
                viewHolder.comment_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String documentid = options.getSnapshots().getSnapshot(i).getId();
                        if(documentid.substring(0,9).equals(userid))
                        {
                            new AlertDialog.Builder(Blog_page.this)
                                    .setMessage("You want to delete the comment?")
                                    .setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which
                                                ) {
                                                    deletecomment();
                                                    dialog.cancel();
                                                }
                                            }).setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.cancel();
                                        }
                                    }).show();
                        }

                        return false;
                    }

                    private void deletecomment() {

                        FirebaseFirestore d_b = FirebaseFirestore.getInstance();

                        d_b.collection("blogs").document(docid).collection("blog_detail")
                                .document("blog").collection("comment").document(options.getSnapshots().getSnapshot(i).getId())
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);

        comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentdata = comment_place.getText().toString();
//                store current user name from session manager in "username"
                String usernamme = "user";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                uniqedoc=userid+currentDateandTime;

                if (!commentdata.isEmpty()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("commentbody", commentdata);
                    user.put("username", usernamme);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("blogs").document(docid).collection("blog_detail")
                            .document("blog").collection("comment").document(uniqedoc)
                            .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    comment_place.setText("");
                } else
                    Toast.makeText(getApplicationContext(), "Feild Empty", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onStart() {

        if (adapter != null)
            adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        if (adapter != null)
            adapter.startListening();
        super.onPostResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
