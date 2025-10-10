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

<!-- Formulario de búsqueda -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="PuestoServlet" class="row g-3">
            <input type="hidden" name="action" value="search">
            <div class="col-md-8">
                <input type="text" class="form-control" name="termino" 
                       placeholder="Buscar por nombre del puesto..." 
                       value="<%= request.getAttribute("termino") != null ? request.getAttribute("termino") : "" %>">
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-search"></i> Buscar
                </button>
                <a href="PuestoServlet" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Limpiar
                </a>
            </div>
        </form>
    </div>
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
                        <th>Fecha Creación</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Puesto puesto : puestos) {
                    %>
                    <tr>
                        <td><%= puesto.getIdPuesto() %></td>
                        <td>
                            <strong><%= puesto.getPuesto() %></strong>
                        </td>
                        <td><%= puesto.getFechaIngreso() != null ? puesto.getFechaIngreso().toString() : "N/A" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="PuestoServlet?action=edit&id=<%= puesto.getIdPuesto() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('PuestoServlet?action=delete&id=<%= puesto.getIdPuesto() %>', 
                                        '¿Está seguro que desea eliminar el puesto <%= puesto.getPuesto() %>?')" 
                                        title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <%
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
