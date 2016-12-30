package org.vaadin.example;

import java.time.Month;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.Shape;
import com.vaadin.addon.charts.model.VerticalAlign;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class DataLabelsChart extends Chart implements View {

	public static final String VIEW_NAME = "data-labels";

	public DataLabelsChart() {
		super(ChartType.LINE);

		Configuration conf = getConfiguration();
		conf.setTitle("Average temperatures in Turku, Finland");
		conf.setSubTitle("1961 - 2016");

		XAxis xAxis = conf.getxAxis();
		xAxis.setType(AxisType.DATETIME);
		xAxis.setTitle("Year");

		YAxis yAxis = conf.getyAxis();
		yAxis.setTitle("Temperature (℃)");
		yAxis.setMax(30);
		yAxis.setMin(0);

		/* get data csv values and map those to a list of DataSeriesItem */
		List<DataSeriesItem> items = DataSeriesItemHelpers.arrayToDataSeriesItems(
				WeatherData.getSeriesArray(Month.JULY), DataSeriesItemHelpers::itemFromYearValuePair);

		// For coldest
		items.stream().sorted(Comparator.comparingDouble(item -> item.getY().doubleValue())).limit(10)
				.forEach(DataLabelsChart::addCalloutBelow);
		// For hottest
		items.stream().sorted(Collections.reverseOrder(Comparator.comparingDouble(item -> item.getY().doubleValue())))
				.limit(10).forEach(DataLabelsChart::addCalloutAbove);

		DataSeries series = DataSeriesItemHelpers.itemsToSeries("july", "July", items, myLineOptions());
		conf.addSeries(series);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		drawChart();
	}

	private static void addCallout(DataSeriesItem item, SolidColor bgColor, Number labelY) {
		DataLabels callout = new DataLabels(true);
		callout.setVerticalAlign(VerticalAlign.MIDDLE);
		callout.setShape(Shape.CALLOUT);
		callout.setY(labelY);
		callout.setBackgroundColor(bgColor);
		callout.setColor(SolidColor.WHITE);
		callout.setBorderWidth(1);
		callout.setBorderRadius(4);
		callout.setPadding(3);
		callout.setFormat("{y}<br/>℃");
		callout.getStyle().setFontSize("9px");
		item.setDataLabels(callout);
	}

	private static void addCalloutAbove(DataSeriesItem item) {
		addCallout(item, new SolidColor("#ff3a49"), -25);
	}

	private static void addCalloutBelow(DataSeriesItem item) {
		addCallout(item, new SolidColor("#00b4f0"), 25);
	}

	private static PlotOptionsLine myLineOptions() {
		PlotOptionsLine options = new PlotOptionsLine();
		DataLabels labels = new DataLabels();
		labels.setAllowOverlap(true);
		labels.setOverflow("none");
		labels.setCrop(false);
		options.setDataLabels(labels);
		options.getTooltip().setValueSuffix(" ℃");
		return options;
	}
}
