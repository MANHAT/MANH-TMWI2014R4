/**
 * 
 */
package com.manh.wmos.services.outbound.data;

import com.logistics.javalib.util.Misc;

/**
 * This is the custom data class for TMWI.
 * 
 * @author ibiswas
 */
public class TMWIRFPackFromToteData extends RFPackFromToteData
{

	public TMWIRFPackFromToteData()
	{
		super();
	}

	private static final long serialVersionUID = 1L;

	private String putWall = Misc.EMPTY_STRING;
	private String section = Misc.EMPTY_STRING;
	private String printRequester = Misc.EMPTY_STRING;
	private boolean ex10Enabled;

	private String oLpnSize = Misc.EMPTY_STRING;
	private String slot = Misc.EMPTY_STRING;
	private boolean menuParam500PutWall=false;
	private boolean refField1 = false;
	private String isLpnRefField1ValueNull = Misc.EMPTY_STRING;

	private boolean EXT13Enabled;
	
	
	
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
	public boolean isEXT13Enabled()
	{
		return EXT13Enabled;
	}
	public void setEXT13Enabled(boolean eXT13Enabled)
	{
		EXT13Enabled = eXT13Enabled;
	}

	private boolean endOfOlpnInfoSupressed;

	public boolean isEx10Enabled()
	{
		return ex10Enabled;
	}
	public void setEx10Enabled(boolean ex10Enabled)
	{
		this.ex10Enabled = ex10Enabled;
	}
	public String getoLpnSize()
	{
		return oLpnSize;
	}
	public void setoLpnSize(String oLpnSize)
	{
		this.oLpnSize = oLpnSize;
	}
	public String getSlot()
	{
		return slot;
	}
	public void setSlot(String slot)
	{
		this.slot = slot;
	}
	public boolean isMenuParam500PutWall()
	{
		return menuParam500PutWall;
	}
	public void setMenuParam500PutWall(boolean menuParam500PutWall)
	{
		this.menuParam500PutWall = menuParam500PutWall;
	}
	public boolean isRefField1()
	{
		return refField1;
	}
	public void setRefField1(boolean refField1)
	{
		this.refField1 = refField1;
	}
	public String getIsLpnRefField1ValueNull()
	{
		return isLpnRefField1ValueNull;
	}
	public void setIsLpnRefField1ValueNull(String isLpnRefField1ValueNull)
	{
		this.isLpnRefField1ValueNull = isLpnRefField1ValueNull;
	}
	
	public boolean isEndOfOlpnInfoSupressed()
	{
		return endOfOlpnInfoSupressed;
	}
	public void setEndOfOlpnInfoSupressed(boolean endOfOlpnInfoSupressed)
	{
		this.endOfOlpnInfoSupressed = endOfOlpnInfoSupressed;
	}
	@Override
	public String toString()
	{
		return "TMWIRFPackFromToteData [putWall=" + putWall + ", section=" + section + ", printRequester=" + printRequester + ", ex10Enabled=" + ex10Enabled + ", oLpnSize=" + oLpnSize + ", slot="
				+ slot + ", menuParam500PutWall=" + menuParam500PutWall + ", refField1=" + refField1 + ", isLpnRefField1ValueNull=" + isLpnRefField1ValueNull + ", EXT13Enabled=" + EXT13Enabled
				+ ", endOfOlpnInfoSupressed=" + endOfOlpnInfoSupressed + "]";
	}
	
	

	

	
}
