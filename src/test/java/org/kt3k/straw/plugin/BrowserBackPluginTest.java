package org.kt3k.straw.plugin;

import org.junit.Test;
import org.kt3k.straw.StrawEvent;
import org.kt3k.straw.StrawPlugin;

import static org.mockito.Mockito.*;

import android.app.Activity;
import android.webkit.WebView;

import static org.junit.Assert.*;

public class BrowserBackPluginTest {

	@Test
	public void testConstructor() {
		assertNotNull(new BrowserBackPlugin());
	}


	@Test
	public void testGetName() {
		StrawPlugin plugin = new BrowserBackPlugin();

		assertEquals("browserBack", plugin.getName());
	}


	@Test
	public void testOnBackPressedWhenCanGoBack() {
		BrowserBackPlugin plugin = new BrowserBackPlugin();

		// mock up activity
		Activity activity = mock(Activity.class);

		plugin.setContext(activity);

		WebView webView = mock(WebView.class);

		when(webView.canGoBack()).thenReturn(true);

		plugin.setWebView(webView);

		plugin.onBackPressed(new StrawEvent(StrawPlugin.EventType.BACK_PRESSED));

		// verify browser back
		verify(webView).goBack();

		verify(activity, never()).finish();
	}


	@Test
	public void testOnBackPressedWhenCannotGoBack() {
		BrowserBackPlugin plugin = new BrowserBackPlugin();

		// mock up activity
		Activity activity = mock(Activity.class);

		plugin.setContext(activity);

		WebView webView = mock(WebView.class);

		when(webView.canGoBack()).thenReturn(false);

		plugin.setWebView(webView);

		plugin.onBackPressed(new StrawEvent(StrawPlugin.EventType.BACK_PRESSED));

		verify(webView, never()).goBack();

		// verify activity finish
		verify(activity).finish();
	}

}
