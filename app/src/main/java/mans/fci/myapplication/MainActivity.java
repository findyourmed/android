package mans.fci.myapplication;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mans.fci.myapplication.LogicModels.CountryInfo;
import mans.fci.myapplication.LogicModels.FormInfo;
import mans.fci.myapplication.LogicModels.MedicineInfo;

public class MainActivity extends AppCompatActivity {
    public static CountryInfo[] m_CountriesList;
    public static FormInfo[] m_FormsList;
    public static MedicineInfo[] m_CatalogMedicinesList;
    public static List<MedicineInfo> m_FavoritesList = new ArrayList<>();

    public  static final String m_SelectedTabTypeKey="m_SelectedTabTypeKey", m_TabTypeCatalog = "Catalog",m_TabTypeFavorite  = "Favorite";
    public  static final String m_UserSelectedMedicineIndexKey = "m_UserSelectedMedicineIndexKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----------------logic --------------------

        //countries fixed data
        m_CountriesList = new CountryInfo[4];
        m_CountriesList[0] = new CountryInfo(1, "USA");
        m_CountriesList[1] = new CountryInfo(2, "India");
        m_CountriesList[2] = new CountryInfo(3, "Belarus");
        m_CountriesList[3] = new CountryInfo(4, "Switzerland");

        //forms fixed data
        m_FormsList =  new FormInfo[4];
        m_FormsList[0] = new FormInfo(1,"tablets");
        m_FormsList[1] = new FormInfo(2,"pills");
        m_FormsList[2] = new FormInfo(3,"liquid");
        m_FormsList[3] = new FormInfo(4,"injections");

        //demo data (medicines)
        Random r = new Random();
        m_CatalogMedicinesList = new MedicineInfo[20];
        for (int i=0 ; i <m_CatalogMedicinesList.length;i++)
        {
            m_CatalogMedicinesList[i] = new MedicineInfo();
            m_CatalogMedicinesList[i].Id = i;
            m_CatalogMedicinesList[i].title = "title "+i;
            m_CatalogMedicinesList[i].country_id =r.nextInt(3)+1;
            m_CatalogMedicinesList[i].description = "description sample content "+i;
            m_CatalogMedicinesList[i].form = new int[]{ r.nextInt(3)+1, r.nextInt(3)+1};

            CountryInfo itemCountry =null;
            for (CountryInfo c: MainActivity.m_CountriesList) {
                if(c.country_id == m_CatalogMedicinesList[i].country_id)
                {
                    itemCountry =c;
                    break;
                }
            }
            if(itemCountry!=null)
                m_CatalogMedicinesList[i].m_View_Country = itemCountry.country_title;

            m_CatalogMedicinesList[i].compatibility_id =r.nextInt(12);

            m_CatalogMedicinesList[i].is_favorite = r.nextBoolean();
            if(m_CatalogMedicinesList[i].is_favorite)
            {
                //search for any conflict with existing favorites
                SearchForFavoriteConflict(i);

                //add new favorite
                m_FavoritesList.add(m_CatalogMedicinesList[i]);

            }

            m_CatalogMedicinesList[i].m_ViewForm ="";
            //get forms
            for(FormInfo f :m_FormsList)
            {
               for (int formId: m_CatalogMedicinesList[i].form)
               {
                   if(formId == f. m_FormId)
                   {
                       m_CatalogMedicinesList[i].m_ViewForm +="- " + f.m_FormName;
                   }
               }
            }
        }

        //----------------UI -------------------------
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void SearchForFavoriteConflict(int i) {
        for (MedicineInfo m:m_FavoritesList) {
            if(m.compatibility_id== m_CatalogMedicinesList[i].compatibility_id)
            {
                //both are conflicting
                m.m_hasConflictWithAnyFavorite = true;
                m_CatalogMedicinesList[i].m_hasConflictWithAnyFavorite = true;
            }
        }
    }

}