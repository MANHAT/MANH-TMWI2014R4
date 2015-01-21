/**
 * 
 */
package com.manh.wmos.services.outbound.backingbean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.logistics.javalib.util.Misc;
import com.manh.ils.db.AppContextUtil;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.ils.wmservices.utils.WMMisc;
import com.manh.ils.wmservices.utils.WMRFConstants;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wmos.services.outbound.TMWIOutboundObjectFactory;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;
import com.manh.wmos.services.outbound.data.TMWIWMConstants;
import com.manh.wmos.services.outbound.service.TMWIIRFPackToteManagerService;
import com.manh.wmos.services.wmcommon.backingbean.RFImplBackingBean;
import com.manh.wmos.services.wmcommon.data.RFImplData;
import com.manh.wmos.util.cache.TMWICacheUtilService;

/**
 * @author ibiswas
 */
public class TMWIRFChangePrinterBackingBean extends RFImplBackingBean
{

	/** The Constant STATE_OBJ_PARM_NAME. */
	// public static final String STATE_OBJ_PARM_NAME = "rfChangePrinterObj";
	public static final String STATE_OBJ_PARM_NAME = "rfPackToteStateObj";

	/** TMWI custom data for change printer : _custStatedata. */
	// private TMWIRFChangePrinterData _custStatedata;

	private TMWIRFPackFromToteData _custStatedata;

	/** Returns handle to the TMWICacheUtilService, for clearing cache. */
	public static TMWICacheUtilService cacheUtil;

	public TMWIRFChangePrinterBackingBean()
	{
		super();
		try
		{
			// only if TranParm 500 is enabled, then the flow can come here.

			_custStatedata = (TMWIRFPackFromToteData) (loadOrCreateStateDataObject(STATE_OBJ_PARM_NAME)).getDataObject();
			// _custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject(); --> Returns null pointer exception.

			TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Data Object is : " + _custStatedata);

			HttpServletRequest req = WMMisc.getHttpServletRequest();

			if (_custStatedata == null)
			{
				TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Data object is NULL.");

				_custStatedata = (TMWIRFPackFromToteData) (com.manh.wmos.services.outbound.OutboundObjectFactory.getInstance()).getRFPackFromToteData();

				if (req.getAttribute(WMRFConstants.TRAN_ID) != null)
				{
					_custStatedata.setTranId((String) WMMisc.getHttpServletRequest().getAttribute(WMRFConstants.TRAN_ID));
					setDataObject(_custStatedata);
					TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "TMWIRFPackFromToteData Data Object: " + _custStatedata.toString());
				}
			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			setDataObject(_custStatedata);
			addUnexpectedError(e);
		}

		TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Saving TMWIRFPackFromToteData data object..");
		setDataObject(_custStatedata);

	}

	/**
	 * Gets the state data.
	 * 
	 * @return the state data
	 */
	public TMWIRFPackFromToteData get_custStatedata()
	{
		return _custStatedata;
	}

	/**
	 * Sets the state data.
	 */
	public void set_custStatedata(TMWIRFPackFromToteData _custStatedata)
	{
		this._custStatedata = _custStatedata;
		setDataObject(_custStatedata);
	}

	/**
	 * Validates and updates Print Requester in Misc_Flags of C-W10. If the Print Requester is validated and updated successfully, it then clears the Java and CPP cache.
	 * 
	 * @return
	 */
	public String changePrintRequesterAction()
	{
		TMWIWMLogHelper.logEnter(TMWIWMConstants.EXT_10, "In changePrintRequesterAction() method");

		if (Misc.isNullTrimmedString(_custStatedata.getPutWall()) || Misc.isNullTrimmedString(_custStatedata.getSection()) || Misc.isNullTrimmedString(_custStatedata.getPrintRequester()))
		{
			TMWIWMLogHelper.logExit(TMWIWMConstants.EXT_10, "One or more values of Putwall, Section or Print Requester are blank. Returning ERROR");
			// EX1001 : One or more null values entered
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("EX1001");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			return "ERROR";
		}

		// Validating PUTWALL, SECTION & PRINT REQUESTER, If VALID, it returns 200.
		int validationStatus = getTMWIRFPackToteManagerServiceImpl().validateData(_custStatedata.getPutWall(), _custStatedata.getSection(), _custStatedata.getPrintRequester());
		TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Validation of PutWall, Section & Print Requestor returned : " + validationStatus);

		if (validationStatus == TMWIWMConstants.PRINT_REQUESTER_NOT_FOUND)
		{
			TMWIWMLogHelper.logError(TMWIWMConstants.EXT_10, "Print Requester NOT found.");
			// 1069 : Invalid Requester
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.SYSCONTROL.getRawInformation("1069");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			return "ERROR";
		}

		if (validationStatus == TMWIWMConstants.PRINTING_SERVER_NOT_FOUND)
		{
			TMWIWMLogHelper.logError(TMWIWMConstants.EXT_10, "Printing Server NOT found.");
			// 1072 : Printer Not Valid For Label
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.SYSCONTROL.getRawInformation("1072");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			return "ERROR";
		}

		if (validationStatus == TMWIWMConstants.INVALID_PUTWALL_SECTION)
		{
			TMWIWMLogHelper.logError(TMWIWMConstants.EXT_10, "Invalid Put Wall & Section. Returning ERROR.");
			// 1000 : Invalid Put Wall and Section
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1000");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			return "ERROR";
		}

		TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Valid Printer Data Found.");
		boolean updateStatus = getTMWIRFPackToteManagerServiceImpl().updatePrintRequester(_custStatedata.getPutWall(), _custStatedata.getSection(), _custStatedata.getPrintRequester());
		if (updateStatus)
		{
			TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "MISC_FIELDS updated");
			try
			{
				clearCache();
				TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Both Java and CPP cache cleared.");
			}
			catch (Exception ex)
			{
				TMWIWMLogHelper.logException(TMWIWMConstants.EXT_10, ex, "FAILED to Clear the Cache..!!");
			}
			return "SUCCESS";
		}
		else
		{
			// EX1002 : System Code update failed
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("EX1002");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			TMWIWMLogHelper.logError(TMWIWMConstants.EXT_10, "Updation of MISC_FIELDS in SYS_CODE table failed. Returning ERROR.");
			return "ERROR";
		}
	}

	/**
	 * Clears cache for a user.
	 */
	public void clearCache()
	{
		TMWIWMLogHelper.logEnter(TMWIWMConstants.EXT_10, "Going to clear cache..");
		String user = WMMisc.getUserWarehouseInfo().getUserId();
		TMWIWMLogHelper.logDebug(TMWIWMConstants.EXT_10, "Clearing Java and CPP cache for User = [" + user + "]");
		cacheUtil = null;
		if (cacheUtil == null)
		{
			cacheUtil = new TMWICacheUtilService();
			cacheUtil.clearCache(user);
		}
		else
			cacheUtil.clearCache(user);
		TMWIWMLogHelper.logExit(TMWIWMConstants.EXT_10, "Cache clearing successful.");
	}

	/**
	 * Setting of back action from the Change Printer screen.
	 */
	@Override
	public void preBackAction()
	{
		TMWIWMLogHelper.logEnter();

		((RFImplData) getIPSDetailData().getDataObject()).setBackAction("ACCEPT_TOTE");

		TMWIWMLogHelper.logExit();
	}

	/**
	 * Returns the handle to Service Layer.
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
