/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006-2017 Oracle and/or its affiliates. All rights reserved.
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

package wsa.submission.fromwsdl.fault.client;

import javax.xml.ws.soap.AddressingFeature;

import junit.framework.TestCase;
import com.sun.xml.ws.developer.MemberSubmissionAddressingFeature;


/**
 * @author Arun Gupta
 */
public class AddNumbersClient extends TestCase {
    public AddNumbersClient(String name) {
        super(name);
    }
    private static final MemberSubmissionAddressingFeature ENABLED_ADDRESSING_FEATURE = new MemberSubmissionAddressingFeature(true);

    private AddNumbersPortType createStub() throws Exception {
        return new AddNumbersService().getPort(AddNumbersPortType.class, ENABLED_ADDRESSING_FEATURE);
    }

    public void testAddNumbersDefaultAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers(-10, 10);
            fail("AddNumbersFault_Exception must be thrown");
        } catch (AddNumbersFault_Exception ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbersDefaultTooBigNumbersAction() throws Exception {
        try {
            createStub().addNumbers(20, 20);
            fail("TooBigNumbersFault_Exception must be thrown");
        } catch (TooBigNumbersFault_Exception ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers2ExplicitAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers2(-10, 10);
            fail("AddNumbers2Fault must be thrown");
        } catch (AddNumbers2Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers2ExplicitTooBigNumbersAction() throws Exception {
        try {
            createStub().addNumbers2(20, 20);
            fail("TooBigNumbers2Fault must be thrown");
        } catch (TooBigNumbers2Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers3ExplicitAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers3(-10, 10);
            fail("AddNumbers3Fault must be thrown");
        } catch (AddNumbers3Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers3DefaultTooBigNumbersAction() throws Exception {
        try {
            createStub().addNumbers3(20, 20);
            fail("TooBigNumbers3Fault must be thrown");
        } catch (TooBigNumbers3Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers4DefaultAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers4(-10, 10);
            fail("AddNumbersFault must be thrown");
        } catch (AddNumbers4Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers4ExplicitTooBigNumbersAction() throws Exception {
        try {
            createStub().addNumbers4(20, 20);
            fail("TooBigNumbers4Fault must be thrown");
        } catch (TooBigNumbers4Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers5ExplicitAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers5(-10, 20);
            fail("AddNumbers5Fault must be thrown");
        } catch (AddNumbers5Fault ex)  {
            assertTrue(true);
        }
    }

    public void testAddNumbers6EmptyAddNumbersAction() throws Exception {
        try {
            createStub().addNumbers6(-10, 20);
            fail("AddNumbers6Fault must be thrown");
        } catch (AddNumbers6Fault ex)  {
            assertTrue(true);
        }
    }
}
