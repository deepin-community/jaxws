/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2004-2017 Oracle and/or its affiliates. All rights reserved.
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

package server.provider.multiple_body.client;

import junit.framework.TestCase;

import javax.xml.ws.Service;
import javax.xml.ws.Dispatch;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;

import com.sun.xml.ws.api.SOAPVersion;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import testutil.ClientServerTestUtil;

public class Client extends TestCase {

    public Client(String name) {
        super(name);
    }

    public void testMultipleBodies() throws SOAPException {
        if (ClientServerTestUtil.useLocal()) {
            System.out.println("Runs only in HTTP!");
            return;
        }
        Service service = Service.create(new QName("urn:test", "Endpoint"));
        service.addPort(new QName("urn:test", "EndpointPort"), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:8080/jaxrpc-provider_tests_multiple_body/endpoint");
        Dispatch<SOAPMessage> disp = service.createDispatch(new QName("urn:test", "EndpointPort"), SOAPMessage.class, Service.Mode.MESSAGE);
        SOAPMessage sm = SOAPVersion.SOAP_11.saajMessageFactory.createMessage();
        SOAPBody sb = sm.getSOAPBody();
        sb.addBodyElement(new QName("http://first.body", "first"));
        sb.addBodyElement(new QName("http://second.body", "second"));
        SOAPMessage resp = disp.invoke(sm);

        SOAPBody body = resp.getSOAPBody();
        NodeList nl = body.getChildNodes();
        assertTrue(nl.getLength() == 2);
        Node n = nl.item(0);
        assertTrue(n.getLocalName().equals("first") && n.getNamespaceURI().equals("http://first.body"));
        n = nl.item(1);
        assertTrue(n.getLocalName().equals("second") && n.getNamespaceURI().equals("http://second.body"));
    }
}
