package org.kt3k.straw.plugin;

import org.kt3k.straw.StrawDrink;
import org.kt3k.straw.StrawPlugin;
import org.kt3k.straw.annotation.PluginAction;

import android.content.Intent;
import android.net.Uri;

public class UriPlugin extends StrawPlugin {

	static final String NO_URI = "13001";
	static final String ACTIVITY_NOT_FOUND = "13002";

	@Override
	public String getName() {
		return "uri";
	}


	@PluginAction
	public void open(SingleStringParam param, StrawDrink drink) {
		String uri = param.value;

		if (uri == null) {
			drink.fail(NO_URI, "no uri given");

			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

		try {
			// try to open uri
			this.activity.startActivity(intent);

			drink.success();

		} catch (android.content.ActivityNotFoundException e) {
			drink.fail(ACTIVITY_NOT_FOUND, "activity not found for uri: " + uri);
			
		}
	}

}
