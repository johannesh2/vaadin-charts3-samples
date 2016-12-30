package org.vaadin.example;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;

import com.vaadin.addon.charts.model.AbstractPlotOptions;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;

public class DataSeriesItemHelpers {

	public static <T> List<DataSeriesItem> arrayToDataSeriesItems(T[] array, BiFunction<T, T, DataSeriesItem> mapper) {
		List<DataSeriesItem> result = new ArrayList<>();
		for (int i = 0; i < array.length; i += 2) {
			result.add(mapper.apply(array[i], array[i + 1]));
		}

		return result;
	}

	public static DataSeriesItem itemFromYearValuePair(Double year, Double temp) {
		Date yearDate = toDate(LocalDate.of(year.intValue(), 1, 1));
		return new DataSeriesItem(yearDate, temp);

	}

	public static <T extends AbstractPlotOptions> DataSeries itemsToSeries(String seriesId, String seriesName,
			List<DataSeriesItem> items, T plotOptions) {
		DataSeries series = new DataSeries(items);
		series.setId(seriesId);
		series.setPlotOptions(plotOptions);
		series.setName(seriesName);

		return series;
	}

	private static Date toDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
