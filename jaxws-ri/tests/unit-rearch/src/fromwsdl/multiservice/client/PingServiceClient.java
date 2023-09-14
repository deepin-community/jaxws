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

package fromwsdl.multiservice.client;

import java.io.File;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import junit.framework.TestCase;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import testutil.ClientServerTestUtil;
import javax.xml.ws.BindingProvider;

public class PingServiceClient extends TestCase{
    PingPort stub;
    PingPort1 stub1;
            
    public PingServiceClient(String s) throws Exception {
        super(s);
        PingService service = new PingService();
        stub = (PingPort) service.getPort(new QName("http://xmlsoap.org/Ping","Ping"), PingPort.class);
        ClientServerTestUtil.setTransport(stub);
        
        PingService1 service1 = new PingService1();
        stub1 = (PingPort1) service1.getPort(new QName("http://xmlsoap.org/Ping","Ping1"), PingPort1.class);
        ClientServerTestUtil.setTransport(stub1);
    }
   
    public void testPing1(){
        TicketType ticket = new TicketType();
        ticket.setValue("SUNW");
        stub1.ping1(ticket, "Hello !");
        
    }
   
    public void testPing(){
        TicketType ticket = new TicketType();
        ticket.setValue("SUNW");
        stub.ping(ticket, "Hello !");
        
    }
    
}
