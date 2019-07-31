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
package org.apache.isis.testsecurity.ldap;

import java.util.concurrent.CountDownLatch;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import lombok.val;

@RunWith(FrameworkRunner.class)
@CreateDS(name = "myDS",
    partitions = {
        @CreatePartition(name = "test", suffix = "dc=myorg,dc=com")
    })
@CreateLdapServer(transports = { 
		@CreateTransport(protocol = "LDAP", address = "localhost", port = 10389)})
@ApplyLdifFiles({"ldap-users.ldif"})
public class LdapEmbeddedServer extends AbstractLdapTestUnit {
	
    @Test
    public void authenticateAgainstLdap() {
    	// at this stage the LDAP server is setup an listening
    }
    
    public static CountDownLatch run() throws InitializationError, InterruptedException {
    	
    	val serverLanchedLatch = new CountDownLatch(1);
    	val serverTerminatedLatch = new CountDownLatch(1);
		val runner = new FrameworkRunner(LdapEmbeddedServer.class);
		
		val notifier = new RunNotifier();
		notifier.addListener(new RunListener() {
			@Override
			public void testFinished(Description description) throws Exception {
				serverLanchedLatch.countDown();
				serverTerminatedLatch.await(); // wait for the latch to be decremented by the caller
			}
		});
		
		val thread = new Thread() {
			@Override
			public void run() {
				runner.run(notifier);
			}
		};
		
		thread.start();
		
		serverLanchedLatch.await();
		
		return serverTerminatedLatch;
		
	}
    
    
}