package com.manh.wmos.util.cache;

import java.util.Iterator;
import java.util.List;

import com.logistics.javalib.util.Misc;
import com.manh.ils.db.cache.Cache;
import com.manh.ils.db.cache.provider.CacheProvider;
import com.manh.ils.wmservices.domain.WMPermissionCodes;
import com.manh.ils.wmservices.domain.pk.SessionUserWarehouseInfo;
import com.manh.ils.wmservices.domain.pk.UserWarehouseInfo;
import com.manh.wm.core.util.TMWIWMLogHelper;
import com.manh.wmos.services.inventorymgmt.helper.BreHelper;
import com.manh.wmos.services.systemctrl.data.ReloadCacheData;
import com.manh.wmos.services.systemctrl.helper.ReloadCacheHelper;
import com.manh.wmos.services.wmcommon.data.DevCacheApp;
import com.manh.wmos.services.wmcommon.data.DevCacheModule;
import com.manh.wmos.services.wmcommon.datahandler.DevCacheDataHandler;

public class TMWICacheUtilService implements ITMWICacheUtilService
{

	/**
	 * This method clears both Java and CPP cache for a particular user.
	 * 
	 * @param userId
	 */
	public String clearCache(String userId)
	{
		TMWIWMLogHelper.logEnter("In clearCache() method.. User is : "+userId);
		
		String retval = "Failed to clear";
		UserWarehouseInfo userWhseInfo = SessionUserWarehouseInfo.getUserWarehouseInfo(userId, WMPermissionCodes.ADMIN_WMS);

		/* The below code should clear all finite value cache */
		CacheProvider provider = CacheProvider.getInstance();
		provider.clearAllCache();

		DevCacheDataHandler handler = new DevCacheDataHandler();
		// Get the cache list from xml and clear all Java and CPP caches
		List<?> cacheAppList = handler.getCacheListFromXML();

		if (!Misc.isNullList(cacheAppList))
		{
			for (Iterator<?> appListIterator = cacheAppList.iterator(); appListIterator.hasNext();)
			{
				DevCacheApp devCacheApp = (DevCacheApp) appListIterator.next();
				if (devCacheApp != null)
				{
					// find the module
					List<?> appModuleList = devCacheApp.getModuleList();
					if (!Misc.isNullList(appModuleList))
					{
						for (Iterator<?> appModuleListItr = appModuleList.iterator(); appModuleListItr.hasNext();)
						{
							DevCacheModule devCacheModule = (DevCacheModule) appModuleListItr.next();
							if (devCacheModule != null)
							{
								TMWIWMLogHelper.logDebug("initiating Cache clearing process..");
								clearJavaCache(devCacheModule.getJavaCacheCode());
								clearCPPCache(devCacheModule.getCppCacheCode(), userWhseInfo);
								if (!Misc.isNullList(devCacheModule.getSubModuleList()))
								{
									// Clear the cache from subModules
									List<?> subModuleList = devCacheModule.getSubModuleList();
									for (Iterator<?> appSubModuleListItr = subModuleList.iterator(); appSubModuleListItr.hasNext();)
									{
										DevCacheModule subModule = (DevCacheModule) appSubModuleListItr.next();
										clearJavaCache(subModule.getJavaCacheCode());
										clearCPPCache(subModule.getCppCacheCode(), userWhseInfo);
									}
								}

							}
						}
					}

				}
			}
		}

		String[] cachenames = com.logistics.javalib.util.cache.Cache.getCacheInstances();
		for (int i = 0; i < cachenames.length; i++)
		{
			TMWIWMLogHelper.logDebug("cache instance name : "+cachenames[i]);
			if (!(cachenames[i].equalsIgnoreCase("uclMultipleLoginCache") || cachenames[i].equalsIgnoreCase("uclUserLoginCache")))
			{
				TMWIWMLogHelper.logDebug("**" + cachenames[i]);
				com.logistics.javalib.util.cache.Cache.clearCache(cachenames[i]);
			}
		}

		retval = "Cache Cleared for WM";
		TMWIWMLogHelper.logExit("Exiting clearCache() method.. The return value is : [" + retval + "]");
		return retval;
	}

	/**
	 * This method is used to clear Java cache.
	 */
	public static void clearJavaCache(String javaCacheCode)
	{
		TMWIWMLogHelper.logEnter("In clearJavaCache()..");
		try
		{
			if (!Misc.isNullTrimmedString(javaCacheCode))
			{
				Cache cache = CacheProvider.getInstance().getRegisteredCache(javaCacheCode);
				if (null != cache)
				{
					cache.clear();
					TMWIWMLogHelper.logDebug("DevCacheBackingBean: Cleared cache for " + javaCacheCode);
				}
				if (javaCacheCode.contains("finitevalue"))
				{
					try
					{
						com.logistics.javalib.util.FiniteManagerAdministration.getInstance().reInitializeFV(javaCacheCode);
					}
					catch (Exception e)
					{
						TMWIWMLogHelper.logDebug("Exception occured while clearing javaCacheCode = " + javaCacheCode);
						TMWIWMLogHelper.logException(e);
					}
				}
			}
		}
		catch (Exception e)
		{
			TMWIWMLogHelper.logDebug("Exception occured while clearing JAVA cache");
			TMWIWMLogHelper.logException(e);
		}
		TMWIWMLogHelper.logExit("From clearJavaCache()..");
	}

	/**
	 * This method is used to clear CPP cache.
	 */
	public static void clearCPPCache(String cppCacheCode, UserWarehouseInfo userWhseInfo)
	{
		TMWIWMLogHelper.logEnter("In clearCPPCache()..");
		try
		{
			if (!Misc.isNullTrimmedString(cppCacheCode))
			{
				TMWIWMLogHelper.logDebug("CPPCacheCode=" + cppCacheCode);

				String[] keys = cppCacheCode.split("/");
				if (keys.length == 2)
				{
					String msgType = keys[0];
					String msgText = keys[1];
					if (msgType.equals("menu"))
					{
						com.manh.ils.ui.component.bindkey.KeyMapManager.reload();
					}
					if (msgType.equals("bre"))
					{
						if ("*".equals(msgText))
							BreHelper.clearEntityAttributeData();
						else
							BreHelper.reloadEntityAttributeData(msgText);
					}
					else
					{
						ReloadCacheData reloadCache = new ReloadCacheData(userWhseInfo, msgType, msgText);
						ReloadCacheHelper.reloadCacheMessage(reloadCache);
					}
				}

			}
		}
		catch (Exception e)
		{
			TMWIWMLogHelper.logDebug("Exception occured while clearing CPP cache");
			TMWIWMLogHelper.logException(e);
		}
		TMWIWMLogHelper.logEnter("From clearCPPCache()..");
	}

}
