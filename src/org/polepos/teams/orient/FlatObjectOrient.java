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

import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.polepos.circuits.flatobject.FlatObject;
import org.polepos.teams.orient.data.IndexedObject;

import java.util.List;


public class FlatObjectOrient extends OrientDriver implements FlatObject{
		
    @Override
    public void prepareDatabase() {
        super.prepareDatabase();
        db().getEntityManager().registerEntityClass(IndexedObject.class);
		indexField(IndexedObject.class, "i");
		indexField(IndexedObject.class , "s");
    }
	@Override
	public void configure(Configuration config) {
	}
	
	public void write(){
        initializeTestId(objects());
		while ( hasMoreTestIds()){
			IndexedObject indexedObject = new IndexedObject(nextTestId());
			store(indexedObject);
			//purge(indexedObject);
			if(doCommit()){
				commit();
			}
            addToCheckSum(indexedObject);
		}
	}

    public void queryIndexedString() {
        initializeTestId(selects());
        String sql = "select from IndexedObject where s = ?";
        final OQuery<IndexedObject> query = new OSQLSynchQuery<IndexedObject>(sql);
        while(hasMoreTestIds()) {
            doQuery(query, IndexedObject.queryString(nextTestId()));
        }
    }

    public void queryIndexedInt() {
        initializeTestId(selects());
        String sql = "select from IndexedObject where i = ?";
        final OQuery<IndexedObject> query = new OSQLSynchQuery<IndexedObject>(sql);
        while(hasMoreTestIds()) {
            doQuery(query, nextTestId());
        }
    }

    public void update() {
        initializeTestId(updates());
        String sql = "select from IndexedObject where i = ?";
        final OQuery<IndexedObject> query = new OSQLSynchQuery<IndexedObject>(sql);
        while(hasMoreTestIds()) {
            List<IndexedObject> results = db().query(query, nextTestId());
            IndexedObject indexedObject = results.get(0);
            indexedObject.updateString();
            db().save(indexedObject);
            addToCheckSum(indexedObject);
        }
        commit();
	}
	
    public void delete() {
        initializeTestId(updates());
        String sql = "select from IndexedObject where i = ?";
        final OQuery<IndexedObject> query = new OSQLSynchQuery<IndexedObject>(sql);
        while(hasMoreTestIds()) {
            List<IndexedObject> results = db().query(query, nextTestId());
            IndexedObject indexedObject = results.get(0);
            indexedObject.updateString();
            delete(indexedObject);
            addToCheckSum(indexedObject);
        }
        commit();
	}
	
}
