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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.R;
import mans.fci.myapplication.ui.FormsSpinnerAdapter;
import mans.fci.myapplication.ui.MedicineHomeCatalogAdapter;
import mans.fci.myapplication.util.ExampleAdapter;
import mans.fci.myapplication.util.MySuggestionProvider;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView m_MainCatalogListView;
    public MedicineHomeCatalogAdapter m_MedicineAdapter ;
    String[] countryNames={"All","Switzerland","USA","India","Belarus"};
    int flags[] = {R.drawable.globe,
            R.drawable.switzerland,
            R.drawable.usa,
            R.drawable.india,
            R.drawable.belarus,
            };

    String[] formNames={"All","Tablets","Caps","Liquid","Injection" , "gel"};
    int formIcons[] = {R.drawable.all_med,
            R.drawable.drug,
            R.drawable.pills,
            R.drawable.liquid,
            R.drawable.injection,
            R.drawable.gel};

    private String m_CurrentSearchPhrase = "";
    private int m_CurrentSearchCountryId=0, m_CurrentSearchFormId = 0;
    private SearchView m_SearchView;
    Spinner spin_forms;
    Spinner countrySpinner;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        //----------ui ----------------
        View root = inflater.inflate(R.layout.fragment_home_catalog, container, false);
        m_MainCatalogListView = root.findViewById(R.id.medicinesListView);

        m_SearchView = root.findViewById(R.id.searchview_text);
        m_SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RefreshSearchResults();
                // If the list contains the search query
                // than filter the adapter
                // using the filter method
                // with the query as its argument
                /*if (list.contains(query)) {
                    adapter.getFilter().filter(query);
                }
                else {
                    // Search query not found in List View
                    Toast
                            .makeText(MainActivity.this,
                                    "Not found",
                                    Toast.LENGTH_LONG)
                            .show();
                }*/
               // m_MedicineAdapter.getFilter().filter(query.toLowerCase());


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //m_MedicineAdapter.getFilter().filter(newText.toLowerCase());
                RefreshSearchResults();
                return false;
            }
        });
        //logic---------------------
        m_MedicineAdapter = new MedicineHomeCatalogAdapter(this.getContext(), MainActivity.m_CurrentVisibleCatalogListItems);


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

        countrySpinner = (Spinner) root.findViewById(R.id.spinner_countries);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_CurrentSearchCountryId = position;
                RefreshSearchResults();
                Toast.makeText(getContext(), countryNames[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FormsSpinnerAdapter countrySpinnerAdapter=new FormsSpinnerAdapter(getContext(),flags,countryNames);
        countrySpinner.setAdapter(countrySpinnerAdapter);

        spin_forms = (Spinner) root.findViewById(R.id.spinner_forms);
        spin_forms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_CurrentSearchFormId = position;
                RefreshSearchResults();
                Toast.makeText(getContext(), formNames[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FormsSpinnerAdapter formSpinnerAdapter=new FormsSpinnerAdapter(getContext(),formIcons,formNames);
        spin_forms.setAdapter(formSpinnerAdapter);
        //search history, based on https://stackoverflow.com/questions/19166537/create-history-to-searchview-on-actionbar
        Intent intent  = getActivity().getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getContext(),
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }

        Button saveButton = root.findViewById(R.id.button_SaveData);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnButtonSaveDataClick(v);
            }
        });
        return root;
    }

    public void OnSearchViewSubmit(View v)
    {

        m_CurrentSearchPhrase= m_SearchView.getQuery().toString();
        RefreshSearchResults();
    }
    private void RefreshSearchResults() {
       //start with all data
        MainActivity.CloneOriginalDataToVisislbeList();

        String query = m_SearchView.getQuery().toString().trim().toLowerCase();
        int formId = spin_forms.getSelectedItemPosition();
        int countrId = countrySpinner.getSelectedItemPosition();

        MedicineInfo temp;
        for(int i=MainActivity.m_CurrentVisibleCatalogListItems.size()-1; i>=0 ; i--) //backward loop
        {
            temp = MainActivity.m_CurrentVisibleCatalogListItems.get(i);
            //apply search query
            if(query.length()>0 && !temp.IsMatch(query))
            {
                MainActivity.m_CurrentVisibleCatalogListItems.remove(i);
            }

            //apply country id search
            else if(countrId!=0 && !ArrayUtils.contains( temp.country_id, countrId))
            {
                MainActivity.m_CurrentVisibleCatalogListItems.remove(i);
            }

            //apply form id search
            else if(formId!=0 && !ArrayUtils.contains( temp.form, formId))
            {
                MainActivity.m_CurrentVisibleCatalogListItems.remove(i);
            }
        }
        //Toast.makeText(this.getContext(),"search called", Toast.LENGTH_SHORT).show();
        m_MedicineAdapter.notifyDataSetChanged();
    }

    public void OnButtonSaveDataClick(View v)
    {
        MainActivity containerActivity = (MainActivity) getActivity();
        if(containerActivity!=null)
        {
            containerActivity.SaveCSVFile();
            Toast.makeText(getContext(),"Data Saved Successfully", Toast.LENGTH_SHORT).show();
        }

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