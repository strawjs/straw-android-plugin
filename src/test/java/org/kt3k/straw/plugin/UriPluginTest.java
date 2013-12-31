package org.kt3k.straw.plugin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kt3k.straw.StrawDrink;
import org.robolectric.RobolectricTestRunner;

import android.app.Activity;
import android.content.Intent;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import static org.kt3k.straw.StrawPlugin.*;

@RunWith(RobolectricTestRunner.class)
public class UriPluginTest {
	
	@Test
	public void testGetName() {
		assertEquals("uri", new UriPlugin().getName());
	}
	
	@Test
	public void testOpenSuccess() {
		StrawDrink drink = mock(StrawDrink.class);
		Activity activity = mock(Activity.class);
		
		SingleStringParam param = new SingleStringParam();
		param.value = "market://details?id=a.b.c";

		UriPlugin plugin = new UriPlugin();
		
		plugin.setContext(activity);
		
		plugin.open(param, drink);
		
		verify(drink).success();
		verify(activity).startActivity(any(Intent.class));
	}
	
	
	@Test
	public void testOpenNull() {
		StrawDrink drink = mock(StrawDrink.class);
		Activity activity = mock(Activity.class);
		
		SingleStringParam param = new SingleStringParam();
		param.value = null;

		UriPlugin plugin = new UriPlugin();
		
		plugin.setContext(activity);
		
		plugin.open(param, drink);
		
		verify(drink).fail("13001", "no uri given");
		verify(activity, never()).startActivity(any(Intent.class));
	}
	
	@Test
	public void testOpenAndActivityNotFound() {
		StrawDrink drink = mock(StrawDrink.class);
		Activity activity = mock(Activity.class);
		
		// stubbing activity's startActivity
		doThrow(new android.content.ActivityNotFoundException()).when(activity).startActivity(any(Intent.class));
		
		SingleStringParam param = new SingleStringParam();
		param.value = "foo://bar/baz";

		UriPlugin plugin = new UriPlugin();
		
		plugin.setContext(activity);
		
		plugin.open(param, drink);
		
		verify(drink).fail("13002", "activity not found for uri: foo://bar/baz");
		verify(activity).startActivity(any(Intent.class));
	}

}
