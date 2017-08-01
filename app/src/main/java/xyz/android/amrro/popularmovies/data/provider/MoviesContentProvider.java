package xyz.android.amrro.popularmovies.data.provider;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import timber.log.Timber;
import xyz.android.amrro.popularmovies.data.db.MoviesDb;
import xyz.android.amrro.popularmovies.data.model.Movie;

/**
 * Created by amrro <amr.elghobary@gmail.com> on 7/31/17.
 * <p>
 * This {@link ContentProvider} will wrap the {@link MoviesDb}
 */

public final class MoviesContentProvider extends ContentProvider {

    public static final String AUTHORITY = "xyz.android.amrro.popularmovies.provider";

    /**
     * The URI for the Movie table.
     */
    public static final Uri URI_MOVIE = Uri.parse("content://" + AUTHORITY + "/" + Movie.TABLE_NAME);

    public static final int CODE_MOVIE_DIR = 11;
    public static final int CODE_MOVIE_ITEM = 22;

    /**
     * The URI matcher.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private MoviesDb db;

    // TODO: 7/31/17 add more fields as required.
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE = "releaseDate";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER = "posterPath";
    public static final String COLUMN_BACKDROP = "backdropPath";
    public static final String COLUMN_VOTE_AVERAGE = "voteAverage";


    static {
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME, CODE_MOVIE_DIR);
        MATCHER.addURI(AUTHORITY, Movie.TABLE_NAME + "/*", CODE_MOVIE_ITEM);
    }

/*
    public MoviesContentProvider(MoviesDb db) {
        super();
        this.db = db;
    }*/


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            db = Room.databaseBuilder(getContext(), MoviesDb.class, "moviesDb").build();
            return true;
        }

        Timber.d("getContext() in MoviesContentProvider is NULL.");
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] strings,
                        @Nullable String s,
                        @Nullable String[] strings1,
                        @Nullable String s1) {
        final Cursor cursor;
        switch (MATCHER.match(uri)) {
            /* return all stored movies.*/
            case CODE_MOVIE_DIR:
                cursor = db.movies().selectAll();
                break;
            case CODE_MOVIE_ITEM:
                cursor = db.movies().findById(ContentUris.parseId(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Movie.TABLE_NAME;
            case CODE_MOVIE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Movie.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                if (contentValues != null) {
                    final Long id = db.movies().insert(fromContentValues(contentValues));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, id);
                }
            case CODE_MOVIE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Specify the row ID not the whole table: " + uri);
            case CODE_MOVIE_ITEM:
                final int count = db.movies().deleteById(ContentUris.parseId(uri));
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (MATCHER.match(uri)) {
            case CODE_MOVIE_DIR:
                throw new IllegalArgumentException("Specify the row ID; You cannot update the whole table: " + uri);
            case CODE_MOVIE_ITEM:
                if (contentValues != null) {
                    final int count = db.movies().update(fromContentValues(contentValues));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
                }
                return 0;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    public static Movie fromContentValues(@NonNull final ContentValues values) {
        Objects.requireNonNull(values);
        final Movie.Builder builder = new Movie.Builder();

        if (values.containsKey(COLUMN_ID)) {
            builder.setId(values.getAsInteger(COLUMN_ID));
        }

        if (values.containsKey(COLUMN_TITLE)) {
            builder.setTitle(values.getAsString(COLUMN_TITLE));
        }

        if (values.containsKey(COLUMN_RELEASE)) {
            builder.setReleaseDate(values.getAsString(COLUMN_RELEASE));
        }

        if (values.containsKey(COLUMN_OVERVIEW)) {
            builder.setOverview(values.getAsString(COLUMN_OVERVIEW));
        }

        if (values.containsKey(COLUMN_POSTER)) {
            builder.setPosterPath(values.getAsString(COLUMN_POSTER));
        }

        if (values.containsKey(COLUMN_BACKDROP)) {
            builder.setBackdropPath(values.getAsString(COLUMN_BACKDROP));
        }

        return builder.build();
    }
}