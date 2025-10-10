<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sistemaempresa.models.Usuario" %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
%>

<!-- Header de la página -->
<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h2 class="mb-0">Cambiar Contraseña</h2>
        <p class="text-muted mb-0">Actualizar contraseña para: <strong><%= usuario.getNombreCompleto() %></strong></p>
    </div>
    <a href="UsuarioServlet" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Volver a la Lista
    </a>
</div>

<!-- Formulario -->
<div class="row justify-content-center">
    <div class="col-lg-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">
                    <i class="fas fa-key"></i> Nueva Contraseña
                </h5>
            </div>
            <div class="card-body">
                <form action="UsuarioServlet" method="post" id="formPassword">
                    <input type="hidden" name="action" value="updatePassword">
                    <input type="hidden" name="idUsuario" value="<%= usuario.getIdUsuario() %>">

                    <div class="mb-3">
                        <label for="usuario" class="form-label">Usuario</label>
                        <input type="text" class="form-control" id="usuario" 
                               value="<%= usuario.getUsuario() %>" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="nuevaPassword" class="form-label">
                            Nueva Contraseña <span class="text-danger">*</span>
                        </label>
                        <input type="password" class="form-control" id="nuevaPassword" name="nuevaPassword" 
                               required minlength="6" placeholder="Mínimo 6 caracteres">
                        <div class="form-text">Ingrese la nueva contraseña</div>
                    </div>

                    <div class="mb-3">
                        <label for="confirmarPassword" class="form-label">
                            Confirmar Nueva Contraseña <span class="text-danger">*</span>
                        </label>
                        <input type="password" class="form-control" id="confirmarPassword" name="confirmarPassword" 
                               required minlength="6" placeholder="Repetir nueva contraseña">
                        <div class="form-text">Debe coincidir con la nueva contraseña</div>
                    </div>

                    <!-- Botones -->
                    <div class="d-flex justify-content-end gap-2 mt-4">
                        <a href="UsuarioServlet" class="btn btn-secondary">
                            <i class="fas fa-times"></i> Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-key"></i> Cambiar Contraseña
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('formPassword').addEventListener('submit', function(e) {
        const nuevaPassword = document.getElementById('nuevaPassword').value;
        const confirmarPassword = document.getElementById('confirmarPassword').value;
        
        if (nuevaPassword !== confirmarPassword) {
            e.preventDefault();
            showAlert('error', 'Error', 'Las contraseñas no coinciden.');
            return false;
        }
        
        if (nuevaPassword.length < 6) {
            e.preventDefault();
            showAlert('error', 'Error', 'La contraseña debe tener al menos 6 caracteres.');
            return false;
        }
    });
</script>
