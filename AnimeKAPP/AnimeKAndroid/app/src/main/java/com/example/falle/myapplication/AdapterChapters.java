package com.example.falle.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
public class AdapterChapters extends  RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder> {
    private Context context;
    private List<Chapters> chaptersList;


    public AdapterChapters(Context context, List<Chapters> chaptersList) {
        this.context = context;
        this.chaptersList = chaptersList;
    }

    @NonNull
    @Override
    public AdapterChapters.ChaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.episode_recycleview, parent, false);
        return new AdapterChapters.ChaptersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersViewHolder holder, final int position) {
        final Chapters c = chaptersList.get(position);
        final int season = c.getSeason();
        final int episode = c.getNumber();
        final String synopsis = c.getSynopsis();
        final String en_jp = c.getEn_jp();
        final String thumbnail = c.getThumbnail();

        holder.season.setText("Season: " + season);
        holder.episode.setText("Episode: " + episode);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EpisodeInformation.class);
                intent.putExtra("season", season);
                intent.putExtra("episode", episode);
                intent.putExtra("synopsis", synopsis);
                intent.putExtra("en_jp", en_jp);
                intent.putExtra("thumbail", thumbnail);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }

    public class ChaptersViewHolder extends RecyclerView.ViewHolder {
        TextView season;
        TextView episode;
        CardView cv;

        public ChaptersViewHolder(View itemView) {
            super(itemView);
            season = itemView.findViewById(R.id.season);
            episode = itemView.findViewById(R.id.episodeAnime);
            cv = itemView.findViewById(R.id.cv_episode);


        }
    }

}

