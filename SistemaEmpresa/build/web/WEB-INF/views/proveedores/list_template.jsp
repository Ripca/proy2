<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Proveedor" %>

<%
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Proveedores");
    request.setAttribute("pageIcon", "fas fa-truck");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/proveedores/list_content.jsp" />
</jsp:include>
