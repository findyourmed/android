package mans.fci.myapplication;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mans.fci.myapplication.LogicModels.CountryInfo;
import mans.fci.myapplication.LogicModels.FormInfo;
import mans.fci.myapplication.LogicModels.MedicineInfo;
import mans.fci.myapplication.util.MySuggestionProvider;

public class MainActivity extends AppCompatActivity {
    public static CountryInfo[] m_CountriesList;
    public static FormInfo[] m_FormsList;
    public static /*MedicineInfo[]*/List<MedicineInfo> m_CatalogMedicinesList = new ArrayList<>();
    public static List<MedicineInfo> m_FavoritesList = new ArrayList<>();

    public  static final String m_SelectedTabTypeKey="m_SelectedTabTypeKey", m_TabTypeCatalog = "Catalog",m_TabTypeFavorite  = "Favorite";
    public  static final String m_UserSelectedMedicineIndexKey = "m_UserSelectedMedicineIndexKey";

    private static final String m_CSVFileName = "medicine_dataset.csv";
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
        m_FormsList =  new FormInfo[5];
        m_FormsList[0] = new FormInfo(1,"tablets");
        m_FormsList[1] = new FormInfo(2,"pills");
        m_FormsList[2] = new FormInfo(3,"liquid");
        m_FormsList[3] = new FormInfo(4,"injections");
        m_FormsList[4] = new FormInfo(5,"gel");

        //Read Source CSV
        ReadCSVData();

//        //demo data (medicines)
//        Random r = new Random();
//        m_CatalogMedicinesList = new MedicineInfo[20];
//        for (int i=0 ; i <m_CatalogMedicinesList.length;i++)
//        {
//            m_CatalogMedicinesList[i] = new MedicineInfo();
//            m_CatalogMedicinesList[i].Id = i;
//            m_CatalogMedicinesList[i].title = "title "+i;
//            m_CatalogMedicinesList[i].country_id =new int[] {r.nextInt(4)+1,r.nextInt(4)+1}; //TODO:test that
//            m_CatalogMedicinesList[i].description = "description sample content "+i;
//            m_CatalogMedicinesList[i].form = new int[]{ r.nextInt(5)+1, r.nextInt(5)+1};//TODO:test that
//
//            List<CountryInfo> itemCountry =new ArrayList<>();
//            m_CatalogMedicinesList[i].m_View_Country = "";
//            for (CountryInfo c: MainActivity.m_CountriesList) {
//                for(int temp_countryId : m_CatalogMedicinesList[i].country_id)
//                if(c.country_id == temp_countryId)
//                {
//                    itemCountry.add(c);
//                    m_CatalogMedicinesList[i].m_View_Country+="-"+c.country_title;
//                    break;
//                }
//            }
//            /*if(itemCountry!=null)
//                m_CatalogMedicinesList[i].m_View_Country = itemCountry.country_title;*/
//
//            m_CatalogMedicinesList[i].compatibility_id =r.nextInt(12);
//
//            m_CatalogMedicinesList[i].is_favorite = r.nextBoolean();
//            if(m_CatalogMedicinesList[i].is_favorite)
//            {
//                //search for any conflict with existing favorites
//                SearchForFavoriteConflict(i);
//
//                //add new favorite
//                m_FavoritesList.add(m_CatalogMedicinesList[i]);
//
//            }
//
//            m_CatalogMedicinesList[i].m_ViewForm ="";
//            //get forms
//            for(FormInfo f :m_FormsList)
//            {
//               for (int formId: m_CatalogMedicinesList[i].form)
//               {
//                   if(formId == f. m_FormId)
//                   {
//                       m_CatalogMedicinesList[i].m_ViewForm +="- " + f.m_FormName;
//                   }
//               }
//            }
//        }

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

    //ref: https://stackoverflow.com/a/19218941/1445776
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
    //ref: https://stackoverflow.com/questions/43055661/reading-csv-file-in-android-app ,
    //http://zetcode.com/java/opencsv/
    public void ReadCSVData()
    {
        try {
            //copy file to app persistent dir if not existing
            File AppDireFile = new File(this.getFilesDir(), m_CSVFileName);
            if(!AppDireFile.exists())
            {
                //AssetManager assetManager = getAssets();

                OutputStream out = null;
                try {
                    InputStream inputStream= this.getResources().openRawResource(R.raw.medicine_dataset);

                    out = new FileOutputStream(AppDireFile);
                    copyFile(inputStream, out);
                    inputStream.close();

                    out.flush();
                    out.close();
                } catch(IOException e) {
                    Log.e("MEDICINE INFO", "Failed to copy asset file: " + m_CSVFileName, e);
                    Toast.makeText(this,"Error in Data File: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                    return ;
                }

            }

            //now, read
            //InputStream inputStream= this.getResources().openRawResource(R.raw.medicine_dataset);
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream( AppDireFile));
            //File csvfile = new File(Environment.getExternalStorageDirectory() + "/"+m_CSVFileName);
            CSVParser parser = new CSVParserBuilder().withQuoteChar('"').withSeparator(',').build();
            CSVReader reader = new CSVReaderBuilder(streamReader).withCSVParser(parser).build();

            //CSVReader reader = new CSVReader(streamReader,',','"');
            String[] nextLine;
            nextLine = reader.readNext();//skip first line
            //List<MedicineInfo> tempMedicines = new ArrayList<>();
            m_CatalogMedicinesList.clear();
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Log.e("MedicineINFO",nextLine[0] + nextLine[1] + "etc...");
                //TODO: extract medicine infor
                MedicineInfo New = new MedicineInfo();
                New.ReadFromCSVParsedLine(nextLine);
                m_CatalogMedicinesList.add(New);

                if(New.is_favorite)
                {
                    //search for any conflict with existing favorites
                    SearchForFavoriteConflict(New);

                    //add new favorite
                    m_FavoritesList.add(New);

                }

                New.m_ViewForm ="";
                //get forms
                for(FormInfo f :m_FormsList)
                {
                    for (int formId: New.form)
                    {
                        if(formId == f. m_FormId)
                        {
                            New.m_ViewForm +="- " + f.m_FormName;
                        }
                    }
                }


            }

            streamReader.close();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

    }


    private static void SearchForFavoriteConflict(MedicineInfo New) {

        for (MedicineInfo m:m_FavoritesList) {
            if(m.compatibility_id== New.compatibility_id)
            {
                //both are conflicting
                m.m_hasConflictWithAnyFavorite = true;
                New.m_hasConflictWithAnyFavorite = true;
            }

        }
    }

    public static void RefreshFavoritesList()
    {
        m_FavoritesList.clear();
        //result conflicts
        for (MedicineInfo mInitial: m_CatalogMedicinesList ) {
            mInitial.m_hasConflictWithAnyFavorite=false;
        }
        for (int i=0 ; i <m_CatalogMedicinesList.size();i++)
        {

            if(m_CatalogMedicinesList.get(i).is_favorite)
            {
                //search for any conflict with existing favorites
                SearchForFavoriteConflict(m_CatalogMedicinesList.get(i));

                //add new favorite
                m_FavoritesList.add(m_CatalogMedicinesList.get(i));

            }


        }
    }
}