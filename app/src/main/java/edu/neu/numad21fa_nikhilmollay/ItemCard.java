package edu.neu.numad21fa_nikhilmollay;

import android.content.Intent;
import android.net.Uri;

/**
 * A class that represents an item card.
 */
public class ItemCard implements LinkClickListener{

    public final String linkName;
    public final String link;

    /**
     * A constructor for the item card
     * @param linkName The nick name given to the link.
     * @param link The actual link.
     */
    public ItemCard(String linkName, String link) {
        this.linkName = linkName;
        this.link = link;
    }

    @Override
    public void onLinkClick(int position) {

    }
}
