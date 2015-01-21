package com.manh.wm.core.util;

import com.logistics.javalib.util.Misc;
import com.manh.javalib.logging.LogLevel;

public class TMWIWMLogHelper {

	public static void logEnter() {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Entering " + classAndMethodName());
	}

	public static void logEnter(String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Entering " + classAndMethodName()+infoMsg);
	}
	
	/**
	 * Custom LOG ENTER functionality with Extension Name.
	 * @param extension - Pass your Extension or Mod Number.
	 * @param infoMsg - The message you want to log.
	 */
	public static void logEnter(String extension, String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI - "+extension+"] Entering " + classAndMethodName()+infoMsg);
	}
	
	/**
	 * Custom LOG EXIT functionality with Extension Name.
	 * @param extension - Pass your Extension or Mod Number.
	 * @param infoMsg - The message you want to log.
	 */
	public static void logExit(String extension, String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI - "+extension+"] Exiting " + classAndMethodName()+infoMsg);
	}
	
	public static void logExit(String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Exiting " + classAndMethodName()+infoMsg);
	}
	
	public static void logExit() {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Exiting " + classAndMethodName());
	}

	/**
	 * Custom LOG DEBUG functionality with Extension Name.
	 * @param extension - Pass your Extension or Mod Number.
	 * @param debugMsg - The message you want to log.
	 */
	public static void logDebug(String extension, String debugMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI - "+extension+"] Class: " +classAndMethodName() + debugMsg);
	}
	
	public static void logDebug(String debugMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logDebug(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Class: " +classAndMethodName() + debugMsg);
	}

	/**
	 * Custom LOG EXCEPTION functionality with Extension Name.
	 * @param extension - Pass your Extension or Mod Number.
	 * @param ex - The Exception.
	 * @param exceptionMsg - The message you want to log.
	 */
	public static void logException(String extension, Exception ex, String exceptionMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logException(TMWIWMDebugLog.TMWI_WM_CATEGORY, ex, "[TMWI - "+extension+"] Class: " +classAndMethodName() + exceptionMsg);
	}
	
	public static void logException(Exception ex, String exceptionMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logException(TMWIWMDebugLog.TMWI_WM_CATEGORY, ex, "[TMWI] Class: " +classAndMethodName() + exceptionMsg);
	}

	public static void logException(Exception ex) {
		TMWIWMDebugLog.DEBUG_LOG.logException(TMWIWMDebugLog.TMWI_WM_CATEGORY, ex, "[TMWI] Class: " +classAndMethodName().toString());
	}

	public static void logInfo(String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logInfo(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Class: " +classAndMethodName() + infoMsg);
	}
	
	/**
	 * Custom LOG ERROR functionality with Extension Name.
	 * @param extension - Pass your Extension or Mod Number.
	 * @param debugMsg - The message you want to log.
	 */
	public static void logError(String extension, String debugMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logError(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI - "+extension+"] Class: " +classAndMethodName() + debugMsg);
	}
	
	public static void logError(String debugMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logError(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Class: " +classAndMethodName() + debugMsg);
	}
	
	public static void logHigh(String infoMsg) {
		TMWIWMDebugLog.DEBUG_LOG.logHigh(TMWIWMDebugLog.TMWI_WM_CATEGORY, "[TMWI] Class: " +classAndMethodName() + infoMsg);
	}

	/**
	 * This method forms a StringBuffer literal with the class name and method
	 * name.
	 * 
	 * @return className.methodName:
	 */
	private static StringBuffer classAndMethodName() {
		StringBuffer sbFinalReturn = new StringBuffer(Misc.EMPTY_STRING);
		try {
			sbFinalReturn.append(Thread.currentThread().getStackTrace()[3].getFileName());
			sbFinalReturn.append(".");
			sbFinalReturn.append(Thread.currentThread().getStackTrace()[3].getLineNumber());
			sbFinalReturn.append(".");
			sbFinalReturn.append(Thread.currentThread().getStackTrace()[3].getMethodName());
			sbFinalReturn.append(": ");
		} catch (Exception e) {
			WMDebugLog.WM_COMMON_CATEGORY.logger().log(LogLevel.INFO,
					"Got exception while trying to prepending the class name and method name in the log");
		}
		return sbFinalReturn;
	}}
