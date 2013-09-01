package org.polepos.teams.orient;

import com.orientechnologies.orient.core.engine.local.OEngineLocal;
import com.orientechnologies.orient.core.engine.local.OEngineLocalPaginated;
import com.orientechnologies.orient.core.engine.memory.OEngineMemory;
import org.polepos.framework.PropertiesHandler;

public class Configuration {

    private static final String CONFIGURATION_FILE = "settings/orientdb.properties";
    private PropertiesHandler properties;

    public String getUsername(){
        return properties.get("orient.username","admin");
    }

    public String getPassword(){
        return properties.get("orient.password","admin");
    }


    public String getDatabase(){
        // One of remote, local, plocal, memory
        return properties.get("orient.databasename","polepos");
    }

    public String getUrl(String engine){
        // One of remote, local, plocal, memory
        String url;
        if ( engine == OEngineMemory.NAME) {
            url = "memory:"+getDatabase();
        }
        else if ( engine == OEngineLocal.NAME || engine == OEngineLocalPaginated.NAME ) {
            url = engine+":"+OrientCar.FOLDER+"/"+engine+"/"+getDatabase();
        }
        else {
            url = properties.get("orient.url."+engine,"");
            url = engine+":"+url+"/"+getDatabase();
        }
        return url;
    }

    public String getEmbeddedConfig() {
        return embeddedConfig;
    }


    public boolean getUseTxLog() {
        return properties.getBoolean("orient.useTxLog");
    }
    public int getMinPoolSize() {
        String sMinPool = properties.get("orient.minPool","1");
        return Integer.parseInt(sMinPool);
    }

    public int getMaxPoolSize() {
        String sMaxPool = properties.get("orient.maxPool","100");
        return Integer.parseInt(sMaxPool);
    }

    public int getMinNetowrkPoolSize() {
        String sMinPool = properties.get("orient.minNetworkPool","1");
        return Integer.parseInt(sMinPool);
    }

    public int getMaxNetworkPoolSize() {
        String sMaxPool = properties.get("orient.maxNetworkPool","1");
        return Integer.parseInt(sMaxPool);
    }

    String embeddedConfig;

    public Configuration() {
        properties = new PropertiesHandler(CONFIGURATION_FILE);

        embeddedConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
             + "<orient-server>"
             + "<network>"
             + "<protocols>"
             + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
             + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
             + "</protocols>"
             + "<listeners>"
             + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
             + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
             + "</listeners>"
             + "</network>"
             + "<users>"
             + "<user name=\""+getUsername()+"\" password=\""+getPassword()+"\" resources=\"*\"/>"
             + "</users>"
             + "<properties>"
             + "<entry name=\"orientdb.www.path\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/\"/>"
             + "<entry name=\"orientdb.config.file\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml\"/>"
             + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
             + "<entry name=\"log.console.level\" value=\"info\"/>" + "<entry name=\"log.file.level\" value=\"fine\"/>"
             + "</properties>" + "</orient-server>";
    }
}
