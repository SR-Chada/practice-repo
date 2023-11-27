package com.macnicagwi.globalportal.core.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.dam.cfm.ContentFragment;

public class MilestoneItem {
	
	
	private String milestoneDescription;
	private Date milestoneDate;
	private int milestoneDecade;

	private String formattedDate;


	public String getMilestoneDescription() {
		return milestoneDescription;
	}

	public MilestoneItem() {
		// do nothing
	}

	public MilestoneItem(Resource resource) throws ParseException {
		ContentFragment milestone = resource.adaptTo(ContentFragment.class);
		this.milestoneDescription = milestone.getElement("milestone-description").getContent();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-dd");

		String date = milestone.getElement("date").getContent();
		this.milestoneDate= dateFormat.parse(date);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(milestoneDate);
		this.milestoneDecade = calendar.getWeekYear() - calendar.getWeekYear() % 10;
		this.formattedDate = dateFormat.format(this.milestoneDate);

	}

	public void setMilestoneDescription(String milestoneDescription) {
		this.milestoneDescription = milestoneDescription;
	}

	public Date getMilestoneDate() {
		return milestoneDate;
	}

	public void setMilestoneDate(Date milestoneDate) {
		this.milestoneDate = milestoneDate;
	}

	public int getMilestoneDecade() {
		return milestoneDecade;
	}

	public void setMilestoneDecade(int milestoneDecade) {
		this.milestoneDecade = milestoneDecade;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	

}
