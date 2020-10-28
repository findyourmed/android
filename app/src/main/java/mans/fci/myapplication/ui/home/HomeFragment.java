package mans.fci.myapplication.ui.home;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.MedicineDescriptionActivity;
import mans.fci.myapplication.R;
import mans.fci.myapplication.ui.CountriesSpinnerAdapter;
import mans.fci.myapplication.ui.MedicineHomeCatalogAdapter;
import mans.fci.myapplication.ui.dashboard.DashboardFragment;
import mans.fci.myapplication.util.ExampleAdapter;
import mans.fci.myapplication.util.MySuggestionProvider;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView m_MainCatalogListView;
    public MedicineHomeCatalogAdapter m_MedicineAdapter ;
    String[] countryNames={"All","USA","India","Belarus","Switzerland"};
    int flags[] = {R.drawable.globe,
            R.drawable.usa,
            R.drawable.india,
            R.drawable.belarus,
            R.drawable.switzerland};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        //----------ui ----------------
        View root = inflater.inflate(R.layout.fragment_home_catalog, container, false);
        m_MainCatalogListView = root.findViewById(R.id.medicinesListView);

        //logic---------------------
        m_MedicineAdapter = new MedicineHomeCatalogAdapter(this.getContext(), MainActivity.m_CatalogMedicinesList);


        m_MainCatalogListView.setAdapter(m_MedicineAdapter);
        m_MainCatalogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(HomeFragment.this.getContext(), "you clicked catalog item : " + position, Toast.LENGTH_SHORT).show();

                //MedicineInfo item = (MedicineInfo) m_MedicineAdapter.getItem(position);
                Intent MedicineDescriptionActivity = new Intent( getActivity(), mans.fci.myapplication.MedicineDescriptionActivity.class);
                MedicineDescriptionActivity.putExtra(MainActivity.m_SelectedTabTypeKey, MainActivity.m_TabTypeCatalog);
                MedicineDescriptionActivity.putExtra(MainActivity.m_UserSelectedMedicineIndexKey, position);
                startActivity(MedicineDescriptionActivity);
            }
        });
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        Spinner spin = (Spinner) root.findViewById(R.id.spinner_countries);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), countryNames[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CountriesSpinnerAdapter customAdapter=new CountriesSpinnerAdapter(getContext(),flags,countryNames);
        spin.setAdapter(customAdapter);
        //search history, based on https://stackoverflow.com/questions/19166537/create-history-to-searchview-on-actionbar
        Intent intent  = getActivity().getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getContext(),
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

        return root;
    }

    private List<String> items;

    private Menu menu;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {

        getActivity().getMenuInflater().inflate(R.menu.example, menu);

        this.menu = menu;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.searchview_text).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {

                    loadHistory(query);

                    return true;

                }

            });

        }

        return true;

    }

    // History
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadHistory(String query) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            String[] columns = new String[] { "_id", "text" };
            Object[] temp = new Object[] { 0, "default" };

            MatrixCursor cursor = new MatrixCursor(columns);

            for(int i = 0; i < items.size(); i++) {

                temp[0] = i;
                temp[1] = items.get(i);

                cursor.addRow(temp);

            }

            // SearchView
            SearchManager manager = (SearchManager)getActivity(). getSystemService(Context.SEARCH_SERVICE);

            final SearchView search = (SearchView) menu.findItem(R.id.searchview_text).getActionView();

            search.setSuggestionsAdapter(new ExampleAdapter(getContext(), cursor, items));

        }

    }
}