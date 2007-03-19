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
package org.sakaiproject.context.api;

import javax.servlet.ServletContext;

/** A service which allows registration and lookup of J2EE ServletContext
 * objects across a container. This is primarily useful to allow location of
 * ServletContexts which are hosting static content (e.g. /reference and
 * /SakaiRSFComponents) in order to resolve requests into the filesystem. 
 * <p/>Should not be used from Contexts holding live code since it represents
 * a form of security risk.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface ServletContextLocator {
  /** Register the supplied context as visible under the given name. This name
   * begins with a leading slash and has no trailing slash **/
  public void registerContext(String contextName, ServletContext context);
  
  /** Deregister the supplied context - if a new context has already been
   * registered from another thread, the deregister request will be ignored */
  public void deregisterContext(String contextName, ServletContext context);
  /** Looks up a context registered under the supplied name, returning
   * <code>null</code> if no context is registered.
   */
  public ServletContext locateContext(String contextName);
}
