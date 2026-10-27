# EXAMEN-U2-LEE-ORBEZO - AgroAPI

Backend Java/Spring Boot del caso AgroSierra. Expone un API REST versionado, protege las rutas con JWT y privilegios, aplica las reglas de negocio y persiste en Oracle mediante JPA y Flyway.

## Requisitos y ejecución

- Java 21.
- Oracle en `localhost:1522/FREEPDB1`.
- Esquema por defecto: `PHARMADB`.
- Contraseña local por defecto: `1234567`.

Se pueden usar las variables `AGRO_DB_USERNAME`, `AGRO_DB_PASSWORD` y `AGRO_JWT_SECRET` sin modificar el repositorio.

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

El servicio queda en `http://localhost:8080`. Flyway crea y actualiza el esquema desde `src/main/resources/db/agro-migration`.

## Usuarios semilla

| Usuario | Nombre visible | Contraseña | Rol |
|---|---|---|---|
| tecnico1 | Lee Orbezo | Agro2026* | TECNICO |
| jefe1 | Pool Orbezo | Agro2026* | JEFE_CAMPO |
| admin | Sonia Orbezo | Agro2026* | ADMINISTRADOR |
| supervisor1 | Henry Orbezo | Agro2026* | SUPERVISOR_CALIDAD |

Las contraseñas se guardan con BCrypt. El JWT dura 60 minutos y transporta el ID del usuario y sus privilegios.

## Contrato REST

- POST `/api/v1/auth/login`
- GET `/api/v1/usuarios/me`
- GET `/api/v1/lotes` y `/api/v1/lotes/{id}`
- GET `/api/v1/lotes/{id}/labores`
- POST y PUT `/api/v1/labores`
- DELETE `/api/v1/labores/{id}`
- POST `/api/v1/lotes/{id}/cerrar-campania`

Los errores siguen un contrato con `status`, `message`, `path` y `validationErrors`. Se distinguen 400, 401, 403, 404, 409 y 500.

## Roles y privilegios

- TECNICO: lectura, registro y corrección de labores propias durante 24 horas.
- JEFE_CAMPO: lectura, registro, edición y eliminación; además cierra campañas.
- ADMINISTRADOR: lectura y gestión de usuarios.
- SUPERVISOR_CALIDAD: lectura y edición de cualquier labor, sin cierre de campaña.

La autorización se configura por privilegio en `security/PermisosPorRol.java` y se aplica en `SecurityConfig`. Los controladores no comparan nombres de rol.

## Reglas de negocio

- RN-01: no se registra ni corrige una labor en una campaña cerrada.
- RN-02: el técnico corrige solamente sus labores durante las primeras 24 horas.
- RN-03: se exige una labor COSECHA antes de cerrar la campaña.
- RN-04: una fumigación no supera dosis máxima por hectárea multiplicada por hectáreas.

## Arquitectura

- `config`: seguridad, CORS y semillas.
- `controller`: rutas REST.
- `dto` y `dto/auth`: contrato externo y autenticación.
- `entity`: entidades JPA.
- `exception`: errores uniformes.
- `mapper`: conversión entidad a DTO.
- `repository`: acceso JPA.
- `security`: JWT, filtro y privilegios.
- `service/service`: contratos.
- `service/impl`: transacciones y reglas.

## Solicitudes de cambio

- SC-A se resuelve en Android mediante un filtro local observable.
- SC-B incorpora SUPERVISOR_CALIDAD sin condicionales de rol en controladores o filtro JWT.
- SC-C agrega `cantidadJornales` a entidad, migración, DTO, servicio y semillas.
- SC-D se resuelve en Android desde SQLDelight.

La colección `postman/AgroAPI_EXAMEN_U2.postman_collection.json` incluye autenticación, CRUD, errores, supervisor y jornales.

## Trazabilidad con la rúbrica

| Criterio | Evidencia en el código |
|---|---|
| REST y paginación | `controller/LoteController.java`, `controller/LaborController.java` y `dto/PaginaResponseDTO.java` |
| CRUD completo | `LaborController.java`, `LaborServiceImpl.java` y `LaborRepository.java` |
| Errores HTTP | `GlobalExceptionHandler.java`, `RestAuthenticationEntryPoint.java` y `RestAccessDeniedHandler.java` |
| JWT | `JwtService.java`, `JwtAuthenticationFilter.java` y `SecurityConfig.java` |
| Roles y privilegios | `PermisosPorRol.java`; los controladores no comparan roles |
| Reglas RN-01 a RN-04 | `LaborServiceImpl.java` y `LoteServiceImpl.java` |
| Persistencia y semillas | entidades JPA, repositorios, Flyway y `DatosSemillaConfig.java` |
| SC-B | SUPERVISOR_CALIDAD definido en `PermisosPorRol.java` y semillas |
| SC-C | `V2__solicitudes_cambio.sql`, entidad, DTO, mapper, servicio y semillas |

Los resultados de ejecución, historial Git, solicitudes de incorporación y etiqueta se demuestran externamente; no se declaran como aprobados únicamente por existir el código.
