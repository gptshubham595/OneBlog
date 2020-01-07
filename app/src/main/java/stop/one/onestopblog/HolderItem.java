package stop.one.onestopblog;

public class HolderItem{

    public HolderItem() {
    }

    String blogname;
    String authorname;
    String blogimage;
    String username;
    String commentbody;

    public HolderItem(String blogname, String authorname, String blogimage, String username, String commentbody) {
        this.blogname = blogname;
        this.authorname = authorname;
        this.blogimage = blogimage;
        this.username = username;
        this.commentbody = commentbody;
    }

    public String getBlogname() {
        return blogname;
    }

    public String getAuthorname() {
        return authorname;
    }

    public String getBlogimage() {
        return blogimage;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentbody() {
        return commentbody;
    }
}
