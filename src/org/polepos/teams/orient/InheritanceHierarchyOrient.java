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

import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.intent.OIntentMassiveRead;
import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.polepos.circuits.inheritancehierarchy.InheritanceHierarchy;
import org.polepos.teams.orient.data.*;

public class InheritanceHierarchyOrient extends OrientDriver implements InheritanceHierarchy {
	

    @Override
    public void prepareDatabase() {
        super.prepareDatabase();
        db().getEntityManager().registerEntityClass(InheritanceHierarchy0.class);
        db().getEntityManager().registerEntityClass(InheritanceHierarchy1.class);
        db().getEntityManager().registerEntityClass(InheritanceHierarchy2.class);
        db().getEntityManager().registerEntityClass(InheritanceHierarchy3.class);
        db().getEntityManager().registerEntityClass(InheritanceHierarchy4.class);
		indexField(InheritanceHierarchy2.class, "i2");
    }

	@Override
	public void configure(Configuration config) {
	}

	@Override
	public void write(){
        int count = setup().getObjectCount();
        declareIntent(new OIntentMassiveInsert());
        //begin();
        for (int i = 1; i<= count; i++) {
            InheritanceHierarchy4 inheritanceHierarchy4 = new InheritanceHierarchy4();
            inheritanceHierarchy4.setAll(i);
            store(inheritanceHierarchy4);
        }
        declareIntent(null);
        //commit();
    }
    
	@Override
	public void read(){
        doQuery(InheritanceHierarchy4.class);
    }
    
	@Override
	public void query(){
        int count = setup().getSelectCount();
        declareIntent(new OIntentMassiveRead());
        String sql = "select from InheritanceHierarchy4 where i2 = ?";
        final OQuery<InheritanceHierarchy4> query = new OSQLSynchQuery<InheritanceHierarchy4>(sql);
        for (int i = 1; i <= count; i++) {
            doQuery(query, i);
        }
        declareIntent(null);
    }
    
	@Override
	public void delete(){
        begin();
        for( InheritanceHierarchy4 o : db().browseClass(InheritanceHierarchy4.class) ){
            db().delete(o);
            addToCheckSum(5);
        }
        commit();
    }

}
