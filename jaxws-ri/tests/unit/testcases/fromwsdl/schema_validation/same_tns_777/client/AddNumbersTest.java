/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package fromwsdl.schema_validation.same_tns_777.client;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;
import com.sun.xml.ws.developer.SchemaValidationFeature;
import junit.framework.TestCase;

import java.net.URL;
import java.io.InputStream;

/**
 * Schema Validation sample
 *
 * @author Jitendra Kotamraju
 */
public class AddNumbersTest extends TestCase {

    private AddNumbersService service;

    public AddNumbersTest(String name) throws Exception {
        super(name);
    }

    public void setUp() {
        service = new AddNumbersService();
    }

    public void testServerValidationFailure() {
        AddNumbersPortType port = service.getAddNumbersPort ();
        int number1 = 10001;     // more than 4 digits, so doesn't pass validation
        int number2 = 20;
        try {
            port.addNumbers (number1, number2);
            fail("Server Schema Validation didn't work");
        } catch(SOAPFaultException se) {
            // Expected exception - Server side validation failed as expected"
            assertEquals("Client", se.getFault().getFaultCodeAsQName().getLocalPart());
        }
    }

    public void testClientValidationFailure() {

        SchemaValidationFeature feature = new SchemaValidationFeature();
        AddNumbersPortType port = service.getAddNumbersPort(feature);
        int number1 = 10001;     // more than 4 digits, so doesn't pass validation
        int number2 = 20;
        try {
            port.addNumbers (number1, number2);
            fail("Client Schema Validation didn't work");
        } catch(SOAPFaultException se) {
            fail("Client Schema Validation didn't work");
        } catch(WebServiceException se) {
            // Expected exception - Client side validation failed as expected
        }
    }

    public void testClientServerValidationPass() {
        SchemaValidationFeature feature = new SchemaValidationFeature();
        AddNumbersPortType port = service.getAddNumbersPort(feature);
        int number1 = 1000;
        int number2 = 20;
        int result = port.addNumbers (number1, number2);
        // Both Client and Server validation passed.
        assertEquals(1020, result);
    }

}
