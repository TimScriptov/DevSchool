package com.devschool.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devschool.R;
import com.devschool.adapters.BookmarksAdapter;
import com.devschool.data.Bookmarks;
import com.devschool.view.MainView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BookmarksFragment extends DialogFragment {
    private ArrayList<String> itemsSrc = new ArrayList<>();
    private ArrayList<String> itemsText = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = Bookmarks.getAllBookmarks();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                itemsSrc.add(cursor.getString(0));
                itemsText.add(cursor.getString(1));
            }
        }
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.bookmarks_fragment, null);

        if (itemsText.size() > 0) {
            RecyclerView rcview = view.findViewById(R.id.bookmarksList);
            rcview.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcview.setAdapter(new BookmarksAdapter(itemsText, itemsSrc, this, (MainView) getActivity()));
            rcview.setVisibility(View.VISIBLE);
        } else view.findViewById(R.id.no_bookmarks).setVisibility(View.VISIBLE);

        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }
}
