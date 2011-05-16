package com.triplelands.fileshare.app;

import java.util.List;

import com.google.inject.Module;

import roboguice.application.RoboApplication;

public class FileShareApp extends RoboApplication {
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new FileShareModule());
	}
}
