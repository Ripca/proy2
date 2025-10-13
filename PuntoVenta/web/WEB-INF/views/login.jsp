<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - PuntoVenta</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <!-- SweetAlert2 -->
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        
        .login-container {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
            padding: 2rem;
            width: 100%;
            max-width: 400px;
        }
        
        .login-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        
        .login-header h1 {
            color: #333;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }
        
        .login-header p {
            color: #666;
            margin: 0;
        }
        
        .form-floating {
            margin-bottom: 1rem;
        }
        
        .form-control {
            border: 2px solid #e1e5e9;
            border-radius: 10px;
            padding: 0.75rem 1rem;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .btn-login {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 0.75rem 2rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
            width: 100%;
        }
        
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .form-check {
            margin: 1rem 0;
        }
        
        .form-check-input:checked {
            background-color: #667eea;
            border-color: #667eea;
        }
        
        .loading {
            display: none;
        }
        
        .loading .spinner-border {
            width: 1rem;
            height: 1rem;
        }
        
        .logo {
            width: 80px;
            height: 80px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            color: white;
            font-size: 2rem;
        }
        
        .footer-text {
            text-align: center;
            margin-top: 2rem;
            color: #666;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-header">
            <div class="logo">
                <i class="fas fa-cash-register"></i>
            </div>
            <h1>PuntoVenta</h1>
            <p>Sistema de Gestión Comercial</p>
        </div>
        
        <form id="loginForm" method="post" action="${pageContext.request.contextPath}/login">
            <input type="hidden" name="action" value="login">
            
            <div class="form-floating">
                <input type="text" class="form-control" id="usuario" name="usuario" 
                       placeholder="Usuario" required autocomplete="username">
                <label for="usuario">
                    <i class="fas fa-user me-2"></i>Usuario
                </label>
            </div>
            
            <div class="form-floating">
                <input type="password" class="form-control" id="password" name="password" 
                       placeholder="Contraseña" required autocomplete="current-password">
                <label for="password">
                    <i class="fas fa-lock me-2"></i>Contraseña
                </label>
            </div>
            
            <div class="form-check">
                <input class="form-check-input" type="checkbox" id="recordarme" name="recordarme">
                <label class="form-check-label" for="recordarme">
                    Recordarme por 7 días
                </label>
            </div>
            
            <button type="submit" class="btn btn-primary btn-login">
                <span class="btn-text">
                    <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                </span>
                <span class="loading">
                    <span class="spinner-border spinner-border-sm me-2" role="status"></span>
                    Iniciando...
                </span>
            </button>
        </form>
        
        <div class="footer-text">
            <p>&copy; 2024 PuntoVenta. Todos los derechos reservados.</p>
            <p><small>Versión 1.0.0</small></p>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
    <script>
        $(document).ready(function() {
            // Enfocar el campo de usuario al cargar
            $('#usuario').focus();
            
            // Manejar envío del formulario
            $('#loginForm').on('submit', function(e) {
                e.preventDefault();
                
                const form = $(this);
                const btnText = $('.btn-text');
                const loading = $('.loading');
                const submitBtn = form.find('button[type="submit"]');
                
                // Validar campos
                const usuario = $('#usuario').val().trim();
                const password = $('#password').val();
                
                if (!usuario || !password) {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Campos requeridos',
                        text: 'Por favor ingrese usuario y contraseña',
                        confirmButtonColor: '#667eea'
                    });
                    return;
                }
                
                // Mostrar loading
                btnText.hide();
                loading.show();
                submitBtn.prop('disabled', true);
                
                // Enviar petición AJAX
                $.ajax({
                    url: form.attr('action'),
                    method: 'POST',
                    data: form.serialize(),
                    dataType: 'json',
                    timeout: 10000
                })
                .done(function(response) {
                    if (response.success) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Bienvenido!',
                            text: response.message,
                            timer: 1500,
                            showConfirmButton: false,
                            confirmButtonColor: '#667eea'
                        }).then(() => {
                            window.location.href = response.redirect || '${pageContext.request.contextPath}/dashboard';
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error de autenticación',
                            text: response.message,
                            confirmButtonColor: '#667eea'
                        });
                    }
                })
                .fail(function(xhr, status, error) {
                    let message = 'Error de conexión. Intente nuevamente.';
                    
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        message = xhr.responseJSON.message;
                    } else if (status === 'timeout') {
                        message = 'La petición ha tardado demasiado. Verifique su conexión.';
                    }
                    
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: message,
                        confirmButtonColor: '#667eea'
                    });
                })
                .always(function() {
                    // Ocultar loading
                    loading.hide();
                    btnText.show();
                    submitBtn.prop('disabled', false);
                });
            });
            
            // Permitir login con Enter
            $('#password').on('keypress', function(e) {
                if (e.which === 13) {
                    $('#loginForm').submit();
                }
            });
            
            // Animación de entrada
            $('.login-container').hide().fadeIn(800);
        });
        
        // Usuarios de prueba (solo para desarrollo)
        function fillTestUser(role) {
            const users = {
                admin: { usuario: 'admin', password: 'admin123' },
                supervisor: { usuario: 'supervisor', password: 'super123' },
                vendedor: { usuario: 'vendedor1', password: 'vend123' },
                cajero: { usuario: 'cajero1', password: 'caj123' }
            };
            
            if (users[role]) {
                $('#usuario').val(users[role].usuario);
                $('#password').val(users[role].password);
            }
        }
        
        // Agregar botones de prueba en desarrollo
        if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
            $(document).ready(function() {
                const testButtons = `
                    <div class="mt-3 text-center">
                        <small class="text-muted d-block mb-2">Usuarios de prueba:</small>
                        <div class="btn-group-vertical w-100" role="group">
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="fillTestUser('admin')">Admin</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="fillTestUser('supervisor')">Supervisor</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="fillTestUser('vendedor')">Vendedor</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="fillTestUser('cajero')">Cajero</button>
                        </div>
                    </div>
                `;
                $('.footer-text').before(testButtons);
            });
        }
    </script>
</body>
</html>
