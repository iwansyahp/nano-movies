package xyz.android.amrro.popularmovies.ui.movie;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import xyz.android.amrro.popularmovies.R;
import xyz.android.amrro.popularmovies.common.BaseActivity;

public class MovieDetailsActivity extends BaseActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

//        final int id = getIntent().getExtras().getInt(MovieDetailsFragment.KEY_MOVIE_ID);
        final MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        detailsFragment.setMovieId(itemId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.details_fragment, detailsFragment)
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
