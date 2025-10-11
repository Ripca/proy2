<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Usuario" %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    boolean esEdicion = usuario != null;
    String titulo = esEdicion ? "Editar Usuario" : "Nuevo Usuario";
    String action = esEdicion ? "update" : "save";
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0"><%= titulo %></h2>
        <p class="text-muted mb-0">Complete la información del usuario del sistema</p>
    </div>
    <a href="UsuarioServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-user-cog"></i> Información del Usuario
                </h5>
            </div>
            <div class="card-body">
                <form action="UsuarioServlet" method="post" id="formUsuario">
                    <input type="hidden" name="action" value="<%= action %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>">
                    <% } %>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="usuario" class="form-label">
                                    Nombre de Usuario <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="usuario" name="usuario" 
                                       value="<%= esEdicion ? usuario.getUsuario() : "" %>" 
                                       required maxlength="50" placeholder="Ej: admin, jperez...">
                                <div class="form-text">Nombre único para iniciar sesión</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="rol" class="form-label">
                                    Rol <span class="text-danger">*</span>
                                </label>
                                <select class="form-select" id="rol" name="rol" required>
                                    <option value="">Seleccionar rol...</option>
                                    <option value="Administrador" <%= esEdicion && "Administrador".equals(usuario.getRol()) ? "selected" : "" %>>
                                        Administrador
                                    </option>
                                    <option value="Usuario" <%= esEdicion && "Usuario".equals(usuario.getRol()) ? "selected" : "" %>>
                                        Usuario
                                    </option>
                                    <option value="Vendedor" <%= esEdicion && "Vendedor".equals(usuario.getRol()) ? "selected" : "" %>>
                                        Vendedor
                                    </option>
                                    <option value="Supervisor" <%= esEdicion && "Supervisor".equals(usuario.getRol()) ? "selected" : "" %>>
                                        Supervisor
                                    </option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <% if (!esEdicion) { %>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="password" class="form-label">
                                    Contraseña <span class="text-danger">*</span>
                                </label>
                                <input type="password" class="form-control" id="password" name="password" 
                                       required minlength="6" placeholder="Mínimo 6 caracteres">
                                <div class="form-text">Contraseña para acceder al sistema</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="confirmarPassword" class="form-label">
                                    Confirmar Contraseña <span class="text-danger">*</span>
                                </label>
                                <input type="password" class="form-control" id="confirmarPassword" name="confirmarPassword" 
                                       required minlength="6" placeholder="Repetir contraseña">
                                <div class="form-text">Debe coincidir con la contraseña anterior</div>
                            </div>
                        </div>
                    </div>
                    <% } %>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="nombres" class="form-label">
                                    Nombres <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="nombres" name="nombres" 
                                       value="<%= esEdicion ? usuario.getNombres() : "" %>" 
                                       required maxlength="100" placeholder="Nombres del usuario">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="apellidos" class="form-label">
                                    Apellidos <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="apellidos" name="apellidos" 
                                       value="<%= esEdicion ? usuario.getApellidos() : "" %>" 
                                       required maxlength="100" placeholder="Apellidos del usuario">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="<%= esEdicion && usuario.getEmail() != null ? usuario.getEmail() : "" %>" 
                                       maxlength="100" placeholder="correo@ejemplo.com">
                                <div class="form-text">Correo electrónico del usuario</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="activo" class="form-label">Estado</label>
                                <select class="form-select" id="activo" name="activo">
                                    <option value="true" <%= !esEdicion || usuario.isActivo() ? "selected" : "" %>>
                                        Activo
                                    </option>
                                    <option value="false" <%= esEdicion && !usuario.isActivo() ? "selected" : "" %>>
                                        Inactivo
                                    </option>
                                </select>
                                <div class="form-text">Estado del usuario en el sistema</div>
                            </div>
                        </div>
                    </div>

                    <% if (esEdicion && usuario.getFechaCreacion() != null) { %>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label class="form-label">Fecha de Creación</label>
                                <input type="text" class="form-control" 
                                       value="<%= usuario.getFechaCreacion().toString() %>" readonly>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label class="form-label">Último Acceso</label>
                                <input type="text" class="form-control" 
                                       value="<%= usuario.getFechaUltimoAcceso() != null ? usuario.getFechaUltimoAcceso().toString() : "Nunca" %>" readonly>
                            </div>
                        </div>
                    </div>
                    <% } %>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="UsuarioServlet" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> <%= esEdicion ? "Actualizar" : "Guardar" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('formUsuario').addEventListener('submit', function(e) {
        <% if (!esEdicion) { %>
        // Validar que las contraseñas coincidan solo en creación
        const password = document.getElementById('password').value;
        const confirmarPassword = document.getElementById('confirmarPassword').value;
        
        if (password !== confirmarPassword) {
            e.preventDefault();
            showAlert('error', 'Error', 'Las contraseñas no coinciden.');
            return false;
        }
        <% } %>
        
        // Validar campos requeridos
        const usuario = document.getElementById('usuario').value.trim();
        const nombres = document.getElementById('nombres').value.trim();
        const apellidos = document.getElementById('apellidos').value.trim();
        const rol = document.getElementById('rol').value;
        
        if (!usuario || !nombres || !apellidos || !rol) {
            e.preventDefault();
            showAlert('error', 'Error', 'Por favor complete todos los campos obligatorios.');
            return false;
        }
    });
</script>
