module server {
    requires java.xml.ws;
     requires jdk.httpserver;
      requires java.logging; 

    // generated by WebServiceWrapperGenerator
    exports fromjava.xml_java_type_adapter.server.jaxws;
    exports fromjava.xml_java_type_adapter.server;
}
