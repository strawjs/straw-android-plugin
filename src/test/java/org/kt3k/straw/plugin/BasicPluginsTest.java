package org.kt3k.straw.plugin;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.kt3k.straw.Straw;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.robolectric.RobolectricTestRunner;

import android.webkit.WebView;

@RunWith(RobolectricTestRunner.class)
public class BasicPluginsTest {

	@Test
	public void testNames() {
		assertArrayEquals(new String[]{
			"org.kt3k.straw.plugin.ActivityPlugin",
			"org.kt3k.straw.plugin.HttpPlugin",
			"org.kt3k.straw.plugin.LogPlugin",
			"org.kt3k.straw.plugin.OptionsMenuPlugin",
			"org.kt3k.straw.plugin.SharedPreferencesPlugin",
			"org.kt3k.straw.plugin.UIPlugin",
			"org.kt3k.straw.plugin.LocalePlugin",
		}, BasicPlugins.names);
	}
	
	@Test
	public void testClasses() {
		assertArrayEquals(new Class[]{
			ActivityPlugin.class,
			HttpPlugin.class,
			LogPlugin.class,
			OptionsMenuPlugin.class,
			SharedPreferencesPlugin.class,
			UIPlugin.class,
			LocalePlugin.class,
		}, BasicPlugins.classes);
	}

	@Test
	public void testBasicPluginLoading() {
		Straw straw = Straw.insertInto(mock(WebView.class));
		straw.addPlugins(BasicPlugins.names);
	}

	@Test
	public void testConstructor() {
		assertNotNull(new BasicPlugins());
	}

}
