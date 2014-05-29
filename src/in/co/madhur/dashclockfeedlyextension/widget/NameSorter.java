package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.service.WidgetData;

import java.util.Comparator;

public class NameSorter implements Comparator<WidgetData> {

	@Override
	public int compare(WidgetData lhs, WidgetData rhs) {
		
		return lhs.getTitle().compareTo(rhs.getTitle());
	}

}
