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

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.intent.OIntent;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.tx.OTransaction;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.polepos.framework.CheckSummable;
import org.polepos.framework.DriverBase;

import java.util.Iterator;
import java.util.List;

public abstract class OrientDriver extends DriverBase {

    private transient ODatabaseDocument _dbDoc;
	private OObjectDatabaseTx _db;
    private Configuration configuration;

    protected OrientDriver() {
        configuration = new Configuration();
    }

    public void prepare() {
		//_db = orientCar().openDb(configuration);
        //_dbDoc = _db.getUnderlying();
	}

	private OrientCar orientCar() {
		return ((OrientCar) car());
	}
	
	public abstract void configure(Configuration config);
	
    protected void indexField(Class clazz, String fieldName)
    {
        indexField(null, clazz, fieldName);
   	}

    protected void setOversize(Class clazz, float v)
    {
        OSchema oSchema = db().getMetadata().getSchema();
        OClass oClass = oSchema.getClass(clazz);
        oClass.setOverSize(v);
   	}

	protected void indexField(Configuration config, Class clazz, String fieldName) {
        OSchema oSchema = db().getMetadata().getSchema();
        OClass oClass = oSchema.getClass(clazz);
        if ( oClass == null ) {
            oClass = oSchema.createClass(clazz);
        }
        if (!oClass.existsProperty(fieldName)) {
            try {
               oClass.createProperty(fieldName, OType.getTypeByClass( clazz.getField(fieldName).getType()));
            }
            catch (NoSuchFieldException e) {

            }
        }
        if (!oClass.areIndexed(fieldName) ) {
            oClass.createIndex(fieldName + "Idx", OClass.INDEX_TYPE.NOTUNIQUE, fieldName);
        }
	}

	public void closeDatabase() {
        if ( _db != null && !_db.isClosed()) {
		    _db.close();
        }
        _db = null;
	}

	public OObjectDatabaseTx db() {
        if ( _db == null || !ODatabaseRecordThreadLocal.INSTANCE.isDefined() ) {
            _db = ((OrientCar)car()).openDb(configuration);
        }
        //ODatabaseRecordThreadLocal.INSTANCE.set(_dbDoc);
        return _db;
	}

	protected <RET extends java.util.List<?>>  RET doQuery(final OQuery<?> q, final Object... iArgs) {
        List<Object> result = db().query(q, iArgs);
        Iterator<?> it = result.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof CheckSummable) {
				addToCheckSum(((CheckSummable) o).checkSum());
			}
		}
        return (RET) result;
	}

	protected void doQuery(Class clazz) {
        for (Object o : db().browseClass(clazz) ) {
            if (o instanceof CheckSummable) {
                addToCheckSum(((CheckSummable) o).checkSum());
            }
        }

	}

    protected void declareIntent(OIntent oIntent) {
        db().declareIntent(oIntent);
    }
	protected void begin() {
        db().begin(OTransaction.TXTYPE.OPTIMISTIC);
	}
	
	protected void commit() {
        db().commit();
	}

	protected void store(Object obj) {
		db().save(obj);
	}

	protected void delete(Object obj) {
		db().delete(obj);
	}


}
