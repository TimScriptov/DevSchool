package com.devschool.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.devschool.R;
import com.devschool.data.Bookmarks;
import com.devschool.ui.BookmarksFragment;
import com.devschool.view.MainView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.devschool.utils.ItemUtils.isRead;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private ArrayList<String> itemsText;
    private ArrayList<String> itemsSrc;
    private MainView mainView;
    private BookmarksFragment bookmarksFragment;

    public BookmarksAdapter(ArrayList<String> itemsText, ArrayList<String> itemsSrc, BookmarksFragment bookmarksFragment, MainView mainView) {
        this.itemsText = itemsText;
        this.itemsSrc = itemsSrc;
        this.bookmarksFragment = bookmarksFragment;
        this.mainView = mainView;
    }

    @Override
    public int getItemCount() {
        return itemsText.size();
    }

    @Override
    public void onBindViewHolder(@NotNull final BookmarksAdapter.ViewHolder holder, final int position) {
        final String text = itemsText.get(position);
        final String url = itemsSrc.get(position);

        holder.itemText.setText(text);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                bookmarksFragment.dismiss();
                mainView.openLesson(url, holder.getAdapterPosition());
            }
        });

        holder.checkMark.setVisibility(isRead(url) ? View.VISIBLE : View.GONE);
        holder.removeBookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bookmarks.remove(url);
                itemsText.remove(holder.getAdapterPosition());
                itemsSrc.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                if (getItemCount() == 0) bookmarksFragment.dismiss();
            }
        });
    }

    @NotNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int p2) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_item, parent, false);
        return new ViewHolder(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView itemText;
        ImageView checkMark;
        SwipeLayout swipeLayout;
        AppCompatImageButton removeBookmarkButton;

        ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.list_item);
            itemText = view.findViewById(R.id.item_text);
            checkMark = view.findViewById(R.id.item_read_sign);
            swipeLayout = view.findViewById(R.id.bookmark_swipe_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            removeBookmarkButton = view.findViewById(R.id.remove_bookmark);
        }
    }
}
