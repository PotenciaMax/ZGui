package me.zmaster.zgui.inventory.element.view;

import me.zmaster.zgui.inventory.element.Clickable;

public interface PagedIconsView<I extends Clickable> extends IconsView<I> {
    int getPage();

    void setPage(int page);

    int getLastPage();
}
