/*
 * Copyright &#169; 2007 Manhattan Associates All rights reserved. Do not copy, modify or redistribute this file without specific written permission from Manhattan Associates.
 */

package com.manh.wmos.services.outbound.service;

import com.manh.wmos.services.outbound.data.RFPackFromToteData;

public interface TMWIIRFPackToteManagerService extends IRFPackToteManagerService
{

	// getoLpnSize
	public String getoLpnSize(String cartonNbr) throws com.manh.wm.core.util.WMException;

	// validate slot against sysCode
	public boolean validateSlotSysCode(String slot) throws com.manh.wm.core.util.WMException;

	// validate slot is already assigned to olpn
	public boolean validateSlotAlreadyAssingnedToOlpn(String slot) throws com.manh.wm.core.util.WMException;

	// validate PutWall For Olpn
	public boolean validatePutWallForOlpn(String slot) throws com.manh.wm.core.util.WMException;

	// update slot to olpn
	public boolean updateSlotOnLPN(String slot, String cartonNbr) throws com.manh.wm.core.util.WMException;

	// update LPN ref_field_1
	public boolean updateLpnRefField(String userId, String tote) throws com.manh.wm.core.util.WMException;

	// validate Lpn Ref_Field_1 is Null
	public String validateLpnRefFieldNull(String cartonNbr) throws com.manh.wm.core.util.WMException;

	// update packingSatus of C_PACK_WAVE_CHUTE_STAGING
	public boolean updatePckStatusOfChuteStage(String cartonNbr) throws com.manh.wm.core.util.WMException;
	

	/**
	 * Validates all three data before changing the Printer.
	 * @param putwall
	 * @param section
	 * @param printRequester
	 * @return
	 */
	public int validateData(String putwall, String section, String printRequester);

	/**
	 * Updates the Printer based on matched PutWall and Section.
	 * @param putWall
	 * @param section
	 * @param printRequester
	 * @return
	 */
	public boolean updatePrintRequester(String putWall, String section, String printRequester);

	/**
	 * Validates Tote against the current user.
	 * @param tote
	 * @param currentUser
	 * @return
	 */
	public boolean validateToteUser(String tote, String currentUser);

	/**
	 * Validates printer from System Code's misc fields for custom printer validation logic.
	 * @param printerName
	 * @return
	 */
	public short validatePrinterFromSysCode(String printerName);

}
