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

package whitebox.endpoint.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import testutil.ClientServerTestUtil;

import javax.xml.ws.Endpoint;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.Provider;
import javax.activation.DataSource;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.BindingType;
import java.util.List;
import java.util.ArrayList;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.net.URL;
import javax.xml.stream.*;

/**
 * Tests xs:redefine schemaLocation patching
 *
 * @author Jitendra Kotamraju
 */
public class SchemaRedefineTester extends TestCase {

    public void testSchemaRedefine() throws Exception {
        int port = Util.getFreePort();
port=1666;
        String address = "http://localhost:"+port+"/redefine";
        Endpoint e = Endpoint.create(new RedefineProvider());

        List<Source> metadata = new ArrayList<Source>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        String[] docs = {
                "WEB-INF/wsdl/SchemaRedefine.wsdl",
                "WEB-INF/wsdl/SchemaRedefine.xsd"
        };
        for (String doc : docs) {
            URL url = cl.getResource(doc);
            metadata.add(new StreamSource(url.openStream(), url.toExternalForm()));
        }
        e.setMetadata(metadata);

        e.publish(address);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(
            new URL(address+"?wsdl").openStream());
        while(reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT &&
                reader.getLocalName().equals("redefine")) {
                String loc = reader.getAttributeValue(null, "schemaLocation");
                assertEquals(address+"?xsd=1", loc);
            }
        }

        e.stop();
    }


    @WebServiceProvider(serviceName="SchemaRedefineService", portName="SchemaRedefinePort", targetNamespace="http://SchemaRedefine/")
    public static class RedefineProvider implements Provider<Source> {
        public Source invoke(Source ds) {
            return null;
        }
    }

}

