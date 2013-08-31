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

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.polepos.circuits.complex.Complex;
import org.polepos.framework.NullVisitor;
import org.polepos.framework.Visitor;
import org.polepos.teams.orient.data.*;

import java.util.List;

public class ComplexOrient extends OrientDriver implements Complex {
	
	private ORID _rootId;

    @Override
    public void configure(Configuration config) {
    }

    @Override
    public void prepareDatabase() {
        super.prepareDatabase();    //To change body of overridden methods use File | Settings | File Templates.
        db().getEntityManager().registerEntityClass(ComplexHolder0.class);
        db().getEntityManager().registerEntityClass(ComplexHolder1.class);
        db().getEntityManager().registerEntityClass(ComplexHolder2.class);
        db().getEntityManager().registerEntityClass(ComplexHolder3.class);
        db().getEntityManager().registerEntityClass(ComplexHolder4.class);
        indexField(ComplexHolder2.class, "_i2");
    }

    @Override
	public Object write() {
		return write(false);
	}
	
	public Object write(boolean disjunctSpecial) {
		ComplexHolder0 holder = ComplexHolder0.generate(depth(), objects(), disjunctSpecial);
        addToCheckSum(holder);
        //db().attach(holder);
        holder = db().save(holder);
		commit();
		_rootId = db().getIdentity(holder);
		return _rootId;
	}

	@Override
	public void read() {
		ComplexHolder0 holder = read(_rootId);
		addToCheckSum(holder);
	}
	

	public ComplexHolder0 read(ORID id) {
		ComplexHolder0 holder = db().load(id, "*:-1");
		return holder;
	}

	@Override
	public void query() {
		int selectCount = selects();
		int firstInt = objects() * objects() + objects();
		int lastInt = firstInt + (objects() * objects() * objects()) - 1;
		int currentInt = firstInt;
        String sql = "select from ComplexHolder2 where _i2 = ?";
        final OQuery<ComplexHolder2> query = (new OSQLSynchQuery<ComplexHolder2>(sql)).setFetchPlan("*:-1");

		for (int run = 0; run < selectCount; run++) {
			List<ComplexHolder2> result = db().query(query, currentInt);
			ComplexHolder2 holder = result.get(0);
            holder = db().detachAll(holder, true);
			addToCheckSum(holder.ownCheckSum());
			List<ComplexHolder0> children = holder.getChildren();
			for (ComplexHolder0 child : children) {
                child = db().detachAll(child, true);
				addToCheckSum(child.ownCheckSum());
			}
			ComplexHolder0[] array = holder.getArray();
			for (ComplexHolder0 arrayElement : array) {
                arrayElement = db().detachAll(arrayElement, true);
				addToCheckSum(arrayElement.ownCheckSum());
			}
			currentInt++;
			if(currentInt > lastInt){
				currentInt = firstInt;
			}
		}
	}
	
	@Override
	public void update() {
		update(_rootId);
	}
	
	public void update(ORID id) {
		ComplexHolder0 holder = read(id);
		holder.traverse(new NullVisitor(),
				new Visitor<ComplexHolder0>() {
			@Override
			public void visit(ComplexHolder0 holder) {
				addToCheckSum(holder.ownCheckSum());
				holder.setName("updated");
				List<ComplexHolder0> children = holder.getChildren();
				ComplexHolder0[] array = new ComplexHolder0[children.size()];
				for (int i = 0; i < array.length; i++) {
					array[i] = children.get(i);
				}
				holder.setArray(array);
				db().save(holder);
			}
		});
		commit();
	}


	@Override
	public void delete() {
		delete(_rootId);
	}
	
	public void delete(ORID id) {
		ComplexHolder0 holder = read(id);
		holder.traverse(
			new NullVisitor(),
			new Visitor<ComplexHolder0>() {
			@Override
			public void visit(ComplexHolder0 holder) {
				addToCheckSum(holder.ownCheckSum());
				db().delete(holder);
			}
		});
	}



}
