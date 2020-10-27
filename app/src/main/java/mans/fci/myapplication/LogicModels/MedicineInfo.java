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

    public MedicineInfo ReadFromCSVParsedLine(String[] lineParts)
    {
        MedicineInfo New = new MedicineInfo();
        /*id,
        country_id,
        title,
        desciption,
        producent ,
        active_ingredient,
        form,category_id,
        compatibility_id,
        ingredient_group_id,
        is_favorite
         */
        Id= Integer.parseInt(lineParts[0]);
        //TODO continue next fields
        return New;
    }
}
