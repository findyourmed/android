package mans.fci.myapplication.LogicModels;

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
        is_favorite = lineParts.length>=11 && lineParts[10].trim()=="1";
        //TODO continue next fields

    }
}
