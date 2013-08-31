
package org.polepos.teams.orient;

import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import org.polepos.circuits.arraylists.ArrayListsDriver;
import org.polepos.circuits.arraylists.ListHolder;

public class ArrayListsOrient extends OrientDriver implements ArrayListsDriver {
	
    @Override
    public void prepareDatabase() {
        super.prepareDatabase();
        db().getEntityManager().registerEntityClass(ListHolder.class);
    }

	@Override
	public void configure(Configuration config) {
		
	}

    public void write() {
        
        int count = 1000;
        int elements = setup().getObjectSize();
        
        declareIntent(new OIntentMassiveInsert());
        //begin();
        for (int i = 1; i<= count; i++) {
            store(ListHolder.generate(i, elements));
        }
        declareIntent(null);
        //commit();
    }

    public void read() {
        doQuery(ListHolder.class);
        
    }

}
