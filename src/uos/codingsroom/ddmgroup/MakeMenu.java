package uos.codingsroom.ddmgroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MakeMenu {

	SlidingMenu menu;

	MakeMenu(MainActivity thisView) {
		menu = new SlidingMenu(thisView);
		menu.setMode(SlidingMenu.LEFT);
		menu.setMenu(R.layout.menu_layout);
		menu.setTouchmodeMarginThreshold(250);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(1.0f);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.attachToActivity(thisView, SlidingMenu.SLIDING_WINDOW);

	}

	public SlidingMenu getMenu() {
		return menu;
	}

}
