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


public class InheritanceHierarchy2 extends InheritanceHierarchy1 {
    
    public int i2;  // index this one for queries
    
    public InheritanceHierarchy2(){
    }
    
    public InheritanceHierarchy2(int i0, int i1, int i2) {
        super(i0, i1);
        this.i2 = i2;
    }

    public void setI2(int i){
        i2 = i;
    }
    
    public int getI2(){
        return i2;
    }
}
