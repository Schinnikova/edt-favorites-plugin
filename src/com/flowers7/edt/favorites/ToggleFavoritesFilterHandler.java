package com.flowers7.edt.favorites;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorFilterService;

public class ToggleFavoritesFilterHandler extends AbstractHandler implements IElementUpdater {

    public static final String COMMAND_ID = "com.flowers7.edt.favorites.toggleFilter";
    private static final String FILTER_ID = FavoritesOnlyFilter.ID;
    private static final String NAVIGATOR_ID = "com._1c.g5.v8.dt.ui2.navigator";

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
        if (window == null) return null;

        IWorkbenchPage page = window.getActivePage();
        if (page == null) return null;

        IViewPart view = page.findView(NAVIGATOR_ID);
        if (!(view instanceof CommonNavigator)) return null;

        CommonNavigator navigator = (CommonNavigator) view;
        CommonViewer viewer = navigator.getCommonViewer();
        INavigatorFilterService filterService =
            viewer.getNavigatorContentService().getFilterService();

        boolean isActive = isFilterActive(viewer);

        if (isActive) {
            filterService.activateFilterIdsAndUpdateViewer(new String[]{});
        } else {
            filterService.activateFilterIdsAndUpdateViewer(new String[]{ FILTER_ID });
        }

        // Обновить иконку кнопки на тулбаре
        ICommandService commandService = window.getService(ICommandService.class);
        if (commandService != null) {
            commandService.refreshElements(COMMAND_ID, null);
        }

        return null;
    }

    private boolean isFilterActive(CommonViewer viewer) {
        for (ViewerFilter f : viewer.getFilters()) {
            if (f instanceof FavoritesOnlyFilter) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateElement(UIElement element, Map parameters) {
        try {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window == null) return;
            IWorkbenchPage page = window.getActivePage();
            if (page == null) return;
            IViewPart view = page.findView(NAVIGATOR_ID);
            if (!(view instanceof CommonNavigator)) return;
            CommonViewer viewer = ((CommonNavigator) view).getCommonViewer();
            boolean isActive = isFilterActive(viewer);
            element.setIcon(isActive
                ? Activator.getImageDescriptor("icons/filter_star_active.png")
                : Activator.getImageDescriptor("icons/filter_star.png"));
            element.setChecked(isActive);
        } catch (Exception e) {
            // игнорируем если вид ещё не открыт
        }
    }
}