package mans.fci.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import mans.fci.myapplication.MainActivity;
import mans.fci.myapplication.R;
import mans.fci.myapplication.ui.MedicineHomeCatalogAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView m_MainCatalogListView;
    public MedicineHomeCatalogAdapter m_MedicineAdapter ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        //----------ui ----------------
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        m_MainCatalogListView = root.findViewById(R.id.medicinesListView);

        //logic---------------------
        m_MedicineAdapter = new MedicineHomeCatalogAdapter(this.getContext(), MainActivity.m_CatalogMedicinesList);


        m_MainCatalogListView.setAdapter(m_MedicineAdapter);
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}