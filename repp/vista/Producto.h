#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include "ConexionBD.h"

using namespace std;

class Producto {
    // Atributos
private:
    int idProducto = 0;
    string producto;
    short idMarca = 0;
    string descripcion;
    string imagen;
    double precio_costo = 0.0;
    double precio_venta = 0.0;
    int existencia = 0;
    string fecha_ingreso;

    // Constructor
public:
    Producto() {}
    Producto(int id, string prod, short idM, string desc, string img, double pc, double pv, int exist, string fecha) {
        idProducto = id;
        producto = prod;
        idMarca = idM;
        descripcion = desc;
        imagen = img;
        precio_costo = pc;
        precio_venta = pv;
        existencia = exist;
        fecha_ingreso = fecha;
    }

    // Métodos get y set
    void setIdProducto(int id) { idProducto = id; }
    void setProducto(string prod) { producto = prod; }
    void setIdMarca(short idM) { idMarca = idM; }
    void setDescripcion(string desc) { descripcion = desc; }
    void setImagen(string img) { imagen = img; }
    void setPrecioCosto(double pc) { precio_costo = pc; }
    void setPrecioVenta(double pv) { precio_venta = pv; }
    void setExistencia(int exist) { existencia = exist; }
    void setFechaIngreso(string fecha) { fecha_ingreso = fecha; }

    int getIdProducto() { return idProducto; }
    string getProducto() { return producto; }
    short getIdMarca() { return idMarca; }
    string getDescripcion() { return descripcion; }
    string getImagen() { return imagen; }
    double getPrecioCosto() { return precio_costo; }
    double getPrecioVenta() { return precio_venta; }
    int getExistencia() { return existencia; }
    string getFechaIngreso() { return fecha_ingreso; }

    // Métodos CRUD
    void crear() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string idM = to_string(idMarca);
            string pc = to_string(precio_costo);
            string pv = to_string(precio_venta);
            string exist = to_string(existencia);

            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Productos(producto, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, fecha_ingreso) VALUES ('" + producto + "', " + idM + ", '" + descripcion + "', '" + imagen + "', " + pc + ", " + pv + ", " + exist + ", '" + fecha_ingreso + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Producto Exitoso..." << endl;
            }
            else {
                cout << "xxx Error al ingresar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void leer() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        MYSQL_ROW fila;
        MYSQL_RES* resultado;
        cn.abrir_conexion();
        if (cn.getConector()) {
            string consulta = "SELECT p.idProducto, p.producto, m.marca, p.descripcion, p.imagen, p.precio_costo, p.precio_venta, p.existencia, p.fecha_ingreso FROM Productos AS p INNER JOIN Marcas AS m ON p.idMarca = m.idmarca;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "---------------------------------------- PRODUCTOS ----------------------------------------" << endl;
                cout << "ID | PRODUCTO | MARCA | DESCRIPCIÓN | IMAGEN | COSTO | VENTA | EXISTENCIA | FECHA INGRESO" << endl;
                cout << "--------------------------------------------------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << " | " << fila[5] << " | " << fila[6] << " | " << fila[7] << " | " << fila[8] << endl;
                }
                cout << "--------------------------------------------------------------------------------------" << endl;
            }
            else {
                cout << "xxx Error al consultar información xxx" << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void actualizar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idProducto);
            string idM = to_string(idMarca);
            string pc = to_string(precio_costo);
            string pv = to_string(precio_venta);
            string exist = to_string(existencia);

            string consulta = "UPDATE Productos SET producto = '" + producto + "', idMarca = " + idM + ", descripcion = '" + descripcion + "', imagen = '" + imagen + "', precio_costo = " + pc + ", precio_venta = " + pv + ", existencia = " + exist + ", fecha_ingreso = '" + fecha_ingreso + "' WHERE idProducto = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Producto Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al actualizar información xxx" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }

    void eliminar() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            string id = to_string(idProducto);
            string consulta = "DELETE FROM Productos WHERE idProducto = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Eliminación de Producto Exitosa..." << endl;
            }
            else {
                cout << "xxx Error al eliminar información xxx" << endl;
                cout << "Puede que este registro esté siendo utilizado en otra tabla" << endl;
                cout << consulta << endl;
            }
        }
        else {
            cout << "xxx Error en la conexión xxx" << endl;
        }
        cn.cerrar_conexion();
    }
};
