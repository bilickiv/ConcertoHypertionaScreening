package hu.aensys.concerto.hypertonia.utility;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import hu.aensys.concerto.hypertonia.MeasurementModel;

public class Statistics {
List<MeasurementModel> measurements;
MeasurementModel firstWeekAverageMeasurement;
MeasurementModel secondWeekAverageMeasurement;
MeasurementModel thirdWeekAverageMeasurement;

public void init(List<MeasurementModel> measurements, String type)
{
	this.measurements = measurements;
	Collections.sort(measurements);
	calculateAverage(type);
}
public MeasurementModel getFirstWeek()
{
	return firstWeekAverageMeasurement;
}
public MeasurementModel getSecondWeek()
{
	return secondWeekAverageMeasurement;
}
public MeasurementModel getThirdWeek()
{
	return secondWeekAverageMeasurement;
}
public void calculateAverage(String type)
{
	Collections.sort(measurements);
	MeasurementModel m = measurements.get(0);
	Date firstWeek = new Date(m.getMeasurementDate().getTime() + (7*24 * 60 * 60 * 1000));
	Date secondWeek = new Date(m.getMeasurementDate().getTime() + (2*7*24 * 60 * 60 * 1000));
	Date thirdWeek = new Date(m.getMeasurementDate().getTime() + (2*7*24 * 60 * 60 * 1000));
	Iterator itr = measurements.iterator();
	int firstWeekCount = 0;
	int secondWeekCount = 0;
	int thirdWeekCount = 0;

	int firstWeekAverage = 0;
	int secondWeekAverage = 0;
	int thirdWeekAverage = 0;

	while(itr.hasNext()){
		MeasurementModel tmp = (MeasurementModel) itr.next();
		if(tmp.getMeasurementDate().before(firstWeek) && tmp.getType() == type)
		{
			firstWeekAverage = firstWeekAverage + tmp.getValue();
			firstWeekCount++;
		}
		if(tmp.getMeasurementDate().before(secondWeek) && tmp.getMeasurementDate().after(firstWeek) && tmp.getType() == type)
		{
			secondWeekAverage = secondWeekAverage + tmp.getValue();
			secondWeekCount++;
		}
		if(tmp.getMeasurementDate().after(secondWeek) && tmp.getType() == type)
		{
			thirdWeekAverage = thirdWeekAverage + tmp.getValue();
			thirdWeekCount++;
			
		}
	 }
	if(firstWeekCount > 0)
	{
	firstWeekAverageMeasurement = new MeasurementModel();
	firstWeekAverageMeasurement.setType(type);
	firstWeekAverageMeasurement.setValue(firstWeekAverage/firstWeekCount);
	firstWeekAverageMeasurement.setMeasurementDate(firstWeek);
	System.out.println("First week average:" + firstWeekAverage/firstWeekCount);
	}
	if(secondWeekCount > 0)
	{
	secondWeekAverageMeasurement = new MeasurementModel();
	secondWeekAverageMeasurement.setType(type);
	secondWeekAverageMeasurement.setValue(secondWeekAverage/secondWeekCount);
	secondWeekAverageMeasurement.setMeasurementDate(secondWeek);
	System.out.println("Second week average:" + secondWeekAverage/secondWeekCount);
	}

	if(thirdWeekCount > 0)
	{
	thirdWeekAverageMeasurement = new MeasurementModel();
	thirdWeekAverageMeasurement.setType(type);
	thirdWeekAverageMeasurement.setValue(thirdWeekAverage/thirdWeekCount);
	thirdWeekAverageMeasurement.setMeasurementDate(thirdWeek);
	System.out.println("Third week average:" + thirdWeekAverage/thirdWeekCount);
	}

}
}
