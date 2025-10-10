<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Cliente" %>

<%
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Clientes");
    request.setAttribute("pageIcon", "fas fa-users");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/clientes/list_content.jsp" />
</jsp:include>
