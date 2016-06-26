package hu.aensys.concerto.hypertonia.utility;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
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
import hu.aensys.concerto.hypertonia.IllnessModel;
import hu.aensys.concerto.hypertonia.MeasurementModel;
import hu.aensys.concerto.hypertonia.UserModel;
public class IllnessRegistration {
	FhirContext ctx;
	String serverBase;
	IGenericClient client;
	UserModel um;
	private List<IllnessModel> illnesses;
	public void init(UserModel um)
	{
		 ctx = new FhirContext();
         serverBase = "http://52.29.254.68:8080/hapi-fhir-jpaserver-example/baseDstu2";
         client = ctx.newRestfulGenericClient(serverBase);
         this.um = um;
	}
	public void evaluate()
	{
		illnesses = um.getIllnesses();
		Iterator itr = illnesses.iterator();
		while(itr.hasNext()){
			IllnessModel tmp = (IllnessModel) itr.next();
			registerIllness(tmp.getType());
		}
	}
	public void registerIllness(String description)
	{
		System.out.println("Registering illness:" + description);
		DetectedIssue d = new DetectedIssue();
    	ResourceReferenceDt patient = new ResourceReferenceDt("Patient/CON1");
    	ResourceReferenceDt doctor = new ResourceReferenceDt("Practitioner/CONDOC1");

    	d.setPatient(patient);
    	d.setAuthor(doctor);
    	d.setSeverity(DetectedIssueSeverityEnum.MODERATE);
    	d.setDetail(description);
    	d.setDate(new Date(),TemporalPrecisionEnum.DAY);
    	List<IResource> resources = new ArrayList<IResource>();
	      resources.add(d);	         	      
	      client.transaction().withResources(resources).execute();
	}

}
