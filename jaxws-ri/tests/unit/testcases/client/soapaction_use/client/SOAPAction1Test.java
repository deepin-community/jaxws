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

package client.soapaction_use.client;

import junit.framework.TestCase;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.Dispatch;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;


/**
 * @author Rama Pulavarthi
 */
public class SOAPAction1Test extends TestCase {

    private final String action = "\"http://example.com/action/echoSOAPAction\"";
    private final String empty_action = "\"\"";
    private final String dummy_action = "dummy";

    public void testSOAPActionWithWSDL1() {
        TestEndpoint1 port = new TestEndpointService1().getTestEndpointPort1();
        String response = port.echoSOAPAction("foo");
        assertEquals(action, response);

    }
    // since use property is default (false), action property is ineffective
    public void testSOAPActionWithDispatch_WithoutUse() throws JAXBException {
        TestEndpoint1 port = new TestEndpointService1().getTestEndpointPort1();
        String address = (String) ((BindingProvider) port).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        Service s = Service.create(new QName("http://client.soapaction_use.server/", "TestEndpointService1"));
        s.addPort(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), SOAPBinding.SOAP11HTTP_BINDING, address);
        Dispatch<Object> d = s.createDispatch(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), JAXBContext.newInstance(ObjectFactory.class), Service.Mode.PAYLOAD);
        ((BindingProvider) d).getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, action);
        JAXBElement<String> r = (JAXBElement<String>) d.invoke(new ObjectFactory().createEchoSOAPAction("dummy"));
        assertEquals(empty_action, r.getValue());

    }
    // use is set to true, so action property is effective
    public void testSOAPActionWithDispatch_WithUse_true() throws JAXBException {
        TestEndpoint1 port = new TestEndpointService1().getTestEndpointPort1();
        String address = (String) ((BindingProvider) port).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        Service s = Service.create(new QName("http://client.soapaction_use.server/", "TestEndpointService1"));
        s.addPort(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), SOAPBinding.SOAP11HTTP_BINDING, address);
        Dispatch<Object> d = s.createDispatch(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), JAXBContext.newInstance(ObjectFactory.class), Service.Mode.PAYLOAD);
        ((BindingProvider) d).getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, action);
        ((BindingProvider) d).getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
        JAXBElement<String> r = (JAXBElement<String>) d.invoke(new ObjectFactory().createEchoSOAPAction("dummy"));
        assertEquals(action, r.getValue());

    }
    // since use property is false, action property is ineffective
    public void testSOAPActionWithDispatch_WithUse_false() throws JAXBException {
        TestEndpoint1 port = new TestEndpointService1().getTestEndpointPort1();
        String address = (String) ((BindingProvider) port).getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        Service s = Service.create(new QName("http://client.soapaction_use.server/", "TestEndpointService1"));
        s.addPort(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), SOAPBinding.SOAP11HTTP_BINDING, address);
        Dispatch<Object> d = s.createDispatch(new QName("http://client.soapaction_use.server/", "TestEndpointPort1"), JAXBContext.newInstance(ObjectFactory.class), Service.Mode.PAYLOAD);
        ((BindingProvider) d).getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, "http://example/action");
        ((BindingProvider) d).getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, false);
        JAXBElement<String> r = (JAXBElement<String>) d.invoke(new ObjectFactory().createEchoSOAPAction("dummy"));
        assertEquals(empty_action, r.getValue());
    }

}
