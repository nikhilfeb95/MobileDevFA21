package edu.neu.numad21fa_nikhilmollay;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
    private TextView loading;
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
        loading = findViewById(R.id.loading);
        searchTitle = findViewById(R.id.searchTitle);
        booksScrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.serviceProgressBar);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        if (!title.getText().toString().isEmpty()) {
            savedInstanceState.putString("title", title.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("year", year.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("author", author_name.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("isbn", isbn.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("publisher", publisher.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("first_sentence", first_sentence.getText().toString().split(":", 2)[1]);
            savedInstanceState.putString("language", language.getText().toString().split(":", 2)[1]);
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
        loading.setText("Searching...");
        loading.setVisibility(View.INVISIBLE);
        booksScrollView.setVisibility(View.INVISIBLE);
        if (searchTitle.getText().toString().isEmpty()) {
            resetData();
            Snackbar snack = Snackbar.make(view, "Movie title required!", Snackbar.LENGTH_LONG).setAction("Action", null);
            View snackView = snack.getView();
            TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snack.show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        callWebService();
    }

    private void callWebService() {
        WebServiceThread webServiceThread = new WebServiceThread();
        new Thread(webServiceThread).start();
    }

    private void resetData() {
        title.setText("");
        year.setText("");
        author_name.setText("");
        isbn.setText("");
        publisher.setText("");
        first_sentence.setText("");
        language.setText("");
    }

    class WebServiceThread implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL("https://openlibrary.org/search.json?title=" + searchTitle.getText());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream = conn.getInputStream();
                String response = convertStreamToString(inputStream);
                JSONObject getBooks = new JSONObject(response);
                JSONArray books = getBooks.getJSONArray("docs");
                books.getJSONObject(0).get("isbn");
                textHandler.post(() -> {
                    try {
                        if (getBooks.has("Error")) {
                            resetData();
                            loading.setText("Book not in service!");
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
                            loading.setVisibility(View.INVISIBLE);
                            booksScrollView.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private String convertStreamToString(InputStream inputStream) {
            Scanner s = new Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next().replace(",", ",\n") : "";
        }
    }
}
