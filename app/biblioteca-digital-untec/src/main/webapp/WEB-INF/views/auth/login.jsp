<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Biblioteca Digital UNTEC</title>

        <link rel="stylesheet" href="assets/css/login.css">
    </head>

    <body>

        <main class="login-container">

            <section class="login-card">

                <header class="login-header">
                    <h1>Biblioteca Digital</h1>
                    <p>UNTEC</p>
                </header>

                <form class="login-form" method="post" action="#">

                    <div class="form-group">
                        <label for="usuario">Usuario</label>

                        <input type="text" id="usuario" name="usuario" placeholder="Ingrese su usuario"
                            autocomplete="username" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Contraseña</label>

                        <input type="password" id="password" name="password" placeholder="Ingrese su contraseña"
                            autocomplete="current-password" required>
                    </div>

                    <button type="submit">
                        Iniciar sesión
                    </button>

                </form>

            </section>

        </main>

    </body>

    </html>