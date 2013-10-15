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
		this.plugin.clear(null, mock(StrawDrink.class));

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


	@Test
	public void testDumpEmpty() {
		this.plugin.dump(null, drink);

		verify(drink).success(captor.capture());

		assertEquals("{}", captor.getValue().value);
	}


	@Test
	public void testDump() {

		kvParam.key = "A";
		kvParam.value = "alice";

		plugin.set(kvParam, mock(StrawDrink.class));

		kvParam.key = "B";
		kvParam.value = "bob";

		plugin.set(kvParam, mock(StrawDrink.class));

		plugin.dump(null, drink);

		verify(drink).success(captor.capture());

		assertEquals("{A=alice, B=bob}", captor.getValue().value);

	}


	@Test
	public void testRemove() {

		kvParam.key = "A";
		kvParam.value = "alice";

		plugin.set(kvParam, mock(StrawDrink.class));

		kvParam.key = "B";
		kvParam.value = "bob";

		plugin.set(kvParam, mock(StrawDrink.class));

		kParam.key = "A";

		// remove A key
		plugin.remove(kParam, mock(StrawDrink.class));

		// dump and check values
		plugin.dump(null, drink);

		verify(drink).success(captor.capture());

		assertEquals("{B=bob}", captor.getValue().value);

	}


	@Test
	public void testRemoveNonexistentKey() {

		kParam.key = "A";

		plugin.remove(kParam, drink);

		// remove nonexistent key results success
		verify(drink).success(captor.capture());

		assertEquals("true", captor.getValue().value);

	}


	@Test
	public void testClear() {

		kvParam.key = "A";
		kvParam.value = "alice";

		plugin.set(kvParam, mock(StrawDrink.class));

		kvParam.key = "B";
		kvParam.value = "bob";

		plugin.set(kvParam, mock(StrawDrink.class));

		// clear things
		plugin.clear(null, mock(StrawDrink.class));

		plugin.dump(null, drink);

		verify(drink).success(captor.capture());

		assertEquals("{}", captor.getValue().value);

	}


	@Test
	public void testHas() {

		kvParam.key = "A";
		kvParam.value = "alice";

		plugin.set(kvParam, mock(StrawDrink.class));

		kParam.key = "A";
		StrawDrink drinkA = mock(StrawDrink.class);

		plugin.has(kParam, drinkA);

		verify(drinkA).success(captor.capture());

		assertEquals("true", captor.getValue().value);

		kParam.key = "B";
		StrawDrink drinkB = mock(StrawDrink.class);

		plugin.has(kParam, drinkB);

		verify(drinkB).success(captor.capture());

		assertEquals("false", captor.getValue().value);

	}

}
