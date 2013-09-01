package org.polepos.teams.orient;

public class OrientMemoryClient extends OrientTeam {

	public OrientMemoryClient(){
		super();
        int[] clientServerOptions = new int[] { OrientOptions.STORAGE_MEMORY };
        configure(clientServerOptions, null);
	}

}
