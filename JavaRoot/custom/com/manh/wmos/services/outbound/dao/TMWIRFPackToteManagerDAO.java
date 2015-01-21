package com.manh.wmos.services.outbound.dao;

import com.manh.ils.db.RootDao;

public interface TMWIRFPackToteManagerDAO extends RootDao
{

	public String getoLpnSize(String cartonNbr) throws com.manh.wm.core.util.WMException;

	public boolean validateSlotSysCode(String slot) throws com.manh.wm.core.util.WMException;

	public boolean validateSlotAlreadyAssingnedToOlpn(String slot) throws com.manh.wm.core.util.WMException;

	public boolean validatePutWallForOlpn(String slot) throws com.manh.wm.core.util.WMException;

	public boolean updateSlotOnLPN(String slot, String cartonNbr) throws com.manh.wm.core.util.WMException;

	public boolean updateLpnRefField(String userId, String tote) throws com.manh.wm.core.util.WMException;

	public String validateLpnRefFieldNull(String cartonNbr) throws com.manh.wm.core.util.WMException;

	public boolean updatePckStatusOfChuteStage(String cartonNbr) throws com.manh.wm.core.util.WMException;

	public int validateData(String putwall, String section, String printRequester);

	public boolean updatePrintRequester(String putWall, String section, String printRequester);

	public boolean validateToteUser(String tote, String currentUser);

	public short validatePrinterFromSysCode(String printRequester);

}
