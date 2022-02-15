package edu.neu.numad21fa_nikhilmollay;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A view holder that holds individual
 */
public class LinkViewHolder extends RecyclerView.ViewHolder {

    public TextView linkName;
    public TextView link;

    public LinkViewHolder(@NonNull View itemView, final LinkClickListener listener) {
        super(itemView);
        linkName = itemView.findViewById(R.id.link_name);
        link = itemView.findViewById(R.id.actual_link);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onLinkClick(position);
                    }
                }
            }
        });
    }
}
