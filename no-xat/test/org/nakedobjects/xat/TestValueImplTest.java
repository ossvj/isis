package org.nakedobjects.xat;

import org.nakedobjects.object.NakedObjectContext;
import org.nakedobjects.object.NakedObjectSpecificationImpl;
import org.nakedobjects.object.NakedObjectSpecificationLoaderImpl;
import org.nakedobjects.object.NakedObjectTestCase;
import org.nakedobjects.object.defaults.LocalReflectionFactory;
import org.nakedobjects.object.defaults.MockObjectManager;
import org.nakedobjects.object.defaults.value.Date;
import org.nakedobjects.object.reflect.ValueFieldSpecification;
import org.nakedobjects.object.reflect.defaults.JavaReflectorFactory;

public class TestValueImplTest extends NakedObjectTestCase {
    private MockObjectManager om;
    private NakedObjectContext context;
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestValueImplTest.class);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        om = MockObjectManager.setup();
        new NakedObjectSpecificationLoaderImpl();
        NakedObjectSpecificationImpl.setReflectionFactory(new LocalReflectionFactory());
        NakedObjectSpecificationImpl.setReflectorFactory(new JavaReflectorFactory());
        context = new NakedObjectContext(om);
    }
    
    public void test() {
        TestValueExample parent = new TestValueExample();
        parent.setContext(context);
        ValueFieldSpecification fld = (ValueFieldSpecification) parent.getSpecification().getFields()[0]; 
        TestValue value = new TestValueImpl(parent, fld);
        value.fieldEntry("+2");
        
        assertEquals(new Date(2003,8,19), parent.getDate());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        
        om.shutdown();
    }
}


/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2004  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/