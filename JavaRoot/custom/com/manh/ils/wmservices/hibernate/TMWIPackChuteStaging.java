package com.manh.ils.wmservices.hibernate;

import java.util.Date;

;

public class TMWIPackChuteStaging
{

	/**
	 * default constructor.
	 */

	private String packWaveNbr;
	private String origChuteId;
	private String currChuteId;
	private int packingStatus;
	private String stagingLocnId;
	private String userId;
	private Date createdDttm;
	private Date modDateDttm;

	public TMWIPackChuteStaging()
	{

	}

	private int chuteStagingId;

	public int getChuteStagingId()
	{
		return chuteStagingId;
	}

	public void setChuteStagingId(int chuteStagingId)
	{
		this.chuteStagingId = chuteStagingId;
	}

	public String getPackWaveNbr()
	{
		return packWaveNbr;
	}

	public void setPackWaveNbr(String packWaveNbr)
	{
		this.packWaveNbr = packWaveNbr;
	}

	public String getOrigChuteId()
	{
		return origChuteId;
	}

	public void setOrigChuteId(String origChuteId)
	{
		this.origChuteId = origChuteId;
	}

	public String getCurrChuteId()
	{
		return currChuteId;
	}

	public void setCurrChuteId(String currChuteId)
	{
		this.currChuteId = currChuteId;
	}

	public int getPackingStatus()
	{
		return packingStatus;
	}

	public void setPackingStatus(int packingStatus)
	{
		this.packingStatus = packingStatus;
	}

	public String getStagingLocnId()
	{
		return stagingLocnId;
	}

	public void setStagingLocnId(String stagingLocnId)
	{
		this.stagingLocnId = stagingLocnId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Date getCreatedDttm()
	{
		return createdDttm;
	}

	public void setCreatedDttm(Date createdDttm)
	{
		this.createdDttm = createdDttm;
	}

	public Date getModDateDttm()
	{
		return modDateDttm;
	}

	public void setModDateDttm(Date modDateDttm)
	{
		this.modDateDttm = modDateDttm;
	}

}
