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

package bugs.jaxws1075.client;

import java.util.Map;
import java.util.List;
import java.util.StringTokenizer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.Service;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * HTTP Basic Auth negtive test for 403 error
 *
 * @author qiang.wang@oracle.com
 */
public class BasicAuthTest extends TestCase {

    public BasicAuthTest(String name) {
        super(name);
    }

    /*
     * Tests Standard HTTP Authorization header on server side
     */
    public void testHttpMsgCtxt() throws Exception {
        final String prop = "com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump";
        String oldV = System.getProperty(prop);
        oldV = (oldV == null) ? "" : oldV;
	System.setProperty(prop,"true");

        Hello proxy = new HelloService().getHelloPort();
        BindingProvider bp = (BindingProvider)proxy;
        bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "auth-user");
        bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "auth-pass");


        PrintStream ps = System.err;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        System.setErr(new PrintStream(baos));

        try {
           proxy.testHttpProperties();
        } catch (Exception ex) {
          ex.printStackTrace();
          //exception must not be soapfault
          assertTrue("The exception can not be soapfault exception", !(ex instanceof javax.xml.ws.ProtocolException));
        } finally{
           System.setErr(ps);
           System.setProperty(prop, oldV);
        }

	String dumpStr = baos.toString();

	System.out.println(dumpStr);
        boolean has403 = false;
        StringTokenizer tokenizer = new StringTokenizer(dumpStr, "\n");
        while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.indexOf("HTTP status code") != -1) {
		  System.out.println("HTTP status code:" + token);
                  if(token.indexOf("403") > 0)
                     has403 = true;
                }
        }

        assertTrue("Server does not return 403 code", has403); 
   }
    
}
