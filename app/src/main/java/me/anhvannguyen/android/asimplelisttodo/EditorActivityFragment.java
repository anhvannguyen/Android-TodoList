package me.anhvannguyen.android.asimplelisttodo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import me.anhvannguyen.android.asimplelisttodo.data.TodoContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class EditorActivityFragment extends Fragment {
    public static final String TODO_ITEM = "todo_item";

    private String mActionString;
    private EditText mEditorEditText;

    public EditorActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_item:
                break;
            case android.R.id.home:
                finishEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);

        mEditorEditText = (EditText) rootView.findViewById(R.id.todo_item_edittext);
        Intent intent = getActivity().getIntent();

        Uri uri = intent.getParcelableExtra(TODO_ITEM);

        if (uri == null) {
            mActionString = Intent.ACTION_INSERT;
            getActivity().setTitle(getString(R.string.new_item_title));
        }

        return rootView;
    }

    private void finishEdit() {
        String newText = mEditorEditText.getText().toString().trim();

        switch (mActionString) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                } else {
                    insertItem(newText);
                }
                break;

        }
        getActivity().finish();
    }

    private void insertItem(String todoText) {
        ContentValues newValue = new ContentValues();

        newValue.put(TodoContract.TodoEntry.COLUMN_TEXT, todoText);
        getActivity().getContentResolver().insert(
                TodoContract.TodoEntry.CONTENT_URI,
                newValue
        );
        getActivity().setResult(Activity.RESULT_OK);
    }

    public void onBackPressed() {
        finishEdit();
    }
}
