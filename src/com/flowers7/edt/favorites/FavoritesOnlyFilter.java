package com.flowers7.edt.favorites;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FavoritesOnlyFilter extends ViewerFilter {

    public static final String ID = "com.flowers7.edt.favorites.filter";

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        // Если сам объект избранный — показываем
        if (FavoriteStore.isFavorite(element)) {
            return true;
        }

        // Если это MdObject но не избранный — скрываем
        if (element instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject) {
            return false;
        }

        // Для всего остального (категории, проект, корень) —
        // показываем только если внутри есть хоть один избранный
        if (viewer instanceof org.eclipse.jface.viewers.AbstractTreeViewer) {
            org.eclipse.jface.viewers.AbstractTreeViewer treeViewer =
                (org.eclipse.jface.viewers.AbstractTreeViewer) viewer;
            return hasFavoriteChild(treeViewer, element);
        }

        return true;
    }

    private boolean hasFavoriteChild(
            org.eclipse.jface.viewers.AbstractTreeViewer viewer, Object element) {
        org.eclipse.jface.viewers.ITreeContentProvider provider =
            (org.eclipse.jface.viewers.ITreeContentProvider)
            viewer.getContentProvider();
        if (provider == null) return false;

        Object[] children = provider.getChildren(element);
        if (children == null) return false;

        for (Object child : children) {
            if (FavoriteStore.isFavorite(child)) {
                return true;
            }
            if (!(child instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject)) {
                if (hasFavoriteChild(viewer, child)) {
                    return true;
                }
            }
        }
        return false;
    }
}