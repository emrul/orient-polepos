package org.polepos.teams.orient;

import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.polepos.circuits.nestedlists.NestedLists;
import org.polepos.framework.Procedure;
import org.polepos.framework.Visitor;
import org.polepos.teams.orient.data.ListHolder;

public class NestedListsOrient extends OrientDriver implements NestedLists {

	@Override
	public void create() throws Throwable {
        store(ListHolder.generate(depth(), objects(), reuse()));
		//commit();
	}

    @Override
    public void prepareDatabase() {
        super.prepareDatabase();
        db().setAutomaticSchemaGeneration(true);
        db().getEntityManager().registerEntityClass(ListHolder.class);
        indexField(ListHolder.class, "name");
        setOversize(ListHolder.class, 2);
    }


	@Override
	public void read() throws Throwable {
		ListHolder root = root();
		root.accept(new Visitor<ListHolder>(){
			public void visit(ListHolder listHolder){
				addToCheckSum(listHolder);
			}
		});
	}

	private ListHolder root() {
        String sql = "select from ListHolder where name = ?";
        final OQuery<ListHolder> query = new OSQLSynchQuery<ListHolder>(sql).setFetchPlan("*:-1");
        ListHolder root = (ListHolder)db().query(query, ListHolder.ROOT_NAME).get(0);
		return root;
	}
	
	@Override
	public void update() throws Throwable {
		ListHolder root = root();
		addToCheckSum(root.update(depth(), 0,  new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				store(obj);
			}
		}));
		commit();
	}

	@Override
	public void delete() throws Throwable {
		ListHolder root = root();
		addToCheckSum(root.delete(depth(), 0,  new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				delete(obj);
			}
		}));
		commit();
	}
	
	@Override
	public void configure(Configuration config) {
        //db().getEntityManager().registerEntityClass(ListHolder.class);
        //indexField(config, ListHolder.class, "_name");
	}

}
