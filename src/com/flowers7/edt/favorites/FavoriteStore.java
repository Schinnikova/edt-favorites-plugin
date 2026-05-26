package com.flowers7.edt.favorites;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.preference.IPreferenceStore;

public class FavoriteStore {

    private static final String PREF_KEY = "favorites";
    private static Set<String> favorites = null;

    public static boolean isFavorite(Object element) {
        String key = getKey(element);
        if (key == null) return false;
        return getFavorites().contains(key);
    }

    public static void toggle(Object element) {
        String key = getKey(element);
        if (key == null) return;
        Set<String> favs = getFavorites();
        if (!favs.remove(key)) {
            favs.add(key);
        }
        save(favs);
    }

    private static String getKey(Object element) {
        if (element instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject) {
            com._1c.g5.v8.dt.metadata.mdclass.MdObject obj =
                (com._1c.g5.v8.dt.metadata.mdclass.MdObject) element;
            StringBuilder path = new StringBuilder();
            org.eclipse.emf.ecore.EObject current = obj;
            while (current != null) {
                if (current instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject) {
                    com._1c.g5.v8.dt.metadata.mdclass.MdObject mdObj =
                        (com._1c.g5.v8.dt.metadata.mdclass.MdObject) current;
                    if (path.length() > 0) path.insert(0, ".");
                    path.insert(0, mdObj.eClass().getName() + ":" + mdObj.getName());
                }
                current = current.eContainer();
            }
            return path.toString();
        }
        return null;
    }

    private static Set<String> getFavorites() {
        if (favorites == null) {
            favorites = new HashSet<>();
            IPreferenceStore store = Activator.getDefault().getPreferenceStore();
            String raw = store.getString(PREF_KEY);
            if (raw != null && !raw.isEmpty()) {
                for (String s : raw.split(",")) {
                    favorites.add(s.trim());
                }
            }
        }
        return favorites;
    }

    private static void save(Set<String> favs) {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setValue(PREF_KEY, String.join(",", favs));
    }
}