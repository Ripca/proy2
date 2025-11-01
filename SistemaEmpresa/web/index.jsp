<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema Empresa - Iniciar Sesión</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 6 -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 20px;
        }

        .login-container {
            width: 100%;
            max-width: 500px;
        }

        .login-card {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .login-header {
            background-color: #343a40;
            color: white;
            padding: 30px 20px;
            text-align: center;
            border-bottom: 3px solid #007bff;
        }

        .login-header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .login-header p {
            font-size: 0.9rem;
            opacity: 0.9;
            margin: 0;
        }

        .login-body {
            padding: 30px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
            font-size: 0.95rem;
        }

        .form-control {
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 10px 12px;
            font-size: 0.95rem;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.15);
            outline: none;
        }

        .form-control::placeholder {
            color: #999;
        }

        .btn-login {
            background-color: #007bff;
            border: none;
            border-radius: 4px;
            padding: 10px 20px;
            font-weight: 600;
            font-size: 1rem;
            color: white;
            width: 100%;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .btn-login:hover {
            background-color: #0056b3;
            color: white;
        }

        .btn-login:active {
            background-color: #004085;
        }

        .error-message {
            color: #721c24;
            font-size: 0.95rem;
            margin-bottom: 20px;
            padding: 14px 16px;
            background: #f8d7da;
            border-radius: 4px;
            border-left: 4px solid #dc3545;
            animation: slideDown 0.3s ease-out;
            display: flex;
            align-items: center;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .login-footer {
            text-align: center;
            padding: 15px 30px;
            background: #f8f9fa;
            color: #666;
            font-size: 0.85rem;
            border-top: 1px solid #ddd;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <!-- Header -->
            <div class="login-header">
                <h1><i class="fas fa-building me-2"></i>Sistema Empresa</h1>
                <p>Gestión Empresarial Integral</p>
            </div>

            <!-- Body -->
            <div class="login-body">
                <!-- Mostrar mensajes de error si existen -->
                <%
                    String error = request.getParameter("error");
                    if (error != null && !error.isEmpty()) {
                %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle me-2"></i><%= error %>
                    </div>
                <% } %>

                <form action="LoginServlet" method="post" id="loginForm">
                    <div class="form-group">
                        <label for="usuario" class="form-label">Usuario</label>
                        <input type="text" class="form-control" id="usuario" name="usuario"
                               placeholder="Ingrese su usuario" required autofocus>
                    </div>

                    <div class="form-group">
                        <label for="password" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="Ingrese su contraseña" required>
                    </div>

                    <button type="submit" class="btn btn-login">
                        <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                    </button>
                </form>
            </div>

            <!-- Footer -->
            <div class="login-footer">
                <p>&copy; 2024 Sistema Empresa. Todos los derechos reservados.</p>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Validación del formulario
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const usuario = document.getElementById('usuario').value.trim();
            const password = document.getElementById('password').value.trim();

            if (!usuario || !password) {
                e.preventDefault();
                alert('Por favor complete todos los campos');
            }
        });

        // Hacer desaparecer el mensaje de error después de 5 segundos
        const errorMessage = document.querySelector('.error-message');
        if (errorMessage) {
            setTimeout(() => {
                errorMessage.style.animation = 'slideUp 0.3s ease-out forwards';
            }, 5000);
        }
    </script>

    <style>
        @keyframes slideUp {
            from {
                opacity: 1;
                transform: translateY(0);
            }
            to {
                opacity: 0;
                transform: translateY(-10px);
            }
        }
    </style>
</body>
</html>

