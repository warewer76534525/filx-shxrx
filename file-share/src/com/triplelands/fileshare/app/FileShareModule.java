package com.triplelands.fileshare.app;

import com.google.inject.Binder;
import com.google.inject.Module;

public class FileShareModule implements Module {

	@Override
	public void configure(Binder binder) {
//		binder.bind(AppManager.class).in(Scopes.SINGLETON);
	}

}
