package org.kt3k.straw.plugin;

import org.kt3k.straw.StrawPlugin;
import org.kt3k.straw.StrawEvent;
import org.kt3k.straw.annotation.EventHandler;

public class BrowserBackPlugin extends StrawPlugin {

	@Override
	public String getName() {
		return "browserBack";
	}


	@EventHandler(EventType.BACK_PRESSED)
	public void onBackPressed(StrawEvent e) {

		// if can go back then go back
		if (this.webView.canGoBack()) {
			this.webView.goBack();
		}

	}

}
