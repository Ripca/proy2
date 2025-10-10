<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sistemaempresa.models.Empleado" %>

<%
    List<Empleado> empleados = (List<Empleado>) request.getAttribute("empleados");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Gestión de Empleados</h2>
        <p class="text-muted mb-0">Administra la información de todos los empleados</p>
    </div>
    <a href="EmpleadoServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Empleado
    </a>
</div>



<!-- Tabla de empleados -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">
            <i class="fas fa-list"></i> Lista de Empleados
            <span class="badge bg-primary ms-2">
                <%= empleados != null ? empleados.size() : 0 %> registros
            </span>
        </h5>
    </div>
    <div class="card-body">
        <%
            if (empleados != null && !empleados.isEmpty()) {
        %>
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Empleado</th>
                        <th>DPI</th>
                        <th>Puesto</th>
                        <th>Teléfono</th>
                        <th>Fecha Ingreso</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Empleado empleado : empleados) {
                    %>
                    <tr>
                        <td><%= empleado.getIdEmpleado() %></td>
                        <td>
                            <strong><%= empleado.getNombres() %> <%= empleado.getApellidos() %></strong>
                        </td>
                        <td><%= empleado.getDpi() != null ? empleado.getDpi() : "N/A" %></td>
                        <td><%= empleado.getNombrePuesto() != null ? empleado.getNombrePuesto() : "Sin asignar" %></td>
                        <td><%= empleado.getTelefono() != null ? empleado.getTelefono() : "N/A" %></td>
                        <td><%= empleado.getFechaIngreso() != null ? empleado.getFechaIngreso().toString() : "N/A" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="EmpleadoServlet?action=edit&id=<%= empleado.getIdEmpleado() %>" 
                                   class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <button type="button" class="btn btn-danger btn-sm" 
                                        onclick="confirmDelete('EmpleadoServlet?action=delete&id=<%= empleado.getIdEmpleado() %>', 
                                        '¿Está seguro que desea eliminar el empleado <%= empleado.getNombres() %> <%= empleado.getApellidos() %>?')" 
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
            <i class="fas fa-user-tie fa-3x text-muted mb-3"></i>
            <h5 class="text-muted">No hay empleados registrados</h5>
            <p class="text-muted">Haz clic en "Nuevo Empleado" para agregar el primer empleado.</p>
            <a href="EmpleadoServlet?action=new" class="btn btn-primary">
                <i class="fas fa-plus"></i> Crear Primer Empleado
            </a>
        </div>
        <%
            }
        %>
    </div>
</div>
