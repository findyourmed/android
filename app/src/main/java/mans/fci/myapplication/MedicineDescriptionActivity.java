package mans.fci.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import mans.fci.myapplication.LogicModels.CountryInfo;
import mans.fci.myapplication.LogicModels.FormInfo;
import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.ui.MedicineHomeCatalogAdapter;
import mans.fci.myapplication.ui.SimilarMedicineAdapter;

public class MedicineDescriptionActivity extends AppCompatActivity {
    List<MedicineInfo> SourceData;
    MedicineInfo m_CurrentSelectedMedicine;
    final int m_notFoundIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_description);

        String SelectedTab = getIntent().getStringExtra(MainActivity.m_SelectedTabTypeKey);
        if(SelectedTab.equals(MainActivity.m_TabTypeCatalog))
            SourceData = MainActivity.m_CurrentVisibleCatalogListItems;
        else
        {
            if(MainActivity.m_FavoritesList.size()>0) {
                SourceData = MainActivity.m_FavoritesList;
            }
        }

        int ArrayPosition =getIntent().getIntExtra(MainActivity.m_UserSelectedMedicineIndexKey, m_notFoundIndex);
        if(SourceData!=null && SourceData.size()> ArrayPosition && ArrayPosition!=m_notFoundIndex)
        {
            //get current medicine and display it
            m_CurrentSelectedMedicine = SourceData.get(ArrayPosition);

            if(m_CurrentSelectedMedicine!=null) {
                //display UI

                //is favorite
                ToggleButton toggleIsFavorite = findViewById(R.id.toggleButton_favorite);
                toggleIsFavorite.setChecked(m_CurrentSelectedMedicine.is_favorite);

                toggleIsFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        m_CurrentSelectedMedicine.is_favorite = isChecked;
                    }
                });

                //header title
                TextView tvHeaderTitle = findViewById(R.id.tv_headerTitle);
                tvHeaderTitle.setText(m_CurrentSelectedMedicine.title);

                //description
                TextView tvTable_Description = findViewById(R.id.tv_table_description);
                tvTable_Description.setText(m_CurrentSelectedMedicine.description);

                //table textViews
                TextView tvTableTitle = findViewById(R.id.tv_table_Name);
                TextView tvTableProducer = findViewById(R.id.tv_table_Producer);
                TextView tvTable_DosageForm = findViewById(R.id.tv_table_dosage_form);
                TextView tvTable_IngredientGroupId = findViewById(R.id.tv_table_IngredientGroupId);
                TextView tvTable_ActiveComponent = findViewById(R.id.tv_table_ActiveComponent);

                tvTableTitle.setText(m_CurrentSelectedMedicine.title);
                tvTableProducer.setText(m_CurrentSelectedMedicine.producent);
                tvTable_ActiveComponent.setText(m_CurrentSelectedMedicine.active_ingredient);
                tvTable_IngredientGroupId.setText("Group:" + m_CurrentSelectedMedicine.ingredient_group_id);
                tvTable_DosageForm.setText(m_CurrentSelectedMedicine.m_ViewForm);

                //check visible forms
                ImageView[] FormImages = new ImageView[]{findViewById(R.id.imageView_tablet),
                        findViewById(R.id.imageView_pills),
                        findViewById(R.id.imageView_liquid),
                        findViewById(R.id.imageView_injection),
                        findViewById(R.id.imageView_gel)};

                //initial visibility
                for (ImageView f : FormImages) {
                    f.setVisibility(View.GONE);
                }
                //show required visibility
                for (int formId : m_CurrentSelectedMedicine.form) {
                    FormImages[formId-1].setVisibility(View.VISIBLE);
                }

                //Show Similar Medicines
                List<MedicineInfo> SimilarItems = new ArrayList<>();
                for (MedicineInfo m :MainActivity.m_CatalogMedicinesList)
                {
                    if(m.Id!=m_CurrentSelectedMedicine.Id && m.ingredient_group_id == m_CurrentSelectedMedicine.ingredient_group_id)
                        SimilarItems.add(m);
                }
                ListView listViewSimilarCountries = findViewById(R.id.medicinesSimilarListView);
                SimilarMedicineAdapter similaritesAdapter = new SimilarMedicineAdapter(this,SimilarItems );


                listViewSimilarCountries.setAdapter(similaritesAdapter);
            }
        }

    }
    public void OnButtonBackClicked(View v)
    {
        finish();
    }


//    @Override
//    protected void onStop() {
//        MainActivity.RefreshFavoritesList();
//        super.onStop();
//
//    }
}