package edu.neu.numad21fa_nikhilmollay;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A class that collects links on user input.
 */
public class LinkCollector extends AppCompatActivity {
    private ArrayList<ItemCard> itemList = new ArrayList<>();
    ;

    private RecyclerView recyclerView;
    private LinkViewAdapter linkViewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FloatingActionButton addButton;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_collector);

        init(savedInstanceState);

        addButton = findViewById(R.id.addLinkButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = 0;
                addLinkUsingAlert(view, position);
            }
        });
    }

    private void init(Bundle savedInstanceState){
        createRecyclerView();
    }

    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);
        for (int i = 0; i < size; i++) {
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getLinkName());
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getLink());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Set the recycler view for the activity.
     */
    private void createRecyclerView(){
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        linkViewAdapter = new LinkViewAdapter(itemList);

        LinkClickListener linkClickListener = new LinkClickListener() {
            @Override
            public void onLinkClick(int position) {
                String linkUrl = itemList.get(position).getLink().toLowerCase(Locale.ROOT);

                if(!linkUrl.startsWith("http") && !linkUrl.startsWith("https")){
                    linkUrl = "http://" + linkUrl;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl));
                startActivity(intent);
            }
        };
        linkViewAdapter.setOnItemClickListener(linkClickListener);
        recyclerView.setAdapter(linkViewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }


    private void addLinkUsingAlert(View view, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Link Collector");
        builder.setMessage("Enter link name and link url");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText linkNameEdit = new EditText(this);
        linkNameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        linkNameEdit.setSingleLine();
        linkNameEdit.setHint("Link Name:");
        layout.addView(linkNameEdit);

        final EditText linkEdit = new EditText(this);
        linkEdit.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        linkEdit.setSingleLine();
        linkEdit.setHint("Link URL:");
        layout.addView(linkEdit);

        builder.setView(layout);

        builder.setPositiveButton("Add Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String linkName = linkNameEdit.getText().toString();
                String message = "";
                String linkUrl = linkEdit.getText().toString();

                if(Patterns.WEB_URL.matcher(linkUrl).matches()){
                    itemList.add(position, new ItemCard(linkName, linkUrl));
                    linkViewAdapter.notifyItemChanged(position);
                    message = "Link successfully added";
                }
                else{
                    message = "URL not valid!";
                }
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
                View snackView = snackbar.getView();
                TextView snackTextView = snackView.findViewById(R.id.snackbar_text);
                snackTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
}
