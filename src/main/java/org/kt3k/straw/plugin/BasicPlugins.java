package org.kt3k.straw.plugin;

public class BasicPlugins {

	public static String[] names = new String[]{
		"org.kt3k.straw.plugin.ActivityPlugin",
		"org.kt3k.straw.plugin.HttpPlugin",
		"org.kt3k.straw.plugin.LogPlugin",
		"org.kt3k.straw.plugin.OptionsMenuPlugin",
		"org.kt3k.straw.plugin.SharedPreferencesPlugin",
		"org.kt3k.straw.plugin.UIPlugin",
		"org.kt3k.straw.plugin.LocalePlugin",
		"org.kt3k.straw.plugin.BrowserPlugin",
		"org.kt3k.straw.plugin.BrowserBackPlugin",
		"org.kt3k.straw.plugin.UriPlugin",
	};

	public static Class<?>[] classes = new Class<?>[]{
		ActivityPlugin.class,
		HttpPlugin.class,
		LogPlugin.class,
		OptionsMenuPlugin.class,
		SharedPreferencesPlugin.class,
		UIPlugin.class,
		LocalePlugin.class,
		BrowserPlugin.class,
		BrowserBackPlugin.class,
		UriPlugin.class,
	};

}
