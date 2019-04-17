package com.devschool.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devschool.R;
import com.devschool.view.MainView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.devschool.data.Constants.SERVER;
import static com.devschool.utils.ItemUtils.isRead;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {
    private SearchFilter filter;
    private ArrayList<String> itemsText;
    private ArrayList<String> itemsSrc;
    private MainView mainView;

    public ListAdapter(ArrayList<String> itemsText, ArrayList<String> itemsSrc, MainView mainView) {
        this.itemsText = itemsText;
        this.itemsSrc = itemsSrc;
        this.mainView = mainView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) filter = new SearchFilter();
        return filter;
    }

    @Override
    public int getItemCount() {
        return itemsText.size();
    }

    @Override
    public void onBindViewHolder(@NotNull final ListAdapter.ViewHolder holder, final int position) {
        final String text = itemsText.get(position);
        final String url = SERVER + itemsSrc.get(position);

        holder.itemText.setText(text);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                mainView.openLesson(url, holder.getAdapterPosition());
            }
        });

        holder.checkMark.setVisibility(isRead(url) ? View.VISIBLE : View.GONE);
    }

    @NotNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int p2) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView itemText;
        ImageView checkMark;

        ViewHolder(View view) {
            super(view);
            item = view.findViewById(R.id.list_item);
            itemText = view.findViewById(R.id.item_text);
            checkMark = view.findViewById(R.id.item_read_sign);
        }
    }

    private class SearchFilter extends Filter {
        private ArrayList<String> itemsText_backup = itemsText;
        private ArrayList<String> itemsSrc_backup = itemsSrc;
        private ArrayList<String> filteredItemsText = new ArrayList<>();
        private ArrayList<String> filteredItemsSrc = new ArrayList<>();

        @Override
        protected Filter.FilterResults performFiltering(CharSequence p1) {
            filteredItemsText.clear();
            filteredItemsSrc.clear();

            for (int x = 0; x < itemsText_backup.size(); x++) {
                String query = p1.toString().toLowerCase();
                if (itemsText_backup.get(x).toLowerCase().contains((query))) {
                    filteredItemsText.add(itemsText_backup.get(x));
                    filteredItemsSrc.add(itemsSrc_backup.get(x));
                }
            }
            return null;
        }

        @Override
        protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
            itemsText = filteredItemsText;
            itemsSrc = filteredItemsSrc;
            notifyDataSetChanged();
        }
    }
}
