/**********************************************************************************
 * $URL: 
 * $Id: 
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/


/*
 * Created on 15 Mar 2007
 */
package org.sakaiproject.context.impl;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

import org.sakaiproject.context.api.ServletContextLocator;


public class ServletContextLocatorImpl implements ServletContextLocator {
	// Use of weak refs allows ServletContexts to be GCed in the case they
	// are unloaded with no replacement. Use of 1.5 ConcurrentMap since
	// oswego is missing composite atomic "remove" operation.
  private ConcurrentMap<String, Reference<ServletContext>> contexts = 
  	new ConcurrentHashMap();

  public ServletContext locateContext(String contextName) {
    Reference<ServletContext> contextref = contexts.get(contextName);
    return contextref == null? null : contextref.get();
  }

  public void registerContext(String contextName, ServletContext context) {
  	// unconditionally overwrite current value
  	contexts.put(contextName, 
  			new EqualityCapableWeakReference<ServletContext>(context));
  }

	public void deregisterContext(String contextName, ServletContext context) {
		// compare with a "probe" reference to ensure that "equals()" operation
		// is symmetric
		Reference<ServletContext> ref = 
			new EqualityCapableWeakReference<ServletContext>(context);
		// only remove value if set to supplied value
		contexts.remove(contextName, ref);
	}
}
