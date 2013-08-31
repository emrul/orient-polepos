/* 
This file is part of the PolePosition database benchmark
http://www.polepos.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA  02111-1307, USA. */

package org.polepos.teams.orient;

import com.orientechnologies.orient.client.remote.OEngineRemote;
import com.orientechnologies.orient.core.OConstants;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.engine.local.OEngineLocal;
import com.orientechnologies.orient.core.engine.local.OEngineLocalPaginated;
import com.orientechnologies.orient.core.engine.memory.OEngineMemory;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.server.OServer;
import org.polepos.framework.Car;
import org.polepos.framework.ConfigurationSetting;
import org.polepos.framework.Team;

import java.io.IOException;

public class OrientCar extends Car {
	
	private transient OServer _server;

    private OObjectDatabasePool pool;

	private transient OObjectDatabaseTx _db;
    public static final int SERVER_PORT = 4488;
    
    public static final String SERVER_HOST = "localhost";
    
    public static final String SERVER_USER = "orient";
    
    public static final String SERVER_PASSWORD = "orient";
    
    public static final String FOLDER;
    
    static {
        FOLDER = OrientTeam.class.getResource("/").getPath() + "data/orient";
    }

    public static final String ORIENT_FILE = "dbbench.orient";

	public static final String PATH = FOLDER + "/" + ORIENT_FILE;

	private String name;
	
	private int[] _options;  
	
	private ConfigurationSetting[] _configurations;
	

	public OrientCar(Team team, int[] options, ConfigurationSetting[] configurations) {
		super(team, "0xFFCA07");
		_options = options;
		_configurations = configurations;
		name = OConstants.getVersion();
	}
	
	public int [] options() {
		return _options;
	}

	@Override
	public String name() {
		return name;
	}
    
    /**
     * Open database in the configured mode.
     */
    public OObjectDatabaseTx openDb(Configuration configuration)
    {
    	//configure(serverConfiguration);
    	//configure(objectContainerConfiguration);
    	/*
        if (isEmbedded()) {
            try {
                _server = OServerMain.create().startup(configuration.getEmbeddedConfig());
                _server.activate();
            } catch (Exception e) {

            }
		}
		*/
        String url = configuration.getUrl(getEngine());
        if (url.startsWith(OEngineMemory.NAME))
          OGlobalConfiguration.STORAGE_KEEP_OPEN.setValue(true);

        /*
        try {
            OServerAdmin _admin = (new OServerAdmin(url)).connect(configuration.getUsername(), configuration.getPassword());
            _admin.createDatabase(configuration.getDatabase(), "document", getEngine());
        }
        catch (Exception e) {

        }
        */

        if ( pool == null ) {
            pool = new OObjectDatabasePool();
            pool.setup(1,100);
        }
        OObjectDatabaseTx db;
        if ( usePooledConnection() ) {
            db = pool.acquire(url, configuration.getUsername(), configuration.getPassword());
        }
        else {
            db = new OObjectDatabaseTx(url);
        }
        /*
        if (!_db.exists() ) {
            db = db.create();
        }
        */
        if ( db.isClosed())
            db = db.open(configuration.getUsername(), configuration.getPassword());

        return db;
	}

	private String getEngine() {
        if (OrientOptions.containsOption(_options, OrientOptions.STORAGE_REMOTE) ) {
            return OEngineRemote.NAME;
        }
        if (OrientOptions.containsOption(_options, OrientOptions.STORAGE_LOCAL) ) {
            return OEngineLocal.NAME;
        }
        if (OrientOptions.containsOption(_options, OrientOptions.STORAGE_PLOCAL) ) {
            return OEngineLocalPaginated.NAME;
        }
		return OEngineMemory.NAME;
	}

    public boolean usePooledConnection() {
   		return OrientOptions.containsOption(_options, OrientOptions.POOLED_CONNECTION);
   	}

	private boolean isEmbedded() {
		return OrientOptions.containsOption(_options, OrientOptions.EMBEDDED_SERVER);
	}
	
    public void configure(Configuration config) {

    	if(_configurations != null){
    		
            ClassLoader loader=this.getClass().getClassLoader();

    		for(ConfigurationSetting setting : _configurations){
    			try{
    				String settingsClassName = setting.getClass().getName();
					Class<?> loadedClass = loader.loadClass(settingsClassName);
					ConfigurationSetting newInstance = (ConfigurationSetting) loadedClass.newInstance();
    				newInstance.apply(config);
    			} catch ( Throwable t){
    				// Hint: ConfigurationSetting should be in the com.orient namespace,
    				//       so it gets loaded by the version ClassLoader.  
                    System.err.println("orient option not available in this version");
                    
                    t.printStackTrace();
    			}
    		}
    	}

    }

    public void deleteDatabaseFile() throws IOException {
    	stopServer();
    }    

	public final String databaseFile(){
        return PATH;
    }

    public void stopServer() {
        close(_db);
        if (_server != null ) {
            _server.shutdown();
        }
        if ( pool != null ) {
            pool.close();
        }
    }

	public void close(OObjectDatabaseTx db) {
		if(db != null && !db.isClosed()) {
            db.close();

		}
		
		if(db == _db){
			// set the state to null here, otherwise we open a session
			// on top of it in the next open call
			_db = null;
		}

	}
	
}
