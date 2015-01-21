/**
 * 
 */
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

/**
 * @author ibiswas
 *
 */
@SuppressWarnings("serial")
public class TMWIEX10EnableDisableFV extends SysCodeTypeFiniteValue {

	//private static final long serialVersionUID = 5353512401603425550L;

	/** The MANAGER name. */
	public static String MANAGER_NAME = "TMWI_EX10_MOD_ENABLE_DISABLE";

	public static SBSysCodeType W10;
	static public WMLogger log = new WMLogger(
			WMDebugLog.INVENTORYMGMT_CATEGORY, TMWIEX10EnableDisableFV.class);

	static {
		W10 = ((SBSysCodeType.SystemSysCodeTypeManager) SBSysCodeType.MANAGER)
				.loadCode(SysCodeTypeType.CUSTOM, "W10");
	}

	/** The MANAGER. */
	public static TMWIEX10EnableDisableFVManager MANAGER = new TMWIEX10EnableDisableFV.TMWIEX10EnableDisableFVManager();

	protected TMWIEX10EnableDisableFV(WhseSysCode sysCode) {
		super(sysCode);
	}

	protected TMWIEX10EnableDisableFV(CdSysCode sysCode) {
		super(sysCode);
	}

	protected TMWIEX10EnableDisableFV(SysCode sysCode) {
		super(sysCode);
	}

	/**
	 * This method checks if the Extension EX10 is enabled or not for TMWI.
	 * @return
	 */
	public static boolean isModEnabled() {
		boolean retValue = false;
		SysCodeTypeFiniteValue syscodeFin = null;
		String syscode = null;

		try {
			syscodeFin = (SysCodeTypeFiniteValue) (TMWIEX10EnableDisableFV.MANAGER.findByCode("W10"));
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
		log.logHigh("TMWI EX10 Mod Enabled ? : " + retValue);
		return retValue;
	}

	public static String getMiscFlags() {
		SysCodeTypeFiniteValue syscodeFin = null;
		String miscFlags = Misc.EMPTY_STRING;

		try {
			syscodeFin = (SysCodeTypeFiniteValue) (TMWIEX10EnableDisableFV.MANAGER
					.findByCode("W10"));
			miscFlags = syscodeFin.getSysCode().getMiscFlags();
		} catch (FiniteValueNotFoundException e) {
			e.printStackTrace();
		}

		return miscFlags;
	}

	/**
	 * The Class TMWIEX10EnableDisableFVManager.
	 */
	public static class TMWIEX10EnableDisableFVManager extends
			SysCodeTypeFiniteValueManager {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2727551372505665033L;

		protected TMWIEX10EnableDisableFVManager() {
			super(MANAGER_NAME, MANAGER_NAME);
		}

		public SysCodeTypeFiniteValue createInstance(Object sysCode) {
			if (sysCode instanceof WhseSysCode) {
				return new TMWIEX10EnableDisableFV((WhseSysCode) sysCode);
			} else if (sysCode instanceof CdSysCode) {
				return new TMWIEX10EnableDisableFV((CdSysCode) sysCode);
			} else
				return new TMWIEX10EnableDisableFV((SysCode) sysCode);
		}

		public SBSysCodeType getSBSysCodeType() {
			return W10;
		}
	}

}
