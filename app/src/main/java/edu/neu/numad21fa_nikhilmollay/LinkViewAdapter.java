package edu.neu.numad21fa_nikhilmollay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LinkViewAdapter extends RecyclerView.Adapter<LinkViewHolder>{
    private final ArrayList<ItemCard> itemList;
    private LinkClickListener listener;

    public LinkViewAdapter(ArrayList<ItemCard> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(LinkClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_card, parent, false);
        return new LinkViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        ItemCard currentCard = itemList.get(position);
        holder.link.setText(currentCard.getLink());
        holder.linkName.setText(currentCard.getLinkName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
