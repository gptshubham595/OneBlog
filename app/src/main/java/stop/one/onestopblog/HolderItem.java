package stop.one.onestopblog;

import androidx.recyclerview.widget.RecyclerView;

public class HolderItem{

    public HolderItem() {
    }

    String description;
    String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HolderItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
