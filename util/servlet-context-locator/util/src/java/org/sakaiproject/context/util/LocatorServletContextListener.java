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
package org.sakaiproject.context.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.context.api.ServletContextLocator;

import uk.org.ponder.servletutil.ServletUtil;

/** Registers the name of this servlet context with the registration service.
 * The context name will be taken from the servlet context parameter named
 * "sakai-context-name" if it is present, or else will be computed automatically
 * using a reasonably effective algorithm.
 * <p>To avoid destroying the context when the service is not present, this
 * implementation invokes all service methods reflectively.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class LocatorServletContextListener implements ServletContextListener {
	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(LocatorServletContextListener.class);

	private ServletContextLocator locator;
	private String contextName;

	private static String error = 
		"Unable to load ServletContextLocator from Sakai component manager, aborting registration"; 
	
	public void contextInitialized(ServletContextEvent sce) {
		try {
		  locator = (ServletContextLocator) ComponentManager.getInstance().get(ServletContextLocator.class);
		  if (locator == null) {
				M_log.error(error);
				return;
			}
		}
		catch(Exception e) {
			M_log.error(error, e);
		}

		ServletContext context = sce.getServletContext();
		String name = context.getInitParameter("sakai-context-name");
		if (name == null) {
			name = ServletUtil.computeContextName(context);
			name = name.substring(0, name.length() - 1);
		}
		locator.registerContext(name, context);
		contextName = name;
	}

	
	public void contextDestroyed(ServletContextEvent sce) {
		if (locator != null && contextName != null) {
			locator.deregisterContext(contextName, sce.getServletContext());
		}
	}


}
