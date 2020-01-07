package stop.one.onestopblog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    LinearLayout cover_image;
    TextView cover_text_blogname,cover_text_authorname;

    public String url;

    public Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cover_image=findViewById(R.id.cover_image);
        cover_text_blogname=findViewById(R.id.cover_text_blogname);
        cover_text_authorname=findViewById(R.id.cover_text_authorname);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        
        Query query = rootRef.collection("blogs");
        recyclerView.hasFixedSize();

        // Attaching the layout to the toolbar.xml object
        toolbar =  findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorToolbar));
        // Setting toolbar.xml as the ActionBar with setSupportActionBar() call
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        FirebaseFirestore coverphoto = FirebaseFirestore.getInstance();

        DocumentReference docRef = coverphoto.collection("cover").document("image");


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cover_text_blogname.setText(document.get("blogName").toString());
                        cover_text_authorname.setText(document.get("authorName").toString());


                        Picasso.with(MainActivity.this).load(document.get("imageLink").toString()).noFade().into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                cover_image.setBackground(new BitmapDrawable(MainActivity.this.getResources(), bitmap));
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
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.card_layout, parent, false);
                return new ViewHolder(view);            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i, @NonNull HolderItem holderItem) {

                viewHolder.blog_name.setText(holderItem.getBlogname());
                viewHolder.author_name.setText(holderItem.getAuthorname());


                Picasso.with(MainActivity.this)
                        .load(holderItem.getBlogimage())
                        .into(viewHolder.blog_image , new Callback() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });

                final String blogName=holderItem.getBlogname();
                final String authorName=holderItem.getAuthorname();



                viewHolder.card_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String documentid = options.getSnapshots().getSnapshot(i).getId();
                        Intent i = new Intent(getApplicationContext(),Blog_page.class);
                        String strName = documentid;
                        i.putExtra("docID", strName);
                        i.putExtra("blogName", blogName);
                        i.putExtra("authorName", authorName);
                        startActivity(i);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        if(adapter!=null)
            adapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(adapter!=null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        if(adapter!=null)
            adapter.startListening();
        super.onPostResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}