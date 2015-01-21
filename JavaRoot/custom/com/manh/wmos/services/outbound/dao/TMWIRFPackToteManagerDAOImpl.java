package com.manh.wmos.services.outbound.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.logistics.javalib.util.Misc;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMException;
import com.manh.wm.dao.WMDAOImpl;
import com.manh.wmos.services.outbound.data.TMWIWMConstants;
import com.manh.ils.wmservices.dataaccess.DataAccessException;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;

public class TMWIRFPackToteManagerDAOImpl extends WMDAOImpl implements TMWIRFPackToteManagerDAO
{

	@Override
	public String getoLpnSize(String cartonNbr) throws WMException
	{

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: getoLpnSize() : cartonNbr=" + cartonNbr);
		}

		String totalLpnQtyStr = "";
		try
		{
			String HQL_TOTAL_LPN_QTY = "select lp.commonLPNFields.totalLpnQty from LPN lp where lp.inboundOutboundIndicator='O' and lp.tcLpnId = :olpnNo";
			String[] nameList = new String[] { "olpnNo" };
			Object[] parmValueList = new Object[] { cartonNbr };
			List oLpnSize = directQuery(HQL_TOTAL_LPN_QTY, nameList, parmValueList);
			if (null != oLpnSize)
			{
				if (oLpnSize.size() > 0 && oLpnSize.get(0)!=null)
					totalLpnQtyStr = (String) oLpnSize.get(0).toString();

			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			throw (WMException) e;
		}

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: getoLpnSize()");
		}

