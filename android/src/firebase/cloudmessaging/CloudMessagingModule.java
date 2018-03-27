/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2017 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package firebase.cloudmessaging;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;
import org.appcelerator.titanium.util.TiConvert;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;
import java.util.HashMap;
import org.appcelerator.kroll.KrollFunction;
import java.util.Map;

@Kroll.module(name = "CloudMessaging", id = "firebase.cloudmessaging")
public class CloudMessagingModule extends KrollModule
{

	private static final String LCAT = "FirebaseCloudMessaging";
	private static CloudMessagingModule instance = null;

	public CloudMessagingModule()
	{
		super();
		instance = this;
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		// put module init code that needs to run when the application is created
	}

	// Methods
	@Kroll.method
	public void registerForPushNotifications()
	{
		FirebaseInstanceId.getInstance().getToken();
	}

	@Kroll.method
	public void subcribeToTopic(String topic)
	{
		FirebaseMessaging.getInstance().subscribeToTopic(topic);
		Log.d(LCAT, "subscribe to " + topic);
	}

	@Kroll.method
	public void unsubcribeFromTopic(String topic)
	{
		FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
		Log.d(LCAT, "unsubscribe from " + topic);
	}

	@Kroll.method
	public void appDidReceiveMessage(KrollDict opt)
	{
		// empty
	}

	@Kroll.method
	public void sendMessage(KrollDict obj)
	{
		FirebaseMessaging fm = FirebaseMessaging.getInstance();

		String fireTo = obj.getString("to");
		String fireMessageId = obj.getString("messageId");
		int ttl = TiConvert.toInt(obj.get("timeToLive"), 0);

		RemoteMessage.Builder rm = new RemoteMessage.Builder(fireTo);
		rm.setMessageId(fireMessageId);
		rm.setTtl(ttl);

		// add custom data
		Map<String, String> data = (HashMap) obj.get("data");
		for (Object o : data.keySet()) {
			rm.addData((String) o, data.get(o));
		}

		if (fireTo != "" && fireMessageId != "") {
			fm.send(rm.build());
		} else {
			Log.e(LCAT, "Please set 'to' and 'messageId'");
		}
	}

	public void onTokenRefresh(String token)
	{
		if (hasListeners("didRefreshRegistrationToken")) {
			KrollDict data = new KrollDict();
			data.put("fcmToken", token);
			fireEvent("didRefreshRegistrationToken", data);
		}
	}

	public void onMessageReceived(HashMap message)
	{
		if (hasListeners("didReceiveMessage")) {
			KrollDict data = new KrollDict();
			data.put("message", new KrollDict(message));
			fireEvent("didReceiveMessage", data);
		}
	}

	@Kroll.getProperty
	public String fcmToken()
	{
		return FirebaseInstanceId.getInstance().getToken();
	}

	@Kroll.setProperty
	public void apnsToken(String str)
	{
		// empty
	}

	public static CloudMessagingModule getInstance()
	{
		return instance;
	}
}
