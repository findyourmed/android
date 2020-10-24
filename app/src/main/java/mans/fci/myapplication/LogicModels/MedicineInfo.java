package mans.fci.myapplication.LogicModels;

import java.util.ArrayList;
import java.util.List;

public class MedicineInfo {

    public int Id;
    public int country_id;
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
}
