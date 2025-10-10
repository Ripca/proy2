<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Empleado" %>

<%
    List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");
    
    // Configurar atributos para la plantilla
    request.setAttribute("pageTitle", "Gestión de Empleados");
    request.setAttribute("pageIcon", "fas fa-user-tie");
%>

<jsp:include page="/WEB-INF/includes/template.jsp">
    <jsp:param name="contentPage" value="/WEB-INF/views/empleados/list_content.jsp" />
</jsp:include>
