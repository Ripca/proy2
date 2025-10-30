/**
 * Token Validator - Valida el token JWT del usuario
 * Permite que el usuario acceda a la aplicación desde múltiples pestañas
 * usando el mismo token
 */

class TokenValidator {
    constructor() {
        this.validationInterval = 60000; // Validar cada 60 segundos
        this.isValidating = false;
    }
    
    /**
     * Inicia la validación automática del token
     */
    startValidation() {
        // Validar al cargar la página
        this.validateToken();
        
        // Validar periódicamente
        setInterval(() => {
            this.validateToken();
        }, this.validationInterval);
        
        // Validar cuando la ventana recupera el foco
        window.addEventListener('focus', () => {
            this.validateToken();
        });
    }
    
    /**
     * Valida el token actual
     */
    validateToken() {
        if (this.isValidating) return;
        
        this.isValidating = true;
        
        fetch('ValidateTokenServlet')
            .then(response => response.json())
            .then(data => {
                if (!data.valid) {
                    // Token inválido, redirigir al login
                    console.warn('Token inválido o expirado');
                    this.redirectToLogin();
                } else {
                    console.log('Token válido:', data.usuario);
                }
                this.isValidating = false;
            })
            .catch(error => {
                console.error('Error validando token:', error);
                this.isValidating = false;
            });
    }
    
    /**
     * Redirige al login
     */
    redirectToLogin() {
        const contextPath = this.getContextPath();
        window.location.href = contextPath + '/index.jsp?session=expired';
    }
    
    /**
     * Obtiene el contexto de la aplicación
     */
    getContextPath() {
        const path = window.location.pathname;
        const parts = path.split('/');
        
        // Buscar el contexto (generalmente es /SistemaEmpresa)
        if (parts.length > 1 && parts[1]) {
            return '/' + parts[1];
        }
        
        return '';
    }
    
    /**
     * Obtiene el token de la cookie
     */
    getTokenFromCookie() {
        const name = 'authToken=';
        const decodedCookie = decodeURIComponent(document.cookie);
        const cookieArray = decodedCookie.split(';');
        
        for (let cookie of cookieArray) {
            cookie = cookie.trim();
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length);
            }
        }
        
        return null;
    }
    
    /**
     * Verifica si el usuario está autenticado
     */
    isAuthenticated() {
        return this.getTokenFromCookie() !== null;
    }
}

// Inicializar validador cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    const validator = new TokenValidator();
    validator.startValidation();
    
    // Hacer disponible globalmente
    window.tokenValidator = validator;
});

