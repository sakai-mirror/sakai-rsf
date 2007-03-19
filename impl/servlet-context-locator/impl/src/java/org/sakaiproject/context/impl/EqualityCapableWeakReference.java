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
import java.lang.ref.WeakReference;

/** A specialised WeakReference that will compare equal to any other 
 * reference to the same referent as well as to the referent. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class EqualityCapableWeakReference<T> extends WeakReference<T> {
	public int stashcode;
	public EqualityCapableWeakReference(T referent) {
		super(referent);
		stashcode = referent.hashCode();
	}
	
	public boolean equals(Object o) {
		T referent = get();
		if (referent == null) return false; // it may have been collected
		if (referent.equals(o)) return true;
		if (o instanceof Reference) {
			Reference<T> otherref = (Reference<T>) o;
			return referent.equals(otherref.get());
		}
		return false;
	}
	
	public int hashCode() {
		return stashcode;
	}
}
