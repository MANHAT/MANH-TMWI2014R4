package com.manh.wmos.services.outbound.backingbean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.logistics.javalib.util.Misc;
import com.logistics.javalib.validation.AdditionalInfo;
import com.manh.cbo.domain.item.Item;
import com.manh.cbo.transactional.domain.lpn.LPN;
import com.manh.ils.wmservices.domain.WMPermissionCodes;
import com.manh.ils.wmservices.hibernate.ItemInfo;
import com.manh.ils.wmservices.utils.WMErrorMessage;
import com.manh.ils.wmservices.utils.WMMisc;
import com.manh.ils.wmservices.utils.WMRFConstants;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wm.core.util.ValidationBeanHelper;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMLogger;
import com.manh.wm.ui.component.itemAttribute.RFItemAttrUtil;
import com.manh.wmos.services.inventorymgmt.helper.InventoryMgmtServiceLocator;
import com.manh.wmos.services.outbound.PackFromToteConfiguration;
import com.manh.wmos.services.outbound.data.RFPackFromToteData;
import com.manh.wmos.services.outbound.data.TMWIRFPackFromToteData;
import com.manh.wmos.services.outbound.service.WMOutboundServiceLocator;
import com.manh.wmos.services.systemctrl.data.ITranData;

public class TMWIRFPackCartonEnterSkuBackingBean extends RFPackCartonEnterSkuBackingBean
{

	/** The Constant STATE_OBJ_PARM_NAME. */
	public static final String STATE_OBJ_PARM_NAME = "rfPackToteStateObj";
	private PackFromToteConfiguration configuration;
	private TMWIRFPackFromToteData _custStatedata;
	private String menuParm;

	public final static WMLogger logger = new WMLogger(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, TMWIRFPackCartonEnterSkuBackingBean.class);

