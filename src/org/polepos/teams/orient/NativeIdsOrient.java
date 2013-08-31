package org.polepos.teams.orient;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import org.polepos.circuits.nativeids.NativeIdsDriver;
import org.polepos.data.Pilot;
import org.polepos.framework.DriverBase;

public class NativeIdsOrient extends OrientDriver implements NativeIdsDriver {
	
    @Override
    public void prepareDatabase() {
        super.prepareDatabase();
        db().getEntityManager().registerEntityClass(Pilot.class);
    }

	@Override
	public void configure(Configuration config) {
	}
	
	@Override
	public void circuitCompleted() {
		ids = null;
	}

    private ORID[] ids;

    public void store() {
        //begin();
        declareIntent(new OIntentMassiveInsert());
        ids = new ORID[setup().getSelectCount()];
        int count = setup().getObjectCount();
        for (int i = 1; i <= count; i++) {
            storePilot(i);
        }
        declareIntent(null);
        //commit();
    }

    public void retrieve() {
        for (ORID id : ids) {

            Pilot pilot = (Pilot) db().load(id);
            if (pilot == null) {
                System.err.println("Object not found by ID.");
            } else {
                addToCheckSum(pilot.getPoints());
            }
        }
    }

    private void storePilot(int idx) {
        Pilot pilot = new Pilot("Pilot_" + idx, "Jonny_" + idx, idx, idx);
        store(pilot);
        if (idx <= setup().getSelectCount()) {
            ids[idx - 1] = db().getIdentity(pilot);
        }
        if (isCommitPoint(idx)) {
            commit();
        }
    }

    private boolean isCommitPoint(int idx) {
        int commitInterval = setup().getCommitInterval();
        return commitInterval > 0 && idx % commitInterval == 0 && idx < setup().getObjectCount();
    }
    
    @Override
    public void copyStateFrom(DriverBase masterDriver) {
    	NativeIdsOrient master = (NativeIdsOrient) masterDriver;
    	ids = master.ids;
    }
}
