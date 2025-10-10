#include <iostream>
#include <string>
#include <iomanip>
#include <sstream>

using namespace std;

// Función para formatear números como moneda con comas
string formatearMoneda(double cantidad) {
    // Convertir a string con 2 decimales
    stringstream ss;
    ss << fixed << setprecision(2) << cantidad;
    string numero = ss.str();
    
    // Encontrar la posición del punto decimal
    size_t puntoDecimal = numero.find('.');
    string parteEntera = numero.substr(0, puntoDecimal);
    string parteDecimal = numero.substr(puntoDecimal);
    
    // Agregar comas a la parte entera (de derecha a izquierda)
    string resultado = "";
    int contador = 0;
    
    for (int i = parteEntera.length() - 1; i >= 0; i--) {
        if (contador > 0 && contador % 3 == 0) {
            resultado = "," + resultado;
        }
        resultado = parteEntera[i] + resultado;
        contador++;
    }
    
    return "Q " + resultado + parteDecimal;
}

int main() {
    cout << "=== PRUEBA DE FORMATO DE MONEDA ===\n\n";
    
    // Pruebas con diferentes cantidades
    double cantidades[] = {2.50, 25.00, 125.00, 1500.00, 15000.00, 150000.00, 1500000.00, 25000000.00, 125000000.50};
    int numCantidades = sizeof(cantidades) / sizeof(cantidades[0]);
    
    for (int i = 0; i < numCantidades; i++) {
        cout << "Cantidad: " << cantidades[i] << " -> Formateado: " << formatearMoneda(cantidades[i]) << endl;
    }
    
    cout << "\n=== EJEMPLO DE FACTURA ===\n";
    cout << "Producto 1: 10 x " << formatearMoneda(2.50) << " = " << formatearMoneda(25.00) << endl;
    cout << "Producto 2: 5 x " << formatearMoneda(25000.00) << " = " << formatearMoneda(125000.00) << endl;
    cout << "Producto 3: 1 x " << formatearMoneda(25000000.00) << " = " << formatearMoneda(25000000.00) << endl;
    cout << "Total: " << formatearMoneda(25125025.00) << endl;
    
    return 0;
}
