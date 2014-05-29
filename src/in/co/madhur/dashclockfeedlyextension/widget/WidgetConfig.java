package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.AppPreferences;
import in.co.madhur.dashclockfeedlyextension.AppPreferences.Keys;

public class WidgetConfig {
	
	private int textSize;
	private int titleColor;
	private int countColor;
	private boolean isWidgetHeader;
	private int sortOrder;
	
	public WidgetConfig() {
	}

	public void RefreshData(AppPreferences appPreferences)
	{
		
		setTextSize(appPreferences.GetFontSize());
		setTitleColor(appPreferences.GetColor(Keys.WIDGET_TITLE_COLOR));
		setCountColor(appPreferences.GetColor(Keys.WIDGET_COUNT_COLOR));
		setWidgetHeader(appPreferences.IsWidgetHeaderEnabled());
		setSortOrder(appPreferences.GetWidgetSortOrder());
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

	public int getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public int getCountColor() {
		return countColor;
	}

	public void setCountColor(int countColor) {
		this.countColor = countColor;
	}

	public boolean isWidgetHeader() {
		return isWidgetHeader;
	}

	public void setWidgetHeader(boolean isWidgetHeader) {
		this.isWidgetHeader = isWidgetHeader;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

}
