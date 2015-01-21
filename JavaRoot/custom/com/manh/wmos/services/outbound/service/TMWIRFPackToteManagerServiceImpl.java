/*
 * Copyright &#169; 2007 Manhattan Associates All rights reserved. Do not copy, modify or redistribute this file without specific written permission from Manhattan Associates.
 */
package com.manh.wmos.services.outbound.service;

import org.springframework.context.ApplicationContext;

import com.logistics.javalib.util.Misc;
import com.manh.ils.db.AppContextUtil;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMException;
import com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAO;

public class TMWIRFPackToteManagerServiceImpl extends RFPackToteManagerServiceImpl implements TMWIIRFPackToteManagerService
{

	@Override
	public String getoLpnSize(String cartonNbr) throws WMException
	{
		String result = Misc.EMPTY_STRING;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: getoLpnSize()");

		result = getTMWIRFPackToteManagerDAOImpl().getoLpnSize(cartonNbr).toString();

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: getoLpnSize():result =" + result);

		return result;

	}

	@Override
	public boolean validateSlotSysCode(String slot) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateSlotSysCode()");

		result = getTMWIRFPackToteManagerDAOImpl().validateSlotSysCode(slot);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateSlotSysCode(): result="
					+ result);

		return result;
	}

	@Override
	public boolean validateSlotAlreadyAssingnedToOlpn(String slot) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
					"Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateSlotAlreadyAssingnedToOlpn()");

		result = getTMWIRFPackToteManagerDAOImpl().validateSlotAlreadyAssingnedToOlpn(slot);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
					"Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateSlotAlreadyAssingnedToOlpn(): result=" + result);

		return result;
	}

	@Override
	public boolean validatePutWallForOlpn(String slot) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validatePutWallForOlpn()");

		result = getTMWIRFPackToteManagerDAOImpl().validatePutWallForOlpn(slot);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
					"Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validatePutWallForOlpn(): result:" + result);
		return result;
	}

	@Override
	public boolean updateSlotOnLPN(String slot, String cartonNbr) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: updateSlotOnLPN()");

		result = getTMWIRFPackToteManagerDAOImpl().updateSlotOnLPN(slot, cartonNbr);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: updateSlotOnLPN() :result="
					+ result);
		return result;
	}

	@Override
	public boolean updateLpnRefField(String userId, String tote) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: updateLpnRefFiled()");

		result = getTMWIRFPackToteManagerDAOImpl().updateLpnRefField(userId, tote);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: updateLpnRefFiled(): result="
					+ result);

		return result;
	}

	@Override
	public String validateLpnRefFieldNull(String cartonNbr) throws WMException
	{
		String result = Misc.EMPTY_STRING;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateLpnRefFieldNull()");

		result = getTMWIRFPackToteManagerDAOImpl().validateLpnRefFieldNull(cartonNbr);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateLpnRefFieldNull()");

		return result;
	}

	@Override
	public boolean updatePckStatusOfChuteStage(String cartonNbr) throws WMException
	{
		boolean result = false;
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateLpnRefFieldNull()");

		result = getTMWIRFPackToteManagerDAOImpl().updatePckStatusOfChuteStage(cartonNbr);

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
					"Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: validateLpnRefFieldNull(): result=" + result);

		return result;
	}

	@Override
	public int validateData(String putwall, String section, String printRequester)
	{
		return getTMWIRFPackToteManagerDAOImpl().validateData(putwall, section, printRequester);
	}

	@Override
	public boolean updatePrintRequester(String putWall, String section, String printRequester)
	{
		return getTMWIRFPackToteManagerDAOImpl().updatePrintRequester(putWall, section, printRequester);
	}

	@Override
	public boolean validateToteUser(String tote, String currentUser)
	{
		return getTMWIRFPackToteManagerDAOImpl().validateToteUser(tote, currentUser);
	}

	@Override
	public short validatePrinterFromSysCode(String printerName)
	{
		return getTMWIRFPackToteManagerDAOImpl().validatePrinterFromSysCode(printerName);
	}

	/**
	 * Getting the DAO layer implementation.
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public TMWIRFPackToteManagerDAO getTMWIRFPackToteManagerDAOImpl()
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
				"Entering com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: getTMWIRFPackToteManagerDAOImpl()");

		// Reference to application context defined in beanRefContext.xml
		ApplicationContext ctx = AppContextUtil.getAppCtx("wm.outbound");
		TMWIRFPackToteManagerDAO service = (TMWIRFPackToteManagerDAO) ctx.getBean("TMWIRFPackToteManagerDAO");

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
				"Exiting com.manh.wmos.services.outbound.service.TMWIRFPackToteManagerServiceImpl :: getTMWIRFPackToteManagerDAOImpl() :service =" + service);
		return service;
	}

}
