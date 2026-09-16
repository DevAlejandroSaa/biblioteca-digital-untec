<!-- Debe mostrar:

* Título de la aplicación: Biblioteca Digital UNTEC.
* Campo para ingresar el usuario.
* Campo para ingresar la contraseña.
* Botón para iniciar sesión.
* Área para mostrar mensajes de error de autenticación.

Cómo debe mostrarlo:

* Formulario centrado.
* Campos claramente identificados.
* Mensajes de error visibles dentro del formulario.
* Interfaz simple y clara.

Con qué debe mostrarlo:

* HTML5.
* CSS3.
* Bootstrap 5 para el formulario, botones y distribución. -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="card shadow-lg border-0" style="max-width: 440px; width: 100%; border-radius: 1rem;">
            <div class="card-body p-4 p-md-5">
                <!-- Título e Identidad de la Aplicación -->
                <div class="text-center mb-4">
                    <div class="mb-3 text-primary">
                        <i class="fa-solid fa-book-open-reader display-4"></i>
                    </div>
                    <h3 class="fw-bold text-dark mb-1">Biblioteca Digital UNTEC</h3>
                    <p class="text-muted small mb-0">Sistema de Gestión de Préstamos y Catálogo</p>
                </div>

                <!-- Área para mostrar mensajes de error de autenticación (dentro del formulario) -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show d-flex align-items-center gap-2 py-2 px-3 small mb-4"
                        role="alert" id="loginAlertError">
                        <i class="fa-solid fa-circle-exclamation flex-shrink-0"></i>
                        <div>
                            <c:out value="${error}" />
                        </div>
                        <button type="button" class="btn-close ms-auto p-2" data-bs-dismiss="alert"
                            aria-label="Cerrar"></button>
                    </div>
                </c:if>

                <!-- Formulario de Inicio de Sesión (sin autollenado ni accesos rápidos) -->
                <form action="${pageContext.request.contextPath}/login" method="POST" id="formLogin" autocomplete="off">
                    <!-- Campo Usuario -->
                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold text-secondary small">
                            <i class="fa-solid fa-user me-1 text-primary"></i> Nombre de Usuario
                        </label>
                        <div class="input-group">
                            <span class="input-group-text bg-light text-muted border-end-0">
                                <i class="fa-solid fa-id-badge"></i>
                            </span>
                            <input type="text" class="form-control border-start-0 ps-0" id="username" name="username"
                                placeholder="Ingrese su nombre de usuario" required autofocus autocomplete="off">
                        </div>
                    </div>

                    <!-- Campo Contraseña -->
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold text-secondary small">
                            <i class="fa-solid fa-lock me-1 text-primary"></i> Contraseña
                        </label>
                        <div class="input-group">
                            <span class="input-group-text bg-light text-muted border-end-0">
                                <i class="fa-solid fa-key"></i>
                            </span>
                            <input type="password" class="form-control border-start-0 ps-0" id="password"
                                name="password" placeholder="Ingrese su contraseña" required
                                autocomplete="new-password">
                        </div>
                    </div>

                    <!-- Botón para iniciar sesión -->
                    <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold shadow-sm"
                        id="btnIniciarSesion">
                        <i class="fa-solid fa-arrow-right-to-bracket me-2"></i> Iniciar Sesión
                    </button>
                </form>
            </div>
        </div>