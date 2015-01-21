package com.manh.wmos.services.outbound.data;

import com.logistics.javalib.util.Misc;
import com.manh.wmos.services.wmcommon.data.RFImplData;

/** Data class for Change Printer */
public class TMWIRFChangePrinterData extends TMWIRFPackFromToteData
//public class TMWIRFChangePrinterData extends RFImplData
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String putWall = Misc.EMPTY_STRING;
	private String section = Misc.EMPTY_STRING;
	private String printRequester = Misc.EMPTY_STRING;

	@Override
	public String toString()
	{
		return "TMWIRFChangePrinterData [putWall=" + putWall + ", section=" + section + ", printRequester=" + printRequester + "]";
	}

	public String getPutWall()
	{
		return putWall;
	}

	public void setPutWall(String putWall)
	{
		this.putWall = putWall;
	}

	public String getSection()
	{
		return section;
	}

	public void setSection(String section)
	{
		this.section = section;
	}

	public String getPrintRequester()
	{
		return printRequester;
	}

	public void setPrintRequester(String printRequester)
	{
		this.printRequester = printRequester;
	}

}
