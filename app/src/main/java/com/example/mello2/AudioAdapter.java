package com.example.mello2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.viewHolder> {
    Context context;
    ArrayList<ModelAudio> audioArrayList;
    public OnItemClickListener onItemClickListener;

    public AudioAdapter(Context context, ArrayList<ModelAudio> audioArrayList) {
        this.context = context;
        this.audioArrayList = audioArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int i) {
        holder.title.setText(audioArrayList.get(i).getaudioTitle());
        holder.artist.setText(audioArrayList.get(i).getaudioArtist());
    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        ImageView delete, edit;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}

