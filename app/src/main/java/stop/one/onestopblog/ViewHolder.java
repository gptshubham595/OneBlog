package stop.one.onestopblog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_head, tv_desc;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_head = itemView.findViewById(R.id.tv_head);
        tv_desc = itemView.findViewById(R.id.tv_desc);
    }
}


