package com.test.spotifyarturtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;

import com.spotify.sdk.android.authentication.*;
import com.spotify.sdk.android.player.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private static final String CLIENT_ID = "c2397cfc3ec14379bb2354a9e95248b1";
    private static final String REDIRECT_URI = "my-spotify-app-login://callback";
    private static final int REQUEST_CODE = 12;

    private RecyclerView mTrackList;
    private Player mPlayer;
    private List<String> mMusicUris = Arrays.asList("spotify:track:2Cr2aD7NuH4auhbjJACOwA",
            "spotify:track:3Bi9awIplCSHPkoeh3BUbk",
            "spotify:track:69LZRWOzjBJAnlxi0OaMT8",
            "spotify:track:4Dq75eGDLZGM9EGP0XHbo2",
            "spotify:track:7dmlNBKi70BPZCTdyIsNww");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrackList = (RecyclerView) findViewById(R.id.track_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTrackList.setLayoutManager(layoutManager);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
//                        mPlayer.play("spotify:track:2vBiZ7nBe0OqTZKWGP0s4K");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("<<<<<<<<<<<<<<<<: Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }

        PlayListAdapter adapter = new PlayListAdapter(this, mMusicUris);
        adapter.setOnClickListener(new PlayListAdapter.OnPlayListener() {
            @Override
            public void play(String uri) {
                mPlayer.pause();
                mPlayer.play(uri);
            }
        });
        mTrackList.setAdapter(adapter);
    }

    @Override
    public void onLoggedIn() {
        System.err.println("<<<<<<<<<<<<<<<<: User logged in");
    }

    @Override
    public void onLoggedOut() {
        System.err.println("<<<<<<<<<<<<<<<<: User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        System.err.println("<<<<<<<<<<<<<<<<: Login failed");

    }

    @Override
    public void onTemporaryError() {
        System.err.println("<<<<<<<<<<<<<<<<: Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        System.err.println("<<<<<<<<<<<<<<<<: Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        System.err.println("<<<<<<<<<<<<<<<<: Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        System.err.println("<<<<<<<<<<<<<<<<: Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}