package com.appirio.muleapp.util;

import java.io.FileReader;
import java.io.IOException;

import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.box.sdk.IAccessTokenCache;
import com.box.sdk.InMemoryLRUAccessTokenCache;

public final class BoxJwtAuthenticationUtils {

	private BoxJwtAuthenticationUtils() {}

	private static int MAX_CACHE_ENTRIES = 100;
	
	/**
	 * box_access_config.json is a config file of box, and it should be under src/main/resources
	 * Please Refer to following URL to create Box Config file. 
	 * https://ja.developer.box.com/docs/setting-up-a-jwt-app
	 * 
	 * */
	private static String CONFIG_RESOURCE = "/box_access_config.json";

	/**
	 * Get AccessToken as Registered Client Application, which is defined in box_access_config.json file.
	 * The config file should be under /src/main/resources
	 * 
	 * @see https://ja.developer.box.com/docs/authenticate-with-jwt
	 * 
	 * @return AccessToken as Client
	 * @throws IOException
	 */
	public static String getAccessToken() throws IOException {
		String confPath= BoxJwtAuthenticationUtils.class.getResource(CONFIG_RESOURCE).getPath();
		BoxConfig boxConfig = BoxConfig.readFrom(new FileReader(confPath));
		IAccessTokenCache accessTokenCache = new InMemoryLRUAccessTokenCache(MAX_CACHE_ENTRIES);
		// Create new app enterprise connection object
		BoxDeveloperEditionAPIConnection client = BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(boxConfig,
				accessTokenCache);
		return client.getAccessToken();
	}

}
