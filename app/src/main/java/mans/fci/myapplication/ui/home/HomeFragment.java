package mans.fci.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.R;
import mans.fci.myapplication.ui.MedicineHomeCatalogAdapter;
import mans.fci.myapplication.ui.dashboard.DashboardFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView m_MainCatalogListView;
    public MedicineHomeCatalogAdapter m_MedicineAdapter ;
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
            }
        });
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}