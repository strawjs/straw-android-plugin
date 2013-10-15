package org.kt3k.straw.plugin;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import org.kt3k.straw.StrawDrink;
import org.mockito.ArgumentCaptor;

import android.app.Activity;
import android.content.Context;

import static org.kt3k.straw.plugin.SharedPreferencesPlugin.*;

@RunWith(RobolectricTestRunner.class)
public class SharedPreferencePluginTest {

	SharedPreferencesPlugin plugin;
	KeyValueParam kvParam;
	KeyParam kParam;
	StrawDrink drink;
	ArgumentCaptor<SharedPreferencesResult> captor = ArgumentCaptor.forClass(SharedPreferencesResult.class);


	@Before
	public void setUp() {
		this.plugin = new SharedPreferencesPlugin();
		this.plugin.setContext(Robolectric.application.getApplicationContext());
		this.kvParam = new KeyValueParam();
		this.kParam = new KeyParam();
		this.drink = mock(StrawDrink.class);
	}


	@Test
	public void testGetName() {
		assertEquals("sharedPreferences", new SharedPreferencesPlugin().getName());
	}


	@Test
	public void testSet() {
		kvParam.key = "a";
		kvParam.value = "abc";

		this.plugin.set(kvParam, mock(StrawDrink.class));

		kvParam.value = "def";

		this.plugin.get(kvParam, drink);

		verify(drink).success(captor.capture());

		assertEquals("abc", captor.getValue().value);
	}

}
