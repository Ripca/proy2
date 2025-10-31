<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error 404 - Página no encontrada</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        .error-container {
            text-align: center;
            background: white;
            border-radius: 15px;
            padding: 60px 40px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            animation: slideIn 0.5s ease-out;
        }

        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .error-code {
            font-size: 120px;
            font-weight: 900;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin: 0;
            line-height: 1;
        }

        .error-icon {
            font-size: 80px;
            color: #667eea;
            margin-bottom: 20px;
            animation: bounce 2s infinite;
        }

        @keyframes bounce {
            0%, 100% {
                transform: translateY(0);
            }
            50% {
                transform: translateY(-20px);
            }
        }

        .error-title {
            font-size: 32px;
            font-weight: 700;
            color: #333;
            margin: 20px 0;
        }

        .error-message {
            font-size: 16px;
            color: #666;
            margin-bottom: 30px;
            line-height: 1.6;
        }

        .error-details {
            background: #f8f9fa;
            border-left: 4px solid #667eea;
            padding: 15px;
            margin-bottom: 30px;
            text-align: left;
            border-radius: 5px;
            font-size: 14px;
            color: #555;
        }

        .error-details strong {
            color: #333;
        }

        .btn-group-custom {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn-custom {
            padding: 12px 30px;
            font-size: 16px;
            font-weight: 600;
            border-radius: 8px;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary-custom {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
        }

        .btn-primary-custom:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            color: white;
            text-decoration: none;
        }

        .btn-secondary-custom {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary-custom:hover {
            background: #667eea;
            color: white;
            text-decoration: none;
            transform: translateY(-3px);
        }

        .footer-text {
            margin-top: 30px;
            font-size: 12px;
            color: #999;
        }

        .status-badge {
            display: inline-block;
            background: #ff6b6b;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <i class="fas fa-exclamation-triangle"></i>
        </div>

        <div class="status-badge">
            Error 404
        </div>

        <h1 class="error-code">404</h1>

        <h2 class="error-title">¡Página no encontrada!</h2>

        <p class="error-message">
            Lo sentimos, la página que estás buscando no existe o ha sido movida.
            <br>
            <small>La URL que ingresaste no es válida en nuestro sistema.</small>
        </p>

        <div class="error-details">
            <strong>Información del error:</strong>
            <br>
            Ruta solicitada: <code><%= request.getRequestURI() %></code>
            <br>
            Contexto: <code><%= request.getContextPath() %></code>
        </div>

        <div class="btn-group-custom">
            <a href="<%= request.getContextPath() %>/LoginServlet" class="btn-custom btn-primary-custom">
                <i class="fas fa-sign-in-alt"></i> Ir a Login
            </a>
            <a href="<%= request.getContextPath() %>/" class="btn-custom btn-secondary-custom">
                <i class="fas fa-home"></i> Ir al Inicio
            </a>
        </div>

        <div class="footer-text">
            Si crees que esto es un error, por favor contacta al administrador.
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

