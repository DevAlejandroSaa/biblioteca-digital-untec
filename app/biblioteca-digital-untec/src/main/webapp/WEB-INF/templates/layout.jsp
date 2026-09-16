<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <title>
                <c:out value="${not empty tituloPagina ? tituloPagina : 'Biblioteca Digital UNTEC'}" />
            </title>

            <!-- Bootstrap 5.3.8 CSS -->
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css">

            <!-- Font Awesome Kit -->
            <script src="https://kit.fontawesome.com/afc3eba033.js" crossorigin="anonymous"></script>

            <!-- Estilos CSS3 Base -->
            <style>
                :root {
                    --untec-bg: #f4f6f9;
                }

                body {
                    background-color: var(--untec-bg);
                    min-height: 100vh;
                    display: flex;
                    flex-direction: column;
                    font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", sans-serif;
                }

                main {
                    flex: 1 0 auto;
                }

                .navbar-brand {
                    font-weight: 700;
                    letter-spacing: -0.5px;
                }

                .nav-tabs .nav-link {
                    font-weight: 500;
                    color: #495057;
                }

                .nav-tabs .nav-link.active {
                    font-weight: 700;
                    color: #0d6efd;
                    border-bottom: 3px solid #0d6efd;
                }

                .card {
                    border-radius: 0.5rem;
                    border: 1px solid #e2e8f0;
                }

                .table-responsive {
                    border-radius: 0.5rem;
                    overflow: hidden;
                }

                /* En pantallas pequeñas (móviles/tablets pequeñas), si es login, asegurar que el formulario ocupe el 100% y se oculte cualquier navbar residual */
                @media (max-width: 576px) {
                    .login-container {
                        padding: 1rem;
                    }

                    .navbar {
                        padding-left: 0.5rem;
                        padding-right: 0.5rem;
                    }
                }
            </style>
        </head>

        <body>

            <!-- Header / Navbar Superior Común (Solo si no es login / no se solicita ocultar) -->
            <c:if test="${empty ocultarNavbar && empty esLogin}">
                <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm mb-4" id="mainNavbar">
                    <div class="container-fluid px-4">
                        <a class="navbar-brand d-flex align-items-center gap-2"
                            href="${pageContext.request.contextPath}/libros">
                            <i class="fa-solid fa-book-bookmark text-primary fs-4"></i>
                            <span>Biblioteca Digital UNTEC</span>
                        </a>

                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false"
                            aria-label="Navegación">
                            <span class="navbar-toggler-icon"></span>
                        </button>

                        <div class="collapse navbar-collapse" id="navbarContent">
                            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                                <!-- Catálogo de libros: tanto personal como estudiante lo pueden ver -->
                                <li class="nav-item">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/libros" id="navLibros">
                                        <i class="fa-solid fa-book me-1"></i> Catálogo de Libros
                                    </a>
                                </li>

                                <!-- Estudiante: Solicitar libros -->
                                <c:if test="${sessionScope.rol == 'ESTUDIANTE'}">
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/estudiante"
                                            id="navSolicitarLibros">
                                            <i class="fa-solid fa-hand-holding-hand me-1"></i> Solicitar Libros
                                        </a>
                                    </li>
                                </c:if>

                                <!-- Personal:
                             - Gestionar recepción y entrega de libros
                             - Gestionar inventario de libros -->
                                <c:if test="${sessionScope.rol == 'PERSONAL' || sessionScope.rol == 'ADMINISTRADOR'}">
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/personal"
                                            id="navRecepcionEntrega">
                                            <i class="fa-solid fa-arrow-right-arrow-left me-1"></i> Recepción y Entrega
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin"
                                            id="navInventarioLibros">
                                            <i class="fa-solid fa-boxes-stacked me-1"></i> Inventario de Libros
                                        </a>
                                    </li>
                                </c:if>
                            </ul>

                            <!-- Login y Logout: tanto personal como estudiante lo pueden ver y utilizar -->
                            <div class="d-flex align-items-center gap-3">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.rol}">
                                        <span class="text-light d-flex align-items-center gap-2 small">
                                            <i class="fa-solid fa-circle-user fs-5 text-secondary"></i>
                                            <span>
                                                <c:set var="usuarioDisplay"
                                                    value="${not empty sessionScope.nombreUsuario ? sessionScope.nombreUsuario : (not empty sessionScope.usuario ? sessionScope.usuario : 'Usuario')}" />
                                                <strong>
                                                    <c:out value="${usuarioDisplay}" />
                                                </strong>
                                                <span class="badge bg-secondary ms-1">
                                                    <c:out value="${sessionScope.rol}" />
                                                </span>
                                            </span>
                                        </span>

                                        <a href="${pageContext.request.contextPath}/logout"
                                            class="btn btn-outline-danger btn-sm" id="btn-logout">
                                            <i class="fa-solid fa-right-from-bracket me-1"></i> Cerrar Sesión
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/login"
                                            class="btn btn-outline-light btn-sm" id="btn-login">
                                            <i class="fa-solid fa-arrow-right-to-bracket me-1"></i> Iniciar Sesión
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </nav>
            </c:if>

            <!-- Contenedor Principal Dinámico -->
            <main
                class="${not empty esLogin ? 'container d-flex align-items-center justify-content-center min-vh-100 py-4 login-container' : 'container-fluid px-4 pb-5'}">
                <c:choose>
                    <c:when test="${not empty vistaContenido}">
                        <jsp:include page="/WEB-INF/views/${vistaContenido}" />
                    </c:when>
                    <c:otherwise>
                        <%-- Si se incluye directamente el contenido o se define en la vista --%>
                    </c:otherwise>
                </c:choose>
            </main>

            <!-- Contenedor de Toasts de Notificación y Errores (Bootstrap 5.3) en la parte superior derecha -->
            <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1100; margin-top: 60px;"
                id="toastContainer">
                <!-- Toast de Error (Servlet o ErrorFilter) -->
                <c:set var="mensajeError"
                    value="${not empty error ? error : (not empty param.error ? param.error : null)}" />
                <c:if test="${not empty mensajeError}">
                    <div class="toast align-items-center text-bg-danger border-0 show shadow" role="alert"
                        aria-live="assertive" aria-atomic="true" id="appErrorToast">
                        <div class="d-flex">
                            <div class="toast-body d-flex align-items-center gap-2">
                                <i class="fa-solid fa-circle-exclamation fs-5"></i>
                                <span>
                                    <c:out value="${mensajeError}" />
                                </span>
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                                aria-label="Cerrar"></button>
                        </div>
                    </div>
                </c:if>

                <!-- Toast de Éxito / Resultado -->
                <c:set var="mensajeExito"
                    value="${not empty mensaje ? mensaje : (not empty param.mensaje ? param.mensaje : (not empty param.resultado ? param.resultado : null))}" />
                <c:if test="${not empty mensajeExito}">
                    <div class="toast align-items-center text-bg-success border-0 show shadow" role="alert"
                        aria-live="polite" aria-atomic="true" id="appSuccessToast">
                        <div class="d-flex">
                            <div class="toast-body d-flex align-items-center gap-2">
                                <i class="fa-solid fa-circle-check fs-5"></i>
                                <span>
                                    <c:out value="${mensajeExito}" />
                                </span>
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"
                                aria-label="Cerrar"></button>
                        </div>
                    </div>
                </c:if>
            </div>

            <!-- Footer Común (Oculto en Login o si se especifica ocultarFooter) -->
            <c:if test="${empty ocultarFooter && empty esLogin}">
                <footer class="footer mt-auto py-3 bg-white border-top text-center text-muted small">
                    <div class="container">
                        <span>&copy; <%= java.time.Year.now().getValue() %> Biblioteca Digital UNTEC — Todos los
                                derechos reservados.</span>
                    </div>
                </footer>
            </c:if>

            <!-- jQuery 4.0.0 -->
            <script src="https://code.jquery.com/jquery-4.0.0.min.js"></script>

            <!-- Bootstrap 5.3.8 Bundle con Popper -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

            <!-- Scripts Base ES6 -->
            <script>
                document.addEventListener('DOMContentLoaded', () => {
                    // Inicializar y auto-ocultar Toasts tras 6 segundos
                    const toastElList = document.querySelectorAll('.toast');
                    toastElList.forEach(toastEl => {
                        const toast = new bootstrap.Toast(toastEl, { delay: 6000 });
                        toast.show();
                    });

                    // Auto-ocultar alertas con dismissible tras 5s
                    document.querySelectorAll('.alert-dismissible').forEach(alertEl => {
                        setTimeout(() => {
                            const alert = bootstrap.Alert.getOrCreateInstance(alertEl);
                            if (alert) alert.close();
                        }, 5000);
                    });
                });

                // Función utilitaria ES6 global para disparar Toasts dinámicamente desde JS
                window.mostrarToast = function (mensaje, tipo = 'danger') {
                    const container = document.getElementById('toastContainer');
                    if (!container) return;
                    const icon = tipo === 'danger' ? 'fa-circle-exclamation' : (tipo === 'success' ? 'fa-circle-check' : 'fa-circle-info');
                    const toastId = 'toast_' + Date.now();
                    const toastHtml = `
                <div id="${toastId}" class="toast align-items-center text-bg-${tipo} border-0 shadow" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body d-flex align-items-center gap-2">
                            <i class="fa-solid ${icon} fs-5"></i>
                            <span>${mensaje}</span>
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button>
                    </div>
                </div>`;
                    container.insertAdjacentHTML('beforeend', toastHtml);
                    const el = document.getElementById(toastId);
                    const bsToast = new bootstrap.Toast(el, { delay: 6000 });
                    bsToast.show();
                    el.addEventListener('hidden.bs.toast', () => el.remove());
                };
            </script>
        </body>

        </html>