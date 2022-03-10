package edu.neu.numad21fa_nikhilmollay;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ServiceActivity extends AppCompatActivity {
    private final Handler textHandler = new Handler();
    private TextView searchTitle;
    private ScrollView booksScrollView;
    private TextView title;
    private TextView year;
    private TextView author_name;
    private TextView isbn;
    private TextView publisher;
    private TextView first_sentence;
    private TextView language;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_activity);
        title = findViewById(R.id.title);
        year = findViewById(R.id.released_year);
        author_name = findViewById(R.id.author_name);
        isbn = findViewById(R.id.isbn);
        publisher = findViewById(R.id.publisher);
        first_sentence = findViewById(R.id.first_sentence);
        language = findViewById(R.id.language);
        searchTitle = findViewById(R.id.searchTitle);
        booksScrollView = findViewById(R.id.scrollViewService);
        progressBar = findViewById(R.id.serviceProgressBar);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (!title.getText().toString().isEmpty()) {
            savedInstanceState.putString("title", title.getText().toString().split(":", 2)[1].trim());
            savedInstanceState.putString("year", year.getText().toString().split(":", 2)[1].trim());
            savedInstanceState.putString("author", author_name.getText().toString().split(":", 2)[1].trim());
            savedInstanceState.putString("isbn", isbn.getText().toString().split(":", 2)[1].trim());
            savedInstanceState.putString("publisher", publisher.getText().toString().split(":", 2)[1].trim());
            if(first_sentence.getText().length() > 0){
                savedInstanceState.putString("first_sentence",
                        first_sentence.getText().toString()
                                .split(":", 2)[1].trim().length() < 2 ? "":first_sentence.getText().toString()
                                .split(":", 2)[1].trim());

            }
            else{
                savedInstanceState.putString("first_sentence","");
            }
            if(language.getText().length() > 0){
                savedInstanceState.putString("language", language.getText().toString().split(":", 2)[1].trim());
            }
            else{
                savedInstanceState.putString("language","");
            }

        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.get("title") != null) {
            title.setText("TITLE : " + savedInstanceState.get("title").toString());
            year.setText("Released Year : " + savedInstanceState.get("year").toString());
            author_name.setText("Author : " + savedInstanceState.get("author").toString());
            isbn.setText("ISBN : " + savedInstanceState.get("isbn").toString());
            publisher.setText("Publisher : " + savedInstanceState.get("publisher").toString());
            first_sentence.setText("First Sentence : " + savedInstanceState.get("first_sentence").toString());
            language.setText("Language : " + savedInstanceState.get("language").toString());
            booksScrollView.setVisibility(View.VISIBLE);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onMovieSearchClick(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        progressBar.setVisibility(View.INVISIBLE);
        booksScrollView.setVisibility(View.INVISIBLE);
        if (searchTitle.getText().toString().isEmpty()) {
            resetEnvironment();
            Snackbar snack = Snackbar.make(view, "Enter a book title", Snackbar.LENGTH_LONG).setAction("Action", null);
            View snackView = snack.getView();
            TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snack.show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ServiceThread serviceThread = new ServiceThread();
        new Thread(serviceThread).start();
    }

    private void resetEnvironment() {
        title.setText("");
        year.setText("");
        author_name.setText("");
        isbn.setText("");
        publisher.setText("");
        first_sentence.setText("");
        language.setText("");
    }

    class ServiceThread implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL("https://openlibrary.org/search.json?title=" + searchTitle.getText());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                String response = streamToString(conn.getInputStream());
                JSONObject getBooks = new JSONObject(response);
                JSONArray books = getBooks.getJSONArray("docs");
                books.getJSONObject(0).get("isbn");
                textHandler.post(() -> {
                    try {
                        if (getBooks.has("Error") || getBooks.length() == 0 ||
                                books.getJSONObject(0).get("first_publish_year").toString().length() == 0) {
                            resetEnvironment();
                            View currentLayout = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar.make(currentLayout, "Couldn't find book in service",Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);
                            snackbarTextView.setTextColor(Color.RED);
                            snackbarTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            snackbar.show();
                            return;
                        } else {
                            title.setText("TITLE : " + books.getJSONObject(0).get("title").toString());
                            year.setText("Released Year : " + books.getJSONObject(0).get("first_publish_year").toString());
                            author_name.setText("Author : " + books.getJSONObject(0).getJSONArray("author_name").get(0).toString());
                            isbn.setText("ISBN : " + books.getJSONObject(0).getJSONArray("isbn").get(0));
                            publisher.setText("Publisher : " + books.getJSONObject(0).getJSONArray("publisher").get(0).toString());
                            try{
                                first_sentence.setText("First Sentence : " + books.getJSONObject(0).getJSONArray("first_sentence").get(0).toString());
                            }
                            catch (JSONException e){
                                //Do not do anything if this attribute not found
                            }
                            try{
                                language.setText("Language : " + books.getJSONObject(0).getJSONArray("language").get(0).toString());
                            }
                            catch (Exception e){

                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            booksScrollView.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        resetEnvironment();
                        System.out.println(e);
                        View currentLayout = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(currentLayout, "Couldn't find book in service",Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);
                        snackbarTextView.setTextColor(Color.RED);
                        snackbarTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snackbar.show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception e) {
                resetEnvironment();
                System.out.println(e);
                View currentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(currentLayout, "Couldn't find book in service",Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);
                snackbarTextView.setTextColor(Color.RED);
                snackbarTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        private String streamToString(InputStream inputStream) {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }
}
