/* Copyright (C) 2004   orientbjects Inc.   http://www.orient.com */
package org.polepos.teams.orient;


public class OrientOptions {
    
    public static final int EMBEDDED_SERVER = 1;
    public static final int POOLED_CONNECTION = 2;
    public static final int STORAGE_REMOTE = 3;
    public static final int STORAGE_LOCAL = 4;
    public static final int STORAGE_PLOCAL = 5;
    public static final int STORAGE_MEMORY = 6;

	public static boolean containsOption(int[] options, int value) {
		if(options == null) {
			return false;
		}
		for (int opt : options) {
			if(opt == value) {
				return true;
			}
		}
		return false;
	}
}
