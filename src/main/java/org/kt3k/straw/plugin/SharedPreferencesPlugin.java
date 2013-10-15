package org.kt3k.straw.plugin;

import org.kt3k.straw.PluginAction;
import org.kt3k.straw.StrawDrink;
import org.kt3k.straw.StrawPlugin;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesPlugin extends StrawPlugin {

	@Override
	public String getName() {
		return "sharedPreferences";
	}


	private SharedPreferences getDefaultPrefs() {
		return PreferenceManager.getDefaultSharedPreferences(this.context);
	}


	public static class KeyParam {
		public String key;
	}


	public static class KeyValueParam {
		public String key;
		public String value;
	}


	public static class SharedPreferencesResult {

		public String value;

		public SharedPreferencesResult() {
		}

		public SharedPreferencesResult(String value) {
			this.value = value;
		}

	}


	@PluginAction
	public void dump(Object _, StrawDrink drink) {
		drink.success(new SharedPreferencesResult(this.getDefaultPrefs().getAll().toString()));
	}


	@PluginAction
	public void set(KeyValueParam param, StrawDrink drink) {
		Boolean result = this.getDefaultPrefs().edit().putString(param.key, param.value).commit();

		drink.success(new SharedPreferencesResult(result.toString()));
	}


	@PluginAction
	public void get(KeyValueParam param, StrawDrink drink) {
		String result = this.getDefaultPrefs().getString(param.key, param.value);

		drink.success(new SharedPreferencesResult(result));
	}


	@PluginAction
	public void remove(KeyParam param, StrawDrink drink) {
		Boolean result = this.getDefaultPrefs().edit().remove(param.key).commit();

		drink.success(new SharedPreferencesResult(result.toString()));
	}


	@PluginAction
	public void has(KeyParam param, StrawDrink drink) {
		Boolean result = this.getDefaultPrefs().contains(param.key);

		drink.success(new SharedPreferencesResult(result.toString()));
	}


	@PluginAction
	public void clear(Object _, StrawDrink drink) {
		Boolean result = this.getDefaultPrefs().edit().clear().commit();

		drink.success(new SharedPreferencesResult(result.toString()));
	}
}
