package com.codepath.apps.tweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweets.models.Tweet;
import com.codepath.apps.tweets.models.User;

import fragments.ComposeTweetFragment;
import fragments.HomeTimelineFragment;
import fragments.MentionsTimelineFragment;
import fragments.TweetListFragment;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetFragment.ComposeTweetListener, TweetsArrayAdapter.UserSelectedListener {


    private ComposeTweetFragment composeFragment;
    private ViewPager viewPager;
    private TweetsPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeView();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

    }

    public void showComposeView() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        composeFragment = new ComposeTweetFragment();
        composeFragment.show(ft, "COMPOSE");

    }

    public void handleTweetClicked(View view) {
        composeFragment.composeTweet();
        composeFragment.dismiss();
    }


    public void handleCancelClicked(View view) {
        composeFragment.dismiss();
    }

    @Override
    public void onTweetCreated(Tweet tweet) {
        pagerAdapter.addTweet(0, tweet);
    }

    public void handleUserSelected(User user) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", user.getScreenName());
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {

        final int PAGES_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};
        private TweetListFragment fragmentHomeTimeline;
        private TweetListFragment fragmentMentionsTimeline;

        public TweetsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            // note: these will get cached for you
            switch (position) {
                case 0:
                    fragmentHomeTimeline = new HomeTimelineFragment();
                    return fragmentHomeTimeline;
                case 1:
                    fragmentMentionsTimeline = new MentionsTimelineFragment();
                    return fragmentMentionsTimeline;
            }

            return null;
        }

        @Override
        public int getCount() {
            return PAGES_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public void addTweet(int index, Tweet tweet) {
            if (fragmentHomeTimeline != null) {
                fragmentHomeTimeline.add(0, tweet);
            }
        }
    }

    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


}
