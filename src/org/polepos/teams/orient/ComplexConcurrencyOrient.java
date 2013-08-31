
package org.polepos.teams.orient;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import org.polepos.circuits.complexconcurrency.ComplexConcurrencyDriver;
import org.polepos.framework.Car;
import org.polepos.framework.TurnSetup;

public class ComplexConcurrencyOrient extends OrientDriver implements ComplexConcurrencyDriver {

	private ComplexOrient _delegate = new ComplexOrient();
	
	@Override
	public void configure(Configuration config) {
		_delegate.configure(config);
	}
	
	@Override
	public void prefillDatabase() {
		_delegate.write();
	}

	@Override
	public void race() {
		ORID[] ids = new ORID[writes()];
        declareIntent(new OIntentMassiveInsert());
		for (int i = 0; i < writes(); i++) {
			ids[i] = ((ORID)_delegate.write(true));
		}
        declareIntent(null);
		_delegate.query();
		for (int i = 0; i < updates(); i++) {
			_delegate.update(ids[i]);
		}
		for (int i = 0; i < deletes(); i++) {
			_delegate.delete(ids[i]);
		}
	}
	
	@Override
	public void prepare() {
		_delegate.prepare();
	}
	
	@Override
	public void prepareDatabase() {
		_delegate.prepareDatabase();
	}
	
	@Override
	public void configure(Car car, TurnSetup setup) {
		super.configure(car, setup);
		_delegate.configure(car, setup);
	}
	
	@Override
	public void closeDatabase() {
		_delegate.closeDatabase();
	}

	
	@Override
	public ComplexConcurrencyOrient clone() {
		ComplexConcurrencyOrient clone = (ComplexConcurrencyOrient) super.clone();
		clone._delegate = new ComplexOrient();
		return clone;
	}

}
