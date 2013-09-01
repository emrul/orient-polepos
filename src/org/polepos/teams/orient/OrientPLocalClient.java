package org.polepos.teams.orient;

public class OrientPLocalClient extends OrientTeam {

	public OrientPLocalClient(){
		super();
        int[] clientServerOptions = new int[] { OrientOptions.STORAGE_PLOCAL };
        configure(clientServerOptions, null);
	}

}
