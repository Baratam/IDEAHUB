package com.vv.repositories;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.vv.model.Idea;
import com.vv.model.Profile;

@Projection(name = "inlineProfile", types = { Idea.class }) 
interface InlineProfile {
  
	long getId();

	Profile getProfile();

	String getItype();

	String getProblem();

	String getIndustry();

	String getAreaOfFunc();

	String getTechnology();

	String getSolnTitle();

	String getSolnDesc();

	String getBuBenefit();

	 String getBuInvest();
	 
	 String getBuIncome();

	 String getDocumentName();

	 String getVideoName();

	 String getStatus();

	 float getRating();

	 Date getCreatedAt();

	 String getContbstatus();

}
