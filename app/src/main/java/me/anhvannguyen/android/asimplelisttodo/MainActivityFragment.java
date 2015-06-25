package me.anhvannguyen.android.asimplelisttodo;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.anhvannguyen.android.asimplelisttodo.data.TodoContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;
    }

    // TODO: Move off of UI thread
    public void insertTodo(String todoText) {
        ContentValues newValue = new ContentValues();

        newValue.put(TodoContract.TodoEntry.COLUMN_TEXT, todoText);
        Uri insertUri = getActivity().getContentResolver().insert(
                TodoContract.TodoEntry.CONTENT_URI,
                newValue
        );
        Toast.makeText(getActivity(), "Inserting", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "Inserted into " + insertUri.getLastPathSegment());
    }
}
