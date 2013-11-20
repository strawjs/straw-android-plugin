package org.kt3k.straw.plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kt3k.straw.StrawDrink;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocalePluginTest {

	@Test
	public void testGetName() {
		assertEquals("locale", new LocalePlugin().getName());
	}


	@Test
	public void testGetLanguage() {
		StrawDrink drink = mock(StrawDrink.class);

		String language = Locale.getDefault().getLanguage();

		new LocalePlugin().getLanguage(new Object(), drink);

		verify(drink).success(language);
	}
}
