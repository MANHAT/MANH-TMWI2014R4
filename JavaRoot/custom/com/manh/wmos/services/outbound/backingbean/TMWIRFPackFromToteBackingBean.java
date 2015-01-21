/**
 * 
 */
package com.manh.wmos.services.outbound.backingbean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.manh.ils.db.AppContextUtil;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.ils.wmservices.utils.WMMisc;
import com.manh.ils.wmservices.utils.WMRFConstants;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wmos.services.outbound.PackFromToteConfiguration;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;
import com.manh.wmos.services.outbound.service.TMWIIRFPackToteManagerService;
import com.manh.wmos.services.systemctrl.data.ITranData;

/**
 * @author IBiswas
 */
public class TMWIRFPackFromToteBackingBean extends RFPackFromToteBackingBean
{

	/** The custom TMWI state data */
	private TMWIRFPackFromToteData _custStatedata;

	private PackFromToteConfiguration configuration;

	public TMWIRFPackFromToteBackingBean()
	{
		super();
		try
		{
			// _custStatedata = (TMWIRFPackFromToteData) (loadOrCreateStateDataObject(STATE_OBJ_PARM_NAME)).getDataObject();
			_custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject();

			TMWIWMLogHelper.logDebug(_custStatedata.toString());
			HttpServletRequest req = WMMisc.getHttpServletRequest();

			if (_custStatedata == null)
			{
				TMWIWMLogHelper.logDebug("Data object is NULL.");
				
				_custStatedata = (TMWIRFPackFromToteData) com.manh.wmos.services.outbound.OutboundObjectFactory.getInstance().getRFPackFromToteData();
				if (req.getAttribute(WMRFConstants.TRAN_ID) != null)
				{
					_custStatedata.setTranId((String) WMMisc.getHttpServletRequest().getAttribute(WMRFConstants.TRAN_ID));
					setDataObject(_custStatedata);
				}
			}

			String ext10EnableParm = "500";

			TMWIWMLogHelper.logDebug("Transaction Id : [" + _custStatedata.getTranId() + "]");
			configuration = PackFromToteConfiguration.getInstance(_custStatedata.getTranId());

			ITranData tranData = (ITranData) configuration.getTranMap().get(ext10EnableParm);
			if (tranData != null && tranData.getMenuParm() != null && tranData.getMenuParm().equalsIgnoreCase("Y"))
			{
				TMWIWMLogHelper.logDebug("Transaction Parameter 500 is ENABLED.");
				_custStatedata.setEx10Enabled(true);
				setDataObject(_custStatedata);
			}

		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			setDataObject(_custStatedata);
			addUnexpectedError(e);
		}
	}

	/**
	 * Forwards request to TMWIRFChangePrinter.xhtml.
	 * 
	 * @return
	 */
	public String ctrlPAction()
	{
		TMWIWMLogHelper.logEnter("ctrlPAction : Change Printer Action on pressing Ctrl+P");
		return "CHANGE_PRINTER";
	}

	@Override
	public String enterAction()
	{
		TMWIWMLogHelper.logEnter("TMWIRFPackFromToteBackingBean :: enterAction method");

		String retVal = super.enterAction();

		if (!_custStatedata.isEx10Enabled())
		{
			return retVal;
		}

		try
		{
			// _custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject(); //todo
			boolean isValidUser = getTMWIRFPackToteManagerServiceImpl().validateToteUser(_custStatedata.getTote(), WMMisc.getUserWarehouseInfo().getUserId());

			if (isValidUser)
			{
				TMWIWMLogHelper.logDebug("Leaving Custom Code and going to Base Code..");
				return retVal;
			}

			TMWIWMLogHelper.logError("NOT a valid User.");
			// 1001 : User %s assigned to Tote
			WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1001");
			_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
			setDataObject(_custStatedata);

			retVal = "ERROR";
		}
		catch (Exception e)
		{
			TMWIWMLogHelper.logException(e);
		}

		TMWIWMLogHelper.logExit();
		return retVal;
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
