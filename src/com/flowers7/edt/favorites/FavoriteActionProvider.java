package com.flowers7.edt.favorites;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;

public class FavoriteActionProvider extends CommonActionProvider {

    private Action toggleAction;

    @Override
    public void init(ICommonActionExtensionSite site) {
        super.init(site);
        toggleAction = new Action() {
            @Override
            public void run() {
                IStructuredSelection sel =
                    (IStructuredSelection) getContext().getSelection();
                for (Object obj : sel.toList()) {
                    FavoriteStore.toggle(obj);
                }
                // Обновить декораторы
                site.getStructuredViewer().refresh();
            }
        };
        toggleAction.setText("В избранное");
        toggleAction.setImageDescriptor(
        	    Activator.getImageDescriptor("icons/star_menu.png")
        	);
    }

    @Override
    public void fillContextMenu(IMenuManager menu) {
        IStructuredSelection sel =
            (IStructuredSelection) getContext().getSelection();
        if (sel.isEmpty()) return;

        // Показываем пункт только для объектов метаданных
        Object first = sel.getFirstElement();
        if (first instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject) {
            menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, toggleAction);
        }
    }
}