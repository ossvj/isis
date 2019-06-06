/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.testdomain.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.incubator.IsisTransactionManagerForJdo;
import org.apache.isis.runtime.spring.IsisBoot;
import org.apache.isis.testdomain.jdo.Book;
import org.apache.isis.testdomain.jdo.Inventory;
import org.apache.isis.testdomain.jdo.JdoTestDomainModule;
import org.apache.isis.testdomain.jdo.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;

@SpringBootTest(
	classes = { 
		IsisBoot.class, 
		JdoTestDomainModule.class,
		IsisTransactionManagerForJdo.class,
	}, 
	properties = {
		"logging.config=log4j2-test.xml",
		// "isis.reflector.introspector.parallelize=false",
		"logging.level.org.apache.isis.incubator.IsisTransactionManagerForJdo=DEBUG"
		 //"logging.level.org.apache.isis.core.metamodel.specloader.specimpl.ObjectSpecificationAbstract=TRACE"
})
@Transactional
class JdoBootstrappingTest {

	@Inject private RepositoryService repository;

	@BeforeAll
	static void launchH2Console() throws SQLException {
		// for troubleshooting ...
		// Util_H2Console.main(null);
	}

	void cleanUp() {

		repository.allInstances(Inventory.class).forEach(repository::remove);
		repository.allInstances(Book.class).forEach(repository::remove);
		repository.allInstances(Product.class).forEach(repository::remove);

	}

	void setUp() {

		// setup sample Inventory
		Set<Product> products = new HashSet<>();

		products.add(Book.of("Sample Book", "A sample book for testing.", 99., "Sample Author", "Sample ISBN",
				"Sample Publisher"));

		val inventory = Inventory.of("Sample Inventory", products);
		repository.persist(inventory);
	}

	@Test @Rollback(false)
	void sampleInventoryShouldBeSetUp() {

		// given - expected pre condition: no inventories

		cleanUp();
		assertEquals(0, repository.allInstances(Inventory.class).size());

		// when

		setUp();

		// then - expected post condition: ONE inventory

		val inventories =repository.allInstances(Inventory.class);
		assertEquals(1, inventories.size());

		val inventory = inventories.get(0);
		assertNotNull(inventory);
		assertNotNull(inventory.getProducts());
		assertEquals(1, inventory.getProducts().size());

		val product = inventory.getProducts().iterator().next();
		assertEquals("Sample Book", product.getName());

	}

}