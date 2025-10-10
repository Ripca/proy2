#pragma once
#include <mysql.h>
#include <iostream>
using namespace std;
class ConexionBD{

private:
	MYSQL* conector;
	bool conexionAbierta;

public :
	ConexionBD() {
		conector = nullptr;
		conexionAbierta = false;
	}

	void abrir_conexion() {
		if (!conexionAbierta) {
			conector = mysql_init(0);
			if (conector) {
				conector = mysql_real_connect(conector, "localhost", "root", "admin", "puntoventa", 3306, NULL, 0);
				// Configurar el conjunto de caracteres para manejar tildes y caracteres especiales
				if (conector) {
					mysql_set_character_set(conector, "utf8mb4");
					conexionAbierta = true;
				}
			}
		}
	}

	MYSQL* getConector() {
		return conector;
	}

	void cerrar_conexion() {
		if (conector != nullptr && conexionAbierta) {
			mysql_close(conector);
			conector = nullptr;
			conexionAbierta = false;
		}
	}

	~ConexionBD() {
		cerrar_conexion();
	}
};

