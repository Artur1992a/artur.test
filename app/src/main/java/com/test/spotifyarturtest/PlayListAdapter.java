package com.test.spotifyarturtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton mPlayButton;
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPlayButton = (ImageButton) itemView.findViewById(R.id.play_button);
            mTextView = (TextView) itemView.findViewById(R.id.track_name);
        }
    }

    public interface OnPlayListener {
        void play(String uri);
    }

    private LayoutInflater mInflater;
    private List<String> mTrackList;

    private OnPlayListener mOnClickListener;

    public PlayListAdapter(Context context, List<String> trackList) {
        mInflater = LayoutInflater.from(context);
        mTrackList = trackList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_play_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.play(mTrackList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }

    public String getItem(int position) {
        return mTrackList.get(position);
    }

    public void setOnClickListener(OnPlayListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
