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

package org.polepos;

import org.polepos.circuits.complex.Complex;
import org.polepos.circuits.complexconcurrency.ComplexConcurrency;
import org.polepos.circuits.complexconcurrency.InsertCentricConcurrency;
import org.polepos.circuits.complexconcurrency.QueryCentricConcurrency;
import org.polepos.circuits.flatobject.FlatObject;
import org.polepos.circuits.inheritancehierarchy.InheritanceHierarchy;
import org.polepos.circuits.nestedlists.NestedLists;
import org.polepos.framework.ArraysP;
import org.polepos.framework.Circuit;
import org.polepos.framework.ReflectiveCircuitBase;
import org.polepos.framework.Team;
import org.polepos.reporters.DefaultReporterFactory;
import org.polepos.reporters.Reporter;
import org.polepos.runner.AbstractRunner;
import org.polepos.teams.db4o.Db4oClientServerTeam;
import org.polepos.teams.db4o.Db4oTeam;
import org.polepos.teams.orient.OrientLocalClient;
import org.polepos.teams.orient.OrientPLocalClient;
import org.polepos.teams.orient.OrientRemotePooledClient;

/**
 * This is the Main class to run PolePosition. If JDO, JPA and JVI are
 * to be tested also, persistent classes have to be enhanced first.
 * 
 * For your convenience you can try {@link RunSeasonAfterEnhancing#main(String[])}
 * or you can use the Ant script to do all in one go.
 * 
 * 
 */
public class RunSeason extends AbstractRunner {
	
	public static void main(String[] args) {
		new RunSeason().run();
	}

	@Override
	public Circuit[] circuits() {
		return ArraysP.concat(concurrencyCircuits(), defaultCircuits());
	}
	
	private Circuit[] defaultCircuits(){
		return new Circuit[] {
				new ReflectiveCircuitBase(Complex.class),
				new ReflectiveCircuitBase(NestedLists.class),
				new ReflectiveCircuitBase(InheritanceHierarchy.class),
				new ReflectiveCircuitBase(FlatObject.class),
//				new Trees(), 
//				new NativeIds(),
//				new Commits(),
//				new ArrayLists(),
//				new Strings(),
		};
	}
	
	private Circuit[] concurrencyCircuits() {
		if(! Settings.isConcurrency()){
			return new Circuit[] {};
		}
		return new Circuit[] {
				new ComplexConcurrency(),
				new QueryCentricConcurrency(),
				new InsertCentricConcurrency(),
		};
	}

	@Override
	public Team[] teams() {
		return new Team[] {
//1				VodJdoTeamFactory.newVodJdoTeam(),
//1				VodJpaTeamFactory.newVodJpaTeam(),
//				new JdoTeam(),
//				new JpaTeam(true),
				
 				new Db4oTeam(),
				new Db4oClientServerTeam(),
				
//1				new JdbcTeam(),
//1				new HibernateTeam(),
//				new MongoDBTeam(),

//                new OrientMemoryClient(),
                new OrientLocalClient(),
                new OrientPLocalClient(),
				new OrientRemotePooledClient()
//				new JpaTeam(),
//				new JviTeam(),
		};
	}

	@Override
	protected Reporter[] reporters() {
		return DefaultReporterFactory.defaultReporters();
	}
	

}
