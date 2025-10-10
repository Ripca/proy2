<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Puesto" %>

<%
    List<Puesto> puestos = (List<Puesto>) request.getAttribute("puestos");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Puestos");
    request.setAttribute("pageIcon", "fas fa-briefcase");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/puestos/list_content.jsp" />
</jsp:include>
