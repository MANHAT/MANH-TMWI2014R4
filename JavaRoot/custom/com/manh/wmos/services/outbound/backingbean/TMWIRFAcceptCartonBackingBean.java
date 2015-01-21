package com.manh.wmos.services.outbound.backingbean;

import org.springframework.context.ApplicationContext;

import com.logistics.javalib.util.Misc;
import com.manh.ils.db.AppContextUtil;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.ils.wmservices.utils.WMMisc;
import com.manh.ils.wmservices.utils.WMRFConstants;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMException;
import com.manh.wm.core.util.WMLogger;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;
import com.manh.wmos.services.outbound.service.TMWIIRFPackToteManagerService;

public class TMWIRFAcceptCartonBackingBean extends RFAcceptCartonBackingBean
{
	private TMWIRFPackFromToteData _custStatedata;
	public final static WMLogger logger = new WMLogger(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, TMWIRFAcceptCartonBackingBean.class);

	public TMWIRFAcceptCartonBackingBean()
	{
		super();
		_custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject();
	}

	public String getLpnSize()
	{

		logger.logEnter("Entering TMWIRFAcceptCartonBackingBean.getLpnSize()");
		long methodStartTime = com.manh.ils.util.monitor.CallTimeTracer.start("Entering com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "getLpnSize");
		String olpnSize = Misc.EMPTY_STRING;
		// String isLpnRefFieldNull = Misc.EMPTY_STRING;
		try
		{

			// validate the Lpn ref_field_1 is Null

			validateRefField1();

			olpnSize = getTMWIRFPackToteManager().getoLpnSize(_custStatedata.getCartonNbr());
			_custStatedata.setoLpnSize(olpnSize);

			logger.logExit("Exiting TMWIRFAcceptCartonBackingBean.getLpnSize()");
			return olpnSize;

		}
		catch (WMException e)
		{

			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e, "Error in TMWIRFAcceptCartonBackingBean::getLpnSize:");
			setDataObject(_custStatedata);
			addUnexpectedError(e);

		}
		finally
		{
			com.manh.ils.util.monitor.CallTimeTracer.stop("Exiting com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "getLpnSize", methodStartTime);
		}

		return olpnSize;

	}

	private void validateRefField1() throws WMException
	{

		logger.logEnter("Entering TMWIRFAcceptCartonBackingBean.validateRefField1()");
		String isLpnRefFieldNull = Misc.EMPTY_STRING;

		isLpnRefFieldNull = getTMWIRFPackToteManager().validateLpnRefFieldNull(_custStatedata.getCartonNbr());
		_custStatedata.setIsLpnRefField1ValueNull(isLpnRefFieldNull);

		if (isLpnRefFieldNull == null)
		{
			// column ref_field_1 doesn't contain value for the item scanned
			_custStatedata.setRefField1(false);

		}
		else
		{
			// column ref_field_1 contains value for the item scanned for the olpn

			_custStatedata.setRefField1(true);

		}

		setDataObject(_custStatedata);
		logger.logExit("Exiting TMWIRFAcceptCartonBackingBean.validateRefField1()");
	}

	// validate methods

	public String validateSlot()
	{

		logger.logEnter("Entering TMWIRFAcceptCartonBackingBean.isValidateSlot()");
		long methodStartTime = com.manh.ils.util.monitor.CallTimeTracer.start("Entering com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "isValidateSlot");
		boolean isSlotValid = false;

		try
		{
			// validating entered slot against Syscode
			isSlotValid = getTMWIRFPackToteManager().validateSlotSysCode(_custStatedata.getSlot());
			logger.logEnter("inside isValidateSlot :after validateSlotSysCode():isSlotValid=" + isSlotValid);

			if (isSlotValid == true)
			{
				// record exist on Syscode

				isSlotValid = getTMWIRFPackToteManager().validateSlotAlreadyAssingnedToOlpn(_custStatedata.getSlot());
				logger.logEnter("inside isValidateSlot():after validateSlotAlreadyAssingnedToOlpn:isSlotValid=" + isSlotValid);
				// if isSlotValid 1=false and true means proceed with next Query

				if (isSlotValid == false)
				{
					// Custom error 1004
					WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1004");
					_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
					return ERROR;

				}
				else
				{
					// validate slot already assigned to olpn
					isSlotValid = getTMWIRFPackToteManager().validatePutWallForOlpn(_custStatedata.getSlot());
					logger.logEnter("inside isValidateSlot():after validatePutWallForOlpn:isSlotValid=" + isSlotValid);

					if (isSlotValid == true)
					{
						// Proceed with LPN_REF_FIEL1 update
						isSlotValid = getTMWIRFPackToteManager().updateSlotOnLPN(_custStatedata.getSlot(), _custStatedata.getCartonNbr());
						logger.logEnter("inside isValidateSlot():after updateSlotOnLPN:isSlotValid=" + isSlotValid);
						if (isSlotValid == true)
						{
							isSlotValid = true;
						}
						// else don't do any anything

					}
					else
					{
						// custom error 1006
						WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1006");
						_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
						return ERROR;
					}
				}

			}
			else
			{
				// //custom error 1003
				WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1003");
				_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
				return ERROR;

			}

		}
		catch (WMException e)
		{

			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e, "Error in TMWIRFAcceptCartonBackingBean::isValidateSlot: ");
			setDataObject(_custStatedata);
			addUnexpectedError(e);
		}
		finally
		{
			com.manh.ils.util.monitor.CallTimeTracer.stop("Exiting com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "getLpnSize", methodStartTime);
		}
		logger.logExit("Exiting TMWIRFAcceptCartonBackingBean.isValidateSlot(): isSlotValid=" + isSlotValid);

		if (isSlotValid == true)
			return "SUCCESS";
		else
			return "FAILURE";

	}

	public String enterAction()
	{

		// check ismenuParam500PutWall is enabled or disabled
		if (_custStatedata.isMenuParam500PutWall() == false)
		{
			return super.enterAction();
		}
		else
		{
			logger.logEnter("Exiting TMWIRFAcceptCartonBackingBean.enterAction()");
			// custom code
			long methodStartTime = com.manh.ils.util.monitor.CallTimeTracer.start("Entering com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "enterAction");
			String result = Misc.EMPTY_STRING;
			String resultOfIsValidate = Misc.EMPTY_STRING;

			try
			{
				if (_custStatedata.getIsLpnRefField1ValueNull() == null)
				{
					resultOfIsValidate = validateSlot();
					logger.logEnter("result of TMWIRFAcceptCartonBackingBean:isValidateSlot():" + resultOfIsValidate);
				}
				else
				{
					if (_custStatedata.getSlot().equalsIgnoreCase(_custStatedata.getIsLpnRefField1ValueNull()))
					{
						resultOfIsValidate = SUCCESS;
					}
					else
					{
						WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1005");
						_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
						return ERROR;

					}
				}
				if (resultOfIsValidate.equalsIgnoreCase(SUCCESS))
				{
					_custStatedata.setNewCartonNbr(_custStatedata.getCartonNbr());
					result = super.enterAction();

					if ((result.equalsIgnoreCase(WMRFConstants.NEW_CARTON)) || result.equalsIgnoreCase(WMRFConstants.TOTE) || result.equalsIgnoreCase(WMRFConstants.SKU))
					{
						String userId = WMMisc.getUserWarehouseInfo().getUserId();

						// if the tote's lpn.REF_FIELD_1 is null then update with the User_userId
						getTMWIRFPackToteManager().updateLpnRefField(userId, _custStatedata.getTote());
						logger.logEnter("after TMWIRFAcceptCartonBackingBean:updateLpnRefFiled()");

						// updatePckStatusOfChuteStage with 10 if the C_PACK_WAVE_CHUTE_STAGING.PACKING_STATUS=0
						getTMWIRFPackToteManager().updatePckStatusOfChuteStage(_custStatedata.getCartonNbr());
						logger.logEnter("after TMWIRFAcceptCartonBackingBean:updatePckStatusOfChuteStage()");
					}

					return result;

				}
				else
				{
					return ERROR;
				}
			}
			catch (Exception e)
			{
				WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Error in TMWIRFAcceptCartonBackingBean::enterAction: " + e);
				setDataObject(_custStatedata);
				addUnexpectedError(e);
			}
			finally
			{
				com.manh.ils.util.monitor.CallTimeTracer.stop("Exiting com.manh.wmos.services.outbound.backingbean.TMWIRFAcceptCartonBackingBean", "enterAction", methodStartTime);
			}

			return result;

		}

	}

	// Utility method to get the Custom Service Object.
	private TMWIIRFPackToteManagerService getTMWIRFPackToteManager()
	{
		// Reference to application context defined in beanRefContext.xml
		ApplicationContext ctx = AppContextUtil.getAppCtx("wm.outbound");
		TMWIIRFPackToteManagerService service = (TMWIIRFPackToteManagerService) ctx.getBean("RFPackToteManagerServiceImpl");

		return service;
	}
}
