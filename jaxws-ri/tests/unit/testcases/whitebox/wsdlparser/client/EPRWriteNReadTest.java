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

package whitebox.wsdlparser.client;

import com.sun.xml.ws.test.VersionRequirement;
import com.sun.xml.ws.util.ByteArrayBuffer;
import com.sun.xml.ws.wsdl.parser.RuntimeWSDLParser;
import com.sun.xml.ws.api.model.wsdl.*;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import junit.framework.TestCase;
import org.xml.sax.EntityResolver;
import com.sun.xml.ws.api.addressing.WSEndpointReference;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;


/**
 * Tests to verify that the EndpointReference inside wsdl:port is captured correctly so that it can be used as the destination EPR.
 * Tests verify that the ns declarations on the golbal/parent elements is captured in to the EPR infoset by writing the
 * infoset and reading it back again
 *
 * @author Rama Pulavarthi
 */
@VersionRequirement(since = "2.2.6")
public class EPRWriteNReadTest extends TestCase {
    public void test1() throws Exception {
        URL fileurl = getResource("hello_literal_identity1.wsdl");
        WSDLModel doc = RuntimeWSDLParser.parse(fileurl, new StreamSource(fileurl.toExternalForm()), getResolver(), true, null);
        WSDLService service = doc.getService(new QName("urn:test", "Hello"));
        WSDLPort port = service.getFirstPort();
        WSEndpointReference wsepr = port.getExtension(WSEndpointReference.class);
        verifyEPR(wsepr);
    }

    public void test2() throws Exception {
        URL fileurl = getResource("hello_literal_identity2.wsdl");
        WSDLModel doc = RuntimeWSDLParser.parse(fileurl, new StreamSource(fileurl.toExternalForm()), getResolver(), true, null);
        WSDLService service = doc.getService(new QName("urn:test", "Hello2"));
        WSDLPort port = service.getFirstPort();
        WSEndpointReference wsepr = port.getExtension(WSEndpointReference.class);
        verifyEPR(wsepr);
    }

    public void test3() throws Exception {
        URL fileurl = getResource("hello_literal_identity3.wsdl");
        WSDLModel doc = RuntimeWSDLParser.parse(fileurl, new StreamSource(fileurl.toExternalForm()), getResolver(), true, null);
        WSDLService service = doc.getService(new QName("urn:test", "Hello2"));
        WSDLPort port = service.getFirstPort();
        WSEndpointReference wsepr = port.getExtension(WSEndpointReference.class);
        verifyEPR(wsepr);
    }

    private static void verifyEPR( WSEndpointReference wsepr) throws Exception {
        ByteArrayBuffer bytebuffer = new ByteArrayBuffer();
        XMLStreamWriter w = XMLOutputFactory.newInstance().createXMLStreamWriter(bytebuffer);
        System.out.println("----------------------------------------------------------------------");
        w.writeStartDocument();
        wsepr.writeTo("EndpointReference",w);
        w.writeEndDocument();
        System.out.println(bytebuffer.toString());

        //verify the validity of infoset by reading it as EPR back again
        EndpointReference epr = W3CEndpointReference.readFrom(new StreamSource(bytebuffer.newInputStream()));

        //Verify it good old way
        XMLStreamReader xsr = XMLInputFactory.newInstance().createXMLStreamReader(new StreamSource(bytebuffer.newInputStream()));
        QName q = new QName("http://schemas.xmlsoap.org/ws/2006/02/addressingidentity", "Identity");
        while(xsr.hasNext()) {
            xsr.next();
            if(xsr.getEventType() == XMLStreamConstants.START_ELEMENT && xsr.getLocalName().equals("Identity")) {
                assertEquals(q.getNamespaceURI(), xsr.getNamespaceURI());
            }
        }
    }

    private static EntityResolver getResolver() {
        EntityResolver resolver = null;
        if (resolver == null) {
            // set up a manager
            CatalogManager manager = new CatalogManager();
            manager.setIgnoreMissingProperties(true);
            try {
                //if(System.getProperty(getClass().getName()+".verbose")!=null)
                manager.setVerbosity(0);
            } catch (SecurityException e) {
                // recover by not setting the debug flag.
            }

            // parse the catalog
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration catalogEnum;
            try {
                if (cl == null)
                    catalogEnum = ClassLoader.getSystemResources("/META-INF/jaxws-catalog.xml");
                else
                    catalogEnum = cl.getResources("/META-INF/jaxws-catalog.xml");

                while (catalogEnum.hasMoreElements()) {
                    URL url = (URL) catalogEnum.nextElement();
                    manager.getCatalog().parseCatalog(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            resolver = new CatalogResolver(manager);
        }

        return resolver;
    }

    public URL getResource(String s) {
        return getClass().getClassLoader().getResource(s);
    }

}
