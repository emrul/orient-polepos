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


package org.polepos.teams.orient.data;

import org.polepos.framework.CheckSummable;
import org.polepos.framework.IdGenerator;
import org.polepos.framework.Procedure;
import org.polepos.framework.Visitor;

import java.util.*;

public class ListHolder implements CheckSummable {
	
	public static final String ROOT_NAME = "root";
	
	private static IdGenerator _idGenerator = new IdGenerator();
	
	private long _id;

	public String name;
	
	private List<ListHolder> _list;
	
	public static ListHolder generate(int depth, int leafs, int reuse){
		ListHolder root = generate(new ArrayList<ListHolder>(), depth, leafs, reuse);
		root.name = ROOT_NAME;
		return root;
	}
	
	public static ListHolder generate(List<ListHolder> flatList, int depth, int leafs, int reuse){
		if(depth == 0){
			return null;
		}
		ListHolder listHolder = new ListHolder();
		listHolder._id = _idGenerator.nextId();
		
		flatList.add(listHolder);
		if(depth == 1){
			return listHolder;
		}
		listHolder._list = new ArrayList<ListHolder>();
		int childDepth = depth -1;
		for (int i = leafs -1; i >= 0; i--) {
			if(i < reuse){
				int indexInList = (flatList.size() - i) / 2;
				if(indexInList < 0){
					indexInList = 0;
				}
				listHolder._list.add(flatList.get(indexInList) );
			} else {
				ListHolder child = generate(flatList, childDepth, leafs, reuse);
				child.name = "child:" + depth + ":" + i;
				listHolder._list.add(child);
			}
		}
		return listHolder;
	}
	
	@Override
	public long checkSum() {
		return getName().hashCode();
	}
	
	public void accept(Visitor<ListHolder> visitor) {
		Set<ListHolder> visited = new HashSet<ListHolder>();
		acceptInternal(visited, visitor);
	}
	
	private void acceptInternal(Set<ListHolder> visited, Visitor<ListHolder> visitor){
		if(visited.contains(this)){
			return;
		}
		visitor.visit(this);
		visited.add(this);
		if(_list == null){
			return;
		}
		Iterator<ListHolder> i = _list.iterator();
		while(i.hasNext()){
			ListHolder child = i.next();
			child.acceptInternal(visited, visitor);
		}
	}
	
	public int update(int maxDepth, int depth, Procedure<ListHolder> storeProcedure) {
		Set<ListHolder> visited = new HashSet<ListHolder>();
		return updateInternal(visited, maxDepth, depth,storeProcedure);
	}

	public int updateInternal(Set<ListHolder> visited, int maxDepth, int depth, Procedure<ListHolder> storeProcedure) {
		if(visited.contains(this)){
			return 0;
		}
		visited.add(this);
		int updatedCount = 1;
		if(depth > 0){
			setName("updated " + getName());
		}
		if(_list != null){
			for (int i = 0; i < _list.size(); i++) {
				ListHolder child = _list.get(i);
				updatedCount += child.updateInternal(visited, maxDepth, depth +  1, storeProcedure);
			}
		}
		storeProcedure.apply(this);
		return updatedCount;
	}
	
	public int delete(int maxDepth, int depth, Procedure<ListHolder> deleteProcedure) {
		Set<ListHolder> visited = new HashSet<ListHolder>();
		return deleteInternal(visited, maxDepth, depth, deleteProcedure);
	}

	public int deleteInternal(Set<ListHolder> visited, int maxDepth, int depth, Procedure<ListHolder> deleteProcedure) {
		if(visited.contains(this)){
			return 0;
		}
		visited.add(this);
		int deletedCount = 1;
		if(_list != null){
			for (int i = 0; i < _list.size(); i++) {
				ListHolder child = _list.get(i);
				deletedCount += child.deleteInternal(visited, maxDepth, depth +  1, deleteProcedure);
			}
		}
		deleteProcedure.apply(this);
		return deletedCount;
	}

	public String getName() {
		return name;
	}

	public List<ListHolder> list() {
		return _list;
	}
	
	public long id(){
		return _id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void list(List<ListHolder> list) {
		this._list = list;
	}
	
	public void id(long id){
		this._id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(obj.getClass() != this.getClass()){
			return false;
		}
		ListHolder other = (ListHolder) obj;
		return _id == other._id;
	}
	
	@Override
	public int hashCode() {
		return (int)_id;
	}
	
	@Override
	public String toString() {
		return "ListHolder " + _id;
	}
	
}
