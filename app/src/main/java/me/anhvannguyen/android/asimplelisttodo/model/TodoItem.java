package me.anhvannguyen.android.asimplelisttodo.model;

/**
 * Created by anhvannguyen on 7/2/15.
 */
public class TodoItem {
    private String mText;
    private String mCreated;

    public TodoItem(String text, String created) {
        mText = text;
        mCreated = created;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }
}
