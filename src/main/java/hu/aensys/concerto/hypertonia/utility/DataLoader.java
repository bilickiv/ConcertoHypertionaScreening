package hu.aensys.concerto.hypertonia.utility;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.DetectedIssue;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Observation.Component;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu2.valueset.DetectedIssueSeverityEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import hu.aensys.concerto.hypertonia.MeasurementModel;

public class DataLoader {
	FhirContext ctx;
	String serverBase;
	IGenericClient client;
	List<MeasurementModel> measurements;
	Date currentDate;
	String patientName;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void init()
	{
		 ctx = new FhirContext();
         serverBase = "http://52.29.254.68:8080/hapi-fhir-jpaserver-example/baseDstu2";
         client = ctx.newRestfulGenericClient(serverBase);
         measurements = new Vector<MeasurementModel>(); 
	}
	private void retrieveData()
	{
		 String id = "CON1";
         try {
        	 String string = "April 30, 2016";
        	 DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        	 currentDate = format.parse(string);
        	 Date targetDate = new Date(currentDate.getTime() - (3*7*24 * 60 * 60 * 1000));
        	 Calendar startingWeek = Calendar.getInstance();
        	 Calendar endingWeek = Calendar.getInstance();
        	 startingWeek.setTime(targetDate);
        	 endingWeek.setTime(currentDate);
        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	 //System.out.println("Target Date:" + sdf.format(calendar.getTime()) );
             Patient patient = client.read(Patient.class, id);
             Bundle response = client
                     .search()
                     .forResource(Observation.class)
                     .where(Observation.SUBJECT.hasId("CON1"))
                     .and(Observation.DATE.after().day(sdf.format(startingWeek.getTime())))
                     .and(Observation.DATE.before().day(sdf.format(endingWeek.getTime())))
                     .and(Observation.CODE.exactly().code("55284-4"))
                     .include(Observation.INCLUDE_PATIENT).returnBundle(Bundle.class)
                     .sort().ascending(Observation.DATE)
                     .execute(); 
             //Do something with patient
            // System.out.println(patient.getName()+"   the name");
            // System.out.println(response.getTotal()+"   the name");
             this.extractData(response);
             int step = 0;
             while(response.getLink(Bundle.LINK_NEXT) != null)
             {            	 
          	   response = client.loadPage().next(response).execute();
          	  //System.out.println(response.getTotal());
          	 this.extractData(response);
          	 step++;
          	}
             // Change the patient gender
            System.out.println("Number of measurements retrieved:" + step*10);
         } catch (ResourceNotFoundException ex) {
             System.out.println("No patient with the ID of " + id);
         } catch (Exception ex){
             System.out.println("Unexpected error:" + ex.getMessage());
         }
	}
	private void extractData(Bundle response)
	{
        int typeCount = 0;
        MeasurementModel measurement;
	  	for (Bundle.Entry entry : response.getEntry()) {
            if (entry.getResource() instanceof Observation) {
            	typeCount = 0;
                Observation observation = (Observation) entry.getResource();
                List<Component> com = observation.getComponent();
             //   DateTimeDT dateTaken = observation.getEffective();
                Field f;
				try {
					f = observation.getClass().getDeclaredField("myEffective");
					f.setAccessible(true);
	                try {
	                	DateTimeDt iWantThis = (DateTimeDt) f.get(observation);
	                	Date d = iWantThis.getValue();
	                	//System.out.println(d);
	                    
	                    for (Component temp : com) {
	                    	QuantityDt q = (QuantityDt) temp.getValue();
	                    	if(typeCount == 0)
	                    	{
	                    		measurement = new MeasurementModel();
	                    		measurement.setMeasurementDate(d);
	                    		measurement.setValue(q.getValue().intValue());
	                    		measurement.setType("Systolic");
	                    		measurements.add(measurement);
	                    		//System.out.println("Systolic:");

	                    	}
	                    	if(typeCount == 1)
	                    	{
	                    		measurement = new MeasurementModel();
	                    		measurement.setMeasurementDate(d);
	                    		measurement.setValue(q.getValue().intValue());
	                    		measurement.setType("Diastolic");
	                    		measurements.add(measurement);
	                			//System.out.println("Diastolic:");

	                    	}
	                    	if(typeCount == 2)
	                    	{
	                			//System.out.println("Pulse:");

	                    	}
	            			//System.out.println(q.getValue());
	            			typeCount++;
	            		}
	                 
	                    //QuantityDt q = observation.VALUE_QUANTITY;
	                   // CodingDt firstCoding = observation.getCode().getCodingFirstRep();
	                   // System.out.println("Observation: code = " + firstCoding.getSystem() + "|" + firstCoding.getCode() + "|" + firstCoding.getDisplay());
	                   // System.out.println("Observation: date = " + d);

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //NoSuchFieldException
                
    
            } else if (entry.getResource() instanceof  Patient) {
                Patient patient = (Patient) entry.getResource();
                patientName = patient.getNameFirstRep().getFamilyFirstRep() + ", " + patient.getNameFirstRep().getGivenFirstRep();
                //System.out.println("Patient: name = " + patient.getNameFirstRep().getFamilyFirstRep() + ", " + patient.getNameFirstRep().getGivenFirstRep());
            }else
    	  	{
    	  		//System.out.println("It is not Observation");
    	  	}
        }
	  	
	}
	public List<MeasurementModel> getMeasurements()
	{
		init();
		retrieveData();
		Collections.sort(measurements);
		return measurements;
	}
	public String getName()
	{
		return patientName;
	}

}