		return totalLpnQtyStr;
	}

	@Override
	public boolean validateSlotSysCode(String slot) throws WMException
	{

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateSlotSysCode(): slot :" + slot);
		}

		String isRecordExistOnSysCode = "";
		try
		{
			String HQL_RECORD_ON_SYS_CODE = "select 1 from SysCode SC where  SC.recType='C' and SC.codeType='W10' and codeId = :slotValue";
			String[] nameList = new String[] { "slotValue" };
			Object[] parmValueList = new Object[] { slot };
			List record = directQuery(HQL_RECORD_ON_SYS_CODE, nameList, parmValueList);
			if (null != record)
			{
				if (record.size() > 0 && record.get(0)!=null)
					isRecordExistOnSysCode = (String) record.get(0).toString();

			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			throw (WMException) e;
		}

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateSlotSysCode()");
		}

		if (isRecordExistOnSysCode.equalsIgnoreCase("1"))
			return true;
		else
			return false;
	}

	@Override
	public boolean validateSlotAlreadyAssingnedToOlpn(String slot) throws WMException
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
				"Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateSlotAlreadyAssingnedToOlpn(): slot=" + slot);
		}

		String isRecordExistOnLpn = "";
		try
		{
			String HQL_RECORD_ON_LPN = "select 1 from LPN lp where  lp.inboundOutboundIndicator='O'and lp.facilityStatusId<'20' and lp.refField1String= :slotValue";
			String[] nameList = new String[] { "slotValue" };
			Object[] parmValueList = new Object[] { slot };
			List record = directQuery(HQL_RECORD_ON_LPN, nameList, parmValueList);
			if (null != record)
			{
				if (record.size() > 0 && record.get(0)!=null)
					isRecordExistOnLpn = (String) record.get(0).toString();

			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			throw (WMException) e;
		}

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateSlotAlreadyAssingnedToOlpn()");
		}

		if (isRecordExistOnLpn.equalsIgnoreCase("1"))
			return false;
		else
			return true;
	}

	@Override
	public boolean validatePutWallForOlpn(String slot) throws WMException
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validatePutWallForOlpn(): slot=" + slot);
		}

		String isValidPutWallForOlpn = "";
		try
		{

			String HQL_RECORD_ON_LPN = "select 1 from SysCode SC ,LPN l,TMWIPackChuteStaging t " + "where SC.recType='C' and SC.codeType='W10'and SC.codeId=:slotValue "
				+ "and TRIM(substr(SC.miscFlags,1,10))=t.currChuteId " + "and l.outboundLPNFields.chuteId=t.origChuteId " + "and t.packWaveNbr=l.outboundLPNFields.packWaveNbr "
				+ "and l.inboundOutboundIndicator='O'";

			String[] nameList = new String[] { "slotValue" };
			Object[] parmValueList = new Object[] { slot };
			List record = directQuery(HQL_RECORD_ON_LPN, nameList, parmValueList);
			if (null != record)
			{
				if (record.size() > 0 && record.get(0)!=null)
					isValidPutWallForOlpn = (String) record.get(0).toString();

			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			throw (WMException) e;
		}

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validatePutWallForOlpn()");
		}

		if (isValidPutWallForOlpn.equalsIgnoreCase("1"))
			return true;
		else
			return false;
	}

	@Override
	public boolean updateSlotOnLPN(String slot, String cartonNbr) throws WMException
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updateSlotOnLPN(): slot =" + slot
				+ "cartonNbr :" + cartonNbr);
		}

		int numberOfRecordsUpdated = 0;
		try
		{
			String updateSQL = " update LPN lp set lp.refField1String =:slotVlaue WHERE lp.inboundOutboundIndicator='O' and lp.tcLpnId = :cartonNbrValue";

			String[] nameList = { "slotVlaue", "cartonNbrValue" };
			Object[] parmList = { slot, cartonNbr };

			try
			{
				numberOfRecordsUpdated = directUpdate(updateSQL, nameList, parmList);
			}
			catch (Exception e)
			{
				WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
				throw (WMException) e;
			}
			finally
			{
				WMDebugLog.DEBUG_LOG
					.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "TMWIRFPackToteManagerDAOImpl :: updateSlotOnLPN(): after directUpdate :slot =" + slot + "cartonNbr :" + cartonNbr);
			}
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			throw (WMException) e;
		}

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updateSlotOnLPN(): slot =" + slot
				+ "cartonNbr :" + cartonNbr);
		}

		if (numberOfRecordsUpdated > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean updateLpnRefField(String userId, String tote) throws WMException
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updateLpnRefFiled(): slot =" + userId
				+ "tote :" + tote);
		}
		String isLpnRefFiledNull = validateLpnRefFieldNull(tote);

		int numberOfRecordsUpdated = 0;
		if (isLpnRefFiledNull == null)
		{
			try
			{
				String updateSQL = " update LPN lp set lp.refField1String =:userid WHERE lp.tcLpnId = :toteValue";

				String[] nameList = { "userid", "toteValue" };
				Object[] parmList = { userId, tote };

				try
				{
					numberOfRecordsUpdated = directUpdate(updateSQL, nameList, parmList);
				}
				catch (Exception e)
				{
					WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
					throw (WMException) e;
				}
				finally
				{
					WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "TMWIRFPackToteManagerDAOImpl :: updateLpnRefFiled():after directUpdate slot =" + userId + "tote :" + tote);
				}

			}
			catch (Exception e)
			{
				WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
				throw (WMException) e;
			}

			if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			{
				WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updateLpnRefFiled(): slot =" + userId
					+ "tote :" + tote + "numberOfRecordsUpdated = " + numberOfRecordsUpdated);
			}

		}

		if (numberOfRecordsUpdated > 0)
			return true;
		else
			return false;
	}

	public String validateLpnRefFieldNull(String tote)
	{

		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateLpnRefFiledNull(): tote :"
				+ tote);
		}

		String isLpnRefFieldNull = "";
		try
		{
			String HQL_RECORD_ON_LPN = "select lp.refField1String from LPN lp where  lp.tcLpnId = :toteValue";
			String[] nameList = new String[] { "toteValue" };
			Object[] parmValueList = new Object[] { tote };
			List record = directQuery(HQL_RECORD_ON_LPN, nameList, parmValueList);
			if (null != record)
			{
				if (record.size() > 0 && record.get(0)!=null)
					isLpnRefFieldNull = (String) (record.get(0));
			}
		}

		catch (Exception e)
		{
			// if NO record then you will get NullPointerException so return null only
			isLpnRefFieldNull = null;
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e, "NullPointerException :No record exist : validateLpnRefFiledNull() isLpnRefFiledNull=null");

		}
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG
				.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validateLpnRefFiledNull(): tote :" + tote);
		}
		return isLpnRefFieldNull;

	}

	@Override
	public boolean updatePckStatusOfChuteStage(String cartonNbr) throws WMException
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
				"Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updatePckStatusOfChuteStage():cartonNbr :" + cartonNbr);
		}

		int pckStatus = 0;
		int chuteStagingIdValue = 0;
		int numberOfRecordsUpdated = 0;
		List packStatusOrChuteStageId = null;

		try
		{
			packStatusOrChuteStageId = validatePckStausOfPackChuteStage(cartonNbr);

			if ((packStatusOrChuteStageId != null)&& packStatusOrChuteStageId.size()>0)
			{
				pckStatus = Integer.parseInt((String) packStatusOrChuteStageId.get(0));
				chuteStagingIdValue = Integer.parseInt((String) packStatusOrChuteStageId.get(1));

				if (pckStatus == 0)
				{

					String updateSQL = " update TMWIPackChuteStaging pcs set pcs.packingStatus ='10' WHERE pcs.chuteStagingId = :chuteStagingIdValue";
					String[] nameList = { "chuteStagingIdValue" };
					Object[] parmList = { chuteStagingIdValue };

					try
					{
						numberOfRecordsUpdated = directUpdate(updateSQL, nameList, parmList);
					}
					catch (Exception e)
					{
						WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
						throw (WMException) e;
					}
					finally
					{
						WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
							"Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: updatePckStatusOfChuteStage(): cartonNbr :" + cartonNbr);
					}
				}
			}

			if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			{
				WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "TMWIRFPackToteManagerDAOImpl::updatePckStatusOfChuteStage():cartonNbr :" + cartonNbr + " :pckStatus=" + pckStatus
					+ ":chuteStagingIdValue=" + chuteStagingIdValue + "ListpackStatusOrChuteStageId=" + packStatusOrChuteStageId);
			}

		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e, "pckStatus=" + pckStatus + ":chuteStagingIdValue" + chuteStagingIdValue);

		}

		if (numberOfRecordsUpdated > 0)
			return true;
		else
			return false;
	}

	private List validatePckStausOfPackChuteStage(String cartonNbr)
	{
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY,
				"Entering com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validatePckStausOfPackChuteStage(): cartonNbr :" + cartonNbr);
		}

		List<String> recordValues = null;
		try
		{
			String HQL_RECORD_ON_LPN = "select pcs.packingStatus,pcs.chuteStagingId from TMWIPackChuteStaging pcs, LPN lp"
				+ " where lp.outboundLPNFields.chuteId=pcs.origChuteId and lp.outboundLPNFields.packWaveNbr=pcs.packWaveNbr" + " and lp.tcLpnId=:cartonNbrValue and lp.inboundOutboundIndicator='O'";

			String[] nameList = new String[] { "cartonNbrValue" };
			Object[] parmValueList = new Object[] { cartonNbr };

			List record = directQuery(HQL_RECORD_ON_LPN, nameList, parmValueList);

			recordValues = new ArrayList<String>();
			if ((record != null) && (record.size() > 0))
			{

				Iterator it = record.iterator();
				while (it.hasNext())
				{
					Object[] value = (Object[]) it.next();
					if (value != null && ((value[0] != null) && value[0].toString().equals(Misc.EMPTY_STRING))
						              && ((value[1] != null) && (value[1].toString().equals(Misc.EMPTY_STRING))))
					{

						System.out.println("value[0] =" + value[0].toString());
						System.out.println("value[1] =" + value[1].toString());
						// value[0]=packingStatus and value[1]=chuteStagingId
						recordValues.add(value[0].toString());
						recordValues.add(value[1].toString());
					}

					if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
					{
						WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "TMWIRFPackToteManagerDAOImpl :: validatePckStausOfPackChuteStage(): records found with Empty values :"
							+ value.toString() + "value[0]+" + value[0].toString() + ": value[0]=" + value[1].toString());
					}

				}
			}
			if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
			{
				WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "TMWIRFPackToteManagerDAOImpl :: validatePckStausOfPackChuteStage(): cartonNbr :" + cartonNbr
					+ " :directQuery :record=" + record.toString());
			}
		}
		catch (Exception e)
		{

			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);

		}
		if (WMDebugLog.isDebugLogEnabled(WMDebugLog.OUTBOUND_SERVICES_CATEGORY))
		{
			WMDebugLog.DEBUG_LOG
				.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Exiting com.manh.wmos.services.outbound.dao.TMWIRFPackToteManagerDAOImpl :: validatePckStausOfPackChuteStage(): cartonNbr :"
					+ cartonNbr + " :recordValues=" + recordValues.toString());
		}
		return recordValues;

	}
	

	/**
	 * Checks for PUTWALL and SECTION combination in C-W10 misc_flags values. Also checks for validity of the requested 'Print Requester'.
	 */
	@Override
	public int validateData(String putwall, String section, String printRequester)
	{
		TMWIWMLogHelper.logEnter("In validateData() method..");
		StringBuffer printerQuery = new StringBuffer("SELECT 1 AS COUNT FROM LPLrfPrtRequestor pr WHERE pr.name = :nm");
		String[] nameList1 =
		{ "nm" };
		Object[] paramValueList1 =
		{ printRequester };
		List<?> namelist = directQuery(printerQuery.toString(), nameList1, paramValueList1);
		if (!Misc.isNullList(namelist))
		{
			TMWIWMLogHelper.logDebug("Found a valid Print Requester.");
			printerQuery.delete(0, printerQuery.length());
			printerQuery.append("SELECT 1 AS COUNT FROM LPLrfPrtQueueDest pqd WHERE pqd.prtServType = :printServerType");
			String[] nameList2 =
			{ "printServerType" };
			Object[] paramValueList2 =
			{ "PackSlip" };
			List<?> printServerlist = directQuery(printerQuery.toString(), nameList2, paramValueList2);
			if (!Misc.isNullList(printServerlist))
			{
				TMWIWMLogHelper.logDebug("Found a valid Print Server Type.");
				StringBuffer query = new StringBuffer(
						"SELECT 1 AS COUNT FROM SysCode sc WHERE sc.recType = 'C' AND sc.codeType = 'W10' AND TRIM(SUBSTR(sc.miscFlags,1,10)) = :pw AND TRIM(SUBSTR(sc.miscFlags,11,1)) = :s");
				String[] nameList =
				{ "pw", "s" };
				Object[] paramValueList =
				{ putwall, section };
				List<?> list = directQuery(query.toString(), nameList, paramValueList);
				if (!Misc.isNullList(list))
				{
					TMWIWMLogHelper.logDebug("Found a valid record for 'change printer' request.");
					return TMWIWMConstants.VALID_PRINTER_DATA_FOUND;
				}
				else
				{
					return TMWIWMConstants.INVALID_PUTWALL_SECTION;
				}
			}
			else
				return TMWIWMConstants.PRINTING_SERVER_NOT_FOUND;
		}
		return TMWIWMConstants.PRINT_REQUESTER_NOT_FOUND;
	}

	/**
	 * Updates MISC_FLAGS with PRINT REQUESTER, which were matched by validatePutwallAndSection().
	 */
	@Override
	public boolean updatePrintRequester(String putWall, String section, String printRequester)
	{
		TMWIWMLogHelper.logEnter();
		StringBuffer misc_flags = new StringBuffer();

		// padding all the data, i.e. making putWall as 10 bytes and
		// printRequester as 19 bytes.
		putWall = pad(putWall, 10);
		printRequester = pad(printRequester, 19);

		misc_flags.append(putWall);
		misc_flags.append(section);
		misc_flags.append(printRequester);

		TMWIWMLogHelper.logDebug("The misc_flag generated is : [" + misc_flags + "], its length is = " + misc_flags.length());

		StringBuffer query = new StringBuffer(
				"UPDATE SysCode sc SET sc.miscFlags = :miscFlag WHERE sc.recType = 'C' AND sc.codeType = 'W10' AND SUBSTR(sc.miscFlags,1,10) = :pw AND TRIM(SUBSTR(sc.miscFlags,11,1)) = :s");
		String[] nameList =
		{ "miscFlag", "pw", "s" };
		Object[] paramValueList =
		{ misc_flags.toString(), putWall, section };
		int rowsUpdated = directUpdate(query.toString(), nameList, paramValueList);
		TMWIWMLogHelper.logDebug("Rows updated : " + rowsUpdated);
		return true;
	}

	/**
	 * This method pads the data as required.
	 * 
	 * @param data
	 * @param paddedLength
	 * @return
	 */
	private String pad(String data, int paddedLength)
	{
		TMWIWMLogHelper.logEnter("In pad() method..");
		int dataLength = data.length();
		StringBuffer d = new StringBuffer(data);
		for (; dataLength < paddedLength; dataLength++)
			d.append(" ");
		return d.toString();
	}

	/**
	 * Validates the user.
	 */
	@Override
	public boolean validateToteUser(String tote, String currentUser)
	{
		TMWIWMLogHelper.logEnter("In validateToteUser() method.");

		StringBuffer query = new StringBuffer("SELECT REF_FIELD_1 FROM LPN WHERE TC_LPN_ID = :tc_lpn_id");
		String[] nameList =
		{ "tc_lpn_id" };
		Object[] paramValueList =
		{ tote };
		List<?> list = directSQLQuery(query.toString(), nameList, paramValueList, null, null);
		if (!Misc.isNullList(list))
		{
			Object[] objArr = (Object[]) list.get(0);
			String user = (String) objArr[0];
			if (!Misc.isNullTrimmedString(user) && !currentUser.equalsIgnoreCase(user))
				return false;
			else
				return true;
		}
		return true;
	}

	/**
	 * This method takes in Printer Name as String, and checks if the same is present in configured Custom System Code. Custom System Code : W10
	 * 
	 * @param print
	 *            requester.
	 */
	@Override
	public short validatePrinterFromSysCode(String printRequester)
	{
		TMWIWMLogHelper.logEnter();

		StringBuffer query = new StringBuffer("SELECT 1 AS COUNT FROM SysCode sc WHERE sc.recType = 'C' AND sc.codeType = 'W10' AND TRIM(SUBSTR(sc.miscFlags,12,31)) = :pr");
		String[] nameList =
		{ "pr" };
		Object[] paramValueList =
		{ printRequester };
		List<?> list = directQuery(query.toString(), nameList, paramValueList);
		if (!Misc.isNullList(list))
		{
			TMWIWMLogHelper.logExit("Valid Printer Found.");
			return TMWIWMConstants.VALID_PRINTER;
		}
		else
		{
			TMWIWMLogHelper.logExit("Valid Printer NOT Found.");
			return TMWIWMConstants.INVALID_PRINTER;
		}
	}

}
