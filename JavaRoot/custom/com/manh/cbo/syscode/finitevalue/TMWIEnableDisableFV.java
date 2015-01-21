package com.manh.cbo.syscode.finitevalue;

import com.logistics.javalib.util.Misc;
import com.manh.cboframework.syscode.domain.CdSysCode;
import com.manh.cboframework.syscode.domain.SysCode;
import com.manh.cboframework.syscode.domain.WhseSysCode;
import com.manh.cboframework.syscode.finitevalue.SBSysCodeType;
import com.manh.cboframework.syscode.finitevalue.SysCodeTypeFiniteValue;
import com.manh.cboframework.syscode.finitevalue.SysCodeTypeFiniteValueManager;
import com.manh.cboframework.syscode.finitevalue.SysCodeTypeType;
import com.manh.wm.core.util.WMDebugLog;
import com.manh.wm.core.util.WMLogger;

@SuppressWarnings("serial")
public class TMWIEnableDisableFV extends SysCodeTypeFiniteValue {
	/** The MANAGER name. */
	public static String MANAGER_NAME = "TMWI_CUSTOM_MOD_ENABLE_DISABLE";

	public static SBSysCodeType C00;
	static public WMLogger log = new WMLogger(WMDebugLog.MHE_OUTBOUND_CATEGORY,
			TMWIEnableDisableFV.class);

	static {
		C00 = ((SBSysCodeType.SystemSysCodeTypeManager) SBSysCodeType.MANAGER)
				.loadCode(SysCodeTypeType.CUSTOM, "C00");
	}

	/** The MANAGER. */
	public static TMWIEnableDisableFiniteValueManager MANAGER = new TMWIEnableDisableFV.TMWIEnableDisableFiniteValueManager();

	protected TMWIEnableDisableFV(WhseSysCode sysCode) {
		super(sysCode);
	}

	protected TMWIEnableDisableFV(CdSysCode sysCode) {
		super(sysCode);
	}

	protected TMWIEnableDisableFV(SysCode sysCode) {
		super(sysCode);
	}

	/**
	 * Checks if Custom Extensions are enabled or not for the client(TMWI).
	 * 
	 * @return
	 */
	public static boolean isModEnabled() {
		boolean retValue = false;
		SysCodeTypeFiniteValue syscodeFin = null;
		String syscode = null;

		try {
			syscodeFin = (SysCodeTypeFiniteValue) (TMWIEnableDisableFV.MANAGER
					.findByCode("C00"));
			syscode = syscodeFin.getSysCode().getMiscFlags();
			syscode = syscode.trim();
			if (syscode != null && !syscode.isEmpty()) {
				if (syscode.substring(0, 1).equals("Y")) {
					retValue = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			retValue = false;
		}
		log.logHigh("Custom Mod Enabled for TMWI ? : " + retValue);
		return retValue;
	}

	public static String getMiscFlags() {
		SysCodeTypeFiniteValue syscodeFin = null;
		String miscFlags = Misc.EMPTY_STRING;

		try {
			syscodeFin = (SysCodeTypeFiniteValue) (TMWIEnableDisableFV.MANAGER
					.findByCode("C00"));
			miscFlags = syscodeFin.getSysCode().getMiscFlags();
		} catch (FiniteValueNotFoundException e) {
			e.printStackTrace();
		}

		return miscFlags;
	}

	/**
	 * The Class TMWIEnableDisableFiniteValueManager.
	 */
	public static class TMWIEnableDisableFiniteValueManager extends
			SysCodeTypeFiniteValueManager {

		protected TMWIEnableDisableFiniteValueManager() {
			super(MANAGER_NAME, MANAGER_NAME);
		}

		public SysCodeTypeFiniteValue createInstance(Object sysCode) {
			if (sysCode instanceof WhseSysCode) {
				return new TMWIEnableDisableFV((WhseSysCode) sysCode);
			} else if (sysCode instanceof CdSysCode) {
				return new TMWIEnableDisableFV((CdSysCode) sysCode);
			} else
				return new TMWIEnableDisableFV((SysCode) sysCode);
		}

		public SBSysCodeType getSBSysCodeType() {
			return C00;
		}
	}

}
