package com.manh.wmos.services.outbound.data;

import com.manh.ils.wmservices.utils.WMConstants;

public interface TMWIWMConstants extends WMConstants 
{
	
	public static final short VALID_PRINTER = 1;
	public static final short INVALID_PRINTER = 0;
	public static final int INVALID_PUTWALL_SECTION = 2;
	
	public static final int VALID_PRINTER_DATA_FOUND = 200;
	public static final int PRINTING_SERVER_NOT_FOUND = 500;
	public static final int PRINT_REQUESTER_NOT_FOUND = 404;
	
	public static final String EXT_10 = "EXT 10";

}
