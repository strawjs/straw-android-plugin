package org.kt3k.straw.plugin;

import java.util.Locale;

import org.kt3k.straw.PluginAction;
import org.kt3k.straw.StrawDrink;
import org.kt3k.straw.StrawPlugin;

public class LocalePlugin extends StrawPlugin {

	@Override
	public String getName() {
		return "locale";
	}

	@PluginAction
	public void getLanguage(Object _, StrawDrink drink) {
		drink.success(Locale.getDefault().getLanguage());
	}

}
