#pragma once
#include <iostream>
#include <mysql.h>
#include <string>
#include "ConexionBD.h"

using namespace std;

class Proveedor {
    // Atributos
private:
    int idProveedor = 0;
    string proveedor;
    string nit;
    string direccion;
    string telefono;

    // Constructor
public:
    Proveedor() {}
    Proveedor(int id, string prov, string n, string dir, string tel) {
        idProveedor = id;
        proveedor = prov;
        nit = n;
        direccion = dir;
        telefono = tel;
    }

    // Métodos get y set
    void setIdProveedor(int id) { idProveedor = id; }
    void setProveedor(string prov) { proveedor = prov; }
    void setNit(string n) { nit = n; }
    void setDireccion(string dir) { direccion = dir; }
    void setTelefono(string tel) { telefono = tel; }

    int getIdProveedor() { return idProveedor; }
    string getProveedor() { return proveedor; }
    string getNit() { return nit; }
    string getDireccion() { return direccion; }
    string getTelefono() { return telefono; }

    // Métodos CRUD
    void crear() {
        int q_estado = 0;
        ConexionBD cn = ConexionBD();
        cn.abrir_conexion();
        if (cn.getConector()) {
            // Modificamos la consulta para usar AUTO_INCREMENT
            string consulta = "INSERT INTO Proveedores(proveedor, nit, direccion, telefono) VALUES ('" + proveedor + "', '" + nit + "', '" + direccion + "', '" + telefono + "');";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Ingreso de Proveedor Exitoso..." << endl;
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
            string consulta = "SELECT * FROM Proveedores;";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                resultado = mysql_store_result(cn.getConector());
                cout << "------------------------- PROVEEDORES -------------------------" << endl;
                cout << "ID | PROVEEDOR | NIT | DIRECCIÓN | TELÉFONO" << endl;
                cout << "-------------------------------------------------------------" << endl;
                while (fila = mysql_fetch_row(resultado)) {
                    cout << fila[0] << " | " << fila[1] << " | " << fila[2] << " | " << fila[3] << " | " << fila[4] << endl;
                }
                cout << "-------------------------------------------------------------" << endl;
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
            string id = to_string(idProveedor);
            string consulta = "UPDATE Proveedores SET proveedor = '" + proveedor + "', nit = '" + nit + "', direccion = '" + direccion + "', telefono = '" + telefono + "' WHERE idProveedor = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Actualización de Proveedor Exitosa..." << endl;
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
            string id = to_string(idProveedor);
            string consulta = "DELETE FROM Proveedores WHERE idProveedor = " + id + ";";
            const char* c = consulta.c_str();
            q_estado = mysql_query(cn.getConector(), c);
            if (!q_estado) {
                cout << "Eliminación de Proveedor Exitosa..." << endl;
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
