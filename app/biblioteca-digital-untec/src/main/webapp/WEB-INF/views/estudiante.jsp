<!-- Debe mostrar:

* Identificación del estudiante.
* Puntos actuales.
* Solicitudes realizadas.
* Préstamos actuales.
* Historial de préstamos y devoluciones.

Solicitudes:

* Libro.
* Fecha de solicitud.
* Estado.

Préstamos actuales:

* Libro.
* Fecha del préstamo.
* Fecha límite de devolución.
* Estado.

Historial:

* Libro.
* Fecha del préstamo.
* Fecha límite.
* Fecha de devolución.
* Estado.
* Resultado de la devolución respecto de la fecha límite.

Cómo debe mostrarlo:

* Información organizada en secciones claramente diferenciadas.
* Solicitudes, préstamos actuales e historial mediante tablas o estructuras equivalentes.
* Los puntos deben destacarse visualmente.
* Los estados deben diferenciarse visualmente.
* El estudiante solamente podrá consultar esta información.
* No debe existir una opción para registrar una devolución.

Con qué debe mostrarlo:

* HTML5.
* CSS3.
* Bootstrap 5 para tablas, tarjetas, badges y navegación.
* JSTL para representar la información recibida desde el servidor. -->

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <div class="row g-4" id="panelEstudianteContainer">
            <!-- Encabezado con Identificación y Puntos Destacados -->
            <div class="col-12">
                <div
                    class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 pb-3 border-bottom">
                    <div>
                        <h2 class="h3 fw-bold text-dark mb-1">
                            <i class="fa-solid fa-user-graduate text-primary me-2"></i>Portal del Estudiante
                        </h2>
                        <p class="text-muted small mb-0">
                            Estudiante: <strong class="text-dark">
                                <c:out value="${sessionScope.nombreUsuario}" />
                            </strong>
                            — Consulte sus solicitudes, libros en préstamo y registro histórico.
                        </p>
                    </div>

                    <!-- Puntos Destacados Visualmente -->
                    <div>
                        <div class="card bg-white border-primary shadow-sm">
                            <div class="card-body py-2 px-3 d-flex align-items-center gap-3">
                                <div class="bg-primary text-white p-2 rounded-circle">
                                    <i class="fa-solid fa-award fs-4"></i>
                                </div>
                                <div>
                                    <span class="text-muted small fw-semibold d-block text-uppercase">Puntos
                                        Disponibles</span>
                                    <span class="h4 fw-bold text-primary mb-0" id="estudiantePuntos">
                                        <c:out value="${not empty puntos ? puntos : 0}" /> pts
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Navegación por Tabs/Pestañas con Bootstrap 5 -->
            <div class="col-12">
                <ul class="nav nav-tabs border-bottom mb-4" id="estudianteTab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active d-flex align-items-center gap-2" id="solicitudes-tab"
                            data-bs-toggle="tab" data-bs-target="#solicitudes-pane" type="button" role="tab"
                            aria-controls="solicitudes-pane" aria-selected="true">
                            <i class="fa-solid fa-clock-rotate-left text-warning"></i>
                            <span>Solicitudes Realizadas</span>
                            <c:if test="${not empty solicitudes && not empty solicitudes.registros}">
                                <span
                                    class="badge bg-warning text-dark rounded-pill">${solicitudes.totalRegistros}</span>
                            </c:if>
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link d-flex align-items-center gap-2" id="prestamos-tab" data-bs-toggle="tab"
                            data-bs-target="#prestamos-pane" type="button" role="tab" aria-controls="prestamos-pane"
                            aria-selected="false">
                            <i class="fa-solid fa-book-bookmark text-primary"></i>
                            <span>Préstamos Actuales</span>
                            <c:if test="${not empty prestamosActuales && not empty prestamosActuales.registros}">
                                <span class="badge bg-primary rounded-pill">${prestamosActuales.totalRegistros}</span>
                            </c:if>
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link d-flex align-items-center gap-2" id="historial-tab" data-bs-toggle="tab"
                            data-bs-target="#historial-pane" type="button" role="tab" aria-controls="historial-pane"
                            aria-selected="false">
                            <i class="fa-solid fa-timeline text-secondary"></i>
                            <span>Historial de Devoluciones</span>
                        </button>
                    </li>
                </ul>

                <!-- Contenido de los Tabs -->
                <div class="tab-content" id="estudianteTabContent">

                    <!-- TAB 1: Solicitudes Realizadas -->
                    <div class="tab-pane fade show active" id="solicitudes-pane" role="tabpanel"
                        aria-labelledby="solicitudes-tab" tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-list-check me-2 text-primary"></i>Solicitudes en Curso
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when test="${not empty solicitudes && not empty solicitudes.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Libro</th>
                                                        <th scope="col">Fecha de Solicitud</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="sol" items="${solicitudes.registros}">
                                                        <c:set var="libroSol" value="${libros[sol.libroId]}" />
                                                        <tr>
                                                            <td class="ps-4">
                                                                <div class="d-flex align-items-center gap-3">
                                                                    <div class="bg-light p-2 rounded text-primary">
                                                                        <i class="fa-solid fa-book"></i>
                                                                    </div>
                                                                    <div>
                                                                        <span class="fw-semibold text-dark d-block">
                                                                            <c:out
                                                                                value="${not empty libroSol ? libroSol.titulo : ('Libro #' + sol.libroId)}" />
                                                                        </span>
                                                                        <span class="text-muted small">
                                                                            <c:out
                                                                                value="${not empty libroSol ? libroSol.autor : ''}" />
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td class="text-secondary">
                                                                <i class="fa-regular fa-calendar me-1"></i>
                                                                <c:out value="${sol.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="badge bg-warning text-dark px-3 py-2">
                                                                    <i class="fa-solid fa-hourglass-half me-1"></i>
                                                                    <c:out value="${sol.estado}" />
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-inbox display-6 mb-2"></i>
                                            <p class="mb-0">No tiene solicitudes pendientes en este momento.</p>
                                            <a href="${pageContext.request.contextPath}/libros"
                                                class="btn btn-outline-primary btn-sm mt-3">
                                                <i class="fa-solid fa-magnifying-glass me-1"></i> Explorar Catálogo
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 2: Préstamos Actuales -->
                    <div class="tab-pane fade" id="prestamos-pane" role="tabpanel" aria-labelledby="prestamos-tab"
                        tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-book-reader me-2 text-primary"></i>Libros Actualmente en su
                                    Poder
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when
                                        test="${not empty prestamosActuales && not empty prestamosActuales.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Libro</th>
                                                        <th scope="col">Fecha Préstamo</th>
                                                        <th scope="col">Fecha Límite Devolución</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="pres" items="${prestamosActuales.registros}">
                                                        <c:set var="libroPres" value="${libros[pres.libroId]}" />
                                                        <tr>
                                                            <td class="ps-4">
                                                                <div class="d-flex align-items-center gap-3">
                                                                    <div class="bg-light p-2 rounded text-primary">
                                                                        <i class="fa-solid fa-book"></i>
                                                                    </div>
                                                                    <div>
                                                                        <span class="fw-semibold text-dark d-block">
                                                                            <c:out
                                                                                value="${not empty libroPres ? libroPres.titulo : ('Libro #' + pres.libroId)}" />
                                                                        </span>
                                                                        <span class="text-muted small">
                                                                            <c:out
                                                                                value="${not empty libroPres ? libroPres.autor : ''}" />
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td class="text-secondary">
                                                                <i class="fa-regular fa-calendar me-1"></i>
                                                                <c:out value="${pres.fechaPrestamo}" />
                                                            </td>
                                                            <td>
                                                                <span
                                                                    class="badge bg-light text-danger border border-danger-subtle px-2 py-1">
                                                                    <i class="fa-solid fa-calendar-day me-1"></i>
                                                                    <c:out value="${pres.fechaLimite}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-center">
                                                                <span class="badge bg-primary px-3 py-2">
                                                                    <i class="fa-solid fa-book-open me-1"></i>
                                                                    <c:out value="${pres.estado}" />
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-circle-check display-6 mb-2"></i>
                                            <p class="mb-0">No tiene préstamos activos actualmente.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <!-- TAB 3: Historial de Préstamos y Devoluciones -->
                    <div class="tab-pane fade" id="historial-pane" role="tabpanel" aria-labelledby="historial-tab"
                        tabindex="0">
                        <div class="card border-0 shadow-sm">
                            <div class="card-header bg-white py-3">
                                <h5 class="card-title fw-bold text-dark mb-0">
                                    <i class="fa-solid fa-clock-rotate-left me-2 text-secondary"></i>Historial de
                                    Devoluciones
                                </h5>
                            </div>
                            <div class="card-body p-0">
                                <c:choose>
                                    <c:when test="${not empty historial && not empty historial.registros}">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle mb-0">
                                                <thead class="table-light text-secondary small text-uppercase">
                                                    <tr>
                                                        <th scope="col" class="ps-4">Libro</th>
                                                        <th scope="col">Fecha Préstamo</th>
                                                        <th scope="col">Fecha Límite</th>
                                                        <th scope="col">Fecha Devolución</th>
                                                        <th scope="col" class="text-center">Estado</th>
                                                        <th scope="col" class="text-center">Resultado</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="h" items="${historial.registros}">
                                                        <c:set var="libroHist" value="${libros[h.libroId]}" />
                                                        <tr>
                                                            <td class="ps-4">
                                                                <span class="fw-semibold text-dark d-block">
                                                                    <c:out
                                                                        value="${not empty libroHist ? libroHist.titulo : ('Libro #' + h.libroId)}" />
                                                                </span>
                                                                <span class="text-muted small">
                                                                    <c:out
                                                                        value="${not empty libroHist ? libroHist.autor : ''}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${h.fechaPrestamo}" />
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out value="${h.fechaLimite}" />
                                                            </td>
                                                            <td class="text-secondary small">
                                                                <c:out
                                                                    value="${not empty h.fechaDevolucion ? h.fechaDevolucion : '-'}" />
                                                            </td>
                                                            <td class="text-center">
                                                                <span
                                                                    class="badge ${h.estado == 'DEVUELTO' ? 'bg-success' : 'bg-secondary'}">
                                                                    <c:out value="${h.estado}" />
                                                                </span>
                                                            </td>
                                                            <td class="text-center">
                                                                <c:choose>
                                                                    <c:when
                                                                        test="${not empty h.fechaDevolucion && h.fechaDevolucion le h.fechaLimite}">
                                                                        <span
                                                                            class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">
                                                                            <i class="fa-solid fa-check me-1"></i>A
                                                                            Tiempo (+puntos)
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when
                                                                        test="${not empty h.fechaDevolucion && h.fechaDevolucion gt h.fechaLimite}">
                                                                        <span
                                                                            class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">
                                                                            <i
                                                                                class="fa-solid fa-triangle-exclamation me-1"></i>Con
                                                                            Atraso
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span
                                                                            class="badge bg-light text-muted border">En
                                                                            Curso</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-5 text-center text-muted">
                                            <i class="fa-solid fa-folder-open display-6 mb-2"></i>
                                            <p class="mb-0">No se registran préstamos anteriores en su historial.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>