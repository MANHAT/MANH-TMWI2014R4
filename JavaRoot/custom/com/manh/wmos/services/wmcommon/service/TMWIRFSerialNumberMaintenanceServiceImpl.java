/*
 * Copyright &#169; 2005 Manhattan Associates All rights reserved. Do not copy, modify or redistribute this file without specific written permission from Manhattan Associates.
 */

package com.manh.wmos.services.wmcommon.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.logistics.javalib.util.Misc;
import com.logistics.javalib.validation.BaseError;
import com.logistics.javalib.validation.SoftCheckError;
import com.manh.ils.wmservices.corba.CorbaException;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.ils.wmservices.utils.WMRFConstants;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMLogger;
import com.manh.wm.data.RFIntHolder;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;
import com.manh.wmos.services.wmcommon.data.RFSerialNumber;
import com.manh.wmos.services.wmcommon.data.RFSerialNumberMaintenanceData;
import com.manh.wmos.services.wmcommon.helper.SerialNumberStatusEnums;

public class TMWIRFSerialNumberMaintenanceServiceImpl extends RFSerialNumberMaintenanceServiceImpl
{

	protected static WMLogger log = new WMLogger(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, TMWIRFSerialNumberMaintenanceServiceImpl.class);

	/*
	 * BASECOPYPASTE
	 */
	@Override
	public RFSerialNumberMaintenanceData addSerialNumber(RFSerialNumberMaintenanceData data) throws CorbaException
	{

		// If EX13 MOD is disabled call base and return.

		if (!this.isEXT13ModEnabled(data) && !this.isEX10Enabled(data))
		{
			log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber EXT 10/13 is disabled.. calling base. " + data);
			return super.addSerialNumber(data);
		}

		log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber Entering " + data);
		if (data.getMajorMinor() == WMRFConstants.MINOR_SRL_NBR_ENTRY)
		{
			// check if the scanned minor serial number is already present on
			// the list
			RFSerialNumber serialNumTemp = data.getSerialNumberScanList().get(data.getSerialNumberScanList().size() - 1);
			List minorSrlList = serialNumTemp.getMinorSerialNumber();

			for (int count = 0; count < minorSrlList.size(); count++)
			{
				if (data.getSerialNumber().equals(minorSrlList.get(count)))
				{
					WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.INVMGMT.getRawInformation("1462");
					data.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
					log.logDebug("Exiting com.manh.wmos.services.wmcommon.service.TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);
					return data;
				}
			}
		}
		short status = 1;
		if (data.getValidationMode() == WMRFConstants.MAINTAIN_SRL_NBRS || data.getValidationMode() == WMRFConstants.MAINTAIN_CONTAINER_SRL_NBRS)
		{
			RFIntHolder state = com.manh.wm.data.RFValueObjectFactory.getInstance().getRFIntHolder();
			state.setValue(SerialNumberStatusEnums.INIT_STATE_ADD.ordinal());

			if (!data.isAddRemove())
			{
				state.setValue(SerialNumberStatusEnums.INIT_STATE_REMOVE.ordinal());
			}
			else
			{
				if (data.getValidationMode() == WMRFConstants.MAINTAIN_CONTAINER_SRL_NBRS)
				{
					state.setValue(SerialNumberStatusEnums.INIT_STATE_ADD_MAINTAIN.ordinal());
				}
			}

			status = getCORBA().validateIfSrlNbrIsExpected(data, data.getSkuServerHandle(), data.getSerialNumber(), state, data.getMajorMinor());

			if (data.hasAnyErrors())
			{
				log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);

				if (data.hasSoftCheckErrors())
				{
					if (((BaseError) (data.getSoftCheckErrors().get(0))).getErrorCode() == 110801441)
					{
						data.clearAllErrors();
					}
					else
					{
						return data;
					}
				}
				else
				{
					return data;
				}
			}
		}
		if (status != 1 || data.getValidationMode() == WMRFConstants.ACCEPT_SRL_NBRS || (data.getValidationMode() == WMRFConstants.MAINTAIN_CONTAINER_SRL_NBRS && status != 1))
		{
			if (data.getCycCntUpdMode() == 1)
			{
				getCORBA().setCycCntUpdMode(data, data.getSkuServerHandle(), (int) data.getCycCntUpdMode());
			}

			RFIntHolder state = com.manh.wm.data.RFValueObjectFactory.getInstance().getRFIntHolder();
			state.setValue(SerialNumberStatusEnums.INIT_STATE_ADD.ordinal());

			if (!data.isAddRemove())
			{
				state.setValue(SerialNumberStatusEnums.INIT_STATE_REMOVE.ordinal());
			}
			else
			{
				if (data.getValidationMode() == WMRFConstants.MAINTAIN_CONTAINER_SRL_NBRS)
				{
					state.setValue(SerialNumberStatusEnums.INIT_STATE_ADD_MAINTAIN.ordinal());
				}
			}

			if (data.getState() > 0)
			{
				state.setValue(data.getState());
			}

			RFIntHolder customState = com.manh.wm.data.RFValueObjectFactory.getInstance().getRFIntHolder();
			int prevCustomState = data.getCustomState();
			if (data.getCustomState() > 0 && data.getCustomState() != 2)
			{
				data.setCustomState(data.getCustomState() + 1);
			}
			customState.setValue(data.getCustomState());

			status = getCORBA().validateSerialNumber(data, data.getSkuServerHandle(), data.getSerialNumber(), state, data.getMajorMinor(), data.getWoComponentScreen(), customState);

			if (prevCustomState == 6 && customState.getValue() == 0)
			{
				data.setCustomState(0);// Initialize to original value
			}
			else
			{
				data.setCustomState(customState.getValue());
			}

			data.setState(state.getValue());

			// EX13, EX10 custom changes starts here

			if (data.hasAnyErrors())
			{
				if (data.getSoftCheckErrors() != null && data.getSoftCheckErrors().size() > 0)
				{
					@SuppressWarnings("unchecked")
					List<SoftCheckError> softCheckErrors = data.getSoftCheckErrors();
					for (SoftCheckError softCheckError : softCheckErrors)
					{
						if (softCheckError != null && softCheckError.getErrorCode() == 110801441)
						{
							this.removeCreateSerialNbrError(data);
						}
						else
						{
							log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber data obj is having soft error other than 1441" + data);
							return data;
						}
					}
				}
				else
				{
					log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);
					return data;
				}
				// if the data object is having any hard check errors return.
				if (data.hasHardCheckErrors())
				{
					log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber data obj is having hard error " + data);
					return data;
				}
			}
		}
		addSerialNumberToListAndDisplay(data);

