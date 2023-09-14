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

package provider.http_status_code_200_oneway.client;

import junit.framework.TestCase;

import javax.activation.DataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Issue WSIT:1354
 *
 * @author Jitendra Kotamraju
 */
public class DispatchTest extends TestCase {

    public DispatchTest(String name) throws Exception {
        super(name);
    }

    /*
     * Service sends status code 200+no envelope
     */
    public void testNoContentType() throws Exception {
        BindingProvider bp = (BindingProvider)new Hello_Service().getHelloPort();
        String address = (String) bp.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);

        Service service = Service.create(new QName("", ""));
        service.addPort(new QName("",""), SOAPBinding.SOAP11HTTP_BINDING, address);
        Dispatch<SOAPMessage> d = service.createDispatch(new QName("",""), SOAPMessage.class, Service.Mode.MESSAGE);
        String str = "<S:Envelope xmlns:S='http://schemas.xmlsoap.org/soap/envelope/'><S:Body/></S:Envelope>";
        MessageFactory fact = MessageFactory.newInstance();
        MimeHeaders headers = new MimeHeaders();
        SOAPMessage req = fact.createMessage(headers, new ByteArrayInputStream(str.getBytes("UTF-8")));
        d.invokeOneWay(req);
    }

}
