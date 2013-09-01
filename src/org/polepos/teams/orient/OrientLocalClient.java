package org.polepos.teams.orient;

public class OrientLocalClient extends OrientTeam {

	public OrientLocalClient(){
		super();
        int[] clientServerOptions = new int[] { OrientOptions.STORAGE_LOCAL };
        configure(clientServerOptions, null);
	}

}
