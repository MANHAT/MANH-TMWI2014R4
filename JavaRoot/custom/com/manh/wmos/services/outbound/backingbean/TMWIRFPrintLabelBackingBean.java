package com.manh.wmos.services.outbound.backingbean;

import org.springframework.context.ApplicationContext;

import com.manh.ils.db.AppContextUtil;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wmos.services.outbound.data.RFPrintLabelData;
import com.manh.wmos.services.outbound.data.TMWIWMConstants;
import com.manh.wmos.services.outbound.service.TMWIIRFPackToteManagerService;

public class TMWIRFPrintLabelBackingBean extends RFPrintLabelBackingBean
{
	/**
	 * The state data
	 */
	private RFPrintLabelData _custStatedata;

	public TMWIRFPrintLabelBackingBean()
	{
		super();
	}

	/**
	 * This action method sets the work station for the login user and prints Carton/content label based on the value of flag. Before setting the workstation, custom validation is also carried out.
	 * 
	 * @return a string constant to decide the the navigation of the screen.
	 */
	@Override
	public String enterAction()
	{
		TMWIWMLogHelper.logEnter("TMWIRFPrintLabelBackingBean - enterAction");
		// checking Print Requester - custom validation

		short status = TMWIWMConstants.INVALID_PRINTER;

		// validating the printer.
		status = getTMWIRFPackToteManagerServiceImpl().validatePrinterFromSysCode(_custStatedata.getPrinterName());

		if (status == TMWIWMConstants.INVALID_PRINTER)
		{
			TMWIWMLogHelper.logDebug("OUTQ : Custom Logic : Printer is NOT Valid. Invalid Printer Error.");
			// 1069 : Invalid Requester
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.SYSCONTROL.getRawInformation("1069");
			_custStatedata.addDataError(rawInfo.getId(), rawInfo.getMessage().add("PrinterName", true), "RFPrintLabelBackingBean");
			TMWIWMLogHelper.logExit("Invalid Printer, returning ERROR.");
			return ERROR;
		}
		else
		{
			// validation successful - calling super for base validations
			TMWIWMLogHelper.logExit("OUTQ : Custom Logic : Printer is Valid. Moving to base validations..");
			return super.enterAction();
		}
	}

	/**
	 * Getting the SERVICE layer.
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private TMWIIRFPackToteManagerService getTMWIRFPackToteManagerServiceImpl()
	{
		TMWIWMLogHelper.logEnter();
		ApplicationContext ctx = AppContextUtil.getAppCtx("wm.outbound");
		TMWIIRFPackToteManagerService service = (TMWIIRFPackToteManagerService) ctx.getBean("RFPackToteManagerServiceImpl");
		TMWIWMLogHelper.logExit();
		return service;
	}

}