		// Check if minor serial number is required

		if (data.getMajorMinor() == WMRFConstants.MAJOR_SRL_NBR_ENTRY)
		{
			if (data.isMinorRequired() && data.getTurnOffMinorSrlNbr() != 1)
			{
				data.setNextScreen(WMRFConstants.ACCEPT_MINOR_SRL_NBR);
				data.setMajorMinor(WMRFConstants.MINOR_SRL_NBR_ENTRY);
				data.setSerialNumber("");// reset the field as this will
				// be used for minor srl also
				data.setState(0);
				data.setCustomState(0);
				log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);
				return data;
			}
			if (data.getSerialNumberScanList().size() < data.getNewQuantity().doubleValue())
			{
				data.setSerialNumber("");
				data.setState(0);
				data.setCustomState(0);
				log.logDebug("TMWIRFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);
				return data;
			}
			else
			{
				maintainSerialNumberList(data);// add the numbers into DB
			}
		}

		WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.WM_COMMON_CATEGORY, "Exiting com.manh.wmos.services.wmcommon.service.RFSerialNumberMaintenanceServiceImpl :: addSerialNumber" + data);
		return data;
	}

	/**
	 * This method removes 110801441 error from the data object and retains other error if any exists.
	 */
	private void removeCreateSerialNbrError(RFSerialNumberMaintenanceData data)
	{
		log("TMWIRFSerialNumberMaintenanceServiceImpl :: removeCreateSerialNbrError Enter");
		boolean createSerialNbrErrorExists = false;
		log("SoftErrorList = " + data.getSoftCheckErrors().toString());
		List<SoftCheckError> softCheckErrorList = new ArrayList<SoftCheckError>();
		for (Iterator iterator = data.getSoftCheckErrors().iterator(); iterator.hasNext();)
		{
			SoftCheckError error = (SoftCheckError) iterator.next();
			log("ErrorCode : " + error.getErrorCode());
			if (error.getErrorCode() == 110801441)
			{
				createSerialNbrErrorExists = true;
				continue;
			}
			else
				softCheckErrorList.add(error);
		}
		if (!Misc.isNullList(softCheckErrorList))
		{
			data.clearSoftCheckErrors();
			data.setSoftCheckErrors(softCheckErrorList);
		}
		else if (createSerialNbrErrorExists && Misc.isNullList(softCheckErrorList))
		{
			data.clearSoftCheckErrors();
		}

		log("TMWIRFSerialNumberMaintenanceServiceImpl :: removeCreateSerialNbrError Exit");
	}

	private boolean isEXT13ModEnabled(RFSerialNumberMaintenanceData _stateData)
	{

		String savedStateId = _stateData.getSaveStateId();
		log.logEnter("TMWIRFSerialNumberEntryBackingBean ::isEXT13ModEnabled Entering savedStateId = " + savedStateId);
		if (!Misc.isNullString(savedStateId) && savedStateId.equalsIgnoreCase("rfPackToteStateObj"))
		{

			if (_stateData.getSaveStateObj() instanceof TMWIRFPackFromToteData)
			{
				TMWIRFPackFromToteData fromToteData = (TMWIRFPackFromToteData) _stateData.getSaveStateObj();
				if (fromToteData.isEXT13Enabled())
				{
					log.logEnter("TMWIRFSerialNumberEntryBackingBean ::isEXT13ModEnabled Exiting MOD is enabled");
					return true;
				}

			}

		}
		log.logEnter("TMWIRFSerialNumberEntryBackingBean ::isEXT13ModEnabled Exiting MOD is disabled");
		return false;
	}

	/**
	 * Checks from Data object if EX10 is enabled or not.
	 * 
	 * @param _stateData
	 * @return
	 */
	private boolean isEX10Enabled(RFSerialNumberMaintenanceData _stateData)
	{
		TMWIWMLogHelper.logEnter("In TMWIRFSerialNumberMaintenanceServiceImpl - isEX10Enabled()..");
		if (!Misc.isNullString(_stateData.getSaveStateId()) && _stateData.getSaveStateId().equalsIgnoreCase("rfPackToteStateObj"))
		{
			if (_stateData.getSaveStateObj() instanceof TMWIRFPackFromToteData)
			{
				TMWIRFPackFromToteData toteData = (TMWIRFPackFromToteData) _stateData.getSaveStateObj();
				if (toteData.isEx10Enabled())
				{
					TMWIWMLogHelper.logDebug("EX10 is Enabled.");
					return true;
				}
			}
		}
		TMWIWMLogHelper.logExit("EX10 not enabled.");
		return false;
	}

}
