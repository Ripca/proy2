<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Puesto" %>

<%
    List<Puesto> puestos = (List<Puesto>) request.getAttribute("puestos");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Puestos</h2>
        <p class="text-muted mb-0">Administra los puestos de trabajo</p>
    </div>
    <a href="PuestoServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Puesto
    </a>
</div>



<!-- Tabla de puestos -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Puestos
            <span class="badge bg-primary ms-2">
                <%= puestos != null ? puestos.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (puestos != null && !puestos.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Puesto</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (puestos != null && !puestos.isEmpty()) {
                            for (Puesto puesto : puestos) {
                    %>
                    <tr>
                        <td><%= puesto.getIdPuesto() %></td>
                        <td>
                            <strong><%= puesto.getPuesto() %></strong>
                        </td>
                        <td>
                            <a href="PuestoServlet?action=edit&id=<%= puesto.getIdPuesto() %>" class="btn btn-sm btn-warning">
                                <i class="fas fa-edit"></i> Editar
                            </a>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
        <%
            } else {
        %>
        <div class="text-center py-5">
            <i class="fas fa-briefcase fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay puestos registrados</h5>
            <p class="text-muted">Haz clic en "Nuevo Puesto" para agregar el primer puesto.</p>
            <a href="PuestoServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primer Puesto
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
