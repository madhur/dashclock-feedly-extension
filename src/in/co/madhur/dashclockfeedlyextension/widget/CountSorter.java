package in.co.madhur.dashclockfeedlyextension.widget;

import in.co.madhur.dashclockfeedlyextension.service.WidgetData;

import java.util.Comparator;

public class CountSorter implements Comparator<WidgetData> {

	@Override
	public int compare(WidgetData lhs, WidgetData rhs) {
		if(Integer.parseInt(lhs.getCount()) > Integer.parseInt(rhs.getCount()))
			return 1;
		else if(Integer.parseInt(lhs.getCount()) < Integer.parseInt(rhs.getCount()))
			return -1;
		
		return 0;
	}

}
