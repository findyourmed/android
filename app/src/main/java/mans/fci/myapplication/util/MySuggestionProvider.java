package mans.fci.myapplication.util;

import android.content.SearchRecentSuggestionsProvider;

//based on https://stackoverflow.com/questions/19166537/create-history-to-searchview-on-actionbar
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}