	public TMWIRFPackCartonEnterSkuBackingBean()
	{
		super();

		logger.logEnter(" TMWIRFPackCartonEnterSkuBackingBean Constructor");
		try
		{

			_custStatedata = (TMWIRFPackFromToteData) (loadOrCreateStateDataObject(STATE_OBJ_PARM_NAME)).getDataObject();

			if (_custStatedata == null)
			{
				_custStatedata = (TMWIRFPackFromToteData) com.manh.wmos.services.outbound.OutboundObjectFactory.getInstance().getRFPackFromToteData();
			}

			configuration = PackFromToteConfiguration.getInstance(_custStatedata.getTranId());

			menuParm = configuration.getMenuParm();

			if (!Misc.isNullString(menuParm) && menuParm.length() >= 500 && menuParm.substring(499, 500).equalsIgnoreCase("Y"))
			{
				_custStatedata.setMenuParam500PutWall(true);

			}
			else
			{
				_custStatedata.setMenuParam500PutWall(false);
			}

			setDataObject(_custStatedata);
		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e);
			setDataObject(_custStatedata);
			addUnexpectedError(e);
		}
		logger.logExit(" TMWIRFPackCartonEnterSkuBackingBean Constructor");
	}

	public TMWIRFPackFromToteData get_custStatedata()
	{
		return _custStatedata;
	}

	public void set_custStatedata(TMWIRFPackFromToteData _custStatedata)
	{
		this._custStatedata = _custStatedata;
	}

	/**
	 * BASECOPYPASTE This method is called to validate the Item entered by the user during packing and set the completion flag Custom Changes : LPN_TOTAL_QTY validation & if serialNbrRequiredAction is
	 * not required call packing updates (validateAndAcceptAction).
	 * 
	 * @return String
	 */
	@Override
	public String enterAction()
	{

		logger.logEnter("TMWIRFPackCartonEnterSkuBackingBean: enterAction");
		long methodStartTime = com.manh.ils.util.monitor.CallTimeTracer.start("Entering com.manh.wmos.services.outbound.backingbean.TMWIRFPackCartonEnterSkuBackingBean", "enterAction");
		try
		{

			_custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject();

			// If Mod EXT 13 parameter 504 is disabled call base.
			boolean isEx13ModEnabled = isTranParmEnabled("504");
			// setting mod enabled flag in cust data obj.
			_custStatedata.setEXT13Enabled(isEx13ModEnabled);

			TMWIWMLogHelper.logDebug("Is TMWI EX 10 Enabled : " + _custStatedata.isMenuParam500PutWall());

			// check with randeep. change required for suppressing serial no.
			_custStatedata.setEx10Enabled(_custStatedata.isMenuParam500PutWall());

			setDataObject(_custStatedata);

			log.logEnter("TMWIRFPackCartonEnterSkuBackingBean::enterAction isEx13ModEnabled = " + isEx13ModEnabled);

			String result = super.enterAction();

			if (!isEx13ModEnabled && !_custStatedata.isMenuParam500PutWall())
			{
				return result;
			}

			// if isMenuPram500==true then TMWI-EX10 RF SCREEN-3.0
			if (_custStatedata.isMenuParam500PutWall() == true && result == "carton")
			{
				// return "TMWICarton";
			}

			_custStatedata = (TMWIRFPackFromToteData) WMOutboundServiceLocator.getOutboundServices().validateSKU(_custStatedata);

			if (_custStatedata.hasAnyErrors())
			{
				return ERROR;
			}

			if (_custStatedata != null && (!getHasHCEErrors()))
			{
				if (configuration.skuVerify().equals(WMRFConstants.SKU))
				{
					WMOutboundServiceLocator.getOutboundServices().setCompletionFlag(_custStatedata, WMRFConstants.COMPLETION_DO_NOT_CLOSE);

					String skuAttrParm = configuration.skuPrompt();

					if (skuAttrParm != null && !skuAttrParm.equals(WMRFConstants.ZERO))
					{
						if (!_custStatedata.hasAnyErrors())
						{
							ItemInfo itemInfo = WMMisc.convertRFSkuToItemInfo(_custStatedata.getRfSku());

							boolean defaultWithBlanks = false;
							if (skuAttrParm.equals(WMRFConstants.ONE))
							{
								defaultWithBlanks = true;
							}

							RFItemAttrUtil.forwardToItemAttr(itemInfo, false, true, true, true, false, defaultWithBlanks, _custStatedata.STATE_OBJ_PARM_NAME, _custStatedata,
									"#{rfPackCartonEnterSkuBackingBean.updateQty}", "#{wm.PackFromTote}");
							return null;
						}
					}

					BigDecimal packQty = _custStatedata.getNewQty();
					BigDecimal xref = new BigDecimal(_custStatedata.getItemQuantityValue());
					packQty = packQty.add(xref);
					_custStatedata.setChngdQty(xref);
					_custStatedata.setNewQty(packQty);

					_custStatedata = (TMWIRFPackFromToteData) WMOutboundServiceLocator.getOutboundServices().getCartonNbr(_custStatedata);
					setDataObject(_custStatedata);

					// EXT13 :: START
					String cartonNbr = _custStatedata.getCartonNbr();
					log.logDebug("TMWIRFPackCartonEnterSkuBackingBean::enterAction cartonNbr = " + cartonNbr);

					if (!Misc.isNullTrimmedString(cartonNbr))
					{
						LPN olpn = InventoryMgmtServiceLocator.getLPNService().findByTCLpnId(WMMisc.getUserWarehouseInfo(), cartonNbr);

						if (olpn != null && olpn.getTotalLpnQty() != 1 && isEx13ModEnabled)
						{
							// ""
							WMErrorMessage.ResourceInformation rawInfo = WMErrorMessage.CUST.getRawInformation("1301");
							_custStatedata.addHardCheckError(rawInfo.getId(), rawInfo.getMessage());
							return ERROR;

						}

					}
					// EXT13 :: END

					_custStatedata.setItem(new Item());
					_custStatedata.setBackAction(WMRFConstants.ENTER_SKU_BARCODE);
					_custStatedata.setNewCarton(cartonNbr);
					_custStatedata.setNewCartonNbr(cartonNbr);

					if (!serialNbrRequiredAction())
					{
						// EXT13 :: START
						log.logDebug("TMWIRFPackCartonEnterSkuBackingBean::enterAction serial nbr required is false, calling validateAndAcceptCarton");
						String returnStr = validateAndAcceptCarton();
						log.logDebug("TMWIRFPackCartonEnterSkuBackingBean::enterAction return string from validateAndAcceptCarton method returnStr = " + returnStr);
						return returnStr;
						// EXT13 :: END
					}
					else
						return null;

				}
				else if (configuration.skuVerify().equals((WMRFConstants.ENTR_QTY)))
				{
					WMOutboundServiceLocator.getOutboundServices().getCartonNbr(_custStatedata);
					setDataObject(_custStatedata);

					String skuAttrParm = configuration.skuPrompt();

					if (skuAttrParm != null && !skuAttrParm.equals(WMRFConstants.ZERO))
					{

						if (!_custStatedata.hasAnyErrors())
						{
							ItemInfo itemInfo = WMMisc.convertRFSkuToItemInfo(_custStatedata.getRfSku());

							boolean defaultWithBlanks = false;
							if (skuAttrParm.equals(WMRFConstants.ONE))
							{
								defaultWithBlanks = true;
							}

							RFItemAttrUtil.forwardToItemAttr(itemInfo, false, true, true, true, false, defaultWithBlanks, _custStatedata.STATE_OBJ_PARM_NAME, _custStatedata, ITEM_ATTR_SUCCESS_STRING,
									"#{wm.PackFromTote}");
							return null;
						}
					}
					else
					{
						_custStatedata.setBackAction(WMRFConstants.ENTER_SKU_BARCODE);
						return WMRFConstants.ENTR_QTY;
					}
				}

			}
			else
			{
				return ERROR;
			}

			return WMRFConstants.CARTON;

		}
		catch (Exception e)
		{
			WMDebugLog.DEBUG_LOG.logException(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, e, "Error in TMWIRFPackCartonEnterSkuBackingBean::enterAction: ");
			return ERROR;
		}
		finally
		{
			logger.logExit("TMWIRFPackCartonEnterSkuBackingBean: enterAction");
			com.manh.ils.util.monitor.CallTimeTracer.stop("com.manh.wmos.services.outbound.backingbean.RFPackCartonEnterSkuBackingBean", "enterAction", methodStartTime);
		}
	}

	/**
	 * validateAndAcceptCarton This method validates the carton and perform the packing updated. This method is called from 2 places (TMWIRFSerialNumberEntryBackingBean.enterAction &
	 * TMWIRFPackCartonEnterSkuBackingBean.enterAction ). Also suppress End Of Olpn info.
	 * 
	 * @return
	 */
	public String validateAndAcceptCarton()
	{

		try
		{

			log.logEnter("TMWIRFPackCartonEnterSkuBackingBean::validateAndAcceptCarton Entering");

			_custStatedata = (TMWIRFPackFromToteData) getIPSDetailData().getDataObject();

			WMOutboundServiceLocator.getOutboundServices().validateCarton(_custStatedata);

			if (_custStatedata != null && (!getHasHCEErrors()))
			{
				if (_custStatedata.isEndCurrentCarton())
				{
					_custStatedata.setEndCurrentCarton(false);
					boolean mode = configuration.newCarton();
					_custStatedata.setCartonMode(mode);
					WMOutboundServiceLocator.getOutboundServices().endCurrentCarton(_custStatedata);

					setDataObject(_custStatedata);
					String oLPNToPrint = _custStatedata.getCartonNbr();

					if (_custStatedata.hasAnyErrors())
					{
						return ERROR;
					}
					if (_custStatedata.getCartonMode())
					{
						WMOutboundServiceLocator.getOutboundServices().generateCartonNumber(_custStatedata);
						if (!_custStatedata.hasAnyErrors())
						{
							WMOutboundServiceLocator.getOutboundServices().splitCarton(_custStatedata);
						}
						if (!_custStatedata.hasAnyErrors())
						{
							generateTrackingNbrForNewoLPN(_custStatedata);
						}
					}

					if (!_custStatedata.hasAnyErrors())
					{
						_custStatedata.setChngdQty(new BigDecimal(0));
					}

					if (_custStatedata.hasAnyErrors())
					{
						return ERROR;
					}
					if (!_custStatedata.getCartonMode())
					{
						// CTRL-E is pressed from qty screen, go back to item
						// barcode screen
						_custStatedata.setItem(new Item());
						_custStatedata.setItemId(new Long(0));
						_custStatedata.setNewCartonNbr("");

						if (checkForPrintLabel(oLPNToPrint, WMRFConstants.NEW_CARTON))
						{
							if (_custStatedata.getManifestFlag())
							{
								return ERROR;
							}
							return null;
						}
						return WMRFConstants.NEW_CARTON;
					}

					if (configuration.skuVerify().equals(WMRFConstants.SCAN_LABEL))
					{
						_custStatedata.setItem(new Item());
						_custStatedata.setItemId(new Long(0));
						_custStatedata.setNewQty(new BigDecimal(0));
						_custStatedata.setNewCartonNbr("");
						if (checkForPrintLabel(oLPNToPrint, WMRFConstants.TOTE))
						{
							if (_custStatedata.getManifestFlag())
							{
								return ERROR;
							}
							return null;
						}
						return WMRFConstants.TOTE;
					}
					else
					{
						_custStatedata.setNewQty(new BigDecimal(0));
						_custStatedata.setItem(new Item());
						_custStatedata.setItemId(new Long(0));
						_custStatedata.setNewCartonNbr("");
						if (checkForPrintLabel(oLPNToPrint, WMRFConstants.SKU))
						{
							if (_custStatedata.getManifestFlag())
							{
								return ERROR;
							}
							return null;
						}
						return WMRFConstants.SKU; // conditions to be added for
													// other menu
						// parms
					}
				}
				if (configuration.skuVerify().equals(WMRFConstants.SKU))
				{
					_custStatedata.setQty(_custStatedata.getQty().subtract(_custStatedata.getChngdQty()));
					if (_custStatedata.getQty().doubleValue() <= 0)
					{
						_custStatedata = (TMWIRFPackFromToteData) WMOutboundServiceLocator.getOutboundServices().setCompletionFlag(_custStatedata, WMRFConstants.COMPLETION_CLOSE);
					}
					else
					{
						_custStatedata = (TMWIRFPackFromToteData) WMOutboundServiceLocator.getOutboundServices().setCompletionFlag(_custStatedata, WMRFConstants.COMPLETION_DO_NOT_CLOSE);
					}

					if (_custStatedata.hasAnyErrors())
					{
						return ERROR;
					}
				}

				_custStatedata = (TMWIRFPackFromToteData) WMOutboundServiceLocator.getOutboundServices().completeDetail(_custStatedata);

				if (!_custStatedata.hasAnyErrors())
				{

					String oLPNToPrint = _custStatedata.getCartonNbr();

					String retVal = WMRFConstants.TOTE;
					if ((_custStatedata.isTaskMode() && _custStatedata.isToteCompleted()) || configuration.skuVerify().equals(WMRFConstants.SCAN_LABEL))
					{
						preBackAction();
						_custStatedata.setTote(WMRFConstants.EMPTY_STRING);
						_custStatedata.getStateStack().clear();
					}
					else if (_custStatedata.getQty() != null && _custStatedata.getQty().doubleValue() > 0)
					{
						_custStatedata.setStateStack(new Stack());
						// _custStatedata.setBackAction(WMRFConstants.ACCEPT_TOTE);
						_custStatedata.setNewCartonNbr(Misc.EMPTY_STRING);
						_custStatedata.setItem(new Item());
						setDataObject(_custStatedata);
						retVal = WMRFConstants.SKU;
					}

					// Custom MOD 13 changes starts here, if 502 parm is enabled
					// then suppress the End Of OlPN Info
					boolean isTranParam502Enabled = this.isTranParmEnabled("502");
					log.logDebug("TMWIRFPackCartonEnterSkuBackingBean::validateAndAcceptCarton retVal = " + retVal + " isTranParam502Enabled = " + isTranParam502Enabled + " hasAdditionalInfo = "
							+ _custStatedata.hasAdditionalInfo());
					if (isTranParam502Enabled && _custStatedata.hasAdditionalInfo())
					{
						List<AdditionalInfo> additionalInfos = _custStatedata.getAdditionalInfo();

						for (AdditionalInfo additionalInfo : additionalInfos)
						{

							if (additionalInfo != null && additionalInfo.getErrorCode() == 112301042)
							{

								// This method will remove 1042 info from the
								// data object.
								this.removeEndOfOlpnError(_custStatedata);
								_custStatedata.setEndOfOlpnInfoSupressed(true);
								break;
							}
							else
							{
								_custStatedata.setEndOfOlpnInfoSupressed(false);
							}

						}

					}
					else
					{
						_custStatedata.setEndOfOlpnInfoSupressed(false);
					}

					// if End Of Olpn info is supressed and return string in not
					// TOTE, then calling base additionalInfoAction for
					// performing printing.
					if (_custStatedata.isEndOfOlpnInfoSupressed() && !retVal.equalsIgnoreCase(WMRFConstants.TOTE))
					{
						return super.additionalInfoAction();
					}
					else if (_custStatedata.isEndOfOlpnInfoSupressed() && retVal.equalsIgnoreCase(WMRFConstants.TOTE))
					{
						preBackAction();
						_custStatedata.setTote(WMRFConstants.EMPTY_STRING);
						_custStatedata.getStateStack().clear();
						return this.additionalInfoAction();
					}

					// Custom MOD 13 changes ends here

					return retVal;
				}
				else
				{
					return ERROR;
				}
			}

			else
			{
				return ERROR;
			}

		}
		catch (Exception e)
		{

			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Error in RFAcceptCartonBackingBean::enterAction: " + e);
			return ERROR;
		}
		finally
		{
			com.manh.ils.util.monitor.CallTimeTracer.stop("com.manh.wmos.services.outbound.backingbean.RFAcceptCartonBackingBean", "enterAction");
		}
	}

	/**
	 * no custom change Generates the Tracking Number for Newly created oLPN.
	 * 
	 * @param RFPackFromToteData
	 *            the data
	 */
	protected void generateTrackingNbrForNewoLPN(RFPackFromToteData data)
	{

		String tcLPNId = data.getCartonNbr();

		try
		{

			getTEService().assignTrackingNumber(WMMisc.getUserWarehouseInfo(WMPermissionCodes.RF_PACK_LPN_FROM_TOTE), tcLPNId);

		}
		catch (Exception ex)
		{
			WMDebugLog.DEBUG_LOG.logDebug(WMDebugLog.OUTBOUND_SERVICES_CATEGORY, "Tracking Nbr generation failed for LPN# " + tcLPNId);
		}
	}

	/*
	 * This method removes 1042 additional info from the data object and retains other additional info if exists.
	 */
	protected void removeEndOfOlpnError(RFPackFromToteData data)
	{
		log.logEnter("TMWIRFPackCartonEnterSkuBackingBean :: removeEndOfOlpnError Entering");

		boolean endOfoLPNExists = false;
		log.logDebug("AdditionalInfo list = " + data.getAdditionalInfo().toString());

		if (data.getAdditionalInfo().size() > 0)
		{
			// TODO
			List<AdditionalInfo> addInfo = new ArrayList<AdditionalInfo>();
			for (Iterator iterator = data.getAdditionalInfo().iterator(); iterator.hasNext();)
			{
				AdditionalInfo error = (AdditionalInfo) iterator.next();
				log.logDebug("ErrorCode : " + error.getErrorCode());

				if (error.getErrorCode() == 112301042)
				{
					endOfoLPNExists = true;
					continue;
				}
				else
					addInfo.add(error);
			}

			if (!Misc.isNullList(addInfo))
			{
				data.clearAdditionalInfo();
				data.setAdditionalInfo(addInfo);
			}
			else if (endOfoLPNExists && Misc.isNullList(addInfo))
			{
				data.clearAdditionalInfo();
			}
		}

		log.logExit("TMWIRFPackCartonEnterSkuBackingBean :: removeEndOfOlpnError Exiting");

	}

	/*
	 * BASECOPYPASTE Custom change : passing TOTE as parameter in checkForPrintLabel. This method is called only for the last item in the TOTE.
	 */
	@Override
	public String additionalInfoAction()
	{
		log.logEnter("TMWIRFPackCartonEnterSkuBackingBean :: additionalInfoAction Entering ");

		if (!_custStatedata.isEXT13Enabled() || !_custStatedata.isEx10Enabled())
		{
			log.logDebug("TMWIRFPackCartonEnterSkuBackingBean :: additionalInfoAction MOD is disabled calling base additional Info Action. ");
			return super.additionalInfoAction();
		}

		RFPackFromToteData _custStatedata = (RFPackFromToteData) getIPSDetailData().getDataObject();

		ValidationBeanHelper.processErrors(_custStatedata, true, getErrorLogContext(WMRFConstants.OUTBOUND_OBJ_TYPE), WMMisc.getHttpServletRequest());
		_custStatedata.clearAllErrors();

		String oLPNToPrint = (Misc.isNullTrimmedString(_custStatedata.getPreviousCartonNbr())) ? _custStatedata.getCartonNbr() : _custStatedata.getPreviousCartonNbr();

		log.logDebug("TMWIRFPackCartonEnterSkuBackingBean :: additionalInfoAction oLPNToPrint= " + oLPNToPrint);

		if (checkForPrintLabel(oLPNToPrint, WMRFConstants.TOTE))
		{
			if (_custStatedata.getManifestFlag())
			{
				return ERROR;
			}
			return null;
		}

		log.logDebug(" calling base additionalInfoAction ");

		String retString = super.additionalInfoAction();

		log.logExit("additionalInfoAction return value " + retString);

		return retString;
	}

	/**
	 * This method reads the configuration and checks the PARM is enabled or not.
	 * 
	 * @param tranParmNbr
	 *            - the transaction parameter value.
	 */
	private boolean isTranParmEnabled(String tranParmNbr)
	{
		log.logEnter(" isTranParmEnabled tranParmNbr = " + tranParmNbr);

		boolean retVal = false;

		ITranData tranData = (ITranData) configuration.getTranMap().get(tranParmNbr);
		if (tranData != null && tranData.getMenuParm() != null && tranData.getMenuParm().equalsIgnoreCase("Y"))
		{
			retVal = true;
		}

		log.logExit(" isTranParmEnabled tranParmNbr = " + tranParmNbr + " retVal = " + retVal);
		return retVal;
	}

}
