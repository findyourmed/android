package mans.fci.myapplication.LogicModels;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MedicineInfo {

    public int Id;
    public int[] country_id;
    public String title;
    public int[] form;
    public String description;
    public String producent;
    public String active_ingredient;
    public int category_id;
    public int compatibility_id;
    public int ingredient_group_id;
    public boolean is_favorite;

    public boolean m_hasConflictWithAnyFavorite;
    public List<MedicineInfo> m_ConflictedFavoriteMedicines = new ArrayList<>();

    public String m_View_Country;
    public String m_ViewForm;

    public void ReadFromCSVParsedLine(String[] lineParts)
    {

        /*  id,
            country_id,
            title,
            desciption,
            producent ,
            active_ingredient,
            form,
            category_id,
            compatibility_id,
        ingredient_group_id,
        is_favorite
         */
        Id= Integer.parseInt(lineParts[0]);
        country_id = new int[]{ Integer.parseInt(lineParts[1])};
        title =  lineParts[2];
        description = lineParts[3];
        producent = lineParts[4];
        active_ingredient = lineParts[5];

        String[] formsParts = lineParts[6].split(",");
        form = new int[formsParts.length];
        for (int i =0; i<formsParts.length;i++)
        {
            form[i] = Integer.parseInt(formsParts[i].trim());
        }
        category_id = Integer.parseInt(lineParts[7]);
        compatibility_id = Integer.parseInt(lineParts[8]);
        ingredient_group_id = Integer.parseInt(lineParts[9]);
        is_favorite = lineParts.length>=11 && lineParts[10].trim().equalsIgnoreCase("1");
        //TODO continue next fields

    }

    public boolean IsMatch(String queryText)
    {
        return description.toLowerCase().contains(queryText) || title.toLowerCase().contains(queryText);
    }
    public String[] GenerateCSVLine()
    {
        String[] lineParts = new String[11];
        //final char  StringDelimiter = '"';
        final String fieldSeparator =",";

        /*  id,
            country_id,
            title,
            desciption,
            producent ,
            active_ingredient,
            form,
            category_id,
            compatibility_id,
        ingredient_group_id,
        is_favorite
         */
        //Id
        lineParts[0] = ""+Id;

        //country_id = new int[]{ Integer.parseInt(lineParts[1])};
        //TODO: when country id is more than one, then update the following line
        lineParts[1] = ""+country_id[0];


        //title =  lineParts[2];
        lineParts[2]= title;

        //description = lineParts[3];
         lineParts[3] =description;

        //producent = lineParts[4];
        lineParts[4] = producent ;

        //active_ingredient = lineParts[5];
         lineParts[5] = active_ingredient;

        //form

         lineParts[6] = "";
         for (int formIndex = 0; formIndex<form.length;formIndex++)
         {
             lineParts[6]+= form[formIndex];
             if(formIndex<form.length-1)
                 lineParts[6]+=",";
         }

        //category_id = Integer.parseInt(lineParts[7]);
        lineParts[7]= ""+category_id ;

        //compatibility_id = Integer.parseInt(lineParts[8]);//
        lineParts[8]= ""+compatibility_id ;

        //ingredient_group_id = Integer.parseInt(lineParts[9]);
        lineParts[9]= ""+ingredient_group_id ;

        //is_favorite
        lineParts[10]= (is_favorite)?"1" :"0";


        //String csvLine =TextUtils.join(fieldSeparator, lineParts);
        //return csvLine;
        return lineParts;
    }
}
