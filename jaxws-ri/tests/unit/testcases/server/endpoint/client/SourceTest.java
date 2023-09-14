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

package server.endpoint.client;

import com.sun.xml.ws.client.WSServiceDelegate;
import java.io.StringReader;
import java.io.ByteArrayOutputStream;
import java.net.URL;
 
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import javax.xml.stream.*;
import java.io.*;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.sun.xml.ws.client.WSServiceDelegate;

import junit.framework.TestCase;

import testutil.PortAllocator;

/**
 * @author Jitendra Kotamraju
 */

public class SourceTest extends TestCase
{
    private static final String NS = "http://echo.org/";

    public void testSource() throws Exception {
        int port = PortAllocator.getFreePort();
        String address = "http://localhost:"+port+"/source";
        Endpoint endpoint = Endpoint.create(new SourceEndpoint());
        endpoint.publish(address);

        Source response = invoke(address);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.transform(response, new StreamResult(bos));
        bos.close();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(
            new ByteArrayInputStream(bos.toByteArray()));
        while(reader.hasNext()) {
            reader.next();
        }
        
        endpoint.stop();
    }

    /**
     * This test ensures that when a valid Source object is provided to the JAX-WS client for
     * the WSDL, it gets used instead of going over the wire to read from the WSDL URL
     * (Associated change is in RuntimeWSDLParser.resolveWSDL)
     * 
     * The test first reads the WSDL into an in-memory Source object and uses that 
     * to construct the Service delegate (using WSServiceDelegate constructor since 
     * no standard API is available). It then creates the service with this Source and makes
     * sure that the InputStream underlying the Source has been consumed 
     * 
     * @throws Exception
     */
    public void testServiceCreateUsingInMemorySource() throws Exception {
    	int port = PortAllocator.getFreePort();
        String address = "http://localhost:"+port+"/source2";
        Endpoint endpoint = Endpoint.create(new SourceEndpoint());
        endpoint.publish(address);
        QName portName = new QName(NS, "RpcLitPort");
        QName serviceName = new QName(NS, "RpcLitEndpoint");
        StreamSource wsdlSource = readWsdlToInMemorySource(address + "?wsdl");
        WSServiceDelegate svcDel = new WSServiceDelegate(wsdlSource, serviceName, Service.class);
        byte[] b = new byte[512];
        int len = wsdlSource.getInputStream().read(b);
        assertTrue("In-memory WSDL Source passed to client must be consumed!", len < 0);
    }
    private StreamSource readWsdlToInMemorySource(String wsdlAddr) throws Exception {
		URL wsdlUrl = new URL(wsdlAddr);
		InputStream wsdlStream = wsdlUrl.openStream();
		byte[] b = new byte[512];
		int len = wsdlStream.read(b);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (len >= 0) {
			bos.write(b, 0, len);
			len = wsdlStream.read(b);
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		StreamSource wsdlSource = new StreamSource(bis, wsdlAddr);
		return wsdlSource;
	}

	private Source invoke(String address) throws Exception {
        QName portName = new QName(NS, "RpcLitPort");
        QName serviceName = new QName(NS, "RpcLitEndpoint");
        Service service = Service.create(new URL(address+"?wsdl"), serviceName);
        Dispatch<Source> d = service.createDispatch(portName, Source.class,
            Service.Mode.PAYLOAD);
        String body  = "<ns0:echoInteger xmlns:ns0=\"http://echo.abstract.org/\"><arg0>2</arg0></ns0:echoInteger>";
        return d.invoke(new StreamSource(new StringReader(body)));
    }
   
    @WebService(name="RpcLit", serviceName="RpcLitEndpoint",
        portName="RpcLitPort", targetNamespace="http://echo.org/",
        endpointInterface="server.endpoint.client.RpcLitEndpointIF")
    @SOAPBinding(style=Style.RPC)
    public static class SourceEndpoint {
        public int echoInteger(int arg0) {
            return arg0;
        }
    }
}
