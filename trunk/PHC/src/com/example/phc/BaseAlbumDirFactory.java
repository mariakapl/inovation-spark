package com.example.phc;

import java.io.File;

import android.os.Environment;

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
	public abstract String getAlbumStorageDirName(String albumName);
}

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}

	@Override
	public String getAlbumStorageDirName(String albumName) {
		Environment.getExternalStorageState();
		
				return Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName + "/";
	}
}