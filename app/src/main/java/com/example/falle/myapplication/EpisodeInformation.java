package com.example.falle.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class EpisodeInformation extends AppCompatActivity {
    TextView title;
    ImageView poster;
    TextView airdate;
    TextView season;
    TextView episode;
    TextView synopsis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_information);

        Intent intent = getIntent();
        String en_jp = intent.getStringExtra("en_jp");
        String posterThumbail = intent.getStringExtra("thumbail");
        String airdateIntent = intent.getStringExtra("fechaEstreno");
        int seasonIntent = intent.getIntExtra("season", 0);
        int episodeIntent = intent.getIntExtra("episode", 0);
        String synopsisIntent = intent.getStringExtra("synopsis");
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Sunflower-Bold.ttf");

        title = findViewById(R.id.titulo);
        poster = findViewById(R.id.poster);
        airdate = findViewById(R.id.airdate);
        season = findViewById(R.id.season);
        episode = findViewById(R.id.episode);
        synopsis = findViewById(R.id.synopsisEpisode);
        title.setTypeface(tf);

        title.setText(en_jp);
        if (posterThumbail == null) {
            poster.setImageResource(R.drawable.animelogo);
            Picasso.with(EpisodeInformation.this).load(posterThumbail).fit().centerInside().into(poster);
        } else {
            Picasso.with(EpisodeInformation.this).load(posterThumbail).fit().centerInside().into(poster);
        }

        Picasso.with(EpisodeInformation.this).load(posterThumbail).fit().centerInside().into(poster);
        airdate.setText("Start Date: " + airdateIntent);
        season.setText("Season: " + seasonIntent);
        episode.setText("Episode: " + episodeIntent);
        synopsis.setText(synopsisIntent);


    }
}
