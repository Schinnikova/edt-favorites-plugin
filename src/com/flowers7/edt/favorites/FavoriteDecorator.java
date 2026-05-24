package com.flowers7.edt.favorites;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;

public class FavoriteDecorator extends LabelProvider
        implements ILightweightLabelDecorator {

    public static final String ID =
        "com.flowers7.edt.favorites.decorator";

    @Override
    public void decorate(Object element, IDecoration decoration) {
        if (FavoriteStore.isFavorite(element)) {
            decoration.addPrefix("★ ");
        }
    }
}
