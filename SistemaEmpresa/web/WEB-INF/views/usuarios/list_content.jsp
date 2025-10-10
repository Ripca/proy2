<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Usuario" %>
<%@ page import="java.util.List" %>

<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Usuarios del Sistema</h2>
        <p class="text-muted mb-0">Gestión de usuarios y permisos del sistema</p>
    </div>
    <a href="UsuarioServlet?action=new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Nuevo Usuario
    </a>
</div>

<!-- Tabla de usuarios -->
<div class="card">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover data-table" id="tablaUsuarios">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Nombre Completo</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Fecha Creación</th>
                        <th>Último Acceso</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (usuarios != null && !usuarios.isEmpty()) {
                            for (Usuario usuario : usuarios) {
                    %>
                    <tr>
                        <td><%= usuario.getIdUsuario() %></td>
                        <td>
                            <strong><%= usuario.getUsuario() %></strong>
                        </td>
                        <td><%= usuario.getNombreCompleto() %></td>
                        <td><%= usuario.getEmail() != null ? usuario.getEmail() : "" %></td>
                        <td>
                            <span class="badge bg-<%= "Administrador".equals(usuario.getRol()) ? "danger" : "primary" %>">
                                <%= usuario.getRol() %>
                            </span>
                        </td>
                        <td>
                            <span class="badge bg-<%= usuario.isActivo() ? "success" : "secondary" %>">
                                <%= usuario.getEstadoTexto() %>
                            </span>
                        </td>
                        <td><%= usuario.getFechaCreacion() != null ? usuario.getFechaCreacion().toString() : "" %></td>
                        <td><%= usuario.getFechaUltimoAcceso() != null ? usuario.getFechaUltimoAcceso().toString() : "Nunca" %></td>
                        <td>
                            <div class="btn-group" role="group">
                                <a href="UsuarioServlet?action=edit&id=<%= usuario.getIdUsuario() %>" 
                                   class="btn btn-sm btn-outline-primary" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="UsuarioServlet?action=changePassword&id=<%= usuario.getIdUsuario() %>" 
                                   class="btn btn-sm btn-outline-warning" title="Cambiar Contraseña">
                                    <i class="fas fa-key"></i>
                                </a>
                                <button type="button" class="btn btn-sm btn-outline-danger" 
                                        onclick="confirmarEliminacion(<%= usuario.getIdUsuario() %>, '<%= usuario.getUsuario() %>')" 
                                        title="Eliminar">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="9" class="text-center text-muted">
                            <i class="fas fa-users-slash fa-2x mb-2"></i><br>
                            No hay usuarios registrados
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    function confirmarEliminacion(id, usuario) {
        Swal.fire({
            title: '¿Estás seguro?',
            text: `¿Deseas eliminar el usuario "${usuario}"? Esta acción no se puede deshacer.`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = `UsuarioServlet?action=delete&id=${id}`;
            }
        });
    }
</script>
