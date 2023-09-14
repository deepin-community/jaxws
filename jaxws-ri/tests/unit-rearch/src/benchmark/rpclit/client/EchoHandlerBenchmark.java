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

package benchmark.rpclit.client;

/**
 * @author JAX-RPC RI Development Team
 */
public class EchoHandlerBenchmark extends RpclitTest {
    public EchoHandlerBenchmark(String name) throws Exception {
        super(name);
    }

    public void testOnce() throws Exception {
    }

    public EchoPortType getStub() {
        EchoPortType stub = null;
/*
        try {
            ServiceFactory20 factory = new ServiceFactory20();
            QName serviceQName = new QName("http://soapinterop.org/", "EchoService");
            QName portQname = new QName("http://soapinterop.org/", "EchoPort");
            
            Class serviceClazz = Class.forName("benchmark.rpclit.client.EchoService");
            Service service = factory.loadService(serviceClazz);
            
            HandlerRegistry handlerRegistry = service.getHandlerRegistry();
            List handlerChain = handlerRegistry.getHandlerChain(portQname);
            HandlerInfo handlerInfo = new HandlerInfo();
            handlerInfo.setHandlerClass(SimpleHandler.class);
            handlerChain.add(handlerInfo);

            Class portIF = Class.forName("benchmark.rpclit.client.EchoPortType");
            stub = (EchoPortType) service.getPort(portQname, portIF);
            
//            stub = (EchoPortType) service.getEchoPort();
            // set transport
            ClientServerTestUtil util = new ClientServerTestUtil();
//            util.setTransport(stub,
//                    "benchmark.rpclit112.server.EchoPortType_Tie",
//                    "benchmark.rpclit112.server.EchoPortTypeImpl");
            util.setTransport(stub,
                    "benchmark.rpclit.server.EchoPortTypeImpl", System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return stub;
    }
}
