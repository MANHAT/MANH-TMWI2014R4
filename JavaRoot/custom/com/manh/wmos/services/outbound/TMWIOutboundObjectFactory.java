/**
 * 
 */
package com.manh.wmos.services.outbound;


/**
 * @author ibiswas
 *
 */
public class TMWIOutboundObjectFactory extends OutboundObjectFactory {
	
	/*
	 TMWI-EXT10
	 */
	public com.manh.wmos.services.outbound.data.RFPackFromToteData getRFPackFromToteData()
	{
		return new com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData();
	}
	
/*	public com.manh.wmos.services.outbound.data.TMWIRFChangePrinterData getRFChangePrinterData()
	{
		return new com.manh.wmos.services.outbound.data.TMWIRFChangePrinterData();
	}*/

}
