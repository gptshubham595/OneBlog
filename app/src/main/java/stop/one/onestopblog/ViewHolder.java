package stop.one.onestopblog;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView blog_name, author_name,user_name,comment_body;
    ImageView blog_image;
    LinearLayout comment_layout,card_layout;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        blog_name = itemView.findViewById(R.id.blog_name);
        author_name = itemView.findViewById(R.id.author_name);
        blog_image = itemView.findViewById(R.id.blog_image);
        user_name = itemView.findViewById(R.id.user_name);
        comment_body = itemView.findViewById(R.id.comment_body);
        comment_layout = itemView.findViewById(R.id.comment_layout);
        card_layout = itemView.findViewById(R.id.card_layout);


    }
}


